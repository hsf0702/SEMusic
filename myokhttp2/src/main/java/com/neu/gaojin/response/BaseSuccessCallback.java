package com.neu.gaojin.response;

/**
 * Created by gaojin on 2017/4/11.
 */

public abstract class BaseSuccessCallback<T> extends BaseCallback<T> {
    @Override
    public void onFailure(int code, String error_msg) {

    }

}
