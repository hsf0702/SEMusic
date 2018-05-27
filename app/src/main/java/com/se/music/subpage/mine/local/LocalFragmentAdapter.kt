package com.se.music.subpage.mine.local

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by gaojin on 2017/12/7.
 */
class LocalFragmentAdapter constructor(fm: FragmentManager?, private val list: List<String>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LocalMusicSongFragment.newInstance()
            1 -> LocalMusicSongFragment.newInstance()
            2 -> LocalMusicSongFragment.newInstance()
            else -> LocalMusicSongFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position % 4]
    }
}