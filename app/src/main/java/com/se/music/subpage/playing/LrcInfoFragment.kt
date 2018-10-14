package com.se.music.subpage.playing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.base.BaseFragment

/**
 *Author: gaojin
 *Time: 2018/10/7 下午7:49
 */

class LrcInfoFragment : BaseFragment() {
    companion object {
        const val TAG = "LrcInfoFragment"
        fun newInstance(): LrcInfoFragment {
            return LrcInfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_playing_lrc_info, container, false)
        view.tag = TAG
        return view
    }
}