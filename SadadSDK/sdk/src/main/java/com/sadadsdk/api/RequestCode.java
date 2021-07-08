package com.sadadsdk.api;


/**
 * Created by ${hitesh} on 12/7/2016.
 */

public enum RequestCode {

    CREATE_TRANSACTION(String.class),
    CREATE_CHECK_SUM(String.class),
    PATCH_TRANSACTION(String.class),
    CHECK_COUNTRY(String.class),
    CHECK_TRANSACTION(String.class);

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
