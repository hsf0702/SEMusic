package com.se.music.entity

import android.os.Parcel
import android.os.Parcelable

/**
 *Author: gaojin
 *Time: 2018/5/13 下午4:19
 */

class AlbumEntity() : Parcelable {
    //专辑名称
    var albumName: String? = null
    //专辑在数据库中的id
    var albumId = -1
    //专辑的歌曲数目
    var numberOfSongs = 0
    //专辑封面图片路径
    var albumArt: String? = null
    var albumArtist: String? = null
    var albumSort: String? = null

    constructor(parcel: Parcel) : this() {
        albumName = parcel.readString()
        albumId = parcel.readInt()
        numberOfSongs = parcel.readInt()
        albumArt = parcel.readString()
        albumArtist = parcel.readString()
        albumSort = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(albumName)
        parcel.writeInt(albumId)
        parcel.writeInt(numberOfSongs)
        parcel.writeString(albumArt)
        parcel.writeString(albumArtist)
        parcel.writeString(albumSort)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlbumEntity> {
        override fun createFromParcel(parcel: Parcel): AlbumEntity {
            return AlbumEntity(parcel)
        }

        override fun newArray(size: Int): Array<AlbumEntity?> {
            return arrayOfNulls(size)
        }
    }
}