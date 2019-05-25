package com.sdkdemo.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sadadsdk.listener.TransactionCallBack;
import com.sadadsdk.model.SadadOrder;
import com.sadadsdk.paymentselection.SadadService;
import com.sdkdemo.R;
import com.sdkdemo.api.DataObserver;
import com.sdkdemo.api.RequestCode;
import com.sdkdemo.api.RequestMethod;
import com.sdkdemo.api.RestClient;
import com.sdkdemo.api.ServerConfig;
import com.sdkdemo.base.BaseActivity;
import com.sdkdemo.base.Constant;
import com.sdkdemo.transactionstatus.TransactionStatusActivity;
import com.sdkdemo.utils.ButtonRobotoRegular;
import com.sdkdemo.utils.TextViewRobotoBold;
import com.sdkdemo.utils.TextViewRobotoRegular;
import com.sdkdemo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, TransactionCallBack {

    //    @BindView(R.id.btnPayNow)
    ButtonRobotoRegular btnPayNow;
    //    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    //    @BindView(R.id.ivMinusOne)
    AppCompatImageView ivMinusOne;
    //    @BindView(R.id.tvQtyOne)
    TextViewRobotoRegular tvQtyOne;
    //    @BindView(R.id.ivPlusOne)
    AppCompatImageView ivPlusOne;
    //    @BindView(R.id.ivMinusTwo)
    AppCompatImageView ivMinusTwo;
    //    @BindView(R.id.tvQtyTwo)
    TextViewRobotoRegular tvQtyTwo;
    //    @BindView(R.id.ivPlusTwo)
    AppCompatImageView ivPlusTwo;
    //    @BindView(R.id.tvTotalAmount)
    TextViewRobotoBold tvTotalAmount;
    SwitchCompat switchLive;

    private int qtyOne = 0, qtyTwo = 0;
    private double amountOne = 1.0, amountTwo = 1.0;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {

        btnPayNow = findViewById(R.id.btnPayNow);
        rootView = findViewById(R.id.rootView);
        ivMinusOne = findViewById(R.id.ivMinusOne);
        tvQtyOne = findViewById(R.id.tvQtyOne);
        ivPlusOne = findViewById(R.id.ivPlusOne);
        ivMinusTwo = findViewById(R.id.ivMinusTwo);
        tvQtyTwo = findViewById(R.id.tvQtyTwo);
        ivPlusTwo = findViewById(R.id.ivPlusTwo);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        switchLive = findViewById(R.id.switchLive);

        btnPayNow.setEnabled(false);
        btnPayNow.setOnClickListener(this);
        ivMinusOne.setOnClickListener(this);
        ivPlusOne.setOnClickListener(this);
        ivMinusTwo.setOnClickListener(this);
        ivPlusTwo.setOnClickListener(this);
    }

    private void validateForm() {

        if (isValidForm()) {
            btnPayNow.setEnabled(true);
        } else {
            btnPayNow.setEnabled(false);
        }
    }

    private boolean isValidForm() {

        totalAmount = (qtyOne * amountOne) + (qtyTwo * amountTwo);

        tvTotalAmount.setText(Utils.formatCurrency(Constant.COMMA_CURRENCY_FORMAT, totalAmount));

        return totalAmount > 0;
    }

    @Override
    public void onClick(View v) {
        if (v == btnPayNow) {
            generateToken();
        } else if (v == ivMinusOne) {
            qtyOne = Integer.parseInt(tvQtyOne.getText().toString().trim()) < 1 ? 0 : Integer.parseInt(tvQtyOne.getText().toString().trim()) - 1;
            tvQtyOne.setText(String.valueOf(qtyOne));
            validateForm();
        } else if (v == ivPlusOne) {
            qtyOne = Integer.parseInt(tvQtyOne.getText().toString().trim()) + 1;
            tvQtyOne.setText(String.valueOf(qtyOne));
            validateForm();
        } else if (v == ivMinusTwo) {
            qtyTwo = Integer.parseInt(tvQtyTwo.getText().toString().trim()) < 1 ? 0 : Integer.parseInt(tvQtyTwo.getText().toString().trim()) - 1;
            tvQtyTwo.setText(String.valueOf(qtyTwo));
            validateForm();
        } else if (v == ivPlusTwo) {
            qtyTwo = Integer.parseInt(tvQtyTwo.getText().toString().trim()) + 1;
            tvQtyTwo.setText(String.valueOf(qtyTwo));
            validateForm();
        }
    }


    public void generateToken() {
        String url;
//        if (switchLive.isChecked()) {
//            url = ServerConfig.SERVER_LIVE_URL;
//        } else {
//            url = ServerConfig.SERVER_SANDBOX_URL;
//        }
        url=ServerConfig.SERVER_LIVE_URL;
//        url=ServerConfig.SERVER_SANDBOX_URL;
        RestClient.getInstance().post(HomeActivity.this, url, RequestMethod.POST, true,
                RequestCode.GENERATE_TOKEN, false,
                new DataObserver() {
                    @Override
                    public void onSuccess(RequestCode mRequestCode, Object mObject) {

                        try {
                            JSONObject jsonObject = new JSONObject(mObject.toString());
                            String token = jsonObject.optString(Constant.ACCESS_TOKEN, "");
                            startNewActivity(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showSnackBar(rootView, "Invalid Token", getString(R.string.str_ok), true, null);
                        }
                    }

                    @Override
                    public void onFailure(RequestCode mRequestCode, String mError, int errorCode) {
                        showSnackBar(rootView, mError, getString(R.string.str_ok), true, null);
                    }
                });
    }

    void startNewActivity(String token) {
        if (!TextUtils.isEmpty(token)) {

            Bundle bundle = new Bundle();
            bundle.putString(SadadOrder.ACCESS_TOKEN, token);
            bundle.putString(SadadOrder.CUST_ID, "ppp");
            bundle.putString(SadadOrder.ORDER_ID, "ppp");
            bundle.putString(SadadOrder.TXN_AMOUNT, String.valueOf(totalAmount));
            bundle.putString(SadadOrder.MOBILE_NO, "9824672292");

            JSONArray productDetails = getProductDetails();
            if (productDetails.length() > 0) {
                bundle.putString(SadadOrder.ORDER_DETAIL, String.valueOf(productDetails));
            }

            SadadOrder sadadOrder = new SadadOrder();
            sadadOrder.setRequestParamMap(bundle);
            SadadService.getProductionService();

//            SadadService.getSandboxService();
//            if (switchLive.isChecked()) {
//                SadadService.getProductionService();
//            } else {
//                SadadService.getSandboxService();
//            }

            SadadService.createTransaction(HomeActivity.this, sadadOrder, this);

        } else {
            showSnackBar(rootView, "Invalid Token", getString(R.string.str_ok), true, null);
        }
    }

    JSONArray getProductDetails() {

        JSONArray jsonArray = new JSONArray();
        if (qtyOne > 0) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("itemname", "Gucci perfume");
                jsonObject.put("quantity", qtyOne);
                jsonObject.put("amount", amountOne);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        if (qtyTwo > 0) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("itemname", "Oudy perfume");
                jsonObject.put("quantity", qtyTwo);
                jsonObject.put("amount", amountTwo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public void onTransactionResponse(String inResponse) {

        int transactionStatusId = Constant.TRANSACTION_STATUS_ID_FAILED;
        double amount = totalAmount;
        String transactionNumber = "";

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(inResponse);
            if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                JSONObject dataJson = jsonObject.optJSONObject("data");
                if (!dataJson.isNull("transactionstatus")) {
                    transactionStatusId = dataJson.optInt("transactionstatus");
                } else {
                    transactionStatusId = dataJson.optInt("transactionstatusId");
                }

                amount = dataJson.optDouble("amount");

                if (!dataJson.isNull("transactionnumber")) {
                    transactionNumber = dataJson.optString("transactionnumber");
                } else {
                    transactionNumber = dataJson.optString("invoicenumber");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(HomeActivity.this, TransactionStatusActivity.class);
        intent.putExtra(Constant.TRANSACTION_STATUS, transactionStatusId);
        intent.putExtra(Constant.AMOUNT, amount);
        intent.putExtra(Constant.TRANSACTION_ID, transactionNumber);
        startActivitywithAnimation(intent, false);
    }

    @Override
    public void onBackPressedCancelTransaction() {

        showSnackBar(rootView, "Payment cancelled by backpress", "Ok", false, null);
    }

    @Override
    public void onTransactionCancel(String errorJson) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(errorJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        showSnackBar(rootView, jsonObject != null ? jsonObject.optString("message") : getString(R.string.str_payment_cancelled_by_user), "Ok", false, null);
    }

    @Override
    public void onTransactionFailed(String errorJson) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(errorJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(HomeActivity.this, TransactionStatusActivity.class);
        intent.putExtra(Constant.TRANSACTION_STATUS, Constant.TRANSACTION_STATUS_ID_FAILED);
        intent.putExtra(Constant.TRANSACTION_STATUS_MESSAGE, jsonObject != null ? jsonObject.optString("message") : "");
        intent.putExtra(Constant.AMOUNT, totalAmount);
        startActivitywithAnimation(intent, false);
    }
}
