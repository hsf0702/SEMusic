package com.se.music.utils

import android.content.Context
import android.content.SharedPreferences
import com.se.music.utils.singleton.ApplicationSingleton

/**
 * Author: gaojin
 * Time: 2018/5/6 下午5:11
 */
class SharePreferencesUtils {

    companion object {
        val ARTIST_SORT_ORDER = "artist_sort_order"
        val SONG_SORT_ORDER = "song_sort_order"
        val ALBUM_SORT_ORDER = "album_sort_order"
        val DOWNMUSIC_BIT = "DOWNMUSIC_BIT"

        private const val SHARE_PREFERENCE_NAME = "past_music_share_preference"

        val instance: SharePreferencesUtils by lazy { SharePreferencesUtils() }
    }

    private val mPreferences: SharedPreferences = ApplicationSingleton.instance!!.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE)


    fun getArtistSortOrder(): String {
        return mPreferences.getString(ARTIST_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z)
    }

    fun getSongSortOrder(): String {
        return mPreferences.getString(SONG_SORT_ORDER, SortOrder.SongSortOrder.SONG_A_Z)
    }

    fun getAlbumSortOrder(): String {
        return mPreferences.getString(ALBUM_SORT_ORDER, SortOrder.AlbumSortOrder.ALBUM_A_Z)
    }

    fun setPlayLink(id: Long, link: String) {
        val editor = mPreferences.edit()
        editor.putString(id.toString() + "", link)
        editor.apply()
    }

    fun getPlayLink(id: Long): String {
        return mPreferences.getString(id.toString() + "", null)
    }

    fun getDownMusicBit(): Int {
        return mPreferences.getInt(DOWNMUSIC_BIT, 192)
    }

}