package com.past.music;/**
 * Created by gaojin on 2017/1/26.
 */

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

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
    public static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Fresco.initialize(this);
        LeakCanary.install(this);
    }
}
