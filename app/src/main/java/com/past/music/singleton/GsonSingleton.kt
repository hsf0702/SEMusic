package com.past.music.singleton

import com.google.gson.Gson

/**
 * Author: gaojin
 * Time: 2018/5/6 下午4:45
 */
class GsonSingleton {
    companion object {
        val instance: Gson by lazy { Gson() }
    }
}