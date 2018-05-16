package com.se.music.main

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.se.music.R
import com.se.music.mine.MvpMineFragment
import com.se.music.online.MvpMusicFragment

/**
 * Creator：gaojin
 * date：2017/11/3 下午8:58
 */
class MainFragmentAdapter(context: Context, fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    private val tabNames = listOf(context.getString(R.string.local_music_tab)
            , context.getString(R.string.online_music_tab))

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> MvpMineFragment.newInstance()
            1 -> MvpMusicFragment.newInstance()
            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabNames[position % 2]
    }

}