package com.ming.voicetime.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    public static final String SP_FILE = "sp_file";
    public static final String CITY_NAME = "city_name";
    public static final String DEFAULT_CITY = "北京市";

    public static void putString(Context context, String file, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(Context context, String file, String key, String defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }
}
