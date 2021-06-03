package com.sadadsdk.model;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Set;

import com.sadadsdk.api.ApiList;
import com.sadadsdk.api.DataObserver;
import com.sadadsdk.api.RequestCode;
import com.sadadsdk.api.RequestMethod;
import com.sadadsdk.api.RestClient;
import com.sadadsdk.listener.TokenReceiver;
import com.sadadsdk.paymentselection.SadadService;

public class Tokenization extends BaseModel {

    private AppCompatActivity context;
    private String token;
    private Bundle sadadOrder;

    public Tokenization(AppCompatActivity context, Bundle sadadOrder) {
        this.context = context;
        this.sadadOrder = sadadOrder;
    }

    public void generateCheckSum(final TokenReceiver tokenReceiver, SadadService sadadService) {

        RestClient.getInstance().post(context, sadadService, ApiList.CREATE_CHECK_SUM, RequestMethod.POST, getRequestBody(), true,
                RequestCode.CREATE_CHECK_SUM, true, true,
                new DataObserver() {
                    @Override
                    public void onSuccess(RequestCode mRequestCode, Object mObject) {

                        tokenReceiver.onCheckSumReceived(mObject.toString());

                    }

                    @Override
                    public void onFailure(RequestCode mRequestCode, String mError, int errorCode) {
                        tokenReceiver.onCheckSumFailed(mError,errorCode);
                    }
                });
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Bundle getSadadOrder() {
        return sadadOrder;
    }

    public void setSadadOrder(Bundle sadadOrder) {
        this.sadadOrder = sadadOrder;
    }

    public JsonObject getRequestBody() {

        JsonObject json = new JsonObject();
        Set<String> keys = sadadOrder.keySet();
        for (final String key : keys) {
            if (!key.equalsIgnoreCase(SadadOrder.ACCESS_TOKEN)) {
                if (sadadOrder.getString(key, "").startsWith("[")) {
//                    JsonArray jsonElements = new JsonArray();
//                    jsonElements.addAll(RestClient.getGsonInstance().toJson());
                    JsonParser parser = new JsonParser();
                    JsonElement tradeElement = parser.parse(sadadOrder.getString(key, ""));
                    json.add(key, tradeElement);
                } else {
                    json.addProperty(key, String.valueOf(sadadOrder.get(key)));
                }
            }
        }
        return json;
    }
}

