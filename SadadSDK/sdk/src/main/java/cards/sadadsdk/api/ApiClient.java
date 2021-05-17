package cards.sadadsdk.api;


import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cards.sadadsdk.paymentselection.SadadService;
import cards.sadadsdk.utils.Debug;
import cards.sadadsdk.utils.PrefUtils;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by ${Hitesh} on 12-12-2017.
 */

class ApiClient {

    private static final int SOCKET_TIMEOUT_MS = /*5 **/ 60 * 5000;
    static boolean requireAuthorization = true;
    private static int cacheSize = 10 * 1024 * 1024; // 10 MB
    private static Retrofit retrofit = null;

    static Retrofit getClient(Context context, SadadService sadadService) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient(context))
                    .baseUrl(sadadService.getServerUrl())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient(final Context context) {
        String UA = System.getProperty("http.agent");
        return new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor(UA))
                .cache(new Cache(context.getCacheDir(), cacheSize))
                .readTimeout(SOCKET_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .connectTimeout(SOCKET_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request.Builder originalRequest = chain.request().newBuilder();
                        originalRequest.addHeader("Content-Type", "application/json");
                        if (requireAuthorization) {
                            Debug.trace("AccessToken", PrefUtils.getInstance(context).getString(PrefUtils.ACCESS_TOKEN, ""));
                            originalRequest.addHeader("Authorization", PrefUtils.getInstance(context).getString(PrefUtils.ACCESS_TOKEN, ""));
                        }

                        return chain.proceed(originalRequest.build()).newBuilder().build();
                    }
                })
                .build();
    }

}
