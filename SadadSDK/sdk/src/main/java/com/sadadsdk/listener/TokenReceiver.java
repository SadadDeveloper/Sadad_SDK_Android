package com.sadadsdk.listener;

import java.io.Serializable;

public interface TokenReceiver extends Serializable {

    void onCheckSumFailed(String errorMsg,int errorCode);

    void onCheckSumReceived(String checkSum);

    void onCreateTransaction(String data);

    void onTransactionFailed(String errorMsg);

    void onPatchTransaction(String data);

    void onPatchTransactionFailed(String errorMsg);

    void onSadadCall(String data);

    void onSuccessCheckTransactionLimit(String response);

    void onFailureCheckTransactionLimit(String errorMsg,int responseCode);
}
