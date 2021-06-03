package com.sadadsdk.api;


/**
 * Created by ${hitesh} on 12/8/2016.
 */

class ResponseManager {

    static <T> Object parseResponse(String mResponse, RequestCode mRequestCode) {

        Object object = null;
        try {

            switch (mRequestCode) {

                case CREATE_TRANSACTION:
                    object = mResponse;
                    break;

                case CREATE_CHECK_SUM:
                    object = mResponse;
                    break;

                case PATCH_TRANSACTION:
                    object = mResponse;
                    break;

                case CHECK_TRANSACTION:
                    object = mResponse;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }
}
