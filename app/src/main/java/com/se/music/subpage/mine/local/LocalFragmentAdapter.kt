package com.se.music.subpage.mine.local

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.se.music.R

/**
 * Created by gaojin on 2017/12/7.
 */
class LocalFragmentAdapter constructor(fm: FragmentManager?, private val context: Context) : FragmentStatePagerAdapter(fm) {

    private val mTabNames = mutableListOf<String>(context.getString(R.string.local_music_song, 0)
            , context.getString(R.string.local_music_singer, 0)
            , context.getString(R.string.local_music_album, 0)
            , context.getString(R.string.local_music_folder, 0))

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LocalMusicSongFragment.newInstance()
            1 -> LocalMusicSongFragment.newInstance()
            2 -> LocalMusicSongFragment.newInstance()
            else -> LocalMusicSongFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return mTabNames.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTabNames[position % 4]
    }

    fun setTitle(position: Int, title: String) {
        if (position in 0 until mTabNames.size) {
            mTabNames[position] = title
            notifyDataSetChanged()
        }
    }


}