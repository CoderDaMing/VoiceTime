package com.ming.voicetime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ming.voicetime.permissions.PermissionHelper;
import com.ming.voicetime.permissions.PermissionCallBack;
import com.ming.voicetime.permissions.PermissionsUtil;
import com.ming.voicetime.util.TextToSpeechUtil;

public class MainActivity extends AppCompatActivity implements PermissionCallBack, View.OnClickListener {
    private PermissionHelper permissionHelper;

    private static final long ONE_MINTER = 60000;
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        initPermissionsHelper();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TextToSpeechUtil.getInstance().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TextToSpeechUtil.getInstance().destroy();
    }
    //endregion

    @Override
    public void onClick(View v) {
        Snackbar.make(v, "Start Task", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        long saveDelayMillis =
        startTask();
    }

    private void startTask(long delayMillis) {
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(0, delayMillis);
    }

    //region 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showCityPickerDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCityPickerDialog() {
        AppCompatDialog dialog = new AppCompatDialog(MainActivity.this);
        View view = View.inflate(this, R.layout.layout_dialog_set_time, null);
        NumberPicker minutePicker = view.findViewById(R.id.minuteicker);
        minutePicker.setMaxValue(60);
        minutePicker.setMinValue(1);
        minutePicker.setValue(1);
        //设置为对当前值不可编辑
        minutePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        tvConfirm.setOnClickListener(v -> {
            dialog.dismiss();
        });
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
}
