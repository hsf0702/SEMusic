package com.se.music.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import com.se.music.R
import com.se.music.base.BaseActivity
import com.se.music.main.init.PermissionsActivity
import com.se.music.utils.PermissionsChecker
import com.se.music.utils.setTransparentForWindow


class MainActivity : BaseActivity() {

    private val requestCode = 0x00 // 请求码
    private val permissions = Array(1) { Manifest.permission.READ_EXTERNAL_STORAGE }
    private var boo: Long = 0

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
        if (mPermissionsChecker!!.lacksPermissions(permissions)) {
            startPermissionsActivity()
        }
    }

    private fun startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, requestCode, permissions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish()
        }
        supportFragmentManager.findFragmentById(R.id.se_main_content)?.onActivityResult(requestCode, resultCode, data)
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (currentIsMain()) {
                if ((System.currentTimeMillis() - boo) > 2000) {
                    Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show()
                    boo = System.currentTimeMillis()
                } else {
                    finish()
                    System.exit(0)
                }
                false
            } else {
                boo = 0
                super.onKeyDown(keyCode, event)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun currentIsMain(): Boolean {
        val main = supportFragmentManager.findFragmentByTag(MainFragment.TAG)
        return main?.isVisible ?: false
    }
}
