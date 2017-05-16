package com.past.music.database.service;

import android.content.Context;

import com.past.music.database.PastMusicDBHelper;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/15 16:58
 * 描述：
 * 备注：
 * =======================================================
 */
public class MusicInfoDBService {
    PastMusicDBHelper pastMusicDBHelper;

    public MusicInfoDBService(Context context) {
        pastMusicDBHelper = new PastMusicDBHelper(context);
    }
}
