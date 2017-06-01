package com.past.music.api;

import com.neu.gaojin.annotation.HttpUrlParams;
import com.neu.gaojin.annotation.RequiredParam;
import com.past.music.api.base.BaseRequestQQApi;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/19 12:36
 * 描述：
 * 备注：
 * =======================================================
 */
public class QQSearchRequest extends BaseRequestQQApi<QQSearchResponse> {

    @HttpUrlParams("URL")
    String URL = "http://route.showapi.com/213-1";

    @RequiredParam("keyword")
    String keyword;

    public QQSearchRequest(String songName) {
        this.keyword = songName;
    }
}
