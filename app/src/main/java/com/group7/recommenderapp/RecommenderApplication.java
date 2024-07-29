package com.group7.recommenderapp;

import android.app.Application;
import com.group7.recommenderapp.util.GlobalExceptionHandler;

public class RecommenderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
    }
}
