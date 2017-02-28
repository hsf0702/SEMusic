package com.neu.gaojin;

import okhttp3.OkHttpClient;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/2/28 下午6:43
 * 版本：
 * 描述：封装的okhttp
 * 备注：
 * =======================================================
 */
public class MyOkHttpClient {
    private OkHttpClient client;
    private static MyOkHttpClient instance;

    private MyOkHttpClient() {
    }

    /**
     * 静态内部类单例模式，线程安全的
     */
    private static class Holder {
        private static final MyOkHttpClient INSTANCE = new MyOkHttpClient();
    }

    public static final MyOkHttpClient getInstance() {
        return Holder.INSTANCE;
    }


}
