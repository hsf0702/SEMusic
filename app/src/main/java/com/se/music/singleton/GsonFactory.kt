package com.se.music.singleton

import com.google.gson.Gson

/**
 * Created by gaojin on 2018/1/1.
 */
class GsonFactory {
    companion object {
        @Volatile
        private var gson: Gson? = null

        fun getInstance(): Gson {
            if (null == gson) {
                synchronized(GsonFactory::class.java) {
                    if (null == gson) {
                        gson = Gson()
                    }
                }
            }
            return gson!!
        }
    }
}