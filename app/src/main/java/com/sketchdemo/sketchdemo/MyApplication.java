package com.sketchdemo.sketchdemo;

import android.app.Application;



/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DisplayUtils.init(getApplicationContext());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();


    }
}