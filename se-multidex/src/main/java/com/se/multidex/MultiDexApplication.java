package com.se.multidex;

import android.app.Application;
import android.content.Context;

/**
 * Author: gaojin
 * Time: 2018/8/26 下午6:52
 */

public class MultiDexApplication extends Application {
    public MultiDexApplication() {
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
