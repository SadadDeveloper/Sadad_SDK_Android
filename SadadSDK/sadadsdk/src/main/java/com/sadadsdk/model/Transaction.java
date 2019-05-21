package com.sadadsdk.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.JsonObject;
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

        RestClient.getInstance().post(mContext, sadadService, ApiList.CREATE_TRANSACTION, RequestMethod.POST, getRequestBody(), true, RequestCode.CREATE_TRANSACTION, true,
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

        RestClient.getInstance().post(mContext, sadadService, ApiList.PATCH_TRANSACTION, RequestMethod.POST, getPatchRequestBody(), true, RequestCode.PATCH_TRANSACTION, true,
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

    private JsonObject getRequestBody() {

        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("createdby", 0);
//        jsonObject.addProperty("modifiedby", 0);
        jsonObject.addProperty("amount", amount);
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
