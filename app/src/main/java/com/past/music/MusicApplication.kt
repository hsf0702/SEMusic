package com.past.music

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Author: gaojin
 * Time: 2018/5/6 下午2:38
 * 两个独立的进程，所以MyApplication被初始化了两次
 */
class MusicApplication : Application() {

    companion object {
        val instance: MusicApplication by lazy { MusicApplication() }
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}