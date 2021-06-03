package com.sadadsdk.cardpayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.sufalamtech.sadad.sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cards.pay.paycardsrecognizer.sdk.Card;
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent;
import com.sadadsdk.api.RestClient;
import com.sadadsdk.base.Constant;
import com.sadadsdk.cardform.CardEditText;
import com.sadadsdk.cardform.CardholderNameEditText;
import com.sadadsdk.cardform.CustomerNameEditText;
import com.sadadsdk.cardform.CvvEditText;
import com.sadadsdk.cardform.ExpirationDateEditText;
import com.sadadsdk.cardform.MobileNumberEditText;
import com.sadadsdk.listener.OnBackPressedEvent;
import com.sadadsdk.paymentselection.PaymentSelectionActivity;
import com.sadadsdk.paymentselection.SadadService;
import com.sadadsdk.transaction.BankFragment;
import com.sadadsdk.utils.ButtonRobotoRegular;
import com.sadadsdk.utils.DateFormatUtils;
import com.sadadsdk.utils.Debug;
import com.sadadsdk.utils.TextViewRobotoBold;
import com.sadadsdk.utils.TextViewRobotoRegular;
import com.sadadsdk.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment implements View.OnClickListener, OnBackPressedEvent, TextWatcher {


    private static final String ARG_PARAM1 = "transactionResponse";
    private static SadadService mSadadService;
    //    @BindView(R2.id.btnCancel)
    private ButtonRobotoRegular btnCancel;
    //    @BindView(R2.id.btnPay)
    private ButtonRobotoRegular btnPay;
    //    @BindView(R2.id.tvAmount)
    private TextViewRobotoBold tvAmount;
    //    @BindView(R2.id.tvEnterCardDetailLabel)
    private TextViewRobotoBold tvEnterCardDetailLabel;
    //    @BindView(R2.id.tvCardType)
    private TextViewRobotoRegular tvCardType;
    //    @BindView(R2.id.etCardNumber)
    private CardEditText etCardNumber;
    //    @BindView(R2.id.etCardExpiryDate)
    private ExpirationDateEditText etCardExpiryDate;
    //    @BindView(R2.id.etCardCvv)
    private CvvEditText etCardCvv;
    //    @BindView(R2.id.etCardHolderName)
    private CardholderNameEditText etCardHolderName;
    //    @BindView(R2.id.etPhoneNumber)
    private MobileNumberEditText etPhoneNumber;
    //    @BindView(R2.id.etCustomerName)
    private CustomerNameEditText etCustomerName;
    //    @BindView(R2.id.rootView)
    private ConstraintLayout rootView;
    private AppCompatImageView ivScanCard;
    //    private Unbinder unbinder;
    private String transactionResponse;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param transactionResponse Parameter 1.
     * @return A new instance of fragment CardFragment.
     */

    public static CardFragment newInstance(String transactionResponse, SadadService sadadService) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, transactionResponse);
        fragment.setArguments(args);
        mSadadService = sadadService;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transactionResponse = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);
//        unbinder = ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnPay = view.findViewById(R.id.btnPay);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvEnterCardDetailLabel = view.findViewById(R.id.tvEnterCardDetailLabel);
        tvCardType = view.findViewById(R.id.tvCardType);
        etCardNumber = view.findViewById(R.id.etCardNumber);
        etCardNumber.displayCardTypeIcon(false);
        etCardExpiryDate = view.findViewById(R.id.etCardExpiryDate);
        etCardCvv = view.findViewById(R.id.etCardCvv);
        etCardHolderName = view.findViewById(R.id.etCardHolderName);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etCustomerName = view.findViewById(R.id.etCustomerName);
        rootView = view.findViewById(R.id.rootView);
        ivScanCard = view.findViewById(R.id.ivScanCard);

        try {
            tvAmount.setText(Utils.formatCurrency(Constant.COMMA_CURRENCY_FORMAT, Double.parseDouble(new JSONObject(transactionResponse).optString(Constant.AMOUNT, "0.00"))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        etCardExpiryDate.useDialogForExpirationDateEntry((PaymentSelectionActivity) getActivity(), true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvEnterCardDetailLabel.setText(Html.fromHtml(getString(R.string.str_enter_your_card_details), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvEnterCardDetailLabel.setText(Html.fromHtml(getString(R.string.str_enter_your_card_details)));
        }
        btnPay.setAlpha(0.5f);

        setListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {

        etCardNumber.addTextChangedListener(this);
        etCardExpiryDate.addTextChangedListener(this);
        etCardCvv.addTextChangedListener(this);
        etCardHolderName.addTextChangedListener(this);
        etPhoneNumber.addTextChangedListener(this);
        etCustomerName.addTextChangedListener(this);

        etCustomerName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (!isValid()) {
                    validate();
                } else {
                    ((PaymentSelectionActivity) Objects.requireNonNull(getActivity())).hideKeyboard();
                    btnPay.performClick();
                }
                return false;
            }
        });

        btnPay.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        ivScanCard.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnPay) {
            JSONObject jsonObject = new JSONObject();

            try {
                JSONObject transactionResponse = new JSONObject(this.transactionResponse);
                jsonObject.put(Constant.TRANSACTION_MODE, Constant.CREDIT_CARD_TRANSACTION_MODE_ID);
                jsonObject.put(Constant.AMOUNT, transactionResponse.optString(Constant.AMOUNT, "0.00"));
                jsonObject.put(Constant.INVOICE_NUMBER, transactionResponse.optString("invoicenumber"));
                jsonObject.put(Constant.CARD_NUMBER, Objects.requireNonNull(etCardNumber.getText()).toString().trim());
                jsonObject.put(Constant.CARD_EXPIRY_DATE, DateFormatUtils.yyyyToyy("yy", etCardExpiryDate.getYear()) + etCardExpiryDate.getMonth());
                jsonObject.put(Constant.CARD_CVV_NUMBER, Objects.requireNonNull(etCardCvv.getText()).toString().trim());
                jsonObject.put(Constant.CARD_TYPE, getResources().getString(etCardNumber.getCardType().getCardType()));

                ((PaymentSelectionActivity) Objects.requireNonNull(getActivity())).pushFragment(BankFragment.newInstance(jsonObject.toString(), ((PaymentSelectionActivity) getActivity()), mSadadService), true, false, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (i == R.id.btnCancel) {
            JSONObject jsonObject;
            jsonObject = new JSONObject();
            try {
                jsonObject.put("statusCode", RestClient.FAILURE_CODE_400);
                jsonObject.put("message", getString(R.string.str_payment_cancelled_by_user));
                ((PaymentSelectionActivity) Objects.requireNonNull(getActivity())).getTransactionCallBack().onTransactionCancel(jsonObject.toString());
                getActivity().finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (i == R.id.ivScanCard) {
            scanCard();
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (isValid()) {
            btnPay.setEnabled(true);
            btnPay.setAlpha(1.0f);
        } else {
            btnPay.setEnabled(false);
            btnPay.setAlpha(0.5f);
        }
    }


    /**
     * @return {@code true} if all require fields are valid, otherwise {@code false}
     */
    private boolean isValid() {
        return etCardHolderName.isValid() &&

                etCardNumber.isValid() &&

                etCardExpiryDate.isValid() &&

                etCardCvv.isValid() &&

                etPhoneNumber.isValid() &&

                etCustomerName.isValid();
    }

    /**
     * Validate all required fields and mark invalid fields with an error indicator
     */
    private void validate() {

        etCardNumber.validate();

        etCardExpiryDate.validate();

        etCardCvv.validate();

        etCardHolderName.validate();

        etPhoneNumber.validate();

        etCustomerName.validate();
    }


    /**
     * show the scan credit card screen
     */
    private void scanCard() {

        Intent intent = new ScanCardIntent.Builder(getActivity()).build();
        startActivityForResult(intent, Constant.REQUEST_CODE_SCAN_CARD);
    }

    /**
     * set the result of scan credit card details
     */
    private void setCard(@NonNull Card card) {
        Debug.trace("CardDetails", card.toString());
        etCardNumber.setText(card.getCardNumber());
        etCardHolderName.setText(card.getCardHolderName());
//        etCardExpiryDate.removeTextListener();
        etCardExpiryDate.setText(DateFormatUtils.stringToStringConversion(card.getExpirationDate(), "MM/yy", "MM/yyyy"));
//        etCardExpiryDate.addDateSlash(etCardExpiryDate.getEditableText());
//        etCardExpiryDate.addTextListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case Constant.REQUEST_CODE_SCAN_CARD:
                if (resultCode == Activity.RESULT_OK) {
                    Card card = data.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD);
                    setCard(card);
//                    Debug.trace("cardImage", Arrays.toString(data.getByteArrayExtra(ScanCardIntent.RESULT_CARD_IMAGE)));
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    @ScanCardIntent.CancelReason final int reason;
                    if (data != null) {
                        reason = data.getIntExtra(ScanCardIntent.RESULT_CANCEL_REASON, ScanCardIntent.BACK_PRESSED);
                    } else {
                        reason = ScanCardIntent.BACK_PRESSED;
                    }

                    if (reason == ScanCardIntent.ADD_MANUALLY_PRESSED) {

                    }
                } else if (resultCode == ScanCardIntent.RESULT_CODE_ERROR) {

                }
                break;
        }
    }
}
