package com.se.music.utils

import android.database.Cursor
import com.se.music.common.SongListEntity
import com.se.music.provider.SongList

/**
 *Author: gaojin
 *Time: 2018/5/27 下午2:08
 */

fun parseCursorToSongList(id: Int, cursor: Cursor): ArrayList<SongListEntity> {
    val list = ArrayList<SongListEntity>()
    if (id == IdUtils.QUERY_SONG_LIST) {
        while (cursor.moveToNext()) {
            val songListEntity = SongListEntity(cursor.getString(SongList.ID_INDEX)
                    , cursor.getString(SongList.NAME_INDEX)
                    , cursor.getString(SongList.CREATE_TIME_INDEX))

            songListEntity.count = cursor.getInt(SongList.COUNT_INDEX)
            songListEntity.creator = cursor.getString(SongList.CREATOR_INDEX)
            songListEntity.listPic = cursor.getString(SongList.PIC_INDEX)
            songListEntity.info = cursor.getString(SongList.INFO_INDEX)
            list.add(songListEntity)
        }
    }
    return list
}