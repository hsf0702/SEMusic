package com.past.music.utils

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * Creator：gaojin
 * date：2017/11/1 下午10:56
 */
class PermissionsChecker constructor(context: Context) {
    private val mContext: Context = context.applicationContext

    // 判断权限集合
    fun lacksPermissions(permissions: Array<String>): Boolean {
        return permissions.any { lacksPermission(it) }
    }

    // 判断是否缺少权限
    fun lacksPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED
    }
}
