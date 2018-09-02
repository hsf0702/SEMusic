package com.se.music

import android.app.Application
import android.content.Context
import com.se.multidex.MultiDexApplication
import com.se.music.base.BaseConfig
import com.se.music.singleton.ApplicationSingleton

/**
 * Author: gaojin
 * Time: 2018/5/6 下午2:38
 * 两个独立的进程，所以MyApplication被初始化了两次
 */
class MusicApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ApplicationSingleton.bindInstance(this)
    }

    override fun onCreate() {
        super.onCreate()
        BaseConfig.init(this)
    }
}