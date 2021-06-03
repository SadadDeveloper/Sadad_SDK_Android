package com.sdkdemo.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.multidex.MultiDex;

import com.sdkdemo.utils.Debug;

/**
 * Created by Hitesh Sarsava on 12/3/19.
 */
public class SdkApp extends Application implements Application.ActivityLifecycleCallbacks {

    public Activity currentActivity;
    private static SdkApp mInstance;
    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;
    public boolean isAppRunnning;

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

    public static boolean isApplicationInBackGround() {
        return paused > resumed;
    }

    public static synchronized SdkApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        isAppRunnning = true;
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {

        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {

        ++resumed;
        isAppRunnning = true;
        Debug.trace("MyActivityLifeCycle", "onActivityResumed isApplicationInForeground() " + isApplicationInForeground());
    }

    @Override
    public void onActivityPaused(Activity activity) {

        ++paused;
        Debug.trace("test", "application is in foreground: " + (resumed > paused));
        Debug.trace("MyActivityLifeCycle", "onActivityPaused isApplicationInForeground() " + isApplicationInForeground());
    }

    @Override
    public void onActivityStopped(Activity activity) {

        ++stopped;
        Debug.trace("test", "application is visible: " + (started > stopped));
        Debug.trace("MyActivityLifeCycle", "onActivityStopped isApplicationInForeground() " + isApplicationInForeground());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

        Debug.trace("test", "onActivityDestroyed: started" + started + "resumed: " + resumed + " paused: " + paused + " stopped" + stopped);

        if (resumed == paused && resumed == started && resumed == stopped) {
            Debug.trace("MyLifecycleHandler", "App is closed");
            isAppRunnning = false;

        } else
            isAppRunnning = true;
    }
}
