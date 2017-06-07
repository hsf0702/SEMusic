package com.past.music.api;

import com.neu.gaojin.annotation.HttpUrlParams;
import com.neu.gaojin.annotation.RequiredParam;
import com.past.music.api.base.BaseRequestQQApi;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/6/5 13:56
 * 描述：
 * 备注：
 * =======================================================
 */
public class LrcRequest extends BaseRequestQQApi<LrcResponse> {

    public LrcRequest(String musicid) {
        this.musicid = musicid;
    }

    @HttpUrlParams("URL")
    String URL = "http://route.showapi.com/213-2";

    @RequiredParam("musicid")
    String musicid;
}
