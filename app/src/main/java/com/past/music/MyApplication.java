package com.past.music;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.past.music.database.service.ImageDBService;
import com.past.music.database.service.MusicInfoDBService;
import com.past.music.database.service.SongListDBService;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 上午12:42
 * 版本：
 * 描述：
 * 备注：两个独立的进程，所以MyApplication被初始化了两次
 * =======================================================
 */
public class MyApplication extends Application {
    public static Context mContext = null;

    public static ImageDBService imageDBService = null;
    public static MusicInfoDBService musicInfoDBService = null;
    public static SongListDBService songListDBService = null;
    private static Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Fresco.initialize(this);
        imageDBService = new ImageDBService(this);
        musicInfoDBService = new MusicInfoDBService(this);
        songListDBService = new SongListDBService(this);
    }

    public static Gson gsonInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
}
