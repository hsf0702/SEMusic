package com.se.music.subpage.mine.local

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *Author: gaojin
 *Time: 2018/5/31 下午11:32
 */

class LocalSingerFragment : Fragment() {
    companion object {
        fun newInstance(): LocalSingerFragment {
            return LocalSingerFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}