package com.se.music.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.se.music.R
import com.se.music.base.BaseFragment

/**
 *Author: gaojin
 *Time: 2018/7/10 上午12:03
 */

class LocalFolderFragment : BaseFragment() {

    lateinit var textView: TextView

    companion object {
        fun newInstance(): LocalFolderFragment {
            return LocalFolderFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_local_folder, container, false)
        textView = root.findViewById(R.id.text_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}