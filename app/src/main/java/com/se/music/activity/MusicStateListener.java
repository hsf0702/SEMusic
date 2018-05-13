package com.se.music.activity;

/**
 * Created by gaojin on 2017/3/22.
 * <p>
 * 音乐状态接口
 */

public interface MusicStateListener {
    /**
     * 更新歌曲状态信息
     */
    void updatePlayInfo();

    /**
     * 更新播放时间
     */
    void updateTime();

    void reloadAdapter();
}
