package com.se.music.subpage.mine.love

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.common.ToolBarActivity

/**
 * Created by gaojin on 2017/11/26.
 */
class CollectedActivity : ToolBarActivity() {
    override fun createContentView(inflater: LayoutInflater, rootView: ViewGroup): View {
        return inflater.inflate(R.layout.activity_collected, rootView, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "我喜欢"
    }
}