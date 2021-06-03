package com.sadadsdk.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Hitesh Sarsava on 12/3/19.
 */
public class Debug {

    private static final String TAG = "tag";
    private static final String LOG_FILE_NAME = "/sadadsdk.txt";
    private static boolean isDebug = true;
    private static boolean isPersistant = false;

    /**
     * This method print message in LOG window
     *
     * @param msg(String) : message to print in log
     */
    public static void trace(final String msg) {
        if (isDebug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.i(TAG, msg);
            }
            if (isPersistant) {
                appendLog(msg);
            }
        }
    }

    /**
     * This method print message in LOG window
     * with keyTag
     *
     * @param tag(String) : it shows the particular class name
     * @param msg(String) : to showProgressBar the appropriate result
     */
    public static void trace(final String tag, final String msg) {
        if (isDebug) {
            if (!TextUtils.isEmpty(msg)) {
                Log.i(tag, msg);
            }
        }
    }

    /**
     * This method create new text file and save all the trace in CodeBase.txt
     *
     * @param text(String) : it pass the appropriate result
     */
    private static void appendLog(final String text) {
        final File logFile = new File(Environment.getExternalStorageDirectory() + LOG_FILE_NAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // BufferedWriter for performance, true to set append to file flag
            final BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
