package cards.sadadsdk.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sufalamtech.sadad.sdk.R;


/**
 * Created by ${hitesh} on 12/6/2016.
 */
public class ToastUtils {

    private static ToastUtils ourInstance;
    private static Toast mToast;
    private boolean showToast = true;

    private ToastUtils() {
    }

    public static ToastUtils getInstance() {

        if (ourInstance == null)
            return new ToastUtils();
        else
            return ourInstance;

    }

    public void showMessage(Context context, String mMessage) {

        mToast = new Toast(context);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.BOTTOM, 0, 200);

        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null, false);
        mToast.setView(view);

        TextView toastMessage = view.findViewById(R.id.tv_toast);
        toastMessage.setText(mMessage);

        if (showToast)
            mToast.show();
    }

    public void showMessage(Context context, String mMessage, int duration) {

        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.NO_GRAVITY, 0, 0);

        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null, false);
        mToast.setView(view);

        TextView toastMessage = view.findViewById(R.id.tv_toast);
        toastMessage.setText(mMessage);

        if (showToast)
            mToast.show();
    }

    public void cancel() {

        if (mToast != null) {
            mToast.cancel();

        }
    }
}
