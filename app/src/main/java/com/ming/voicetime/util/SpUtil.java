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


    public static void putTimeValue(long value) {
        if (sp == null) {
            sp = MyApp.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(SP_SAVE_TIME_KEY, value);
        editor.apply();
    }

    public static long getTimeValue() {

        if (sp == null) {
            sp = MyApp.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        }

        return sp.getLong(SP_SAVE_TIME_KEY, TimeDateUtil.ONE_MINTER);
    }
}
