package com.natercio.myhome.utils;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 10/10/13
 * Time: 5:23 PM
 */
public class MyHomeLog {

    private static final String LOG_TAG = "My Home";

    public static void LogInfo(String className, String info) {
        Log.i(LOG_TAG+"@"+className, info);
    }

    public static void LogError(String className, String error) {
        Log.e(LOG_TAG+"@"+className, error);
    }

    public static void LogDebug(String className, String debug) {
        Log.d(LOG_TAG+"@"+className, debug);
    }

    public static void LogWarning(String className, String warning) {
        Log.i(LOG_TAG+"@"+className, warning);
    }

    public static void LogVerbose(String className, String verbose) {
        Log.i(LOG_TAG+"@"+className, verbose);
    }
}
