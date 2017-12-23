package com.past.music.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.adapter.MainFragmentAdapter
import com.past.music.pastmusic.R

class AppMainFragment : Fragment() {

    private var mToolBar: Toolbar? = null
    private var mTabLayout: TabLayout? = null
    private var mViewPager: ViewPager? = null
    private var mAdapter: MainFragmentAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_app_main, container, false)
        mToolBar = view.findViewById(R.id.toolbar)
        mTabLayout = view.findViewById(R.id.tab_layout)
        mViewPager = view.findViewById(R.id.view_pager)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTabLayout!!.tabGravity = TabLayout.GRAVITY_CENTER
        mAdapter = MainFragmentAdapter(context, activity.supportFragmentManager)
        mViewPager!!.adapter = mAdapter
        mTabLayout!!.setupWithViewPager(mViewPager)
    }

    companion object {

        fun newInstance(): AppMainFragment {
            val fragment = AppMainFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}