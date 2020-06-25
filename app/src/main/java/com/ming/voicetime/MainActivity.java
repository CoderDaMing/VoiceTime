package com.ming.voicetime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.ming.voicetime.permissions.PermissionHelper;
import com.ming.voicetime.permissions.PermissionCallBack;
import com.ming.voicetime.permissions.PermissionsUtil;
import com.ming.voicetime.service.ForegroundService;
import com.ming.voicetime.util.SpUtil;
import com.ming.voicetime.util.TextToSpeechUtil;
import com.ming.voicetime.util.ToastUtil;
import com.ming.voicetime.util.VersionUtil;
import com.ming.voicetime.weather.WeatherSearchActivity;

public class MainActivity extends AppCompatActivity implements PermissionCallBack, View.OnClickListener, WeatherSearch.OnWeatherSearchListener ,SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "MainActivity";
    private PermissionHelper permissionHelper;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipe_refresh;

    //region 生命周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * 用于判断这个Activity的启动标志，看它所在的应用是不是从后台跑到前台的。如果是，则直接把它finish（）掉，
         * 然后系统会去Activity启动历史栈查询上一个activity，然后再新建它，所以还原到了我们按home键出去的那个界面。
         */
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipe_refresh = findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(this);

        city = (TextView) findViewById(R.id.city);
        city.setOnClickListener(this);
        reporttime1 = (TextView) findViewById(R.id.reporttime1);
        weather = (TextView) findViewById(R.id.weather);
        Temperature = (TextView) findViewById(R.id.temp);
        wind = (TextView) findViewById(R.id.wind);
        humidity = (TextView) findViewById(R.id.humidity);

        TextView tv_version = findViewById(R.id.tv_version);
        tv_version.setText("版本：" + VersionUtil.getVerName(this));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        initPermissionsHelper();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        //绑定到服务。如果服务处于前台模式，则会向服务发出信号，因为此活动位于前台，所以该服务可以退出前台模式
        bindService(new Intent(this, ForegroundService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherReport();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: ");
        if (mBound) {
            //取消绑定服务。这向服务发出信号，表明该活动不再位于前台，并且该服务可以通过将自身提升为前台服务来进行响应。
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        if (mService != null) {
            mService.clearTask();
        }
        TextToSpeechUtil.getInstance().destroy();
    }
    //endregion

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city:
                startActivity(new Intent(this, WeatherSearchActivity.class));
                break;
            case R.id.fab:
                if (TextToSpeechUtil.getInstance().isPlay()) {
                    if (mService != null) {
                        mService.clearTask();
                    }
                    fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                    Snackbar.make(v, "Stop Task", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if (mService != null) {
                        mService.sendTask(true);
                    }
                    fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    Snackbar.make(v, "Start Task", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        getWeatherReport();
    }

    //region 天气
    private TextView city;
    private TextView reporttime1;
    private TextView weather;
    private TextView Temperature;
    private TextView wind;
    private TextView humidity;
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private String cityname = null;

    private void getWeatherReport() {
        String spName = SpUtil.getString(MainActivity.this, SpUtil.SP_FILE, SpUtil.CITY_NAME, SpUtil.DEFAULT_CITY);
        if (spName.equals(cityname)){
            swipe_refresh.setRefreshing(false);
            return;
        }else {
            cityname = spName;
        }

        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (MainActivity.this.isFinishing() || MainActivity.this.isDestroyed()){
            return;
        }

        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                setWeatherLive(weatherLiveResult.getLiveResult());
            } else {
                ToastUtil.show(MainActivity.this, R.string.no_result);
                setWeatherLive(null);
            }
        } else {
            ToastUtil.showerror(MainActivity.this, rCode);
            setWeatherLive(null);
        }
    }

    private void setWeatherLive(LocalWeatherLive weatherlive) {
        swipe_refresh.setRefreshing(false);
        if (weatherlive != null) {
            city.setText(cityname);

            reporttime1.setText(weatherlive.getReportTime() + "发布");
            weather.setText(weatherlive.getWeather());
            Temperature.setText(weatherlive.getTemperature() + "°");
            wind.setText(weatherlive.getWindDirection() + "风     " + weatherlive.getWindPower() + "级");
            humidity.setText("湿度         " + weatherlive.getHumidity() + "%");
        } else {
            reporttime1.setText("-");
            weather.setText("-");
            Temperature.setText("-");
            wind.setText("-");
            humidity.setText("-");
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult weatherForecastResult, int rCode) {

    }
    //endregion

    //region 权限
    public void initPermissionsHelper() {
        permissionHelper = new PermissionHelper(this);
        //检查权限
        getWindow().getDecorView().post(this::checkWriteAndReadPermission);
    }

    private void checkWriteAndReadPermission() {
        //在这里做Gps权限的判断
        if (permissionHelper.checkWriteExternalStorage(MainActivity.this)) {
            if (permissionHelper.checkReadExternalStorage(MainActivity.this)) {

            }
        }
    }

    @Override
    public void onUserAllow(int permissionCode) {
        checkWriteAndReadPermission();
    }

    @Override
    public void onUserDeny(int permissionCode) {
        checkWriteAndReadPermission();
    }

    @Override
    public void onSystemDeny(int permissionCode) {
        PermissionsUtil.getInstance().goPermissionSettings(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkWriteAndReadPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
    }
    //endregion

    //region Service
    private ForegroundService mService = null;
    // 跟踪服务的绑定状态。
    private boolean mBound = false;

    //监视与服务的连接状态。
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ForegroundService.LocalBinder binder = (ForegroundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            startService(new Intent(getApplicationContext(), ForegroundService.class));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }

    };
    //endregion
}
