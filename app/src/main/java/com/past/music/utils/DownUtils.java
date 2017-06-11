package com.past.music.utils;

import android.content.Context;
import android.content.Intent;

import com.past.music.download.DownService;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/6/8 20:29
 * 描述：
 * 备注：
 * =======================================================
 */
public class DownUtils {
    public static void downMusic(final Context context, final String id, final String name, final String artist) {
        String url = "http://dl.stream.qqmusic.qq.com/" + id + ".mp3?vkey=DB5FCB17A7D5953C9048981743126FC15011710D25DBDE21B4AF697A474E7B170FAFF5F2FBDEB2F7990B82440626695F08317A4E296EA7B2&guid=2718671044";
        Intent i = new Intent(context, DownService.class);
        i.setAction(DownService.ADD_DOWNTASK);
        i.putExtra("id", id);
        i.putExtra("name", name);
        i.putExtra("artist", artist);
        i.putExtra("url", url);
        i.setPackage(MConstants.PACKAGE);
        context.startService(i);
    }
}
