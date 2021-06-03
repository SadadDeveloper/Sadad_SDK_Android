package com.sdkdemo.api;


/**
 * Created by ${hitesh} on 12/7/2016.
 */

public enum RequestCode {

    GENERATE_TOKEN(String.class);

    Class mLocalClass;

    RequestCode(Class mLocalClass) {

        this.mLocalClass = mLocalClass;
    }

    public Class getLocalClass() {
        return mLocalClass;
    }

    public void setLocalClass(Class mLocalClass) {
        this.mLocalClass = mLocalClass;
    }
}
