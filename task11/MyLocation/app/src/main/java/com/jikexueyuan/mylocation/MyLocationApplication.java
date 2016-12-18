package com.jikexueyuan.mylocation;


import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * 主Application，SDKInitializer
 */
public class MyLocationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        SDKInitializer.initialize(getApplicationContext());
    }
}
