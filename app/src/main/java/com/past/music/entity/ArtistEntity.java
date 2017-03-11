package com.past.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaojin on 2017/3/11.
 */

public class ArtistEntity implements Parcelable {

    public String artist_name;
    public int number_of_tracks;
    public long artist_id;
    public String artist_sort;

    public ArtistEntity() {
    }

    public ArtistEntity(Parcel in) {
        artist_name = in.readString();
        number_of_tracks = in.readInt();
        artist_id = in.readLong();
        artist_sort = in.readString();
    }

    public static final Creator<ArtistEntity> CREATOR = new Creator<ArtistEntity>() {
        @Override
        public ArtistEntity createFromParcel(Parcel in) {
            return new ArtistEntity(in);
        }

        @Override
        public ArtistEntity[] newArray(int size) {
            return new ArtistEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artist_name);
        dest.writeInt(number_of_tracks);
        dest.writeLong(artist_id);
        dest.writeString(artist_sort);
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public int getNumber_of_tracks() {
        return number_of_tracks;
    }

    public void setNumber_of_tracks(int number_of_tracks) {
        this.number_of_tracks = number_of_tracks;
    }

    public long getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(long artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_sort() {
        return artist_sort;
    }

    public void setArtist_sort(String artist_sort) {
        this.artist_sort = artist_sort;
    }
}
