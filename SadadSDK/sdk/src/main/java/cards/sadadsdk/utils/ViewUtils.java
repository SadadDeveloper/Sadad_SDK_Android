package cards.sadadsdk.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;

import com.sufalamtech.sadad.sdk.R;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class ViewUtils {

    public static int dp2px(Context context, float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()));
    }

    public static boolean isDarkBackground(AppCompatActivity activity) {
        int color = activity.getResources().getColor(R.color.colorWhite);
        try {
            Drawable background = activity.getWindow().getDecorView().getRootView().getBackground();
            if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
            }
        } catch (Exception ignored) {
        }

        double luminance = (0.2126 * Color.red(color)) + (0.7152 * Color.green(color)) +
                (0.0722 * Color.blue(color));

        return luminance < 128;
    }
}
