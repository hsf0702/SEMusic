package com.se.music.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import com.se.music.R
import com.se.music.base.BaseActivity
import com.se.music.main.init.PermissionsActivity
import com.se.music.utils.PermissionsChecker

class MainActivity : BaseActivity() {

    private val REQUEST_CODE = 0x00 // 请求码
    private val PERMISSIONS = Array(1, { Manifest.permission.READ_EXTERNAL_STORAGE })

    private var mPermissionsChecker: PermissionsChecker? = null

    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        supportFragmentManager.beginTransaction()
                .add(R.id.content, AppMainFragment.newInstance())
                .commitAllowingStateLoss()
        mPermissionsChecker = PermissionsChecker(this)
    }

    override fun onResume() {
        super.onResume()
        if (mPermissionsChecker!!.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity()
        }
    }

    private fun startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish()
        }
        supportFragmentManager.findFragmentById(R.id.content)?.onActivityResult(requestCode, resultCode, data)
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out)
    }
}
