package com.sadadsdk.listener;

import java.io.Serializable;

public interface TransactionCallBack extends Serializable {

    void onTransactionResponse(String inResponse);

    void onBackPressedCancelTransaction();

    void onTransactionCancel(String errorJson);

    void onTransactionFailed(String errorJson);
}
