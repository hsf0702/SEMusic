package com.se.music.common.entity

import android.os.Parcel
import android.os.Parcelable

/**
 *Author: gaojin
 *Time: 2018/5/13 下午4:24
 */

class ArtistEntity() : Parcelable {
    var artistName: String? = null
    var numberOfTracks: Int = 0
    var artistId: Long = 0
    var artistSort: String? = null

    constructor(parcel: Parcel) : this() {
        artistName = parcel.readString()
        numberOfTracks = parcel.readInt()
        artistId = parcel.readLong()
        artistSort = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(artistName)
        parcel.writeInt(numberOfTracks)
        parcel.writeLong(artistId)
        parcel.writeString(artistSort)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArtistEntity> {
        override fun createFromParcel(parcel: Parcel): ArtistEntity {
            return ArtistEntity(parcel)
        }

        override fun newArray(size: Int): Array<ArtistEntity?> {
            return arrayOfNulls(size)
        }
    }
}