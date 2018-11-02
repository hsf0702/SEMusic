package com.se.music.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.se.music.mine.MvpMineFragment
import com.se.music.online.MvpMusicFragment
import com.se.music.fragment.LocalFolderFragment

/**
 * Creator：gaojin
 * date：2017/11/3 下午8:58
 */
class MainFragmentAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> MvpMineFragment.newInstance()
            1 -> MvpMusicFragment.newInstance()
            2 -> LocalFolderFragment.newInstance()
            else -> null
        }
    }

    override fun getCount(): Int {
        return 3
    }
}