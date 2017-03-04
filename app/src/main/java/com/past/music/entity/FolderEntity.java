package com.past.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/4 下午6:20
 * 描述：音频文件的文件夹信息
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
public class FolderEntity implements Parcelable {

    private String folder_name;
    private String folder_path;
    private String folder_sort;
    private int folder_count;

    public FolderEntity() {
    }

    protected FolderEntity(Parcel in) {
        folder_name = in.readString();
        folder_path = in.readString();
        folder_sort = in.readString();
        folder_count = in.readInt();
    }

    public static final Creator<FolderEntity> CREATOR = new Creator<FolderEntity>() {
        @Override
        public FolderEntity createFromParcel(Parcel in) {
            return new FolderEntity(in);
        }

        @Override
        public FolderEntity[] newArray(int size) {
            return new FolderEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(folder_name);
        dest.writeString(folder_path);
        dest.writeString(folder_sort);
        dest.writeInt(folder_count);
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getFolder_path() {
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }

    public String getFolder_sort() {
        return folder_sort;
    }

    public void setFolder_sort(String folder_sort) {
        this.folder_sort = folder_sort;
    }

    public int getFolder_count() {
        return folder_count;
    }

    public void setFolder_count(int folder_count) {
        this.folder_count = folder_count;
    }
}
