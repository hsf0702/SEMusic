package com.past.music.api;

import com.neu.gaojin.annotation.HttpUrlParams;
import com.neu.gaojin.annotation.RequiredParam;
import com.past.music.api.base.BaseRequestQQApi;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/23 13:57
 * 描述：
 * 备注：
 * =======================================================
 */
public class HotListResquest extends BaseRequestQQApi<HotListResponse> {
    public HotListResquest(String topid) {
        this.topid = topid;
    }

    @HttpUrlParams("URL")
    String URL = "http://route.showapi.com/213-4";

    @RequiredParam("topid")
    String topid;

    public String getTopid() {
        return topid;
    }

    public void setTopid(String topid) {
        this.topid = topid;
    }
}
