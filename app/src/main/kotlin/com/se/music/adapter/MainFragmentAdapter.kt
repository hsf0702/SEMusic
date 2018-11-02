package com.se.music.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.se.music.fragment.LocalFolderFragment
import com.se.music.mine.MvpMineFragment
import com.se.music.online.MvpMusicFragment

/**
 * Creator：gaojin
 * date：2017/11/3 下午8:58
 */
class MainFragmentAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = when (position) {
        0 -> MvpMineFragment.newInstance()
        1 -> MvpMusicFragment.newInstance()
        2 -> LocalFolderFragment.newInstance()
        else -> null
    }

    override fun getCount() = 3
}