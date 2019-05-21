package com.sdkdemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sdkdemo.R;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class CustomDialog {


    private static CustomDialog instance;
    private static Dialog mDialog;


    public static CustomDialog getInstance() {

        if (instance != null)
            return instance;
        else
            return instance = new CustomDialog();
    }

    public void showNoInternetConnectionDialog(Context context, View.OnClickListener onClickListener) {

        try {
            if (mDialog == null && context != null) {

                mDialog = new Dialog(context, R.style.dialogStyleWithoutAnimation);
                mDialog.setContentView(R.layout.custom_dialog_no_internet_connection);
                mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mDialog.setCancelable(false);

                Button btnTryAgain = mDialog.findViewById(R.id.btnTryAgain);
                if (onClickListener != null) {
                    btnTryAgain.setOnClickListener(onClickListener);
                } else {
                    btnTryAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                }
                mDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showProgress(Context mContext, String mTitle, boolean mIsCancelable) {

        try {
            if (mDialog == null && mContext != null) {

                mDialog = new Dialog(mContext, R.style.transparentStyle);
                mDialog.setContentView(R.layout.custom_dialog_progress_update);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView mDialogTitle = mDialog.findViewById(R.id.tv_customProgressBarTitle);
                if (mTitle != null && !mTitle.isEmpty()) {
                    mDialogTitle.setVisibility(View.VISIBLE);
                    mDialogTitle.setText(mTitle);
                } else {
                    mDialogTitle.setVisibility(View.GONE);
                }

                mDialog.setCancelable(mIsCancelable);

                if (mDialog != null) {
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
