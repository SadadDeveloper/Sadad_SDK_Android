package com.sadadsdk.paymentselection;

import android.content.Context;
import android.content.Intent;

import com.sadadsdk.api.ServerConfig;
import com.sadadsdk.base.Constant;
import com.sadadsdk.listener.TransactionCallBack;
import com.sadadsdk.model.SadadOrder;


public class SadadService implements TransactionCallBack {

    private static TransactionCallBack mTransactionCallBack;
    private static volatile String serverUrl;
    private static volatile String creditUrl;
    private static volatile String debitUrl;
    private static volatile String sadadLoginUrl;

    public static void createTransaction(Context context, SadadOrder sadadOrder, TransactionCallBack transactionCallBack) {
        mTransactionCallBack = transactionCallBack;
        SadadService sadadService = new SadadService();
        sadadOrder.validateOrder(context, sadadService);
        context.startActivity(new Intent()
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setClass(context.getApplicationContext(), PaymentSelectionActivity.class)
                .putExtra(Constant.INTERFACE, sadadService)
                .putExtra(Constant.SDK_DATA, sadadOrder.getRequestParamMap()));
    }

    public static void getSandboxService() {

        serverUrl = ServerConfig.SERVER_SANDBOX_URL;
        creditUrl = ServerConfig.CREDIT_CARD_SANDBOX_URL;
        debitUrl = ServerConfig.DEBIT_CARD_SANDBOX_URL;
        sadadLoginUrl = ServerConfig.SADAD_LOGIN_SANDBOX_URL;
    }

    public static void getProductionService() {

        serverUrl = ServerConfig.SERVER_LIVE_URL;
        creditUrl = ServerConfig.CREDIT_CARD_LIVE_URL;
        debitUrl = ServerConfig.DEBIT_CARD_LIVE_URL;
        sadadLoginUrl = ServerConfig.SADAD_LOGIN_LIVE_URL;
    }


    @Override
    public void onTransactionResponse(String inResponse) {
        mTransactionCallBack.onTransactionResponse(inResponse);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        mTransactionCallBack.onBackPressedCancelTransaction();
    }

    @Override
    public void onTransactionCancel(String errorJson) {
        mTransactionCallBack.onTransactionCancel(errorJson);
    }

    @Override
    public void onTransactionFailed(String errorJson) {
        mTransactionCallBack.onTransactionFailed(errorJson);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getCreditUrl() {
        return creditUrl;
    }

    public String getDebitUrl() {
        return debitUrl;
    }

    public String getSadadLoginUrl() {
        return sadadLoginUrl;
    }
}
