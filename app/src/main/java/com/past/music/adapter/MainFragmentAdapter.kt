package com.past.music.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.past.music.fragment.MineFragment
import com.past.music.online.MusicFragment
import com.past.music.pastmusic.R

/**
 * Creator：gaojin
 * date：2017/11/3 下午8:58
 */
class MainFragmentAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val tabNames = ArrayList<String>()

    init {
        tabNames.add(context.getString(R.string.local_music_tab))
        tabNames.add(context.getString(R.string.online_music_tab))
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return MineFragment.newInstance()
            1 -> return MusicFragment.newInstance()
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