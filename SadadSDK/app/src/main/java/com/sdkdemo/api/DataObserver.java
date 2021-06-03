package com.sdkdemo.api;

/**
 * Created by ${hitesh} on 12/7/2016.
 */

public interface DataObserver {

    void onSuccess(RequestCode mRequestCode, Object mObject);

    void onFailure(RequestCode mRequestCode, String mError, int errorCode);
}
