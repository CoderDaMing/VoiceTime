package com.ming.voicetime;

import android.app.Application;

import com.tencent.bugly.Bugly;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //统一初始化方法
        Bugly.init(getApplicationContext(), "626d26ad65", false);
    }
}
