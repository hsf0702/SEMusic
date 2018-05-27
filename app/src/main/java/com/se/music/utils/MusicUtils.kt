package com.se.music.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.github.promeg.pinyinhelper.Pinyin
import com.se.music.R
import com.se.music.common.AlbumEntity
import com.se.music.common.ArtistEntity
import com.se.music.common.FolderEntity
import com.se.music.common.MusicEntity
import com.se.music.utils.MConstants.Companion.START_FROM_ALBUM
import com.se.music.utils.MConstants.Companion.START_FROM_ARTIST
import com.se.music.utils.MConstants.Companion.START_FROM_FOLDER
import com.se.music.utils.MConstants.Companion.START_FROM_LOCAL
import java.io.File
import java.util.*

/**
 * Created by gaojin on 2018/3/6.
 */
class MusicUtils : MConstants {
    companion object {
        //用于检索本地文件
        val FILTER_SIZE = 1024 * 1024// 1MB
        val FILTER_DURATION = 60 * 1000// 1分钟


        //查询数据库的列名称
        private val info_music = arrayOf(MediaStore.Audio.Media._ID          //音乐ID
                , MediaStore.Audio.Media.TITLE      //音乐的标题
                , MediaStore.Audio.Media.DATA       //日期
                , MediaStore.Audio.Media.ALBUM_ID   //专辑ID
                , MediaStore.Audio.Media.ALBUM      //专辑
                , MediaStore.Audio.Media.ARTIST     //艺术家
                , MediaStore.Audio.Media.ARTIST_ID  //艺术家ID
                , MediaStore.Audio.Media.DURATION   //音乐时长
                , MediaStore.Audio.Media.SIZE)     //音乐大小

        private val proj_music = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE)

        private val info_album = arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ARTIST)

        private val info_artist = arrayOf(MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_TRACKS, MediaStore.Audio.Artists._ID)

        private val info_folder = arrayOf(MediaStore.Files.FileColumns.DATA)
        /**
         * 获取音频文件的文件夹信息
         *
         * @param context
         * @return
         */
        fun queryFolder(context: Context): List<FolderEntity> {

            val uri = MediaStore.Files.getContentUri("external")
            //ContentProvider获取数据
            val cr = context.contentResolver

            //筛选条件
            val mSelection = StringBuilder(MediaStore.Files.FileColumns.MEDIA_TYPE
                    + " = " + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO + " and " + "("
                    + MediaStore.Files.FileColumns.DATA + " like'%.mp3' or " + MediaStore.Audio.Media.DATA
                    + " like'%.wma')")
            // 查询语句：检索出.mp3为后缀名，时长大于1分钟，文件大小大于1MB的媒体文件
            mSelection.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE)
            mSelection.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION)
            mSelection.append(") group by ( " + MediaStore.Files.FileColumns.PARENT)

            return getFolderList(cr.query(uri, info_folder, mSelection.toString(), null, null))
        }

        fun getFolderList(cursor: Cursor?): List<FolderEntity> {
            val list = ArrayList<FolderEntity>()
            while (cursor!!.moveToNext()) {
                val filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))
                val folderPath = filePath.substring(0, filePath.lastIndexOf(File.separator))
                val folderName = folderPath.substring(folderPath.lastIndexOf(File.separator) + 1)
                val folderSort = Pinyin.toPinyin(folderName[0]).substring(0, 1).toUpperCase()
                val info = FolderEntity(folderName, folderPath, folderSort)
                list.add(info)
            }
            //cursor一定一定要关闭
            cursor.close()
            return list
        }

        /**
         * 查询获取本地音乐
         *
         * @param context
         * @param from    不同的界面进来要做不同的查询
         * @return
         */
        fun queryMusic(context: Context, from: Int): List<MusicEntity> {
            return queryMusic(context, null, from)
        }


        fun queryMusic(context: Context, id: String?, from: Int): ArrayList<MusicEntity> {

            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val cr = context.contentResolver

            val select = StringBuilder(" 1=1 and title != ''")
            // 查询语句：检索出.mp3为后缀名，时长大于1分钟，文件大小大于1MB的媒体文件
            select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE)
            select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION)

            when (from) {
                START_FROM_LOCAL -> {
                    return getMusicListCursor(cr.query(uri, info_music, select.toString(), null, SharePreferencesUtils.instance.getSongSortOrder()))
                }
                START_FROM_ARTIST -> {
                    select.append(" and " + MediaStore.Audio.Media.ARTIST_ID + " = " + id)
                    return getMusicListCursor(cr.query(uri, info_music, select.toString(), null, SharePreferencesUtils.instance.getArtistSortOrder()))
                }
                START_FROM_ALBUM -> {
                    select.append(" and " + MediaStore.Audio.Media.ALBUM_ID + " = " + id)
                    return getMusicListCursor(cr.query(uri, info_music, select.toString(), null, SharePreferencesUtils.instance.getAlbumSortOrder()))
                }
                START_FROM_FOLDER -> {
                    val list = getMusicListCursor(cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj_music,
                            select.toString(), null, null))
                    return list.filterTo(ArrayList()) { it.data!!.substring(0, it.data!!.lastIndexOf(File.separator)) == id }
                }
                else -> return getMusicListCursor(cr.query(uri, info_music, select.toString(), null, SharePreferencesUtils.instance.getSongSortOrder()))
            }

        }

        private fun getMusicListCursor(cursor: Cursor): ArrayList<MusicEntity> {

            val musicList = ArrayList<MusicEntity>()
            while (cursor.moveToNext()) {
                val songId = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media._ID)).toLong()
                val albumId = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val albumName = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                val albumData = getAlbumArtUri(albumId.toLong()).toString() + ""
                val duration = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION))
                val musicName = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.TITLE))
                val size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                val artist = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                val filePath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA))
                val data = filePath
                val folderPath = filePath.substring(0,
                        filePath.lastIndexOf(File.separator))
                val folder = folderPath
                val sort = Pinyin.toPinyin(musicName!![0]).substring(0, 1).toUpperCase()
                val music = MusicEntity(songId, albumName)
                musicList.add(music)
            }
            cursor.close()
            return musicList
        }

        fun getAlbumArtUri(albumId: Long): Uri {
            return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId)
        }

        /**
         * 获取歌手信息
         *
         * @param context
         * @return
         */
        fun queryArtist(context: Context): List<ArtistEntity> {

            val uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
            val cr = context.contentResolver
            val where = StringBuilder(MediaStore.Audio.Artists._ID
                    + " in (select distinct " + MediaStore.Audio.Media.ARTIST_ID
                    + " from audio_meta where (1=1 )")
            where.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE)
            where.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION)

            where.append(")")

            return getArtistList(cr.query(uri, info_artist,
                    where.toString(), null, SharePreferencesUtils.instance.getArtistSortOrder()))

        }

        fun getArtistList(cursor: Cursor?): List<ArtistEntity> {
            val list = ArrayList<ArtistEntity>()
            while (cursor!!.moveToNext()) {
                val info = ArtistEntity(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                        , cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
                        , cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID))
                        , Pinyin.toPinyin(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))[0]).substring(0, 1).toUpperCase())
                list.add(info)
            }
            cursor.close()
            return list
        }


        /**
         * 获取专辑信息
         *
         * @param context
         * @return
         */
        fun queryAlbums(context: Context): List<AlbumEntity> {

            val cr = context.contentResolver
            val where = StringBuilder(MediaStore.Audio.Albums._ID
                    + " in (select distinct " + MediaStore.Audio.Media.ALBUM_ID
                    + " from audio_meta where (1=1)")
            where.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE)
            where.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION)

            where.append(" )")

            // Media.ALBUM_KEY 按专辑名称排序
            return getAlbumList(cr.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, info_album,
                    where.toString(), null, SharePreferencesUtils.instance.getAlbumSortOrder()))

        }

        fun getAlbumList(cursor: Cursor?): List<AlbumEntity> {
            val list = ArrayList<AlbumEntity>()
            while (cursor!!.moveToNext()) {
                val info = AlbumEntity(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                        , cursor.getColumnIndex(MediaStore.Audio.Albums._ID)
                        , cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
                        , getAlbumArtUri(cursor.getColumnIndex(MediaStore.Audio.Albums._ID).toLong()).toString()
                        , cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
                        , Pinyin.toPinyin(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)).substring(0, 1)[0]).toUpperCase())
                list.add(info)
            }
            cursor.close()
            return list
        }

        fun makeTimeString(milliSecs: Long): String {
            val sb = StringBuffer()
            val m = milliSecs / (60 * 1000)
            sb.append(if (m < 10) "0" + m else m)
            sb.append(":")
            val s = milliSecs % (60 * 1000) / 1000
            sb.append(if (s < 10) "0" + s else s)
            return sb.toString()
        }

        fun makeShortTimeString(context: Context, secs: Long): String {
            var secs = secs
            val hours: Long
            val mins: Long

            hours = secs / 3600
            secs %= 3600
            mins = secs / 60
            secs %= 60

            val durationFormat = context.resources.getString(if (hours == 0L) R.string.durationformatshort else R.string.durationformatlong)
            return String.format(durationFormat, hours, mins, secs)
        }
    }
}