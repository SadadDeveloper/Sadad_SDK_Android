package com.sadadsdk.utils;

import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Hitesh Sarsava on 11/3/19.
 */
public class ColorUtils {

    public static int getColor(AppCompatActivity activity, String attr, int fallbackColor) {
        TypedValue color = new TypedValue();
        try {
            int colorId = activity.getResources().getIdentifier(attr, "attr", activity.getPackageName());
            if (activity.getTheme().resolveAttribute(colorId, color, true)) {
                return color.data;
            }
        } catch (Exception ignored) {
        }

        return activity.getResources().getColor(fallbackColor);
    }
}
