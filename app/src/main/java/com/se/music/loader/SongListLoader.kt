package com.se.music.loader

import android.content.Context
import android.content.CursorLoader

/**
 *Author: gaojin
 *Time: 2018/5/19 下午8:45
 */

class SongListLoader(context: Context) : CursorLoader(context) {
    companion object {
        const val uri = ""
        val projection = arrayOf("", "")
        fun newInstance(context: Context): CursorLoader {
            return SongListLoader(context)
        }
    }

}