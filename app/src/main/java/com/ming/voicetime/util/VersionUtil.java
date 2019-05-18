package com.ming.voicetime.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 类描述:app版本工具类
 * 创建人: sunming
 * 创建时间：2019/3/12 13:13
 * version：1.0
 * Email:sunming@radacat.com
 */
public class VersionUtil {
//    /**
//     * 获得app versionCode
//     *
//     * @param context
//     * @return versionCode
//     */
//    public static int getVersionCode(Context context) {
//        int versionCode = 0;
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//            versionCode = packageInfo.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return versionCode;
//    }

    public static String getVerName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            return "" + versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
