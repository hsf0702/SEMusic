package com.past.music.api;

import com.neu.gaojin.annotation.HttpMethod;
import com.neu.gaojin.annotation.RequiredParam;
import com.past.music.api.base.BaseLastFmApi;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/23 13:56
 * 描述：
 * 备注：
 * =======================================================
 */
@HttpMethod("GET")
public class AvatarRequest extends BaseLastFmApi<AvatarResponse> {
    @RequiredParam("method")
    String method = "artist.getinfo";

    @RequiredParam("artist")
    String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
