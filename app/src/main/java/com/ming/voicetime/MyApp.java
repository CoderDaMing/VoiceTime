package com.ming.voicetime;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;

public class MyApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        //single
        instance = this;
        //统一初始化方法
        //设置渠道
        Bugly.setAppChannel(getApplicationContext(), getMetaDataChannel());
        //初始化
        Bugly.init(getApplicationContext(), "626d26ad65", false);
    }

    //获取value
    private String getMetaDataChannel() {
        String value = "";
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString("channel_name");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取MyApplication单例
     *
     * @return instance
     */
    public static Application getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex
        MultiDex.install(this);
    }
}
