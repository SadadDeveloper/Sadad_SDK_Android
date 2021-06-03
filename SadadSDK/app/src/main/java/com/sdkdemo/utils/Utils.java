package com.sdkdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.sdkdemo.R;

import java.util.Locale;

import com.sdkdemo.base.BaseActivity;

public class Utils {

    private Snackbar snackbar;

    public static void openApp(Context context, String appName, String packageName) {
        if (isAppInstalled(context, packageName))
            if (isAppEnabled(context, packageName))
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
            else
                Toast.makeText(context, appName + " app is not enabled.", Toast.LENGTH_LONG).show();
        else Toast.makeText(context, appName + " app is not installed.", Toast.LENGTH_LONG).show();
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    public static boolean isAppEnabled(Context context, String packageName) {
        boolean appStatus = false;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (ai != null) {
                appStatus = ai.enabled;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appStatus;
    }

    public static String formatCurrency(String currencyFormat, double value) {

        return String.format(Locale.ENGLISH, currencyFormat, value);

    }

    public static String formatCurrency(String currencyFormat, int value) {

        return String.format(Locale.ENGLISH, currencyFormat, value);
    }


    public void startActivitywithAnimation(Intent intent, AppCompatActivity activity, boolean isFinishActivity) {
        hideKeyboard(activity);
        activity.startActivity(intent);
        if (isFinishActivity) activity.finish();
        Configuration config = activity.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        } else {
            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }

    }

    public void startActivityForResultwithAnimation(Intent intent, AppCompatActivity activity, int requestCode, boolean isFinishActivity) {
        hideKeyboard(activity);
        activity.startActivityForResult(intent, requestCode);
        if (isFinishActivity) activity.finish();
        Configuration config = activity.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        } else {
            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }

    }

    public void startActivityForResultBottomToTop(Intent intent, AppCompatActivity activity, int requestCode, boolean isFinishActivity) {
        hideKeyboard(activity);
        activity.startActivityForResult(intent, requestCode);
        if (isFinishActivity) activity.finish();
        activity.overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

    public void startActivityForResultFadeIn(Intent intent, AppCompatActivity activity, int requestCode, boolean isFinishActivity) {
        hideKeyboard(activity);
        activity.startActivityForResult(intent, requestCode);
        if (isFinishActivity) activity.finish();
//        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void startActivityWithAllTaskAnimation(Intent intent, AppCompatActivity activity) {
        hideKeyboard(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
        Configuration config = activity.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        } else {
            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }
    }

    public void exitActivityWithAnimation(Intent intent, AppCompatActivity activity) {
        hideKeyboard(activity);
        activity.startActivity(intent);
        activity.finish();
        Configuration config = activity.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        } else {
            activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        }

    }

    public void exitActivityWithAnimation(AppCompatActivity activity) {
        hideKeyboard(activity);
        activity.finish();
        Configuration config = activity.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        } else {
            activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        }
//        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    public void exitActivityFromTopToBottom(AppCompatActivity activity) {
        hideKeyboard(activity);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_down, R.anim.slide_down);
//        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    public void exitActivityWithAllTaskAnimation(Intent intent, AppCompatActivity activity) {
        hideKeyboard(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
        Configuration config = activity.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        } else {
            activity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        }
//        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    public void hideKeyboard(AppCompatActivity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showKeyboard(AppCompatActivity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void showKeyboard(View view, AppCompatActivity activity) {
        if (view.requestFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void showSnackBar(View parentView, String message, String btnText, boolean isIndefinite, final BaseActivity.OnSnackListener mListener) {
        try {
            snackbar = Snackbar.make(parentView, message, (isIndefinite) ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG)
                    .setAction(btnText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            if (mListener != null) mListener.selectOk();
                        }
                    });

            snackbar.setActionTextColor(parentView.getResources().getColor(R.color.colorYellow));
            final View view = snackbar.getView();
            final TextView tv = view.findViewById(R.id.snackbar_text);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, parentView.getResources().getDimensionPixelSize(R.dimen._10sdp));
            tv.setMaxLines(3);
            final Button btn = view.findViewById(R.id.snackbar_action);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, parentView.getResources().getDimensionPixelSize(R.dimen._10sdp));

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

    public static void getQatarCurrencySuperScriptFormat(Context mContext, String
            currentFormat, TextView textView, boolean isBold, String prefix) {

        String[] fullstring;

        fullstring = currentFormat.split("\\.");
        String floatQAR = "";


        if (fullstring.length > 1) {
            floatQAR = fullstring[1];
        } else {
            floatQAR = "00 QAR";
        }

        String[] floatAndQAR = floatQAR.split(" ");
        if (isBold && (prefix == null)) {
            textView.setText(Html.fromHtml("<b>" + fullstring[0] + "</b>" + "." + floatAndQAR[0] + "<sup><small><small><b>" + floatAndQAR[1] + "</b></small></small></sup>"));
        } else if ((prefix != null) && !isBold) {
            textView.setText(Html.fromHtml("<big>" + prefix + "</big>" + "<b>" + fullstring[0] + "</b>" + "." + floatAndQAR[0] + "<sup><small><small>" + floatAndQAR[1] + "</small></small></sup>"));
        } else if ((prefix != null) && isBold) {
            textView.setText(Html.fromHtml("<big>" + prefix + "</big>" + "<b>" + fullstring[0] + "</b>" + "." + floatAndQAR[0] + "<sup><small><small><b>" + floatAndQAR[1] + "</b></small></small></sup>"));
        } else {
            textView.setText(Html.fromHtml("<b>" + fullstring[0] + "</b>" + "." + floatAndQAR[0] + "<sup><small><small>" + floatAndQAR[1] + "</small></small></sup>"));
        }
    }
}
