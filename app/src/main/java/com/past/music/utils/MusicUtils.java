package com.past.music.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.github.promeg.pinyinhelper.Pinyin;
import com.past.music.entity.FolderEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/4 下午3:17
 * 描述：
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
public class MusicUtils implements MConstants {

    //用于检索本地文件
    public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟


    //查询数据库的列名称
    private static String[] info_music = new String[]{
            MediaStore.Audio.Media._ID          //音乐ID
            , MediaStore.Audio.Media.TITLE      //音乐的标题
            , MediaStore.Audio.Media.DATA       //日期
            , MediaStore.Audio.Media.ALBUM_ID   //专辑ID
            , MediaStore.Audio.Media.ALBUM      //专辑
            , MediaStore.Audio.Media.ARTIST     //艺术家
            , MediaStore.Audio.Media.ARTIST_ID  //艺术家ID
            , MediaStore.Audio.Media.DURATION   //音乐时长
            , MediaStore.Audio.Media.SIZE};     //音乐大小

    private static String[] info_album = new String[]{
            MediaStore.Audio.Albums._ID
            , MediaStore.Audio.Albums.ALBUM_ART
            , MediaStore.Audio.Albums.ALBUM
            , MediaStore.Audio.Albums.NUMBER_OF_SONGS
            , MediaStore.Audio.Albums.ARTIST};

    private static String[] info_artist = new String[]{
            MediaStore.Audio.Artists.ARTIST
            , MediaStore.Audio.Artists.NUMBER_OF_TRACKS
            , MediaStore.Audio.Artists._ID};

    private static String[] info_folder = new String[]{MediaStore.Files.FileColumns.DATA};

    public static List<FolderEntity> queryFolder(Context context) {

        Uri uri = MediaStore.Files.getContentUri("external");
        //ContentProvider获取数据
        ContentResolver cr = context.getContentResolver();

        //筛选条件
        StringBuilder mSelection = new StringBuilder(MediaStore.Files.FileColumns.MEDIA_TYPE
                + " = " + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO + " and " + "("
                + MediaStore.Files.FileColumns.DATA + " like'%.mp3' or " + MediaStore.Audio.Media.DATA
                + " like'%.wma')");
        // 查询语句：检索出.mp3为后缀名，时长大于1分钟，文件大小大于1MB的媒体文件
        mSelection.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        mSelection.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);
        mSelection.append(") group by ( " + MediaStore.Files.FileColumns.PARENT);
        List<FolderEntity> folderList = getFolderList(cr.query(uri, info_folder, mSelection.toString(), null, null));

        return folderList;
    }

    public static List<FolderEntity> getFolderList(Cursor cursor) {
        List<FolderEntity> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            FolderEntity info = new FolderEntity();
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            info.setFolder_path(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            info.setFolder_name(info.getFolder_path().substring(info.getFolder_path().lastIndexOf(File.separator) + 1));
            info.setFolder_sort(Pinyin.toPinyin(info.getFolder_name().charAt(0)).substring(0, 1).toUpperCase());
            list.add(info);
        }
        //cursor一定一定要关闭
        cursor.close();
        return list;
    }
}
