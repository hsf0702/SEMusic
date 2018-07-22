package com.se.music.subpage.mine.local

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R

/**
 *Author: gaojin
 *Time: 2018/7/10 上午12:03
 */

class LocalFolderFragment : Fragment() {
    companion object {
        fun newInstance(): LocalFolderFragment {
            return LocalFolderFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_local_folder, container, false)
    }
}