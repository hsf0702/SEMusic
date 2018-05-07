package com.past.music

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco
import com.past.music.base.BaseConfig
import com.past.music.singleton.ApplicationSingleton

/**
 * Author: gaojin
 * Time: 2018/5/6 下午2:38
 * 两个独立的进程，所以MyApplication被初始化了两次
 */
class MusicApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ApplicationSingleton.bindInstance(this)
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        BaseConfig.init(this)
    }
}