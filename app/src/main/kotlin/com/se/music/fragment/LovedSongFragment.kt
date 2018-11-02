package com.se.music.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.base.BasePageFragment

/**
 *Author: gaojin
 *Time: 2018/10/25 下午10:24
 */

class LovedSongFragment : BasePageFragment() {

    companion object {
        fun newInstance(): LovedSongFragment {
            return LovedSongFragment()
        }
    }

    private lateinit var content: RecyclerView

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        content = inflater.inflate(R.layout.fragment_love_song, container, false) as RecyclerView
        return content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTitle(context!!.getString(R.string.love_music_title))
    }
}