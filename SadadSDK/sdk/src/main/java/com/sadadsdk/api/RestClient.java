package com.sadadsdk.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sufalamtech.sadad.sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.sadadsdk.base.BaseActivity;
import com.sadadsdk.paymentselection.SadadService;
import com.sadadsdk.utils.CustomDialog;
import com.sadadsdk.utils.Debug;
import com.sadadsdk.utils.WebUtils;
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
    public final static int FAILURE_CODE_402 = 402;
    public final static int FAILURE_CODE_400 = 400;
    public final static int FAILURE_CODE_100 = 100;
    public final static int FAILURE_CODE_500 = 500;
    public static final int FAILURE_CODE_422 = 422;
    public final static int FAILURE_CODE_403 = 403;
    public static final int FAILURE_CODE_430 = 430;

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

    private static String getAbsoluteUrl(String url, SadadService sadadService) {

        if (url.startsWith("http")) {
            url = url.replaceAll(" ", "%20");
            return url;

        } else {
            return sadadService.getServerUrl() + url.replaceAll(" ", "%20");
        }
    }

    public void post(final Context mContext, SadadService sadadService, final String url, RequestMethod method, final boolean isDialogRequired,
                     final RequestCode mRequestCode, boolean requireAuthorization, final DataObserver dataObserver) {

        post(mContext, sadadService, url, method, null, isDialogRequired, mRequestCode, requireAuthorization, false, dataObserver);
    }

    public void post(final Context mContext, final SadadService sadadService, final String url, final RequestMethod method, final JsonObject mParams,
                     final boolean isDialogRequired, final RequestCode mRequestCode, final boolean requireAuthorization, final boolean isIpNeed, final DataObserver dataObserver) {

        validateNetworkConnection(mContext, new NetWorkConnectionListener() {
            @Override
            public void onConnected() {
                request(mContext, sadadService, url, method, mParams, isDialogRequired, mRequestCode, requireAuthorization, isIpNeed, dataObserver);
            }

            @Override
            public void onNotConnected() {
                dataObserver.onFailure(mRequestCode, mContext.getString(R.string.str_no_internet_connection_available), FAILURE_CODE_400);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void request(final Context mContext, final SadadService sadadService, final String url, final RequestMethod method, final JsonObject mParams, final boolean isDialogRequired, final RequestCode mRequestCode, final boolean requireAuthorization, boolean isIpNeed, final DataObserver dataObserver) {

        //set isIpNeed = false because now don't need to pass in API params.
        isIpNeed = false;

        if (isIpNeed) {
            new AsyncTask<Void, Void, String>() {
                String ip = "";

                @Override
                protected String doInBackground(Void... voids) {
                    try {
                        ip = getExternalIpAddress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return ip;
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    if (result != null && !result.isEmpty()) {
                        //mParams.addProperty("txnip", "" + result);

                        Debug.trace("RequestBody_IP", mParams.toString());
                        request(mContext, sadadService, url, method, mParams, isDialogRequired, mRequestCode, requireAuthorization, dataObserver);
                    } else {
                        dataObserver.onFailure(mRequestCode, mContext.getString(R.string.str_no_internet_connection_available), FAILURE_CODE_400);
                    }

                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            request(mContext, sadadService, url, method, mParams, isDialogRequired, mRequestCode, requireAuthorization, dataObserver);
        }
    }

    private void request(final Context mContext, SadadService sadadService, String url, RequestMethod method, JsonObject mParams, boolean isDialogRequired, final RequestCode mRequestCode, boolean requireAuthorization, final DataObserver dataObserver) {

        if (isDialogRequired) {
            CustomDialog.getInstance().showProgress(mContext, "", false);
        }

        String baseUrl = getAbsoluteUrl(url, sadadService);

        Debug.trace("postUrl", baseUrl);
        Debug.trace("requestBody", "" + mParams);

        ApiClient.requireAuthorization = requireAuthorization;

        ApiInterface apiService = ApiClient.getClient(mContext, sadadService).create(ApiInterface.class);

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
                    verifyResponse(mContext, response.body(), mRequestCode, dataObserver);
                } else {
                    try {
                        /** 21-11-2019 : If Request for "GenerateCheckSum" API so pass status code in "dataObserver.onFailure" which get in errorBody
                         * else pass "FAILURE_CODE_400" */
                        if (response.errorBody() != null) {
                            String errorBody = "" + response.errorBody().string();
                            Debug.trace("Error Response", "" + errorBody);
                            //dataObserver.onFailure(mRequestCode, response.errorBody().string(), FAILURE_CODE_400);
                            if (!errorBody.isEmpty()) {
                                JSONObject jsonObject = null;
                                try {

                                    jsonObject = new JSONObject(errorBody);
                                    JSONObject errorjsonObject = jsonObject.getJSONObject("error");
                                    if (errorjsonObject != null) {
                                        if (errorjsonObject.has("statusCode")) {
                                            int statusCode = errorjsonObject.getInt("statusCode");
                                            if (errorjsonObject.has("message")) {
                                                String message = errorjsonObject.optString("message", mContext.getString(R.string.str_ws_network));
                                                dataObserver.onFailure(mRequestCode, message, statusCode);
                                            } else {
                                                dataObserver.onFailure(mRequestCode, errorBody, statusCode);
                                            }

                                        } else {
                                            dataObserver.onFailure(mRequestCode, errorBody, FAILURE_CODE_400);
                                        }
                                    } else {
                                        dataObserver.onFailure(mRequestCode, errorBody, FAILURE_CODE_400);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    dataObserver.onFailure(mRequestCode, errorBody, FAILURE_CODE_400);
                                }
                            } else {
                                dataObserver.onFailure(mRequestCode, errorBody, FAILURE_CODE_400);
                            }
                        } else {
                            dataObserver.onFailure(mRequestCode, mContext.getString(R.string.str_ws_network), FAILURE_CODE_400);
                        }
                        /************************************************************************************/
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

        if (!WebUtils.isInternetAvailable(mContext)) {
            CustomDialog.getInstance().showNoInternetConnectionDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (WebUtils.isInternetAvailable(mContext)) {
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

    private void verifyResponse(Context context, String response, RequestCode mRequestCode, DataObserver dataObserver) {

        if (response != null && !response.isEmpty()) {
            Object object = ResponseManager.parseResponse(response, mRequestCode);
            dataObserver.onSuccess(mRequestCode, object);
        } else {
            dataObserver.onFailure(mRequestCode, context.getString(R.string.str_ws_network), FAILURE_CODE_400);
        }
    }

    interface NetWorkConnectionListener {

        void onConnected();

        void onNotConnected();
    }

    public static String getExternalIpAddress() throws Exception {
        URL whatismyip = new URL("https://api.ipify.org");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            Debug.trace("getExternalIpAddress 1: ", "" + ip);
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
