package com.se.music.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.se.music.fragment.FolderFragment
import com.se.music.fragment.LocalAlbumFragment
import com.se.music.fragment.LocalMusicSongFragment
import com.se.music.fragment.LocalSingerFragment
import java.util.*

/**
 * Created by gaojin on 2017/12/7.
 */
class LocalFragmentAdapter constructor(fm: FragmentManager?, list: List<String>) : FragmentStatePagerAdapter(fm) {
    private var tabNames = ArrayList<String>()

    init {
        tabNames = list as ArrayList<String>
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LocalMusicSongFragment.newInstance()
            1 -> LocalSingerFragment.newInstance()
            2 -> LocalAlbumFragment.newInstance()
            else -> FolderFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabNames[position % 4]
    }
}