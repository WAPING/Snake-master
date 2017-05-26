package com.example.android.snake;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by kangxu on 2017/5/26.
 */

public class MyApplication extends Application {
    private static RefWatcher sRefWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        sRefWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher() {
        return sRefWatcher;
    }
}
