package com.se.music.utils

import android.database.Cursor
import com.se.music.common.MusicEntity
import com.se.music.common.SongListEntity
import com.se.music.provider.metadata.*

/**
 *Author: gaojin
 *Time: 2018/5/27 下午2:08
 */

fun parseCursorToSongList(id: Int, cursor: Cursor): ArrayList<SongListEntity> {
    val list = ArrayList<SongListEntity>()
    if (id == IdUtils.QUERY_SONG_LIST) {
        while (cursor.moveToNext()) {
            val songListEntity = SongListEntity(cursor.getString(SL_ID_INDEX)
                    , cursor.getString(SL_NAME_INDEX)
                    , cursor.getString(SL_CREATE_TIME_INDEX))

            songListEntity.count = cursor.getInt(SL_COUNT_INDEX)
            songListEntity.creator = cursor.getString(SL_CREATOR_INDEX)
            songListEntity.listPic = cursor.getString(SL_PIC_INDEX)
            songListEntity.info = cursor.getString(SL_INFO_INDEX)
            list.add(songListEntity)
        }
    }
    return list
}

/**
 * 本地音乐Cursor转化为List
 */
fun parseCursorToMusicEntityList(id: Int, cursor: Cursor): ArrayList<MusicEntity> {
    val list = ArrayList<MusicEntity>()
    if (id == IdUtils.QUERY_LOCAL_SONG) {
        while (cursor.moveToNext()) {
            val musicEntity = MusicEntity(cursor.getLong(LM_ID_INDEX)
                    , cursor.getString(LM_TITLE_INDEX)
                    , cursor.getString(LM_ARTIST_INDEX)
                    , cursor.getLong(LM_ALBUM_ID_INDEX)
                    , null
                    , null
                    , cursor.getLong(LM_DURATION_INDEX)
                    , cursor.getString(LM_ALBUM_INDEX)
                    , cursor.getLong(LM_ARTIST_ID_INDEX)
                    , null
                    , cursor.getLong(LM_SIZE_INDEX)
                    , null
                    , null,
                    null
                    , 0
                    , false)
            list.add(musicEntity)
        }
    }
    return list
}