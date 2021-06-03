package com.sadadsdk.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.JsonObject;
import com.sufalamtech.sadad.sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.sadadsdk.api.ApiList;
import com.sadadsdk.api.DataObserver;
import com.sadadsdk.api.RequestCode;
import com.sadadsdk.api.RequestMethod;
import com.sadadsdk.api.RestClient;
import com.sadadsdk.listener.TokenReceiver;
import com.sadadsdk.paymentselection.SadadService;

public class Transaction extends BaseModel {

    private int createdby;
    private int modifiedby;
    private String amount;
    private int senderId;
    private int receiverId;
    private int transactionstatusId;
    private int transactionmodeId;
    private int transactionentityId;
    private String transaction_summary;
    private String hash;
    private String transactionno;

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public int getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(int modifiedby) {
        this.modifiedby = modifiedby;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public int getTransactionstatusId() {
        return transactionstatusId;
    }

    public void setTransactionstatusId(int transactionstatusId) {
        this.transactionstatusId = transactionstatusId;
    }

    public int getTransactionmodeId() {
        return transactionmodeId;
    }

    public void setTransactionmodeId(int transactionmodeId) {
        this.transactionmodeId = transactionmodeId;
    }

    public int getTransactionentityId() {
        return transactionentityId;
    }

    public void setTransactionentityId(int transactionentityId) {
        this.transactionentityId = transactionentityId;
    }

    public String getTransaction_summary() {
        return transaction_summary;
    }

    public void setTransaction_summary(String transaction_summary) {
        this.transaction_summary = transaction_summary;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTransactionno() {
        return transactionno;
    }

    public void setTransactionno(String transactionno) {
        this.transactionno = transactionno;
    }

    public void createTransaction(AppCompatActivity mContext, final TokenReceiver tokenReceiver, SadadService sadadService) {

        RestClient.getInstance().post(mContext, sadadService, ApiList.CREATE_TRANSACTION, RequestMethod.POST, getRequestBody(), true, RequestCode.CREATE_TRANSACTION, true, true,
                new DataObserver() {
                    @Override
                    public void onSuccess(RequestCode mRequestCode, Object mObject) {
                        tokenReceiver.onCreateTransaction(mObject.toString());
                    }

                    @Override
                    public void onFailure(RequestCode mRequestCode, String mError, int errorCode) {
                        tokenReceiver.onTransactionFailed(mError);
                    }
                });
    }

    public void patchTransaction(FragmentActivity mContext, final TokenReceiver tokenReceiver, SadadService sadadService) {

        RestClient.getInstance().post(mContext, sadadService, ApiList.PATCH_TRANSACTION, RequestMethod.POST, getPatchRequestBody(), true, RequestCode.PATCH_TRANSACTION, true, true,
                new DataObserver() {
                    @Override
                    public void onSuccess(RequestCode mRequestCode, Object mObject) {
                        tokenReceiver.onPatchTransaction(mObject.toString());
                    }

                    @Override
                    public void onFailure(RequestCode mRequestCode, String mError, int errorCode) {
                        tokenReceiver.onPatchTransactionFailed(mError);
                    }
                });
    }


    public void checkTransactionAmount(final FragmentActivity mContext, final TokenReceiver tokenReceiver, SadadService sadadService) {
        RestClient.getInstance().post(mContext, sadadService, ApiList.CHECK_TRANSACTION_LIMIT, RequestMethod.GET, new JsonObject(), true, RequestCode.CHECK_TRANSACTION, true, true,
                new DataObserver() {
                    @Override
                    public void onSuccess(RequestCode mRequestCode, Object mObject) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(mObject));
                            double minAmount = Double.parseDouble(jsonObject.optString("value"));
                            double sdkAmount = Double.parseDouble(amount);
                            if (sdkAmount < minAmount) {
                                tokenReceiver.onFailureCheckTransactionLimit(mContext.getString(R
                                        .string.str_amount_check_msg, jsonObject.optString("value")), RestClient.FAILURE_CODE_402);
                            } else {
                                tokenReceiver.onSuccessCheckTransactionLimit(mObject.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            tokenReceiver.onFailureCheckTransactionLimit(mContext.getString(R.string.str_ws_network), RestClient.FAILURE_CODE_400);
                        }
                    }

                    @Override
                    public void onFailure(RequestCode mRequestCode, String mError, int errorCode) {
                        tokenReceiver.onFailureCheckTransactionLimit(mError, errorCode);
                    }
                });
    }

    private JsonObject getRequestBody() {

        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("createdby", 0);
//        jsonObject.addProperty("modifiedby", 0);
        try {
            if (amount != null && !amount.isEmpty()) {
                jsonObject.addProperty("amount", Double.parseDouble(amount));
            } else {
                jsonObject.addProperty("amount", Double.parseDouble("0"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("amount", Double.parseDouble("0"));
        }
//        jsonObject.addProperty("senderId", 0);
//            jsonObject.put("receiverId", receiverId);
        jsonObject.addProperty("transactionstatusId", transactionstatusId);
        jsonObject.addProperty("transactionmodeId", transactionmodeId);
        jsonObject.addProperty("transactionentityId", transactionentityId);
        jsonObject.addProperty("transaction_summary", transaction_summary.replaceAll("\\\\", ""));
        jsonObject.addProperty("hash", hash);

        return jsonObject;
    }

    private JsonObject getPatchRequestBody() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("transactionno", transactionno);
        jsonObject.addProperty("transactionstatusId", transactionstatusId);
        return jsonObject;
    }
}
