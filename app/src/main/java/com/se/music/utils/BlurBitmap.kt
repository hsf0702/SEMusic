package com.se.music.utils

import android.content.Context
import android.graphics.Bitmap

/**
 *Author: gaojin
 *Time: 2018/8/19 下午4:07
 */

fun blurBitmap(context: Context, bitmap: Bitmap, process: Int): Bitmap? {
    var blurBitmap: Bitmap? = null
    val manager = BlurManager(bitmap)
    blurBitmap = manager.process(process)
    return blurBitmap
}