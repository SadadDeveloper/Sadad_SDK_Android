package com.sadadsdk.cardpayment;

public interface CardView {

    void onSuccessOfValidCountry(boolean startBankFragment);

    void onErrorOfValidCountry(String error);
}
