package com.se.music.utils

import android.database.Cursor
import com.se.music.entity.AlbumEntity
import com.se.music.entity.ArtistEntity
import com.se.music.entity.MusicEntity
import com.se.music.entity.SongListEntity
import com.se.music.provider.database.provider.ImageStore
import com.se.music.provider.metadata.*

/**
 *Author: gaojin
 *Time: 2018/5/27 下午2:08
 */

/**
 * 歌单Cursor转化List
 */
fun parseCursorToSongList(id: Int, cursor: Cursor): ArrayList<SongListEntity> {
    val list = ArrayList<SongListEntity>()
    if (id == QUERY_SONG_LIST) {
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
fun parseCursorToMusicEntityList(id: Int, cursor: Cursor, list: ArrayList<MusicEntity>) {
    if (id == QUERY_LOCAL_SONG) {
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
}

/**
 * 歌手Cursor转换List
 */
fun parseCursorToArtistEntityList(id: Int, cursor: Cursor, list: ArrayList<ArtistEntity>) {
    if (id == QUERY_LOCAL_SINGER) {
        while (cursor.moveToNext()) {
            val artistEntity = ArtistEntity(cursor.getString(LS_ID_ARTIST)
                    , cursor.getInt(LS_ID_NUMBER_OF_TRACKS)
                    , cursor.getInt(LS_ID_INDEX)
                    , cursor.getString(LS_ARTIST_KEY))
            artistEntity.imageId = ImageStore.instance.query(cursor.getString(LS_ARTIST_KEY))
            list.add(artistEntity)
        }
    }
}

/**
 * 专辑Cursor转换List
 */
fun parseCursorToAlbumEntityList(id: Int, cursor: Cursor, list: ArrayList<AlbumEntity>) {
    if (id == QUERY_LOCAL_ALBUM) {
        while (cursor.moveToNext()) {
            val albumEntity = AlbumEntity(cursor.getInt(LA_ID)
                    , cursor.getString(LA_ALBUM)
                    , cursor.getInt(LA_SONG_NUMBER)
                    , cursor.getString(LA_ARTIST)
                    , cursor.getString(LA_ALBUM_KEY))
            albumEntity.imageId = ImageStore.instance.query(cursor.getString(LA_ALBUM_KEY))
            list.add(albumEntity)
        }
    }
}