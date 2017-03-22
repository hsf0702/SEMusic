package com.past.music.service;

/**
 * Created by gaojin on 2017/3/21.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is used by the music playback service to track the music tracks it is playing
 * It has extra meta data to determine where the track came from so that we can show the appropriate
 * song playing indicator
 */
public class MusicTrack implements Parcelable {


    public long mId;
    public int mSourcePosition;
//    public String mTitle;
//    public String mAlbum;
//    public String mArtist;


    public MusicTrack(long id, int sourcePosition) {
        mId = id;
        mSourcePosition = sourcePosition;

    }

    protected MusicTrack(Parcel in) {
        mId = in.readLong();
        mSourcePosition = in.readInt();
    }

    public static final Creator<MusicTrack> CREATOR = new Creator<MusicTrack>() {
        @Override
        public MusicTrack createFromParcel(Parcel in) {
            return new MusicTrack(in);
        }

        @Override
        public MusicTrack[] newArray(int size) {
            return new MusicTrack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mSourcePosition);
    }
}
