package com.se.music.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.se.music.R
import com.se.music.base.BaseActivity
import com.se.music.main.init.PermissionsActivity
import com.se.music.utils.PermissionsChecker
import com.se.music.utils.setTransparentForWindow


class MainActivity : BaseActivity() {

    private val REQUEST_CODE = 0x00 // 请求码
    private val PERMISSIONS = Array(1) { Manifest.permission.READ_EXTERNAL_STORAGE }

    private var mPermissionsChecker: PermissionsChecker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTransparentForWindow(this)
        supportFragmentManager.beginTransaction()
                .add(R.id.se_main_content, MainFragment.newInstance(), MainFragment.TAG)
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
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish()
        }
        supportFragmentManager.findFragmentById(R.id.se_main_content)?.onActivityResult(requestCode, resultCode, data)
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out)
    }
}
