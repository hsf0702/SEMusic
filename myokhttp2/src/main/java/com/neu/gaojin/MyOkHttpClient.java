package com.neu.gaojin;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.neu.gaojin.entity.RequestEntity;
import com.neu.gaojin.exception.NetWorkException;
import com.neu.gaojin.request.RequestBase;
import com.neu.gaojin.response.BaseCallback;
import com.neu.gaojin.response.JsonResponse;
import com.neu.gaojin.response.ResponseBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/2/28 下午6:43
 * 版本：
 * 描述：封装的okhttp
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
public class MyOkHttpClient implements mConstant {
    private OkHttpClient client;
    private static MyOkHttpClient instance;
    private Context mContext;

    public OkHttpClient getClient() {
        return client;
    }

    private MyOkHttpClient(Context context) {
        this.mContext = context;
        client = new OkHttpClient();
    }

    public static final MyOkHttpClient getInstance(Context context) {
        if (instance == null) {
            synchronized (MyOkHttpClient.class) {
                if (instance == null) {
                    instance = new MyOkHttpClient(context);
                }
            }
        }
        return instance;
    }

    public <T> void sendNet(RequestBase<T> requestBase, final ResponseBase responseBase) {
        RequestEntity requestEntity = null;
        try {
            requestEntity = requestBase.getmRequestEntity();
            Request request = null;
            if (requestEntity.getRequestMethod() == GET) {
                Log.i("MyOkHttp", "Get请求");
                Bundle urlParams = requestEntity.getUrlParams();
                Bundle requiredParam = requestEntity.getRequiredParam();
                String get_url = urlParams.getString("URL");
                urlParams.remove("URL");
                //post builder 参数
                if (requiredParam != null && requiredParam.size() > 0) {
                    int i = 0;
                    for (String key : requiredParam.keySet()) {
                        if (i++ == 0) {
                            get_url = get_url + "?" + key + "=" + requiredParam.getString(key);
                        } else {
                            get_url = get_url + "&" + key + "=" + requiredParam.getString(key);
                        }
                    }
                }
                if (mContext == null) {
                    request = new Request.Builder()
                            .url(get_url)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(get_url)
                            .tag(mContext)
                            .build();
                }
            } else {
                Log.i("MyOkHttp", "Post请求");
                Bundle urlParams = requestEntity.getUrlParams();
                Bundle requiredParam = requestEntity.getRequiredParam();
                String url = urlParams.getString("URL");
                urlParams.remove("URL");
                //post builder 参数
                FormBody.Builder builder = new FormBody.Builder();
                if (requiredParam != null && requiredParam.size() > 0) {
                    for (String key : requiredParam.keySet()) {
                        builder.add(key, requiredParam.getString(key));
                    }
                }
                if (mContext == null) {
                    request = new Request.Builder()
                            .url(url)
                            .post(builder.build())
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(url)
                            .post(builder.build())
                            .tag(mContext)
                            .build();
                }
            }
            client.newCall(request).enqueue(new MyCallback(new Handler(), responseBase));
        } catch (NetWorkException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14";
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("MyOkHttp", response.toString());
            }
        });

    }

    //callback
    private class MyCallback implements Callback {

        private Handler mHandler;
        private ResponseBase mResponseHandler;

        public MyCallback(Handler handler, ResponseBase responseHandler) {
            mHandler = handler;
            mResponseHandler = responseHandler;
        }


        @Override
        public void onFailure(Call call, final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(0, e.toString());
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (response.isSuccessful()) {
                final String response_body = response.body().string();
                if (mResponseHandler instanceof JsonResponse) {       //json回调
                    try {
                        final JSONObject jsonBody = new JSONObject(response_body);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((JsonResponse) mResponseHandler).onSuccess(response.code(), jsonBody);
                            }
                        });
                    } catch (JSONException ignored) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResponseHandler.onFailure(response.code(), "fail parse jsonobject, body=" + response_body);
                            }
                        });
                    }
                } else if (mResponseHandler instanceof BaseCallback) {    //gson回调
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Gson gson = new Gson();
                                ((BaseCallback) mResponseHandler).onSuccess(response.code(), gson.fromJson(response_body, ((BaseCallback) mResponseHandler).getType()));
                            } catch (Exception ignored) {
                                mResponseHandler.onFailure(response.code(), "fail parse gson, body=" + response_body);
                            }

                        }
                    });
                }
            } else {

            }
        }
    }
}
