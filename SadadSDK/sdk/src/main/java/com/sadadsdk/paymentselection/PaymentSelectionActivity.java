package com.sadadsdk.paymentselection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sadadsdk.api.RestClient;
import com.sadadsdk.base.BaseActivity;
import com.sadadsdk.base.Constant;
import com.sadadsdk.cardpayment.CardFragment;
import com.sadadsdk.listener.OnBackPressedEvent;
import com.sadadsdk.listener.TokenReceiver;
import com.sadadsdk.model.SadadOrder;
import com.sadadsdk.model.Tokenization;
import com.sadadsdk.model.Transaction;
import com.sadadsdk.sadad.SadadFragment;
import com.sadadsdk.transaction.BankFragment;
import com.sadadsdk.utils.ToastUtils;
import com.sadadsdk.utils.Utils;
import com.sufalamtech.sadad.sdk.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class PaymentSelectionActivity extends BaseActivity implements View.OnClickListener, TokenReceiver {

    public FragmentManager fragmentManager;
    private BottomSheetDialog bottomSheetDialog;
    private int transactionMode;
    private Tokenization tokenization;
    private String hash;
    /*Fragment variables*/
//    private Fragment currentFragment;
//    private View.OnClickListener onClickEvent;
    private OnBackPressedEvent onBackPressedEvent;
    private boolean doubleBackToExitPressedOnce = false;
    private SadadService transactionCallBack;
    private Utils utils;
    private Transaction transaction;

    public PaymentSelectionActivity() {

    }

    public SadadService getTransactionCallBack() {
        return transactionCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_payment_selection);
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        transactionCallBack = (SadadService) getIntent().getSerializableExtra(Constant.INTERFACE);
        utils = new Utils();
        generateCheckSum(getIntent().getBundleExtra(Constant.SDK_DATA));
    }

    private void generateCheckSum(Bundle extras) {

        tokenization = new Tokenization(PaymentSelectionActivity.this, extras);
        tokenization.generateCheckSum(this, getTransactionCallBack());
    }

    private void failedResult(String errorMsg, int errorCode) {
        /** 21-11-2019 : Add "errorCode" in function and passing in json. */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.STATUS_CODE, errorCode);
            jsonObject.put(Constant.MESSAGE, errorMsg);
            getTransactionCallBack().onTransactionFailed(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish();
    }


    /**
     * To show pop up from bottom to select valid payment method
     */
    private void showBottomSheetView() {

        bottomSheetDialog = new BottomSheetDialog(PaymentSelectionActivity.this, R.style.dialogStyle);
        bottomSheetDialog.setContentView(R.layout.dialog_payment_selection);
        Button btnCancel = bottomSheetDialog.findViewById(R.id.btnCancel);
        LinearLayoutCompat llCreditCard = bottomSheetDialog.findViewById(R.id.llCreditCard);
        LinearLayoutCompat llDebitCard = bottomSheetDialog.findViewById(R.id.llDebitCard);
        LinearLayoutCompat llPaySadad = bottomSheetDialog.findViewById(R.id.llPaySadad);

        if (btnCancel != null) {
            btnCancel.setOnClickListener(this);
        }
        if (llCreditCard != null) {
            llCreditCard.setOnClickListener(this);
        }
        if (llDebitCard != null) {
            llDebitCard.setOnClickListener(this);
        }
        if (llPaySadad != null) {
            llPaySadad.setOnClickListener(this);
        }

        bottomSheetDialog.setOwnerActivity(PaymentSelectionActivity.this);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnCancel) {
            try {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(Constant.STATUS_CODE, RestClient.FAILURE_CODE_400);
                        jsonObject.put(Constant.MESSAGE, PaymentSelectionActivity.this.getString(R.string.str_payment_cancelled_by_user));
                        PaymentSelectionActivity.this.getTransactionCallBack().onTransactionCancel(jsonObject.toString());
                        PaymentSelectionActivity.this.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, 500);
        } else if (i == R.id.llCreditCard) {
            try {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }

                transactionMode = Constant.CREDIT_CARD_TRANSACTION_MODE_ID;
                createTransaction();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (i == R.id.llDebitCard) {
            try {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }

                transactionMode = Constant.DEBIT_CARD_TRANSACTION_MODE_ID;
                createTransaction();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (i == R.id.llPaySadad) {
            if (bottomSheetDialog != null) {
                bottomSheetDialog.dismiss();
            }
            checkValidAmount();
        }
    }

    private void checkValidAmount() {
        createTransactionInstance();
        transaction.setAmount(tokenization.getSadadOrder().getString(SadadOrder.TXN_AMOUNT));
        transaction.setTransactionstatusId(Constant.TRANSACTION_STATUS_ID_INPROGRESS);
        transaction.setTransactionmodeId(transactionMode);
        transaction.setTransactionentityId(Constant.TRANSACTION_ENTITY_SDK);
        transaction.setTransaction_summary(tokenization.getRequestBody());
        transaction.setHash(hash);
        transaction.checkTransactionAmount(PaymentSelectionActivity.this, this, getTransactionCallBack());
    }

    private void flowToSadadApp() {
        if (Utils.isAppInstalled(PaymentSelectionActivity.this, Constant.SADAD_PACKAGE_NAME)) {
            if (Utils.isAppEnabled(PaymentSelectionActivity.this, Constant.SADAD_PACKAGE_NAME)) {
                try {

                    transactionMode = Constant.SADAD_TRANSACTION_MODE_ID;
                    Intent intent = getPackageManager().getLaunchIntentForPackage(Constant.SADAD_PACKAGE_NAME);

                    if (intent != null) {
                        intent.setFlags(0);
                        intent.putExtra(Constant.IS_FROM_SDK, true);
                        intent.putExtra(Constant.SDK_DATA, getIntent().getBundleExtra(Constant.SDK_DATA));
                        utils.startActivityForResultwithAnimation(intent, PaymentSelectionActivity.this, Constant.REQUEST_CODE_PAY_VIA_SADAD, false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(PaymentSelectionActivity.this, getString(R.string.str_sadad_app_is_not_enabled), Toast.LENGTH_LONG).show();
            }
        } else {
            //startActivityForResultwithAnimation(new Intent(PaymentSelectionActivity.this, SadadWebLoginActivity.class), Constant.REQUEST_CODE_PAY_VIA_SADAD, false);
            pushFragment(SadadFragment.newInstance(PaymentSelectionActivity.this, transactionCallBack), true, false, getIntent().getBundleExtra(Constant.SDK_DATA));
        }
    }

    void createTransaction() {
        createTransactionInstance();
        transaction.setAmount(tokenization.getSadadOrder().getString(SadadOrder.TXN_AMOUNT));
        transaction.setTransactionstatusId(Constant.TRANSACTION_STATUS_ID_INPROGRESS);
        transaction.setTransactionmodeId(transactionMode);
        transaction.setTransactionentityId(Constant.TRANSACTION_ENTITY_SDK);
        transaction.setTransaction_summary(tokenization.getRequestBody());
        transaction.setHash(hash);
        transaction.createTransaction(PaymentSelectionActivity.this, this, getTransactionCallBack());
    }

    private void createTransactionInstance() {
        if (transaction == null) {
            transaction = new Transaction();
        }

//        transaction = new Transaction();
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedEvent != null) {
            onBackPressedEvent.onBackPressed();
        }
        getTransactionCallBack().onBackPressedCancelTransaction();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == Constant.REQUEST_CODE_PAY_VIA_SADAD) {

            switch (resultCode) {
                case Constant.SDK_RESULT_CODE:
                    String data;
                    if (intent != null) {
                        data = intent.getStringExtra("data");
                        if (TextUtils.isEmpty(data)) {
                            failedResult(getString(R.string.str_transaction_failed), RestClient.FAILURE_CODE_400);
                        } else {
                            getTransactionCallBack().onTransactionResponse(data);
                            finish();
                        }
                    } else {
                        failedResult(getString(R.string.str_transaction_failed), RestClient.FAILURE_CODE_400);
                    }
                    break;

                default:
                    failedResult(getString(R.string.str_transaction_failed), RestClient.FAILURE_CODE_400);
                    break;
            }
        }
    }

    @Override
    public void onCheckSumReceived(String checkSum) {

        if (TextUtils.isEmpty(checkSum)) {
            failedResult(getString(R.string.str_invalid_checksum), RestClient.FAILURE_CODE_400);
        } else {
            try {
                this.hash = new JSONObject(checkSum).optString("hash", "");
                if (hash.isEmpty()) {
                    failedResult(getString(R.string.str_invalid_checksum), RestClient.FAILURE_CODE_400);
                } else {
                    showBottomSheetView();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                failedResult(getString(R.string.str_invalid_checksum), RestClient.FAILURE_CODE_400);
            }
        }
    }

    @Override
    public void onCheckSumFailed(String errorMsg, int errorCode) {
        /** 21-11-2019 : If errorCode FAILURE_CODE_403, so pass the diffrent message*/
        if (errorCode == RestClient.FAILURE_CODE_403) {
            failedResult(getString(R.string.str_merchant_has_no_permission_to_use_sdk), errorCode);
        } else {
            failedResult(getString(R.string.str_invalid_checksum), RestClient.FAILURE_CODE_400);
        }

    }

    @Override
    public void onCreateTransaction(String data) {

        if (data != null && !data.isEmpty()) {

            switch (transactionMode) {

                case Constant.CREDIT_CARD_TRANSACTION_MODE_ID:

                    pushFragment(CardFragment.newInstance(data, getTransactionCallBack()), true, false, null);
                    break;

                case Constant.DEBIT_CARD_TRANSACTION_MODE_ID:
                    JSONObject jsonObject = new JSONObject();
                    try {
                        JSONObject transactionResponse = new JSONObject(data);
                        jsonObject.put(Constant.TRANSACTION_MODE, Constant.DEBIT_CARD_TRANSACTION_MODE_ID);
                        jsonObject.put(Constant.AMOUNT, transactionResponse.optString(Constant.AMOUNT, "0.00"));
                        jsonObject.put(Constant.INVOICE_NUMBER, transactionResponse.optString("invoicenumber"));
                        pushFragment(BankFragment.newInstance(jsonObject.toString(), PaymentSelectionActivity.this, transactionCallBack), true, false, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        failedResult(getString(R.string.str_transaction_failed), RestClient.FAILURE_CODE_400);
                    }
                    break;
            }
        } else {
            failedResult(getString(R.string.str_transaction_failed), RestClient.FAILURE_CODE_400);
        }
    }

    @Override
    public void onTransactionFailed(String errorMsg) {
        failedResult(errorMsg, RestClient.FAILURE_CODE_400);
    }

    @Override
    public void onPatchTransaction(String data) {
        getTransactionCallBack().onTransactionResponse(data);
        finish();
    }

    @Override
    public void onPatchTransactionFailed(String errorMsg) {
        failedResult(errorMsg, RestClient.FAILURE_CODE_430);
//        getTransactionCallBack().onTransactionFailed(errorMsg);
        finish();
    }

    @Override
    public void onSadadCall(String data) {
        getTransactionCallBack().onTransactionResponse(data);
        finish();
    }

    @Override
    public void onSuccessCheckTransactionLimit(String response) {
        flowToSadadApp();
    }

    @Override
    public void onFailureCheckTransactionLimit(String errorMsg, int responseCode) {
        failedResult(errorMsg, responseCode);
    }

    public void pushFragment(Fragment fragment, boolean addToBackStack, boolean shouldAnimate, Bundle bundle) {

//        currentFragment = fragment;
//        onClickEvent = (View.OnClickListener) fragment;
        onBackPressedEvent = (OnBackPressedEvent) fragment;

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (shouldAnimate) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        }
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getCanonicalName());
        }

        // Replace whatever is in the fragment_container dataView with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.replace(R.id.content, fragment, fragment.getClass().getCanonicalName());

        //currentFragment = (Fragment) onClickEvent;

        // Commit the transaction
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void popBackFragment() {

        try {
            int backStackCount = fragmentManager.getBackStackEntryCount();

            if (backStackCount > 1) {

                FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackCount - 2);

                String className = backStackEntry.getName();

                Fragment fragment = fragmentManager.findFragmentByTag(className);

//                currentFragment = fragment;
//                onClickEvent = (View.OnClickListener) fragment;
                onBackPressedEvent = (OnBackPressedEvent) fragment;

                fragmentManager.popBackStack();
            } else {
                if (doubleBackToExitPressedOnce) {
                    finish();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                ToastUtils.getInstance().showMessage(PaymentSelectionActivity.this, getString(R.string.str_click_back_again));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Remove all fragments from back stack*/
    public void removeAllFragment() {

        int fragmentsCount = fragmentManager.getBackStackEntryCount();

        if (fragmentsCount > 0) {

            FragmentTransaction ft = fragmentManager.beginTransaction();

            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commitAllowingStateLoss();
        }
    }

    /*Remove Fragments until provided Fragment class*/
    public void removeFragmentUntil(Class<?> fragmentClass) {

        try {
            int backStackCountMain = fragmentManager.getBackStackEntryCount();
            if (backStackCountMain > 1) {
                /*Note: To eliminate pop menu fragments and push base menu fragment animation effect at a same times*/

                int backStackCount = backStackCountMain;
                for (int i = 0; i < backStackCountMain; i++) {
                    FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount - 1);
                    String str = backEntry.getName();
                    Fragment fragment = fragmentManager.findFragmentByTag(str);
                    if (fragment != null && fragment.getClass().getCanonicalName().equals(fragmentClass.getCanonicalName())) {
                        onBackPressedEvent = (OnBackPressedEvent) fragment;
                        break;
                    } else
                        fragmentManager.popBackStack();

                    backStackCount--;
                }

            } else
                finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
