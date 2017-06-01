package com.past.music.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.past.music.entity.MusicEntity;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.IMediaAidlInterface;

import java.util.Arrays;
import java.util.HashMap;
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

    public static final String TAG = "MusicPlayer";

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
            if (mService != null) {
                if (mService.isPlaying()) {
                    mService.pause();
                } else {
                    mService.play();
                }
            }
        } catch (final Exception ignored) {
        }
    }

    public static boolean getIsPlaying() {
        try {
            if (mService != null) {
                return mService.isPlaying();
            } else {
                return false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static final int getQueueSize() {
        try {
            if (mService != null) {
                return mService.getQueueSize();
            } else {
            }
        } catch (final RemoteException ignored) {
        }
        return 0;
    }

    public static final int getQueuePosition() {
        try {
            if (mService != null) {
                return mService.getQueuePosition();
            }
        } catch (final RemoteException ignored) {
        }
        return 0;
    }

    /**
     * 播放所有音乐
     *
     * @param infos        ID到音乐实体的映射
     * @param list         音乐ID的集合
     * @param position     当前播放的音乐的位置
     * @param forceShuffle
     */
    public static synchronized void playAll(final HashMap<Long, MusicEntity> infos, final long[] list, int position, final boolean forceShuffle) {
        if (list == null || list.length == 0 || mService == null) {
            return;
        }
        try {
            final long currentId = mService.getAudioId();
            final int currentQueuePosition = getQueuePosition();
            if (position != -1) {
                final long[] playlist = getQueue();
                if (Arrays.equals(list, playlist)) {
                    if (currentQueuePosition == position && currentId == list[position]) {
                        mService.play();
                        return;
                    } else {
                        mService.setQueuePosition(position);
                        return;
                    }

                }
            }
            if (position < 0) {
                position = 0;
            }
            mService.open(infos, list, position);
            mService.play();
        } catch (final RemoteException ignored) {
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放下一首音乐
     */
    public static void nextPlay() {
        try {
            if (mService != null) {
                MyLog.i(TAG, "nextPlay");
                mService.nextPlay();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void previous(final Context context, final boolean force) {
        final Intent previous = new Intent(context, MediaService.class);
        if (force) {
            previous.setAction(MediaService.PREVIOUS_FORCE_ACTION);
        } else {
            previous.setAction(MediaService.PREVIOUS_ACTION);
        }
        context.startService(previous);
    }

    public static void playNext(Context context, final HashMap<Long, MusicEntity> map, final long[] list) {
//        if (mService == null) {
//            return;
//        }
//        try {
//            int current = -1;
//            long[] result = list;
//
//            for (int i = 0; i < list.length; i++) {
//                if (MusicPlayer.getCurrentAudioId() == list[i]) {
//                    current = i;
//                } else {
//                    MusicPlayer.removeTrack(list[i]);
//                }
//            }
//            mService.enqueue(list, map, MediaService.NEXT);
//
//            Toast.makeText(context, "已加入下一首播放", Toast.LENGTH_SHORT).show();
//        } catch (final RemoteException ignored) {
//        }
    }


    /**
     * 获取当前播放的音乐的名字
     *
     * @return
     */
    public static String getTrackName() {
        try {
            if (mService != null) {
                return mService.getTrackName();
            }
        } catch (final RemoteException ignored) {
        }
        return null;
    }

    /**
     * 获取当前音乐歌手的名字
     *
     * @return
     */
    public static String getArtistName() {
        try {
            if (mService != null) {
                return mService.getArtistName();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAlbumPic() {
        try {
            if (mService != null) {
                return mService.getAlbumPic();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回播放列表ID到音乐实体的映射
     *
     * @return
     */
    public static HashMap<Long, MusicEntity> getPlayinfos() {
        try {
            if (mService != null) {
                return (HashMap<Long, MusicEntity>) mService.getPlaylistInfo();
            }
        } catch (final RemoteException ignored) {
        }
        return null;
    }

    /**
     * 返回播放列表的ID
     *
     * @return
     */
    public static long[] getQueue() {
        try {
            if (mService != null) {
                return mService.getQueue();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setQueue(int index) {
        try {
            if (mService != null) {
                mService.setQueuePosition(index);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static long getCurrentAudioId() {
        try {
            if (mService != null) {
                return mService.getAudioId();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
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
                //调用baseActivity的回调方法
                mCallback.onServiceConnected(name, service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mCallback != null) {
                //调用baseActivity的回调方法
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
