package com.se.music.entity

import android.os.Parcel
import android.os.Parcelable

/**
 *Author: gaojin
 *Time: 2018/5/13 下午4:28
 */

class MusicEntity() : Parcelable {
    /**
     * 数据库中音乐文件的列名
     */
    var songId: Long = -1
    var albumId = -1
    var albumName: String? = null        //专辑名字
    var albumData: String? = null        //
    var albumPic: String? = null
    var duration: Int = 0
    var musicName: String? = null
    var artist: String? = null
    var artistId: Long = 0
    var data: String? = null
    var folder: String? = null
    var lrc: String? = null
    var islocal: Boolean = false
    var sort: String? = null

    var size: Int = 0
    /**
     * 0表示没有收藏 1表示收藏
     */
    var favorite = 0

    constructor(parcel: Parcel) : this() {
        songId = parcel.readLong()
        albumId = parcel.readInt()
        albumName = parcel.readString()
        albumData = parcel.readString()
        albumPic = parcel.readString()
        duration = parcel.readInt()
        musicName = parcel.readString()
        artist = parcel.readString()
        artistId = parcel.readLong()
        data = parcel.readString()
        folder = parcel.readString()
        lrc = parcel.readString()
        islocal = parcel.readByte() != 0.toByte()
        sort = parcel.readString()
        size = parcel.readInt()
        favorite = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(songId)
        parcel.writeInt(albumId)
        parcel.writeString(albumName)
        parcel.writeString(albumData)
        parcel.writeString(albumPic)
        parcel.writeInt(duration)
        parcel.writeString(musicName)
        parcel.writeString(artist)
        parcel.writeLong(artistId)
        parcel.writeString(data)
        parcel.writeString(folder)
        parcel.writeString(lrc)
        parcel.writeByte(if (islocal) 1 else 0)
        parcel.writeString(sort)
        parcel.writeInt(size)
        parcel.writeInt(favorite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicEntity> {
        override fun createFromParcel(parcel: Parcel): MusicEntity {
            return MusicEntity(parcel)
        }

        override fun newArray(size: Int): Array<MusicEntity?> {
            return arrayOfNulls(size)
        }
    }
}