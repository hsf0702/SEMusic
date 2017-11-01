package com.past.music.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.past.music.pastmusic.R
import com.past.music.utils.PermissionsChecker


class PermissionsActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 0x00
    private val PACKAGE_URL_SCHEME = "package:" // 方案

    private var mChecker: PermissionsChecker? = null // 权限检测器
    private var isRequireCheck: Boolean = false

    companion object {

        val EXTRA_PERMISSIONS = "com.past.music.permission"
        const val PERMISSIONS_GRANTED = 0 // 权限授权
        const val PERMISSIONS_DENIED = 1 // 权限拒绝

        // 启动当前权限页面的公开接口
        fun startActivityForResult(activity: Activity, requestCode: Int, permissions: Array<String>) {
            val intent = Intent(activity, PermissionsActivity::class.java)
            intent.putExtra(EXTRA_PERMISSIONS, permissions)
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            throw RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!")
        }
        setContentView(R.layout.activity_permissions)

        mChecker = PermissionsChecker(this)
        isRequireCheck = true
    }

    override fun onResume() {
        super.onResume()
        if (isRequireCheck) {
            val permissions = getPermissions()
            if (mChecker!!.lacksPermissions(*permissions)) {
                requestPermissions(permissions, PERMISSION_REQUEST_CODE) // 请求权限
            } else {
                allPermissionsGranted() // 全部权限都已获取
            }
        } else {
            isRequireCheck = true
        }
    }

    // 返回传递的权限参数
    private fun getPermissions(): Array<String> {
        return intent.getStringArrayExtra(EXTRA_PERMISSIONS)
    }

    // 请求权限兼容低版本
    private fun requestPermissions(vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    // 全部权限均已获取
    private fun allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true
            allPermissionsGranted()
        } else {
            isRequireCheck = false
            showMissingPermissionDialog()
        }
    }

    // 显示缺失权限提示
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this@PermissionsActivity)
        builder.setTitle(R.string.help)
        builder.setMessage(R.string.string_help_text)

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit, DialogInterface.OnClickListener { dialog, which ->
            setResult(PERMISSIONS_DENIED)
            finish()
        })

        builder.setPositiveButton(R.string.settings, DialogInterface.OnClickListener { dialog, which -> startAppSettings() })

        builder.show()
    }

    // 含有全部的权限
    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        return !grantResults.contains(PackageManager.PERMISSION_DENIED)
    }

    // 启动应用的设置
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + packageName)
        startActivity(intent)
    }

}
