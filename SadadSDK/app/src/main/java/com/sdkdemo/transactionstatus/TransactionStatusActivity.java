package com.sdkdemo.transactionstatus;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.sdkdemo.R;
import com.sdkdemo.base.BaseActivity;
import com.sdkdemo.base.Constant;
import com.sdkdemo.home.HomeActivity;
import com.sdkdemo.utils.ButtonRobotoRegular;
import com.sdkdemo.utils.DateFormatConversion;
import com.sdkdemo.utils.TextViewRobotoBold;
import com.sdkdemo.utils.TextViewRobotoRegular;
import com.sdkdemo.utils.Utils;

import java.util.Date;

public class TransactionStatusActivity extends BaseActivity implements View.OnClickListener {


    AppCompatImageView ivTransferStatus;
    TextViewRobotoRegular tvAmount;
    TextViewRobotoBold tvTransferStatus;
    TextViewRobotoRegular tvCompletedTime;
    TextViewRobotoRegular tvReceiverName;
    LinearLayout llReceiverName;
    TextViewRobotoRegular tvTransactionId;
    LinearLayout llTransactionId;
    ButtonRobotoRegular btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_status);

        initViews();
    }

    private void initViews() {

        ivTransferStatus = findViewById(R.id.ivTransferStatus);
        tvAmount = findViewById(R.id.tvAmount);
        tvTransferStatus = findViewById(R.id.tvTransferStatus);
        tvCompletedTime = findViewById(R.id.tvCompletedTime);
        tvReceiverName = findViewById(R.id.tvReceiverName);
        tvTransactionId = findViewById(R.id.tvTransactionId);
        llTransactionId = findViewById(R.id.llTransactionId);
        llReceiverName = findViewById(R.id.llReceiverName);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(this);

        int transactionStatus = getIntent().getIntExtra(Constant.TRANSACTION_STATUS, Constant.TRANSACTION_STATUS_ID_FAILED);

        double amount = getIntent().getDoubleExtra(Constant.AMOUNT, 0.0);
        String completedTime = getIntent().hasExtra(Constant.COMPLETED_TIME) ? getIntent().getStringExtra(Constant.COMPLETED_TIME) != null ? getIntent().getStringExtra(Constant.COMPLETED_TIME) : DateFormatConversion.timestampToDateStrConversion(new Date().getTime(), DateFormatConversion.yyyy_MM_dd_T_HH_mm_ss_SSS_Z) : DateFormatConversion.timestampToDateStrConversion(new Date().getTime(), DateFormatConversion.yyyy_MM_dd_T_HH_mm_ss_SSS_Z);
        String receiverName = getIntent().hasExtra(Constant.RECEIVER_NAME) ? getIntent().getStringExtra(Constant.RECEIVER_NAME) != null ? getIntent().getStringExtra(Constant.RECEIVER_NAME) : "" : "";
        String transactionId = getIntent().hasExtra(Constant.TRANSACTION_ID) ? getIntent().getStringExtra(Constant.TRANSACTION_ID) != null ? getIntent().getStringExtra(Constant.TRANSACTION_ID) : "" : "";
        String transferStatusMsg = getIntent().hasExtra(Constant.TRANSACTION_STATUS_MESSAGE) ? getIntent().getStringExtra(Constant.TRANSACTION_STATUS_MESSAGE) != null ? getIntent().getStringExtra(Constant.TRANSACTION_STATUS_MESSAGE) : "" : "";

        switch (transactionStatus) {

            case Constant.TRANSACTION_STATUS_ID_SUCCESS:
                playSound(R.raw.success);
                ivTransferStatus.setImageResource(R.drawable.ic_check_mark_round_green);
                tvTransferStatus.setTextColor(getResources().getColor(R.color.colorMedGreen));
                if (transferStatusMsg.isEmpty()) {
                    transferStatusMsg = getString(R.string.str_payment_success);
                }
                tvTransferStatus.setText(transferStatusMsg);
                break;

            case Constant.TRANSACTION_STATUS_ID_FAILED:
                playSound(R.raw.failure);
                ivTransferStatus.setImageResource(R.drawable.ic_close_rounded_orange);
                tvTransferStatus.setTextColor(getResources().getColor(R.color.colorOrange));
                if (transferStatusMsg.isEmpty()) {
                    transferStatusMsg = getString(R.string.str_payment_failed);
                }
                tvTransferStatus.setText(transferStatusMsg);
                break;
        }

        Utils.getQatarCurrencySuperScriptFormat(this, Utils.formatCurrency(Constant.COMMA_CURRENCY_FORMAT, amount), tvAmount, true, null);
        tvCompletedTime.setText(DateFormatConversion.stringToStringConversion(completedTime, DateFormatConversion.yyyy_MM_dd_T_HH_mm_ss_SSS_Z, DateFormatConversion.dd_MMM_yyyy_hh_mm_a));
        if (receiverName.isEmpty()) {
            llReceiverName.setVisibility(View.GONE);
        } else {
            llReceiverName.setVisibility(View.VISIBLE);
            tvReceiverName.setText(receiverName);
        }

        if (transactionId.isEmpty()) {
            llTransactionId.setVisibility(View.GONE);
        } else {
            llTransactionId.setVisibility(View.VISIBLE);
            tvTransactionId.setText(transactionId);
        }
    }

    void playSound(final int sound) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mp = MediaPlayer.create(TransactionStatusActivity.this, sound);
                mp.setVolume(1, 1);
                mp.start();
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {

        if (v == btnOk) {
            exitActivityWithAnimation(new Intent(TransactionStatusActivity.this, HomeActivity.class));
        }
    }

    @Override
    public void onBackPressed() {

    }
}
