package cards.sdkdemo.api;


/**
 * Created by ${hitesh} on 12/8/2016.
 */

class ResponseManager {

    static <T> Object parseResponse(String mResponse, RequestCode mRequestCode) {

        Object object = null;
        try {

            switch (mRequestCode) {

                case GENERATE_TOKEN:
                    object = mResponse;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }
}
