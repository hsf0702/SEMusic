package com.se.music.main

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.base.BasePageFragment
import com.se.music.widget.TitleZoomTabLayout

class MainFragment : BasePageFragment() {

    private lateinit var mTabLayout: TitleZoomTabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var mAdapter: MainFragmentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToolBar.visibility = View.GONE

        mTabLayout.setTabGravity(TitleZoomTabLayout.GRAVITY_CENTER)
        mAdapter = MainFragmentAdapter(context!!, activity?.supportFragmentManager)
        mViewPager.adapter = mAdapter
        mTabLayout.selectTextAppend(10F)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(R.layout.fragment_app_main, container, false)
        mTabLayout = view.findViewById(R.id.tab_layout)
        mViewPager = view.findViewById(R.id.view_pager)
        return view
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}