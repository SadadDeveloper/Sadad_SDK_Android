package com.sadadsdk.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.sadadsdk.R;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class BaseActivity extends AppCompatActivity {

    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startActivitywithAnimation(Intent intent, boolean isFinishActivity) {
        hideKeyboard();
        startActivity(intent);
        if (isFinishActivity) finish();
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        } else {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }

    }

    public void startActivityForResultwithAnimation(Intent intent, int requestCode, boolean isFinishActivity) {
        hideKeyboard();
        startActivityForResult(intent, requestCode);
        if (isFinishActivity) finish();
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        } else {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }

    }

    public void startActivityForResultBottomToTop(Intent intent, int requestCode, boolean isFinishActivity) {
        hideKeyboard();
        startActivityForResult(intent, requestCode);
        if (isFinishActivity) finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

    public void startActivityForResultFadeIn(Intent intent, int requestCode, boolean isFinishActivity) {
        hideKeyboard();
        startActivityForResult(intent, requestCode);
        if (isFinishActivity) finish();
//        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void startActivityWithAllTaskAnimation(Intent intent) {
        hideKeyboard();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        } else {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }
    }

    public void exitActivityWithAnimation(Intent intent) {
        hideKeyboard();
        startActivity(intent);
        finish();
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        } else {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        }

    }

    public void exitActivityWithAnimation() {
        hideKeyboard();
        finish();
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        } else {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        }
//        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    public void exitActivityFromTopToBottom() {
        hideKeyboard();
        finish();
        overridePendingTransition(R.anim.slide_down, R.anim.slide_down);
//        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    public void exitActivityWithAllTaskAnimation(Intent intent) {
        hideKeyboard();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        } else {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        }
//        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void showSnackBar(View parentView, String message, String btnText, boolean isIndefinite, final OnSnackListener mListener) {
        try {
            snackbar = Snackbar.make(parentView, message, (isIndefinite) ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG)
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            if (mListener != null) mListener.selectOk();
                        }
                    });

            snackbar.setActionTextColor(getResources().getColor(R.color.colorYellow));
            final View view = snackbar.getView();
            final TextView tv = view.findViewById(R.id.snackbar_text);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._10sdp));
            tv.setMaxLines(3);
            final Button btn = view.findViewById(R.id.snackbar_action);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._10sdp));

            snackbar.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dismissSnackBar() {
        try {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface OnSnackListener {
        void selectOk();
    }
}

