package com.sdkdemo.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.ContextThemeWrapper;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class LocaleUtils {
    private static Locale sLocale;
    public static boolean isLocaleChanged = false;

    public static void setLocale(Context context, Locale locale) {
        sLocale = locale;
        if (sLocale != null) {
            Locale.setDefault(sLocale);
            setLocaleForPref(context, locale.getLanguage());
        }
    }

    public static String getLocale(Context context) {
        String lan = getLocaleFromPref(context);
        if (!lan.equals(""))
            return lan;
        else
            return "en";
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {
        if (sLocale != null) {
            Configuration configuration = new Configuration();
            configuration.setLocale(sLocale);
            wrapper.applyOverrideConfiguration(configuration);
        }
    }

    public static void updateConfig(Application app, Configuration configuration) {
        if (sLocale != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Wrapping the configuration to avoid Activity endless loop
            Configuration config = new Configuration(configuration);
            config.locale = sLocale;
            Resources res = app.getBaseContext().getResources();
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
    }

    protected static void setLocaleForPref(Context context, String language) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("language", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lan", language);
        editor.commit();
    }

    protected static String getLocaleFromPref(Context context) {
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences("language", MODE_PRIVATE);
        return sharedPreferences.getString("lan", "");
    }


}