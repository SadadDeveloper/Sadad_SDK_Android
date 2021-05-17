package cards.sadadsdk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.sufalamtech.sadad.sdk.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import cards.sadadsdk.base.BaseActivity;

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

    /**
     * Get IP address from first non-localhost interface
     *
     * @return address or empty string
     */
    public static String getIPAddress() {
        try {
            return getLocalIpAddress();

        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    private static String getLocalIpAddress() {
        String ip = "N/A";
        try {

            /*Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()){
                Enumeration inetAddresses = ((NetworkInterface)networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()){
                    InetAddress inetAddress = (InetAddress)inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address){
                        ip = inetAddress.getHostAddress().toString();
                        Debug.trace("IpAddress", ip);
                    }
                }
            }*/


            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    // for getting IPV4 format
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {

                        ip = inetAddress.getHostAddress();
                        Debug.trace("IpAddress", ip);
                        // return inetAddress.getHostAddress().toString();
                        //return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Debug.trace("SocketException Address", ex.toString());
        }catch (Exception ex) {
            Debug.trace("IP Address", ex.toString());
        }

        return ip;
    }
}
