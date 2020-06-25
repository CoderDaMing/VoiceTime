package com.ming.voicetime.util;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.ming.voicetime.MyApp;

import java.util.Locale;

public class TextToSpeechUtil {
    //region 单例
    private static TextToSpeechUtil instance;

    private TextToSpeechUtil() {

    }

    public static TextToSpeechUtil getInstance() {
        if (instance == null) {
            synchronized (TextToSpeechUtil.class) {
                if (instance == null) {
                    instance = new TextToSpeechUtil();
                }
            }
        }
        return instance;
    }
    //endregion

    private static final String TAG = "TTS";
    private TextToSpeech textToSpeech = null;//创建自带语音对象
    private boolean isPlay = false;

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    private void initTTS() {
        if (textToSpeech == null) {
            //实例化自带语音对象
            textToSpeech = new TextToSpeech(MyApp.getInstance(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setPitch(1.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    //判断是否支持下面两种语言
                    int result = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                    boolean unEnable = (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED);
                    if (unEnable) {
                        //设置语言en
                        textToSpeech.setLanguage(Locale.US);
                    }
                } else {
                    Toast.makeText(MyApp.getInstance(), "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    public void speakCurrentTime(long time, boolean isWholeMin) {
        setPlay(true);
        initTTS();
        //输入中文，若不支持的设备则不会读出来
        String data = TimeDateUtil.long2String(time, TimeDateUtil.hms);
        Log.i(TAG, ": speakCurrenTime:" + data + " isWholeMin:" + isWholeMin);
        String dataMin = TimeDateUtil.long2String(time, TimeDateUtil.hm);
        if (isWholeMin) {
            textToSpeech.speak(dataMin, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void stop() {
        setPlay(false);
        if (textToSpeech != null) {
            textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        }
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.shutdown(); // 关闭，释放资源
        }
    }
}
