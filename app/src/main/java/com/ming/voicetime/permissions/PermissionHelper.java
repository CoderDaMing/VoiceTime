package com.ming.voicetime.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;


/**
 * 类描述：获取权限帮助类
 */

public class PermissionHelper {
    private PermissionCallBack mPermissionCallBack;

    public PermissionHelper() {

    }

    public PermissionHelper(PermissionCallBack permissionCallBack) {
        this.mPermissionCallBack = permissionCallBack;
    }

    //region 私有方法
    /*
     *  是否大于23需要动态获取权限
     */

    private boolean shouldCheckPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    //region Android原生授权
    /*
     *  Activity获取存储权限
     */
    private boolean checkWriteExternalStoragePermission(Activity activity) {
        boolean isExternalStorage = PermissionsUtil.getInstance().checkWriteExternalStoragePermission(activity);
        if (!isExternalStorage) {
            PermissionsUtil.getInstance().requestForWriteExternalStoragePermission(activity);
        }
        return isExternalStorage;
    }

    /*
     *  Activity获取读取权限
     */
    private boolean checkReadExternalStoragePermission(Activity activity) {
        boolean isReadExternalStorage = PermissionsUtil.getInstance().checkReadExternalStoragePermission(activity);
        if (!isReadExternalStorage) {
            PermissionsUtil.getInstance().requestForReadExternalStoragePermission(activity);
        }
        return isReadExternalStorage;
    }

    /*
     * Activity获取语音权限
     */
    private boolean checkRecordAudioPermission(Activity activity) {
        boolean isRecordAudio = PermissionsUtil.getInstance().checkRecordAudioPermission(activity);
        if (!isRecordAudio) {
            PermissionsUtil.getInstance().requestForRecordAudioPermission(activity);
        }
        return isRecordAudio;
    }

    /*
     * Activity获取相机权限
     */
    private boolean checkCameraPermission(Activity activity) {
        boolean isCamera = PermissionsUtil.getInstance().checkCameraPermission(activity);
        if (!isCamera) {
            PermissionsUtil.getInstance().requestForCameraPermission(activity);
        }
        return isCamera;
    }

    /*
     *  Activity获取Gps权限
     */
    private boolean checkLocationPermission(Activity activity) {
        boolean isLocation = PermissionsUtil.getInstance().checkLocationPermission(activity);
        if (!isLocation) {
            PermissionsUtil.getInstance().requestForLocationPermission(activity);
        }
        return isLocation;
    }

    /*
     * 用于权限请求回调
     * 拒绝、不再提示、禁止的提示框
     *
     * @param activity   activity
     * @param permissionRequestCode 权限
     */
    private void showDialogForPermissionDeny(final Activity activity, final int permissionRequestCode) {
        String permission = "";
        switch (permissionRequestCode) {
            case PermissionsUtil.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
            case PermissionsUtil.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                break;
            case PermissionsUtil.RECORD_AUDIO_PERMISSION_REQUEST_CODE:
                permission = Manifest.permission.RECORD_AUDIO;
                break;
            case PermissionsUtil.CAMERA_PERMISSION_REQUEST_CODE:
                permission = Manifest.permission.CAMERA;
                break;
            case PermissionsUtil.LOCATION_PERMISSION_REQUEST_CODE:
                permission = Manifest.permission.ACCESS_FINE_LOCATION;
                break;
            default:
                break;
        }

        final String finalPermission = permission;

        if (!TextUtils.isEmpty(permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                //单纯的点了拒绝
                String hintMsg = "";
                switch (permissionRequestCode) {
                    case PermissionsUtil.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                        hintMsg = "need_write_external_storage_permission";
                        break;
                    case PermissionsUtil.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                        hintMsg = "need_read_external_storage_permission";
                        break;
                    case PermissionsUtil.RECORD_AUDIO_PERMISSION_REQUEST_CODE:
                        hintMsg = "need_read_external_storage_permission";
                        break;
                    case PermissionsUtil.CAMERA_PERMISSION_REQUEST_CODE:
                        hintMsg = "need_read_external_storage_permission";
                        break;
                    case PermissionsUtil.LOCATION_PERMISSION_REQUEST_CODE:
                        hintMsg = "need_read_external_storage_permission";
                        break;
                    default:
                        break;
                }
                //用户点击了不在询问
                AlertDialog hintDialog = new AlertDialog.Builder(activity)
                        .setTitle("Hint")
                        .setMessage(hintMsg)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PermissionsUtil.getInstance().checkPermission(activity, finalPermission, permissionRequestCode);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (mPermissionCallBack != null) {
                                    mPermissionCallBack.onUserDeny(permissionRequestCode);
                                }
                            }
                        })
                        .setCancelable(false)
                        .create();
                hintDialog.show();
            } else {
                //勾选了不在询问
                String msg = "";
                switch (permissionRequestCode) {
                    case PermissionsUtil.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                        msg = "need_write_external_storage_permission";
                        break;
                    case PermissionsUtil.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                        msg = "need_read_external_storage_permission";
                        break;
                    case PermissionsUtil.RECORD_AUDIO_PERMISSION_REQUEST_CODE:
                        msg = "need_read_external_storage_permission";
                        break;
                    case PermissionsUtil.CAMERA_PERMISSION_REQUEST_CODE:
                        msg = "need_read_external_storage_permission";
                        break;
                    case PermissionsUtil.LOCATION_PERMISSION_REQUEST_CODE:
                        msg = "need_read_external_storage_permission";
                        break;
                    default:
                        break;
                }
                //用户点击了不在询问
                AlertDialog neverHintDialog = new AlertDialog.Builder(activity)
                        .setTitle("Hint")
                        .setMessage(msg + "go_set_permission?")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PermissionsUtil.getInstance().goPermissionSettings(activity);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (mPermissionCallBack != null) {
                                    mPermissionCallBack.onSystemDeny(permissionRequestCode);
                                }
                            }
                        })
                        .setCancelable(false)
                        .create();

                neverHintDialog.show();
            }
        }
    }
    //endregion
    //endregion

    //region 公共方法

    /**
     * 默认返回有写入权限
     *
     * @return boolean
     */
    public boolean checkWriteExternalStorage(Activity activity) {
        if (shouldCheckPermission()) {
            return checkWriteExternalStoragePermission(activity);
        }
        return true;
    }

    /**
     * 默认返回有读取权限
     *
     * @return boolean
     */
    public boolean checkReadExternalStorage(Activity activity) {
        if (shouldCheckPermission()) {
            return checkReadExternalStoragePermission(activity);
        }
        return true;
    }

    /**
     * 默认返回有语音权限
     *
     * @return boolean
     */
    public boolean checkRecordAudio(Activity activity) {
        if (shouldCheckPermission()) {
            return checkRecordAudioPermission(activity);
        }
        return true;
    }

    /**
     * 默认返回有相机权限
     *
     * @return boolean
     */
    public boolean checkCamera(Activity activity) {
        if (shouldCheckPermission()) {
            return checkCameraPermission(activity);
        }
        return true;
    }

    /**
     * 默认返回有位置权限
     *
     * @return boolean
     */
    public boolean checkLocation(Activity activity) {
        if (shouldCheckPermission()) {
            return checkLocationPermission(activity);
        }
        return true;
    }


    /**
     * 处理权限回调
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 0 && grantResults.length > 0) {
            switch (requestCode) {
                case PermissionsUtil.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (mPermissionCallBack != null) {
                            mPermissionCallBack.onUserAllow(requestCode);
                        }
                    } else {
                        //无写外部存储权限
                        showDialogForPermissionDeny(activity, requestCode);
                    }
                    break;

                case PermissionsUtil.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (mPermissionCallBack != null) {
                            mPermissionCallBack.onUserAllow(requestCode);
                        }
                    } else {
                        //无读取权限
                        showDialogForPermissionDeny(activity, requestCode);
                    }
                    break;

                case PermissionsUtil.RECORD_AUDIO_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (mPermissionCallBack != null) {
                            mPermissionCallBack.onUserAllow(requestCode);
                        }
                    } else {
                        //无语音权限
                        showDialogForPermissionDeny(activity, requestCode);
                    }
                    break;

                case PermissionsUtil.CAMERA_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (mPermissionCallBack != null) {
                            mPermissionCallBack.onUserAllow(requestCode);
                        }
                    } else {
                        //无相机权限
                        showDialogForPermissionDeny(activity, requestCode);
                    }
                    break;
                case PermissionsUtil.LOCATION_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (mPermissionCallBack != null) {
                            mPermissionCallBack.onUserAllow(requestCode);
                        }
                    } else {
                        //无定位权限
                        showDialogForPermissionDeny(activity, requestCode);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 是否有某个权限
     *
     * @param context
     * @param permission
     * @return
     */
    public boolean hasOnePermission(Context context, String permission) {
        return PermissionsUtil.getInstance().hasOnePermission(context, permission);
    }

    //endregion
}
