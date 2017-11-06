package com.past.music.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import com.past.music.fragment.KtAppMainFragment
import com.past.music.pastmusic.R
import com.past.music.utils.PermissionsChecker

class KtMainActivity : KtBaseActivity() {

    private val REQUEST_CODE = 0x00 // 请求码
    private val PERMISSIONS = Array(1, { Manifest.permission.READ_EXTERNAL_STORAGE })

    private var mPermissionsChecker: PermissionsChecker? = null

    private var mDrawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        supportFragmentManager.beginTransaction()
                .add(R.id.content, KtAppMainFragment.newInstance())
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
        KtPermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == KtPermissionsActivity.PERMISSIONS_DENIED) {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out)
    }
}
