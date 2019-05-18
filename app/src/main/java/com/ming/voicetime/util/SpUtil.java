package com.ming.voicetime.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ming.voicetime.MyApp;


/**
 * description:用于sp存储
 */
public class SpUtil {

    public static final String SP_FILE_NAME = "voice_time_file";
    public static final String SP_SAVE_TIME_KEY = "voice_save_time_key";

    private static SharedPreferences sp;


    /**
     * @param fileName
     * @param key
     * @param value
     */
    public static void putValue(String fileName, String key, Object value) {
        if (sp == null) {
            sp = MyApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }

        if (value instanceof String) {
            editor.putString(key, (String) value);
        }

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }

        if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }

        if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.apply();
    }


    /**
     * @param fileName
     * @param key
     * @param defValue
     * @return
     */
    public static Object getValue(String fileName, String key, Object defValue) {

        if (sp == null) {
            sp = MyApp.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        Object value = null;
        if (defValue instanceof Boolean) {
            value = sp.getBoolean(key, (Boolean) defValue);
        }

        if (defValue instanceof String) {
            value = sp.getString(key, (String) defValue);
        }

        if (defValue instanceof Long) {
            value = sp.getLong(key, (Long) defValue);
        }

        if (defValue instanceof Float) {
            value = sp.getFloat(key, (Float) defValue);
        }

        if (defValue instanceof Integer) {
            value = sp.getInt(key, (Integer) defValue);
        }

        return value;
    }
}
