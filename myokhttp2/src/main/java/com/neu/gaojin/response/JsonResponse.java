package com.neu.gaojin.response;

import org.json.JSONObject;

/**
 * Created by gaojin on 2017/4/9.
 */

public abstract class JsonResponse implements ResponseBase {

    public abstract void onSuccess(int code, JSONObject response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
