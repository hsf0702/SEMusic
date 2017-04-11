package com.neu.gaojin.entity;

import android.os.Bundle;

/**
 * Created by gaojin on 2017/4/10.
 */

public class RequestEntity {

    private Bundle urlParams;

    private Bundle requiredParam;

    private int requestMethod = -1;

    public int getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Bundle getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(Bundle urlParams) {
        this.urlParams = urlParams;
    }

    public Bundle getRequiredParam() {
        return requiredParam;
    }

    public void setRequiredParam(Bundle requiredParam) {
        this.requiredParam = requiredParam;
    }
}
