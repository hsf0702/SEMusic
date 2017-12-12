package com.past.music.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.past.music.fragment.KtMineFragment
import com.past.music.fragment.MusicFragment
import java.util.*

/**
 * Creator：gaojin
 * date：2017/11/3 下午8:58
 */
class MainFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val tabNames = ArrayList<String>()

    init {
        tabNames.add("我的")
        tabNames.add("音乐馆")
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return KtMineFragment.newInstance()
            1 -> return MusicFragment.newInstance("", "")
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabNames[position % 2]
    }

}