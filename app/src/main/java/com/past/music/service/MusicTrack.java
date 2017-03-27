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

    /**
     * 音乐的ID
     */
    public long mId;
    /**
     * 此首歌在播放列表中的位置
     */
    public int mSourcePosition;


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
