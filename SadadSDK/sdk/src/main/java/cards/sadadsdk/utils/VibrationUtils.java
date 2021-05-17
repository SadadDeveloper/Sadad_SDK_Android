package cards.sadadsdk.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class VibrationUtils {

    @SuppressLint("MissingPermission")
    public static void vibrate(Context context, int duration) {
        if (hasVibrationPermission(context)) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(duration);
        }
    }

    public static boolean hasVibrationPermission(Context context) {
        return (context.getPackageManager().checkPermission(Manifest.permission.VIBRATE,
                context.getPackageName()) == PackageManager.PERMISSION_GRANTED);
    }
}
