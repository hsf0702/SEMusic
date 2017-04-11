package com.past.music.api.base;

import com.neu.gaojin.annotation.HttpUrlParams;
import com.neu.gaojin.annotation.RequiredParam;
import com.neu.gaojin.request.RequestBase;

/**
 * Created by gaojin on 2017/4/11.
 */

public class BaseLastFmApi<T> extends RequestBase<T> {

    @HttpUrlParams("URL")
    String URL = "http://ws.audioscrobbler.com/2.0";

    @RequiredParam("api_key")
    String api_key = "269ac032c31a44b7f3ef3c8d661747d0";

    @RequiredParam("format")
    String format = "json";
}
