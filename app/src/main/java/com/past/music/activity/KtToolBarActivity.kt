package com.past.music.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.past.music.pastmusic.R

/**
 * Creator：gaojin
 * date：2017/11/6 下午8:32
 */
abstract class KtToolBarActivity : KtBaseActivity() {
    private var mAppBarLayout: AppBarLayout? = null
    private var mToolBar: Toolbar? = null
    private var mTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        mAppBarLayout = findViewById(R.id.appbar_layout)
        mToolBar = findViewById(R.id.base_toolbar)
        mTitle = findViewById(R.id.toolbar_title)

        setSupportActionBar(mToolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    protected abstract fun getLayoutId(): Int

    override fun setTitle(title: CharSequence) {
        mTitle!!.text = title
    }

    override fun setStatusBar() {}

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

}