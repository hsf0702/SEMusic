package com.past.music.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.past.music.pastmusic.IMediaAidlInterface;

import java.util.WeakHashMap;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/3 下午4:18
 * 版本：
 * 描述：界面和Service的桥   MusicPlayer维护着一个WeakHashMap  保存Context和ServiceConnection的键值对
 * 备注：
 * =======================================================
 */
public class MusicPlayer {

    public static IMediaAidlInterface mService = null;

    /**
     * 实现规范化Context到ServiceConnection的映射
     * 一般用weak reference引用的对象是有价值被cache, 而且很容易被重新被构建, 且很消耗内存的对象.
     * GC执行的时候就会回收weak reference
     */
    private static final WeakHashMap<Context, ServiceConnect> mConnectionMap;

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceConnect>();
    }


    public static final ServiceToken bindToService(final Context context,
                                                   final ServiceConnection callback) {

        //document:Return the parent activity if this view is an embedded child.
        //如果是内嵌的Activity则获得他所在activity的上下文
        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        final ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MediaService.class));
        final ServiceConnect sc = new ServiceConnect(callback, contextWrapper.getApplicationContext());
        if (contextWrapper.bindService(new Intent().setClass(contextWrapper, MediaService.class), sc, Context.BIND_AUTO_CREATE)) {
            mConnectionMap.put(contextWrapper, sc);
            return new ServiceToken(contextWrapper);
        }
        return null;
    }

    public static void unbindFromService(final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceConnect sc = mConnectionMap.remove(mContextWrapper);
        if (sc == null) {
            return;
        }
        mContextWrapper.unbindService(sc);
        if (mConnectionMap.isEmpty()) {
            mService = null;
        }
    }

    /**
     * 播放或者暂停音乐
     */
    public static void playOrPause() {
        try {
//            if (mService != null) {
//                if (mService.isPlaying()) {
//                    mService.pause();
//                } else {
//                    mService.play();
//                }
//            }
            mService.play();
        } catch (final Exception ignored) {
        }
    }


    public static final class ServiceConnect implements ServiceConnection {

        private final ServiceConnection mCallback;
        private final Context mContext;

        public ServiceConnect(ServiceConnection mCallback, Context mContext) {
            this.mCallback = mCallback;
            this.mContext = mContext;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMediaAidlInterface.Stub.asInterface(service);
            if (mCallback != null) {
                mCallback.onServiceConnected(name, service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(name);
            }
            mService = null;
        }
    }

    public static final class ServiceToken {
        //Service和Activity的父类
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }


}
