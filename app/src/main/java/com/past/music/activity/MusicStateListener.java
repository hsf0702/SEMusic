package com.past.music.activity;

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

    void updateTime();

    void reloadAdapter();


}
