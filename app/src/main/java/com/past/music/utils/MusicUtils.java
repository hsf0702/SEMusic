package com.past.music.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.github.promeg.pinyinhelper.Pinyin;
import com.past.music.entity.AlbumEntity;
import com.past.music.entity.ArtistEntity;
import com.past.music.entity.FolderEntity;
import com.past.music.entity.MusicEntity;

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

    private static String[] proj_music = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE};

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

    /**
     * 获取音频文件的文件夹信息
     *
     * @param context
     * @return
     */
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

    /**
     * 查询获取本地音乐
     *
     * @param context
     * @param from    不同的界面进来要做不同的查询
     * @return
     */
    public static List<MusicEntity> queryMusic(Context context, int from) {
        return queryMusic(context, null, from);
    }


    public static ArrayList<MusicEntity> queryMusic(Context context, String id, int from) {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        StringBuilder select = new StringBuilder(" 1=1 and title != ''");
        // 查询语句：检索出.mp3为后缀名，时长大于1分钟，文件大小大于1MB的媒体文件
        select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);
        String selectionStatement = "is_music=1 AND title != ''";
        final String songSortOrder = SharePreferencesUtils.getInstance(context).getSongSortOrder();

        switch (from) {
            case START_FROM_LOCAL:
                ArrayList<MusicEntity> list3 = getMusicListCursor(cr.query(uri, info_music, select.toString(), null, songSortOrder));
                return list3;
            case START_FROM_ARTIST:
                select.append(" and " + MediaStore.Audio.Media.ARTIST_ID + " = " + id);
                return getMusicListCursor(cr.query(uri, info_music, select.toString(), null,
                        SharePreferencesUtils.getInstance(context).getArtistSortOrder()));
            case START_FROM_ALBUM:
                select.append(" and " + MediaStore.Audio.Media.ALBUM_ID + " = " + id);
                return getMusicListCursor(cr.query(uri, info_music, select.toString(), null,
                        SharePreferencesUtils.getInstance(context).getAlbumSortOrder()));
            default:
                return null;
        }

    }

    public static ArrayList<MusicEntity> getMusicListCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        ArrayList<MusicEntity> musicList = new ArrayList<>();
        while (cursor.moveToNext()) {
            MusicEntity music = new MusicEntity();
            music.songId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));
            music.albumId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            music.albumName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            music.albumData = getAlbumArtUri(music.albumId) + "";
            music.duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            music.musicName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            music.artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            music.artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            music.data = filePath;
            music.folder = filePath.substring(0, filePath.lastIndexOf(File.separator));
            music.size = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));
            music.islocal = true;
            music.sort = Pinyin.toPinyin(music.musicName.charAt(0)).substring(0, 1).toUpperCase();
            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }

    public static Uri getAlbumArtUri(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }

    /**
     * 获取歌手信息
     *
     * @param context
     * @return
     */
    public static List<ArtistEntity> queryArtist(Context context) {

        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();
        StringBuilder where = new StringBuilder(MediaStore.Audio.Artists._ID
                + " in (select distinct " + MediaStore.Audio.Media.ARTIST_ID
                + " from audio_meta where (1=1 )");
        where.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        where.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);

        where.append(")");

        List<ArtistEntity> list = getArtistList(cr.query(uri, info_artist,
                where.toString(), null, SharePreferencesUtils.getInstance(context).getArtistSortOrder()));
        return list;

    }

    public static List<ArtistEntity> getArtistList(Cursor cursor) {
        List<ArtistEntity> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            ArtistEntity info = new ArtistEntity();
            info.artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
            info.number_of_tracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
            info.artist_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
            info.artist_sort = Pinyin.toPinyin(info.artist_name.charAt(0)).substring(0, 1).toUpperCase();
            list.add(info);
        }
        cursor.close();
        return list;
    }


    /**
     * 获取专辑信息
     *
     * @param context
     * @return
     */
    public static List<AlbumEntity> queryAlbums(Context context) {

        ContentResolver cr = context.getContentResolver();
        StringBuilder where = new StringBuilder(MediaStore.Audio.Albums._ID
                + " in (select distinct " + MediaStore.Audio.Media.ALBUM_ID
                + " from audio_meta where (1=1)");
        where.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        where.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);

        where.append(" )");

        // Media.ALBUM_KEY 按专辑名称排序
        List<AlbumEntity> list = getAlbumList(cr.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, info_album,
                where.toString(), null, SharePreferencesUtils.getInstance(context).getAlbumSortOrder()));
        return list;

    }

    public static List<AlbumEntity> getAlbumList(Cursor cursor) {
        List<AlbumEntity> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            AlbumEntity info = new AlbumEntity();
            info.album_name = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            info.album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
            info.number_of_songs = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
            info.album_art = getAlbumArtUri(info.album_id) + "";
            info.album_artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
            info.album_sort = Pinyin.toPinyin(info.album_name.charAt(0)).substring(0, 1).toUpperCase();
            list.add(info);
        }
        cursor.close();
        return list;
    }

    public static MusicEntity getMusicInfo(Context context, long id) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj_music, "_id = " + String.valueOf(id), null, null);
        if (cursor == null) {
            return null;
        }
        MusicEntity music = new MusicEntity();
        while (cursor.moveToNext()) {
            music.songId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));
            music.albumId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            music.albumName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            music.albumData = getAlbumArtUri(music.albumId) + "";
            music.duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            music.musicName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            music.size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            music.artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            music.artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            music.data = filePath;
            String folderPath = filePath.substring(0,
                    filePath.lastIndexOf(File.separator));
            music.folder = folderPath;
            music.sort = Pinyin.toPinyin(music.musicName.charAt(0)).substring(0, 1).toUpperCase();
        }
        cursor.close();
        return music;
    }

}
