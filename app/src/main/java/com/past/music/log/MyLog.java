package com.past.music.log;

import android.util.Log;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/3 下午6:45
 * 版本：
 * 描述：自定义Log工具类，方便统一关闭Log
 * 备注：
 * =======================================================
 */
public class MyLog {
    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;
    public static final int WTF = 5;

    //屏蔽log的时候，可以将NONE 的值赋给LEVEL
    public static final int NONE = 6;

    public static final int LEVEL = VERBOSE;

    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        if (LEVEL <= WTF) {
            Log.wtf(tag, msg);
        }
    }
}
