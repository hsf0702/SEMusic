package com.se.music.common.entity

import android.os.Parcel
import android.os.Parcelable

/**
 *Author: gaojin
 *Time: 2018/5/13 下午4:27
 */

class FolderEntity() : Parcelable {
    var folder_name: String? = null
    var folder_path: String? = null
    var folder_sort: String? = null
    var folder_count: Int = 0

    constructor(parcel: Parcel) : this() {
        folder_name = parcel.readString()
        folder_path = parcel.readString()
        folder_sort = parcel.readString()
        folder_count = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(folder_name)
        parcel.writeString(folder_path)
        parcel.writeString(folder_sort)
        parcel.writeInt(folder_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FolderEntity> {
        override fun createFromParcel(parcel: Parcel): FolderEntity {
            return FolderEntity(parcel)
        }

        override fun newArray(size: Int): Array<FolderEntity?> {
            return arrayOfNulls(size)
        }
    }
}