package com.ming.voicetime.permissions;

/**
 * 类描述:权限回调描述
 */

public interface PermissionCallBack {
    void onUserAllow(int permissionCode);
    void onUserDeny(int permissionCode);
    void onSystemDeny(int permissionCode);
}
