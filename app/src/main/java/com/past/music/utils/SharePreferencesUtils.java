package com.past.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gaojin on 2017/3/11.
 */

public class SharePreferencesUtils {

    public static final String ARTIST_SORT_ORDER = "artist_sort_order";


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
}
