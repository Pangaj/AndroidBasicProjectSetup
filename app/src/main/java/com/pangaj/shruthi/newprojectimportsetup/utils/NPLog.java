package com.pangaj.shruthi.newprojectimportsetup.utils;

import android.util.Log;

import com.pangaj.shruthi.newprojectimportsetup.NPApplication;

/**
 * Created by Manikandan on 24/02/17.
 */
public class NPLog {
    /**
     * Verbose level logcat message (only output for debug builds)
     *
     * @param tag     for tag
     * @param message for message
     */
    public static void v(String tag, String message) {
        Log.v(tag, message);
    }

    /**
     * Debug level logcat message (only output for debug builds)
     *
     * @param tag     for tag
     * @param message for message
     */
    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    /**
     * Information level logcat message (always output)
     *
     * @param tag     for tag
     * @param message for message
     */
    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    /**
     * Warning level logcat message (always output)
     *
     * @param tag     for tag
     * @param message for message
     */

    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    /**
     * Error level logcat message (always output)
     *
     * @param tag       for tag
     * @param message   for message
     * @param exception for exception
     */

    public static void e(String tag, String message, Exception exception) {
        NPApplication.getInstance().trackException(exception);
        Log.e(tag, message);
    }

    public static void e(String tag, String message, Throwable throwable) {
        NPApplication.getInstance().trackEvent("Exception", throwable.getMessage(), message);
        NPApplication.getInstance().trackException(throwable);
        Log.e(tag, message);
    }
}