package com.se.music.base

import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.se.music.R

/**
 * Created by gaojin on 2018/2/28.
 */
abstract class BasePageFragment : BaseFragment() {
    protected lateinit var mToolBar: Toolbar
    private lateinit var mTitle: TextView
    private lateinit var statusBar: View
    protected lateinit var fm: FragmentManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val linearLayout = inflater.inflate(R.layout.fragment_base, container, false) as LinearLayout?
        linearLayout!!.addView(createContentView(inflater, container))

        mToolBar = linearLayout.findViewById(R.id.base_toolbar)
        mTitle = linearLayout.findViewById(R.id.toolbar_title)
        statusBar = linearLayout.findViewById(R.id.fake_status_bar)
        fm = fragmentManager!!

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(mToolBar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        return linearLayout
    }

    fun setTitle(title: String?) {
        mTitle.text = title
    }

    fun setStatusBarColor(@ColorRes color: Int) {
        statusBar.setBackgroundResource(color)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                fm.popBackStack()
            }
        }
        return true
    }

    abstract fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View
}