package com.sdkdemo.api;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sdkdemo.R;

import java.io.IOException;

import com.sdkdemo.base.BaseActivity;
import com.sdkdemo.base.SdkApp;
import com.sdkdemo.utils.CustomDialog;
import com.sdkdemo.utils.Debug;
import com.sdkdemo.utils.WebUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ${Hitesh} on 12-12-2017.
 */

public class RestClient {

    public static final int FAILURE_CODE_499 = 499;
    public static final int FAILURE_CODE_429 = 429;
    public static final int FAILURE_CODE_401 = 401;
    public final static int FAILURE_CODE_400 = 400;
    public final static int FAILURE_CODE_100 = 100;
    public final static int FAILURE_CODE_500 = 500;
    public static final int FAILURE_CODE_422 = 422;


    public static final int SUCCESS_CODE = 200;
    public static final int SUCCESS_CODE_304 = 304;
    public static final int SUCCESS_CODE_204 = 204;

    private static final RestClient ourInstance = new RestClient();

    private RestClient() {
    }

    public static RestClient getInstance() {
        return ourInstance;
    }

    public static Gson getGsonInstance() {

        return new GsonBuilder().setPrettyPrinting().create();
    }

    public void post(final Context mContext, final String url, RequestMethod method, final boolean isDialogRequired,
                     final RequestCode mRequestCode, boolean requireAuthorization, final DataObserver dataObserver) {

        post(mContext, url, method, null, isDialogRequired, mRequestCode, requireAuthorization, dataObserver);
    }


    private void post(final Context mContext, final String url, final RequestMethod method, final JsonObject mParams,
                      final boolean isDialogRequired, final RequestCode mRequestCode, final boolean requireAuthorization, final DataObserver dataObserver) {

        validateNetworkConnection(mContext, new NetWorkConnectionListener() {
            @Override
            public void onConnected() {
                request(mContext, url, method, mParams, isDialogRequired, mRequestCode, requireAuthorization, dataObserver);
            }

            @Override
            public void onNotConnected() {
                dataObserver.onFailure(mRequestCode, mContext.getString(R.string.str_no_internet_connection_available), FAILURE_CODE_400);
            }
        });
    }

    private void request(final Context mContext, String url, RequestMethod method, JsonObject mParams, boolean isDialogRequired, final RequestCode mRequestCode, boolean requireAuthorization, final DataObserver dataObserver) {

        if (isDialogRequired) {
            CustomDialog.getInstance().showProgress(mContext, "", false);
        }

        String baseUrl = getAbsoluteUrl(url);

        Debug.trace("postUrl", baseUrl);
        Debug.trace("requestBody", "" + mParams);

        ApiClient.requireAuthorization = requireAuthorization;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<String> response;

        switch (method) {

            case GET:
                response = apiService.get(baseUrl);
                break;

            case POST:
                if (mParams != null /*&& mParams.length() > 0*/) {
                    response = apiService.post(baseUrl, mParams);
                } else {
                    response = apiService.post(baseUrl);
                }
                break;

            case PATCH:
                if (mParams != null /*&& mParams.length() > 0*/) {
                    response = apiService.patch(baseUrl, mParams);
                } else {
                    Toast.makeText(mContext, "Improper request", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;

            case DELETE:
                response = apiService.get(baseUrl);
                break;

            default:
                Toast.makeText(mContext, "Improper request", Toast.LENGTH_SHORT).show();
                return;
        }

        response.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                CustomDialog.getInstance().dismiss();

                if (response.isSuccessful()) {
                    Debug.trace("Response", response.body());
                    verifyResponse(response.body(), mRequestCode, dataObserver);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            dataObserver.onFailure(mRequestCode, response.errorBody().string(), FAILURE_CODE_400);
                        } else {
                            dataObserver.onFailure(mRequestCode, mContext.getString(R.string.str_ws_network), FAILURE_CODE_400);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        dataObserver.onFailure(mRequestCode, mContext.getString(R.string.str_ws_network), FAILURE_CODE_400);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                CustomDialog.getInstance().dismiss();
                dataObserver.onFailure(mRequestCode, t.getLocalizedMessage(), FAILURE_CODE_400);
            }
        });
    }

    private void validateNetworkConnection(final Context mContext, final NetWorkConnectionListener netWorkConnectionListener) {

        if (!WebUtils.isInternetAvailable()) {
            CustomDialog.getInstance().showNoInternetConnectionDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (WebUtils.isInternetAvailable()) {
                        CustomDialog.getInstance().dismiss();
                        ((BaseActivity) mContext).dismissSnackBar();
                        netWorkConnectionListener.onConnected();
                    }
                }
            });
            netWorkConnectionListener.onNotConnected();
        } else {
            netWorkConnectionListener.onConnected();
        }
    }


    private static String getAbsoluteUrl(String url) {

        if (url.startsWith("http")) {
            url = url.replaceAll(" ", "%20");
            return url;

        } else {
            return ServerConfig.SERVER_LIVE_URL + url.replaceAll(" ", "%20");
        }
    }

    private void verifyResponse(String response, RequestCode mRequestCode, DataObserver dataObserver) {

        if (response != null && !response.isEmpty()) {
            Object object = ResponseManager.parseResponse(response, mRequestCode);
            dataObserver.onSuccess(mRequestCode, object);
        } else {
            dataObserver.onFailure(mRequestCode, SdkApp.getInstance().getString(R.string.str_ws_network), FAILURE_CODE_400);
        }
    }

    interface NetWorkConnectionListener {

        void onConnected();

        void onNotConnected();
    }
}
