package com.sadadsdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PrefUtils {

    public static final String ACCESS_TOKEN = "accessToken";

    private static PrefUtils prefUtils;
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private static final Method sApplyMethod = findApplyMethod();
    private static final String PREF_NAME = "com.sadadsdk";
    private static final int PRIVATE_MODE = 0;

    /**
     * This Keys are taken for app tutorials for different screens
     */

    private static final String UTF8 = "utf-8";
    private static final char[] SEKRIT = "sadadapp".toCharArray();
    private static Context mContext;


    public static PrefUtils getInstance(Context context) {
        mContext = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
        if (prefUtils == null) {
            prefUtils = new PrefUtils();
        }
        return prefUtils;
    }

    public boolean containKey(String key) {
        return pref.contains(key);
    }

    public void deletePreference(final String key) {
        editor.remove(key);
        editor.apply();
        editor.commit();
    }

    public void deletePreferences(final String[] keys) {
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                editor.remove(key);
                editor.commit();
                editor.apply();
            }
        }
    }


    /**
     * Delete all preference of app except passed argument
     */
    public void deletePreferencesExcept(final ArrayList<String> keysNotDelete) {

        Map<String, ?> keys = pref.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Debug.trace("img_map values", entry.getKey() + ": " +
                    entry.getValue().toString());
            if (!keysNotDelete.contains(entry.getKey())) {
                editor.remove(entry.getKey());
                editor.apply();
            }
        }

    }

    private static Method findApplyMethod() {
        try {
            final Class<SharedPreferences.Editor> cls = SharedPreferences.Editor.class;
            return cls.getMethod("apply");
        } catch (final NoSuchMethodException unused) {
            unused.printStackTrace();
        }
        return null;
    }

    public static void apply(final SharedPreferences.Editor editor) {
        if (sApplyMethod != null) {
            try {
                sApplyMethod.invoke(editor);
                return;
            } catch (final InvocationTargetException | IllegalAccessException unused) {
                // fall through
            }
        }
        editor.commit();
    }


    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {

        return pref.getString(key, defaultValue);
    }

    public void setInt(String key, int value) {

        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {

        return pref.getInt(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {

        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }

    public void setLong(String key, long value) {

        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defaultValue) {

        return pref.getLong(key, defaultValue);
    }

    public void setFloat(String key, float value) {

        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float defaultValue) {

        return pref.getFloat(key, defaultValue);
    }


    @SuppressLint("HardwareIds")
    protected String encrypt(String value) {

        try {
            final byte[] bytes = value != null ? value.getBytes(UTF8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(UTF8.getBytes(Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID)), 20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), UTF8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("HardwareIds")
    protected String decrypt(String value) {
        try {
            final byte[] bytes = value != null ? Base64.decode(value, Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(UTF8), 20));
            return new String(pbeCipher.doFinal(bytes), UTF8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
