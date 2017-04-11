package com.past.music.api;

import com.neu.gaojin.annotation.HttpMethod;
import com.neu.gaojin.annotation.RequiredParam;
import com.past.music.api.base.BaseLastFmApi;

/**
 * Created by gaojin on 2017/4/11.
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
