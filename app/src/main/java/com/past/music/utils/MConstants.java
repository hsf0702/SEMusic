package com.past.music.utils;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/4 下午3:15
 * 描述：常量接口
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
public interface MConstants {

    String MUSIC_COUNT_CHANGED = "com.past.music.musiccountchanged";
    String PLAYLIST_ITEM_MOVED = "com.past.music.moved";
    String PLAYLIST_COUNT_CHANGED = "com.past.music.playlistcountchanged";
    String EMPTY_LIST = "com.past.music.emptyplaylist";
    String PACKAGE = "com.past.music";

    int MUSICOVERFLOW = 0;
    int ARTISTOVERFLOW = 1;
    int ALBUMOVERFLOW = 2;
    int FOLDEROVERFLOW = 3;

    //歌手和专辑列表点击都会进入MyMusic 此时要传递参数表明是从哪里进入的
    int START_FROM_ARTIST = 1;
    int START_FROM_ALBUM = 2;
    int START_FROM_LOCAL = 3;
    int START_FROM_FOLDER = 4;
    int START_FROM_FAVORITE = 5;

    int FAV_PLAYLIST = 10;

}
