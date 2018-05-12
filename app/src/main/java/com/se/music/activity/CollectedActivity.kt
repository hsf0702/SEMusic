package com.se.music.activity

import android.os.Bundle
import com.se.music.pastmusic.R

/**
 * Created by gaojin on 2017/11/26.
 */
class CollectedActivity : ToolBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "我喜欢"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_collected
    }

}