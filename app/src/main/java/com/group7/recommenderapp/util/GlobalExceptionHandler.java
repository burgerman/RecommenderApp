package com.group7.recommenderapp.util;

import android.util.Log;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "GlobalExceptionHandler";

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "Uncaught exception", ex);
        // You can add additional error reporting logic here
    }
}
