package com.se.music.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.widget.TitleZoomTabLayout

class AppMainFragment : Fragment() {

    private lateinit var mToolBar: Toolbar
    private lateinit var mTabLayout: TitleZoomTabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var mAdapter: MainFragmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_app_main, container, false)
        mToolBar = view.findViewById(R.id.toolbar)
        mTabLayout = view.findViewById(R.id.tab_layout)
        mViewPager = view.findViewById(R.id.view_pager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTabLayout.setTabGravity(TitleZoomTabLayout.GRAVITY_CENTER)
        mAdapter = MainFragmentAdapter(context!!, activity?.supportFragmentManager)
        mViewPager.adapter = mAdapter
        mTabLayout.selectTextAppend(10F)
        mTabLayout.setupWithViewPager(mViewPager)
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