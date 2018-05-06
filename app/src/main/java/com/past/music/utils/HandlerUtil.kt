package com.past.music.utils

import android.os.Handler

/**
 * Author: gaojin
 * Time: 2018/5/6 下午5:25
 */
class HandlerUtil : Handler() {
    companion object {
        val instance: HandlerUtil by lazy { HandlerUtil() }
    }
}