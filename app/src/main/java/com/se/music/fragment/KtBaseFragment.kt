package com.se.music.fragment

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.se.music.pastmusic.R

/**
 * Created by gaojin on 2018/2/28.
 */
abstract class KtBaseFragment : Fragment() {
    private var mToolBar: Toolbar? = null
    private var mTitle: TextView? = null
    protected var fm: FragmentManager? = null

    fun setTitle(title: String?) {
        mTitle!!.text = title
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val linearLayout = inflater.inflate(R.layout.fragment_base, container, false) as LinearLayout?
        linearLayout!!.addView(createContentView(inflater, container))

        mToolBar = linearLayout.findViewById(R.id.base_toolbar)
        mTitle = linearLayout.findViewById(R.id.toolbar_title)
        fm = fragmentManager

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(mToolBar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        return linearLayout
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                fm!!.popBackStack()
            }
        }
        return true
    }

    abstract fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View
}