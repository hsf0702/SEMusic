package com.past.music.activity;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.past.music.fragment.QuickControlsFragment;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.IMediaAidlInterface;
import com.past.music.pastmusic.R;
import com.past.music.service.MediaService;
import com.past.music.service.MusicPlayer;
import com.past.music.utils.MConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 上午12:38
 * 描述：Activity基类
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
public class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private MusicPlayer.ServiceToken mToken;
    public static IMediaAidlInterface mService = null;
    private QuickControlsFragment fragment; //底部播放控制栏
    private ArrayList<MusicStateListener> mMusicListener = new ArrayList<>();
    private PlaybackStatus mPlaybackStatus; //BroadCastReceiver 接受播放状态变化等

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        //每次新建一个activity都会连接一次，然后把上下文和Sc保存在一个WeakHashMap中
        mToken = MusicPlayer.bindToService(this, this);
        setStatusBar();
        mPlaybackStatus = new PlaybackStatus(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.PLAYSTATE_CHANGED);
        intentFilter.addAction(MediaService.META_CHANGED);
        intentFilter.addAction(MediaService.MUSIC_CHANGED);
        intentFilter.addAction(MediaService.QUEUE_CHANGED);
        intentFilter.addAction(MConstants.MUSIC_COUNT_CHANGED);
        intentFilter.addAction(MediaService.TRACK_PREPARED);
        intentFilter.addAction(MediaService.BUFFER_UP);
        intentFilter.addAction(MConstants.EMPTY_LIST);
        intentFilter.addAction(MediaService.MUSIC_CHANGED);
        intentFilter.addAction(MediaService.LRC_UPDATED);
        intentFilter.addAction(MConstants.PLAYLIST_COUNT_CHANGED);
        intentFilter.addAction(MediaService.MUSIC_LODING);
        registerReceiver(mPlaybackStatus, intentFilter);
        showQuickControl(true);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }


    /**
     * @param show 显示或关闭底部播放控制栏
     */
    protected void showQuickControl(boolean show) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (show) {
            if (fragment == null) {
                fragment = QuickControlsFragment.newInstance();
                ft.add(R.id.bottom_container, fragment).commitAllowingStateLoss();
            } else {
                ft.show(fragment).commitAllowingStateLoss();
            }
        } else {
            if (fragment != null)
                ft.hide(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * @param p 更新歌曲缓冲进度值，p取值从0~100
     */
    public void updateBuffer(int p) {

    }

    /**
     * @param l 歌曲是否加载中
     */
    public void loading(boolean l) {

    }

    /**
     * 更新播放队列
     */
    public void updateQueue() {

    }

    /**
     * 歌曲切换
     */
    public void updateTrack() {

    }

    public void updateLrc() {

    }


    public void setMusicStateListenerListener(final MusicStateListener status) {
        if (status == this) {
            throw new UnsupportedOperationException("Override the method, don't add a listener");
        }

        if (status != null) {
            mMusicListener.add(status);
        }
    }

    public void removeMusicStateListenerListener(final MusicStateListener status) {
        if (status != null) {
            mMusicListener.remove(status);
        }
    }

    public final static class PlaybackStatus extends BroadcastReceiver {

        private final WeakReference<BaseActivity> mReference;


        public PlaybackStatus(final BaseActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            BaseActivity baseActivity = mReference.get();
            if (baseActivity != null) {
                if (action.equals(MediaService.META_CHANGED)) {
                    baseActivity.baseUpdatePlayInfo();
                } else if (action.equals(MediaService.PLAYSTATE_CHANGED)) {

                } else if (action.equals(MediaService.TRACK_PREPARED)) {
                    baseActivity.updateTime();
                } else if (action.equals(MediaService.BUFFER_UP)) {
                    baseActivity.updateBuffer(intent.getIntExtra("progress", 0));
                } else if (action.equals(MediaService.MUSIC_LODING)) {
                    baseActivity.loading(intent.getBooleanExtra("isloading", false));
                } else if (action.equals(MediaService.REFRESH)) {

                } else if (action.equals(MConstants.MUSIC_COUNT_CHANGED)) {
                    baseActivity.refreshUI();
                } else if (action.equals(MConstants.PLAYLIST_COUNT_CHANGED)) {
                    baseActivity.refreshUI();
                } else if (action.equals(MediaService.QUEUE_CHANGED)) {
                    baseActivity.updateQueue();
                } else if (action.equals(MediaService.TRACK_ERROR)) {
                    Toast.makeText(baseActivity, "错误了嘤嘤嘤", Toast.LENGTH_SHORT).show();
                } else if (action.equals(MediaService.MUSIC_CHANGED)) {
                    MyLog.i("测试", "发送了MUSIC_CHANGED广播");
                    baseActivity.updateTrack();
                } else if (action.equals(MediaService.LRC_UPDATED)) {
                    baseActivity.updateLrc();
                }
            }
        }
    }

    /**
     * 更新歌曲状态信息
     */
    public void baseUpdatePlayInfo() {
        for (MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.reloadAdapter();
                listener.updatePlayInfo();
            }
        }
    }

    /**
     * fragment界面刷新
     */
    public void refreshUI() {
        for (final MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.reloadAdapter();
            }
        }
    }

    public void updateTime() {
        for (final MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.updateTime();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
//        this.overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        this.overridePendingTransition(R.anim.empty, R.anim.empty);
    }


    /**
     * X轴方向滑动打开Activity
     *
     * @param intent
     * @param isInFromRight
     */
    public final void startActivityByX(Intent intent, boolean isInFromRight) {
        this.startActivity(intent);
        if (isInFromRight) {
            this.overridePendingTransition(R.anim.empty, R.anim.empty);
        } else {
            this.overridePendingTransition(R.anim.empty, R.anim.empty);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
        mMusicListener.clear();
        unregisterReceiver(mPlaybackStatus);
    }

    public void unbindService() {
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken);
            mToken = null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IMediaAidlInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }
}
