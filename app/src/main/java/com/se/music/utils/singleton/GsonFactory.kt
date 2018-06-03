package com.se.music.utils.singleton

import com.google.gson.Gson

/**
 * Created by gaojin on 2018/1/1.
 */
class GsonFactory {
    companion object {
        val instance: Gson by lazy { Gson() }
    }
}