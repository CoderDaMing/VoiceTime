package com.ming.voicetime.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ming.voicetime.R;
import com.ming.voicetime.util.TextToSpeechUtil;
import com.ming.voicetime.util.TimeDateUtil;

import java.sql.Time;

public class ForegroundService extends Service {
    private static final String TAG = ForegroundService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 100;
    private static final String CHANNEL_ID = "voice_time";
    private static final String CHANNEL_NAME = "voicetime foregroundService";

    private NotificationManager mNotificationManager;

    private Notification getNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_report_time))
                .setTicker(getString(R.string.app_report_time))
                .setOngoing(true)
                .setAutoCancel(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
        return builder.build();
    }

    private final IBinder mBinder = new LocalBinder();

    /*用于检查绑定的活动是否真的消失了，并且没有作为方向更改的一部分解除绑定。仅当前者发生时，我们才创建前台服务通知。 */
    private boolean mChangingConfiguration = false;

    private Handler mServiceHandler;

    public ForegroundService() {
    }

    @Override
    public void onCreate() {
        //初始化NotificationManager
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Android O需要一个通知频道。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知的渠道
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            //为通知管理器设置通知通道。
            mNotificationManager.createNotificationChannel(mChannel);
        }

        //HandlerThread
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                sendTask(TimeDateUtil.long2String(System.currentTimeMillis(),TimeDateUtil.ss).equals("00"));
            }
        };
    }

    public void clearTask() {
        mServiceHandler.removeCallbacksAndMessages(null);
        TextToSpeechUtil.getInstance().stop();
    }

    public void sendTask(boolean isWholeMin) {
        mServiceHandler.removeCallbacksAndMessages(null);
        TextToSpeechUtil.getInstance().speakCurrentTime(System.currentTimeMillis(), isWholeMin);
        mServiceHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        //告诉系统在终止服务后不要尝试重新创建该服务。
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //当客户端(MainActivity）到达前台并与此服务绑定时调用。该服务应该不再是前台服务。
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        //当客户端（MainActivity）返回到前台并再次与该服务绑定时调用。当这种情况发生时，服务应该不再是前台服务。
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        //当最后一个客户端（MainActivity）从此服务解除绑定时调用。如果由于MainActivity中的配置更改而调用了此方法，则
        // 不执行任何操作。否则，我们将此服务作为前台服务。
        if (!mChangingConfiguration) {
            Log.i(TAG, "Starting foreground service");
            if (TextToSpeechUtil.getInstance().isPlay()) {
                sendTask(true);
            }
            startForeground(NOTIFICATION_ID, getNotification(this));
        }
        //确保客户端重新绑定时调用onRebind（）。
        return true;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
    }

    public class LocalBinder extends Binder {
        public ForegroundService getService() {
            return ForegroundService.this;
        }
    }
}