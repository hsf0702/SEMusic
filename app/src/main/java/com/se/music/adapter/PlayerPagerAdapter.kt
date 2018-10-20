package com.se.music.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.se.music.fragment.AlbumInfoFragment
import com.se.music.fragment.LrcInfoFragment
import com.se.music.fragment.SongInfoFragment

/**
 *Author: gaojin
 *Time: 2018/10/7 下午7:42
 */

class PlayerPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    companion object {
        const val SONG_INFO = 0
        const val ALBUM_INFO = 1
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            SONG_INFO -> SongInfoFragment.newInstance()
            ALBUM_INFO -> AlbumInfoFragment.newInstance()
            else -> LrcInfoFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 3
    }
}