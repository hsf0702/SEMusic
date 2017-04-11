package com.neu.gaojin.response;

/**
 * Created by gaojin on 2017/4/9.
 */

public interface ResponseBase {
    void onFailure(int code, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
