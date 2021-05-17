package cards.sdkdemo.api;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cards.sdkdemo.base.SdkApp;
import cards.sdkdemo.utils.Debug;
import cards.sdkdemo.utils.PrefUtils;
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

    static boolean requireAuthorization = true;
    private static final int SOCKET_TIMEOUT_MS = /*5 **/ 60 * 5000;
    private static int cacheSize = 10 * 1024 * 1024; // 10 MB
    private static Retrofit retrofit = null;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cache(new Cache(SdkApp.getInstance().getCacheDir(), cacheSize))
            .readTimeout(SOCKET_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .connectTimeout(SOCKET_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request.Builder originalRequest = chain.request().newBuilder();
//                    originalRequest.addHeader("Content-Type", "application/json");
                    if (requireAuthorization) {
                        Debug.trace("AccessToken", PrefUtils.getInstance().getString(PrefUtils.ACCESS_TOKEN, ""));
                        originalRequest.addHeader("Authorization", PrefUtils.getInstance().getString(PrefUtils.ACCESS_TOKEN, ""));
                    }

                    return chain.proceed(originalRequest.build()).newBuilder().build();
                }
            })
            .build();


    static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ServerConfig.SERVER_LIVE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
