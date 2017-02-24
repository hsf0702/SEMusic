package com.past.music.Config;/**
 * Created by gaojin on 2017/1/26.
 */

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 上午12:42
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
