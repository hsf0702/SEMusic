package com.se.music.main

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.se.music.R
import com.se.music.base.BaseFragment

class MainFragment : BaseFragment(), ViewPager.OnPageChangeListener, View.OnClickListener {

    private lateinit var mineView: TextView
    private lateinit var musicRoomView: TextView
    private lateinit var findView: TextView

    private lateinit var mViewPager: ViewPager
    private lateinit var mAdapter: MainFragmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_app_main, container, false)
        mViewPager = view.findViewById(R.id.view_pager)
        mineView = view.findViewById(R.id.tool_bar_mine)
        musicRoomView = view.findViewById(R.id.tool_bar_music_room)
        findView = view.findViewById(R.id.tool_bar_find)

        mineView.setOnClickListener(this)
        musicRoomView.setOnClickListener(this)
        findView.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = MainFragmentAdapter(activity?.supportFragmentManager)
        mViewPager.adapter = mAdapter
        mViewPager.currentItem = 1
        setTitleStyle(1)
        mViewPager.addOnPageChangeListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.tool_bar_mine -> {
                mViewPager.currentItem = 0
                setTitleStyle(0)
            }
            R.id.tool_bar_music_room -> {
                mViewPager.currentItem = 1
                setTitleStyle(1)
            }
            R.id.tool_bar_find -> {
                mViewPager.currentItem = 2
                setTitleStyle(2)
            }
        }
    }

    private fun setTitleStyle(position: Int) {
        when (position) {
            0 -> {
                mineView.textSize = 19.toFloat()
                mineView.setTextColor(ContextCompat.getColor(context!!, R.color.white))

                musicRoomView.textSize = 18.toFloat()
                musicRoomView.setTextColor(ContextCompat.getColor(context!!, R.color.light_gray))

                findView.textSize = 18.toFloat()
                findView.setTextColor(ContextCompat.getColor(context!!, R.color.light_gray))
            }
            1 -> {
                musicRoomView.textSize = 19.toFloat()
                musicRoomView.setTextColor(ContextCompat.getColor(context!!, R.color.white))

                mineView.textSize = 18.toFloat()
                mineView.setTextColor(ContextCompat.getColor(context!!, R.color.light_gray))

                findView.textSize = 18.toFloat()
                findView.setTextColor(ContextCompat.getColor(context!!, R.color.light_gray))
            }
            2 -> {
                findView.textSize = 19.toFloat()
                findView.setTextColor(ContextCompat.getColor(context!!, R.color.white))

                mineView.textSize = 18.toFloat()
                mineView.setTextColor(ContextCompat.getColor(context!!, R.color.light_gray))

                musicRoomView.textSize = 18.toFloat()
                musicRoomView.setTextColor(ContextCompat.getColor(context!!, R.color.light_gray))
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        setTitleStyle(position)
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