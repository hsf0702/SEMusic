package com.past.music.kotlinutils

import android.content.Context
import android.content.SharedPreferences
import com.past.music.utils.SortOrder

/**
 * Creator：gaojin
 * date：2017/8/6 下午9:39
 */

class SharePreferencesUtils constructor(context: Context) {

    var sharePreference: SharedPreferences? = null
    //类型后面加问号表示变量可以为null
    var sInstance: SharePreferencesUtils? = null

    init {
        sharePreference = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun getInstance(context: Context): SharePreferencesUtils {
        if (sInstance == null) {
            sInstance = SharePreferencesUtils(context.applicationContext)
        }
        return sInstance as SharePreferencesUtils
    }

    fun getArtistSortOrder(): String {
        return sharePreference!!.getString(ARTIST_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z)
    }

    fun getSongSortOrder(): String {
        return sharePreference!!.getString(SONG_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z)
    }

    fun getAlbumSortOrder(): String {
        return sharePreference!!.getString(ALBUM_SORT_ORDER, SortOrder.AlbumSortOrder.ALBUM_A_Z)
    }

    fun getPlayLink(id: Long): String {
        return sharePreference!!.getString(id.toString(), null)
    }

    fun setPlayLink(id: Long, link: String) {
        val editor: SharedPreferences.Editor = sharePreference!!.edit()
        editor.putString(id.toString(), link)
        editor.apply()
    }

    fun getDownMusicBit(): Int {
        return sharePreference!!.getInt(DOWNMUSIC_BIT, 192)
    }

    fun setDownMusicBit(bit: Int) {
        var editor: SharedPreferences.Editor = sharePreference!!.edit()
        editor.putInt(DOWNMUSIC_BIT, bit)
        editor.apply()
    }
}