package com.se.music.block

/**
 *Author: gaojin
 *Time: 2018/9/27 下午6:58
 */

interface ViewBlockAction {
    fun updateBlock()
    /**
     * 切换歌曲
     */
    fun musicChanged()
}