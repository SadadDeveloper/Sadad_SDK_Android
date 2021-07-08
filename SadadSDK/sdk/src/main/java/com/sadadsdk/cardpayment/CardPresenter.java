package com.sadadsdk.cardpayment;

import android.content.Context;

import com.sadadsdk.paymentselection.SadadService;

public interface CardPresenter {

    void validateCountry(Context mContext, SadadService sadadService, String cardNumber, boolean showLoader,boolean startBankFragment);

    boolean isValidCountry();
}
