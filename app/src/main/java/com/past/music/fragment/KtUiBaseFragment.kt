package com.past.music.fragment

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
import android.widget.TextView
import com.past.music.pastmusic.R


/**
 * Creator：gaojin
 * date：2017/10/30 下午10:25
 */
abstract class KtUiBaseFragment : Fragment() {
    private var mAppBarLayout: AppBarLayout? = null
    private var mToolBar: Toolbar? = null
    private var mTitle: TextView? = null

    var fm: FragmentManager? = null


    fun setTitle(title: String?) {
        mTitle!!.text = title
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(getLayoutId(), container, false)
        mAppBarLayout = view.findViewById(R.id.appbar_layout)
        mToolBar = view.findViewById(R.id.base_toolbar)
        mTitle = view.findViewById(R.id.toolbar_title)
        fm = fragmentManager

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(mToolBar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        return view
    }

    abstract fun getLayoutId(): Int

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> fm!!.popBackStack()
        }
        return true
    }


}