package com.past.music.service;

import com.past.music.pastmusic.IMediaAidlInterface;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/3 下午4:18
 * 版本：
 * 描述：界面和Service的桥
 * 备注：
 * =======================================================
 */
public class MusicPlayer {

    public static IMediaAidlInterface mService = null;

    /**
     * 播放或者暂停音乐
     */
    public static void playOrPause() {
        try {
            if (mService != null) {
                if (mService.isPlaying()) {
                    mService.pause();
                } else {
                    mService.play();
                }
            }
        } catch (final Exception ignored) {
        }
    }

}
