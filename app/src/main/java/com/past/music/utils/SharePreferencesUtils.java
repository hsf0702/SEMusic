package com.past.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gaojin on 2017/3/11.
 */

public class SharePreferencesUtils {

    public static final String ARTIST_SORT_ORDER = "artist_sort_order";
    public static final String SONG_SORT_ORDER = "song_sort_order";
    public static final String ALBUM_SORT_ORDER = "album_sort_order";

    private static SharePreferencesUtils sInstance;

    private static SharedPreferences mPreferences;

    private static final String SHARE_PREFERENCE_NAME = "past_music_share_preference";

    public SharePreferencesUtils(final Context context) {
        mPreferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static final SharePreferencesUtils getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new SharePreferencesUtils(context.getApplicationContext());
        }
        return sInstance;
    }


    public final String getArtistSortOrder() {
        return mPreferences.getString(ARTIST_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z);
    }

    public final String getSongSortOrder() {
        return mPreferences.getString(SONG_SORT_ORDER, SortOrder.SongSortOrder.SONG_A_Z);
    }

    public final String getAlbumSortOrder() {
        return mPreferences.getString(ALBUM_SORT_ORDER, SortOrder.AlbumSortOrder.ALBUM_A_Z);
    }

    public void setPlayLink(long id, String link) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(id + "", link);
        editor.apply();
    }

    public String getPlayLink(long id) {
        return mPreferences.getString(id + "", null);
    }

}
