package com.se.music.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.se.music.R

/**
 * Creator：gaojin
 * date：2017/11/6 下午8:32
 */
abstract class ToolBarActivity : AppCompatActivity() {
    private lateinit var mToolBar: Toolbar
    private lateinit var mTitle: TextView
    private lateinit var statusBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        val rootView = inflater.inflate(R.layout.fragment_base, null) as LinearLayout
        rootView.addView(createContentView(inflater, rootView))
        setContentView(rootView)
        mToolBar = findViewById(R.id.base_toolbar)
        mTitle = findViewById(R.id.toolbar_title)
        statusBar = findViewById(R.id.fake_status_bar)
        setSupportActionBar(mToolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    protected abstract fun createContentView(inflater: LayoutInflater, rootView: ViewGroup): View

    override fun setTitle(title: CharSequence) {
        mTitle.text = title
    }

    fun setStatusBarColor(@ColorRes color: Int) {
        statusBar.setBackgroundResource(color)
    }

    fun hideStatStatusBar() {
        statusBar.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

}