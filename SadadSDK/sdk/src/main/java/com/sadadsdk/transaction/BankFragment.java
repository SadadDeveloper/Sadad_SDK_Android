package com.sadadsdk.transaction;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sufalamtech.sadad.sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.sadadsdk.base.Constant;
import com.sadadsdk.listener.OnBackPressedEvent;
import com.sadadsdk.listener.TokenReceiver;
import com.sadadsdk.model.Transaction;
import com.sadadsdk.paymentselection.SadadService;
import com.sadadsdk.utils.Debug;
import com.sadadsdk.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankFragment extends Fragment implements OnBackPressedEvent {

    private static final String ARG_PARAM1 = "cardDetails";
    private static TokenReceiver mTokenReceiver;
    private static SadadService mSadadService;
    //    @BindView(R2.id.wvBankAPI)
    WebView wvBankAPI;
    //    private Unbinder unbinder;
    private String cardDetails;


    public BankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BankFragment.
     */

    public static BankFragment newInstance(String param1, TokenReceiver tokenReceiver, SadadService sadadService) {
        BankFragment fragment = new BankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        mTokenReceiver = tokenReceiver;
        mSadadService = sadadService;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardDetails = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bank, container, false);
//        unbinder = ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        wvBankAPI = view.findViewById(R.id.wvBankAPI);

        wvBankAPI.getSettings().setJavaScriptEnabled(true);
        wvBankAPI.getSettings().setLoadWithOverviewMode(true);
        wvBankAPI.setWebChromeClient(new WebChromeClient());
        wvBankAPI.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //CustomDialog.getInstance().showProgress(getActivity(), "Loading...", false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //CustomDialog.getInstance().dismiss();
            }
        });

        wvBankAPI.clearHistory();
        wvBankAPI.clearCache(true);

        wvBankAPI.addJavascriptInterface(new JavaScriptInterface(this, wvBankAPI), "MyHandler");

        postData();
    }

    private void postData() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(cardDetails);
            int transactionMode = jsonObject.optInt(Constant.TRANSACTION_MODE);
            double amount = Double.parseDouble(jsonObject.optString(Constant.AMOUNT)) * 100;
            String transactionId = jsonObject.optString(Constant.INVOICE_NUMBER);
            String postData = "";
            String url = "";

            if (transactionMode == Constant.DEBIT_CARD_TRANSACTION_MODE_ID) {
                url = mSadadService.getDebitUrl();

                postData = "amount=" + URLEncoder.encode(Utils.formatCurrency("%.0f", amount), "UTF-8");
                postData = postData + "&PUN=" + URLEncoder.encode(transactionId, "UTF-8");
//                postData = postData + "&type=" + URLEncoder.encode("sandbox", "UTF-8");

            } else if (transactionMode == Constant.CREDIT_CARD_TRANSACTION_MODE_ID) {

                url = mSadadService.getCreditUrl();

                String cardType = jsonObject.optString(Constant.CARD_TYPE);
                String cardNumber = jsonObject.optString(Constant.CARD_NUMBER);
                String cardExpiryDate = jsonObject.optString(Constant.CARD_EXPIRY_DATE);
                String cardCSVCVVNumber = jsonObject.optString(Constant.CARD_CVV_NUMBER);

                postData = "vpc_Version=" + URLEncoder.encode("1", "UTF-8")
                        + "&vpc_Command=" + URLEncoder.encode("pay", "UTF-8")
                        + "&vpc_Merchant=" + URLEncoder.encode("DB93443", "UTF-8")
                        + "&vpc_AccessCode=" + URLEncoder.encode("F4996AF0", "UTF-8")
                        + "&vpc_MerchTxnRef=" + URLEncoder.encode(transactionId, "UTF-8")
                        + "&vpc_OrderInfo=" + URLEncoder.encode("SADAD SDK Order", "UTF-8")
                        + "&vpc_Amount=" + URLEncoder.encode(Utils.formatCurrency("%.0f", amount), "UTF-8")
                        + "&vpc_Currency=" + URLEncoder.encode("QAR", "UTF-8")
                        + "&vpc_TicketNo=" + URLEncoder.encode("6AQ89F3", "UTF-8")
                        + "&vpc_Gateway=" + URLEncoder.encode("ssl", "UTF-8")
                        + "&vpc_card=" + URLEncoder.encode(cardType, "UTF-8")
                        + "&vpc_CardNum=" + URLEncoder.encode(cardNumber, "UTF-8")
                        + "&vpc_CardExp=" + URLEncoder.encode(cardExpiryDate, "UTF-8")
                        + "&vpc_CardSecurityCode=" + URLEncoder.encode(cardCSVCVVNumber, "UTF-8");
            }

            Debug.trace("URL", url);

            Debug.trace("PostData", postData);
            wvBankAPI.postUrl(url, postData.getBytes());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {

    }


    void javascriptCallFinished(String value) {

        JSONObject jsonObject;
        Transaction transaction = new Transaction();

        try {
            jsonObject = new JSONObject(value);
            transaction.setTransactionno(jsonObject.optString("vpc_MerchTxnRef"));

            if (jsonObject.optString("vpc_Message").equalsIgnoreCase("Approved")) {

                transaction.setTransactionstatusId(Constant.TRANSACTION_STATUS_ID_SUCCESS);
            } else {

                transaction.setTransactionstatusId(Constant.TRANSACTION_STATUS_ID_FAILED);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            transaction.setTransactionstatusId(Constant.TRANSACTION_STATUS_ID_FAILED);
        }
        transaction.patchTransaction(getActivity(), mTokenReceiver, mSadadService);
    }

    void javascriptCallForDebitFinished(String value) {

        JSONObject jsonObject;

        Transaction transaction = new Transaction();
        try {
            Log.e("Response: ", "-> " + value);
            jsonObject = new JSONObject(value);
            transaction.setTransactionno(jsonObject.optString("Response_PUN"));

            if (jsonObject.optString("Response_StatusMessage").equalsIgnoreCase("Payment processed successfully.")) {

                transaction.setTransactionstatusId(Constant.TRANSACTION_STATUS_ID_SUCCESS);
            } else {

                transaction.setTransactionstatusId(Constant.TRANSACTION_STATUS_ID_FAILED);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            transaction.setTransactionstatusId(Constant.TRANSACTION_STATUS_ID_FAILED);
        }

        transaction.patchTransaction(getActivity(), mTokenReceiver, mSadadService);
    }
}
