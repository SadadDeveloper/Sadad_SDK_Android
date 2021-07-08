package com.sadadsdk.cardpayment;

import android.content.Context;

import com.google.gson.JsonObject;
import com.sadadsdk.api.ApiList;
import com.sadadsdk.api.DataObserver;
import com.sadadsdk.api.RequestCode;
import com.sadadsdk.api.RequestMethod;
import com.sadadsdk.api.RestClient;
import com.sadadsdk.paymentselection.SadadService;
import com.sufalamtech.sadad.sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CardPresenterImpl implements CardPresenter {

    private final CardView cardView;
    boolean isValidCountry = false;

    public CardPresenterImpl(CardView cardView) {
        this.cardView = cardView;
    }

    @Override
    public void validateCountry(final Context mContext, SadadService sadadService, String cardNumber, boolean showLoader, final boolean startBankFragment) {

        if (cardNumber.length() < 6) {
            return;
        }

        String sixDigits = cardNumber.subSequence(0, 6).toString();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cardsixdigit", sixDigits);

        RestClient.getInstance().post(mContext, sadadService, ApiList.CHECK_ALLOWED_COUNTRY, RequestMethod.POST, jsonObject, showLoader, RequestCode.CHECK_COUNTRY, true, true, new DataObserver() {
            @Override
            public void onSuccess(RequestCode mRequestCode, Object mObject) {

                try {
                    JSONObject response = new JSONObject(mObject.toString());
                    if (response.optBoolean("isAllowed", false)) {
                        isValidCountry = true;
                        cardView.onSuccessOfValidCountry(startBankFragment);
                    } else {
                        isValidCountry = false;
                        cardView.onErrorOfValidCountry(mContext.getString(R.string.str_invalid_credit_card));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isValidCountry = false;
                    cardView.onErrorOfValidCountry(mContext.getString(R.string.str_invalid_credit_card));
                }
            }

            @Override
            public void onFailure(RequestCode mRequestCode, String mError, int errorCode) {
                isValidCountry = false;
                cardView.onErrorOfValidCountry(mContext.getString(R.string.str_invalid_credit_card));
            }
        });

    }

    @Override
    public boolean isValidCountry() {
        return isValidCountry;
    }
}
