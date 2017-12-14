package com.past.music.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by gaojin on 2017/3/28.
 */

public class SysUtils {
    //获取手机状态栏高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static String ASCIIUtils(String string) {
        string = string.replace("&#10;", "\n").replace("&#13;", "\r").replace("&#32;", " ").replace("&#33;", "!").replace("&#34;", "\"");
        string = string.replace("&#39;", ",").replace("&#40;", "(").replace("&#41;", ")").replace("&#42;", "*").replace("&#43;", "+").replace("&#44;", ",");
        string = string.replace("&#45;", "-").replace("&#46;", ".").replace("&#58;", ":");
        return string;
    }
}
