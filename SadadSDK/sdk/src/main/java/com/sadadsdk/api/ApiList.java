package com.sadadsdk.api;

/**
 * Created by ${hitesh} on 12/7/2016.
 */

public class ApiList {

    public static final String CREATE_TRANSACTION = "transactions";

    public static final String CREATE_CHECK_SUM = "transactions/genchecksum";

    public static final String PATCH_TRANSACTION = "transactions/sdkpay";

//    public static final String CHECK_TRANSACTION_LIMIT = "settings?filter[where][key]=sdk_min_amount";
    public static final String CHECK_TRANSACTION_LIMIT = "settings/findOne?filter[where][key]=sdk_min_amount";
}
