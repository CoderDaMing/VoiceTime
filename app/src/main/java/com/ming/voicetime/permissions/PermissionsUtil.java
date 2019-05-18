package com.ming.voicetime.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 请求权限帮助类
 */
public class PermissionsUtil {

    private static PermissionsUtil permissionsAndroid;

    public static PermissionsUtil getInstance() {
        if (null == permissionsAndroid) {
            synchronized (PermissionsUtil.class) {
                if (null == permissionsAndroid) {
                    permissionsAndroid = new PermissionsUtil();
                }
            }
        }
        return permissionsAndroid;
    }

    private PermissionsUtil() {

    }

    // Request Code for request Permissions Must be between 0 to 255.

    //region Write External Storage Permission.
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 100;

    public boolean checkWriteExternalStoragePermission(Activity activity) {
        return boolValue(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    public void requestForWriteExternalStoragePermission(Activity activity) {
        checkPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
    }
    //endregion

    //region Read External Storage Permission.
    public static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 101;

    public boolean checkReadExternalStoragePermission(Activity activity) {
        return boolValue(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    public void requestForReadExternalStoragePermission(Activity activity) {
        checkPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
    }
    //endregion

    //region Record Audio Permission.
    public static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 102;

    public boolean checkRecordAudioPermission(Activity activity) {
        return boolValue(ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO));
    }

    public void requestForRecordAudioPermission(Activity activity) {
        checkPermission(activity,Manifest.permission.RECORD_AUDIO,RECORD_AUDIO_PERMISSION_REQUEST_CODE);
    }
    //endregion

    //region Camera Permission
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 103;

    public boolean checkCameraPermission(Activity activity) {
        return boolValue(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA));
    }

    public void requestForCameraPermission(Activity activity) {
        checkPermission(activity,Manifest.permission.CAMERA,CAMERA_PERMISSION_REQUEST_CODE);
    }
    // endregion

    //region Location Permission
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 104;

    public boolean checkLocationPermission(Activity activity) {
        return boolValue(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public void requestForLocationPermission(final Activity activity) {
        checkPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION,LOCATION_PERMISSION_REQUEST_CODE);
    }
    //endregion

    //check permission
    public void checkPermission(Activity activity,String permission,int requestCode){
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    // function to return true or false based on the permission result
    private boolean boolValue(int value) {
        return value == PackageManager.PERMISSION_GRANTED;
    }
    public boolean hasOnePermission(Context context, String permission){
        return boolValue(ContextCompat.checkSelfPermission(context,permission));
    }
    //region 跳转到系统详情设置
    public static final int ACTION_APPLICATION_DETAILS_SETTINGS_REQUEST_CODE = 200;

    public void goPermissionSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        //在安卓系统中，在非Acitivity中启动Activity，使用context.startAcitivityt()需要给Intent意图添加此标志：
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivityForResult(intent,ACTION_APPLICATION_DETAILS_SETTINGS_REQUEST_CODE);
    }
    //endregion
}
