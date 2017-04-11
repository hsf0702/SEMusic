package com.past.music;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.past.music.database.service.DBService;

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

    public static DBService dbService = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Fresco.initialize(this);
        dbService = new DBService(this);
//        LeakCanary.install(this);
    }
}
