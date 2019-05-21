package com.sadadsdk.listener;

import java.io.Serializable;

public interface TransactionCallBack extends Serializable {

    public void onTransactionResponse(String inResponse);

    public void onBackPressedCancelTransaction();

    public void onTransactionCancel(String errorJson);

    public void onTransactionFailed(String errorJson);


}
