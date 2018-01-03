package com.past.music.Config;/**
 * Created by gaojin on 2017/1/26.
 */

import android.os.Environment;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 下午2:44
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class BaseConfig {
    public static final String URL = "URL";
    public static final String MSG = "MSG";

    public static final String SECRET = "0bfb8ffd39e045fcaa90f0f6c2ee4078";
    public static final String APPID = "32384";
    public static final String QQ_MUSIC_URL = "http://route.showapi.com/213-4";

    public static final int Alpha = 0;

    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/pastmusic/";


    public static class PlayerMsg {
        public static final int PLAY_MSG = 0x01;
        public static final int PAUSE_MSG = 0x02;
        public static final int STOP_MSG = 0x03;
    }

    public static final String picBaseUrl = "https://y.gtimg.cn/music/photo_new/T001R300x300M000%1$s.jpg";
}
