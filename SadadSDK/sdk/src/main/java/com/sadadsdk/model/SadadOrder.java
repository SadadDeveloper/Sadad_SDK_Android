package com.sadadsdk.model;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.Serializable;

import com.sadadsdk.paymentselection.SadadService;
import com.sadadsdk.utils.PrefUtils;

public class SadadOrder implements Serializable {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String CUST_ID = "CUST_ID";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String TXN_AMOUNT = "TXN_AMOUNT";
    public static final String MOBILE_NO = "MOBILE_NO";
    public static final String ORDER_DETAIL = "PRODUCT_DETAILS";
    private Bundle requestParamMap;

    public void validateOrder(Context context, SadadService sadadService) {

        if (sadadService.getServerUrl() == null || sadadService.getCreditUrl() == null || sadadService.getDebitUrl() == null || sadadService.getSadadLoginUrl() == null) {

            throw new IllegalArgumentException("Sadad Service is not initialised");

        } else if (requestParamMap == null || requestParamMap.size() <= 0) {

            throw new IllegalArgumentException("Invalid request params");

        } else if (!requestParamMap.containsKey(ACCESS_TOKEN) || TextUtils.isEmpty(requestParamMap.getString(ACCESS_TOKEN, ""))) {

            throw new IllegalArgumentException("ACCESS_TOKEN not present");

        } else if (!requestParamMap.containsKey(CUST_ID) || TextUtils.isEmpty(requestParamMap.getString(CUST_ID, ""))) {

            throw new IllegalArgumentException("CUST_ID not present");

        } else if (!requestParamMap.containsKey(ORDER_ID) || TextUtils.isEmpty(requestParamMap.getString(ORDER_ID, ""))) {

            throw new IllegalArgumentException("ORDER_ID not present");

        } else if (!requestParamMap.containsKey(TXN_AMOUNT) || TextUtils.isEmpty(requestParamMap.getString(TXN_AMOUNT, ""))) {

            throw new IllegalArgumentException("TXN_AMOUNT not present");

        } else if (Double.parseDouble(requestParamMap.getString(TXN_AMOUNT, "0")) <= 0.0) {

            throw new IllegalArgumentException("TXN_AMOUNT not valid");

        } else if (!requestParamMap.containsKey(MOBILE_NO) || TextUtils.isEmpty(requestParamMap.getString(MOBILE_NO, ""))) {

            throw new IllegalArgumentException("MOBILE_NO not present");
        }

        PrefUtils.getInstance(context).setString(PrefUtils.ACCESS_TOKEN, requestParamMap.getString(ACCESS_TOKEN));
    }

    public Bundle getRequestParamMap() {
        return requestParamMap;
    }

    public void setRequestParamMap(Bundle requestParamMap) {
        this.requestParamMap = requestParamMap;
    }

//    public JsonObject getRequestBody() {
//
//        JsonObject json = new JsonObject();
//        Set<String> keys = requestParamMap.keySet();
//        for (String key : keys) {
//            if (!key.equalsIgnoreCase(ACCESS_TOKEN))
//                json.addProperty(key, String.valueOf(requestParamMap.get(key)));
//        }
//
//        return json;
//    }
}
