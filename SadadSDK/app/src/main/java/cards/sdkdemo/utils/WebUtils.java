package cards.sdkdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cards.sdkdemo.base.SdkApp;

/**
 * Created by Hitesh Sarsava on 12/3/19.
 */
public class WebUtils {

    public static boolean isInternetAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) SdkApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
