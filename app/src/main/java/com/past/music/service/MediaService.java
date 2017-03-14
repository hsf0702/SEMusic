package com.past.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;

import com.past.music.MyApplication;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.IMediaAidlInterface;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MediaService extends Service {

    private static final String TAG = "MediaService";
    private static final int IDCOLIDX = 0;
    private static final int TRACK_ENDED = 1;
    private static final int TRACK_WENT_TO_NEXT = 2;
    private static final int RELEASE_WAKELOCK = 3;
    private static final int SERVER_DIED = 4;
    private static final int FOCUSCHANGE = 5;
    private static final int FADEDOWN = 6;
    private static final int FADEUP = 7;


    private Context mContext = null;


    /**
     * 提供访问控制音量和钤声模式的操作
     */
    private AudioManager mAudioManager;
    private MusicPlayerHandler mPlayerHandler;
    private HandlerThread mHandlerThread;

    private final IBinder mBinder = new ServiceStub(this);

    public MediaService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        MyLog.i(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.i(TAG, "onCreate");
        mContext = this;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mHandlerThread = new HandlerThread("MusicPlayerHandler",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mPlayerHandler = new MusicPlayerHandler(this, mHandlerThread.getLooper());
    }

    public void play(boolean createNewNextTrack) {
        int status = mAudioManager.requestAudioFocus(mAudioFocusListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (status != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }

        MyLog.i(TAG, "播放音乐");
    }

    public void pause() {
    }

    public void stop() {
    }

    public void isPlaying() {
    }

    private final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(final int focusChange) {
            mPlayerHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };


    private static final class ServiceStub extends IMediaAidlInterface.Stub {

        private final WeakReference<MediaService> mService;

        private ServiceStub(final MediaService service) {
            mService = new WeakReference<MediaService>(service);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return false;
        }

        @Override
        public void stop() throws RemoteException {

        }

        @Override
        public void pause() throws RemoteException {

        }

        @Override
        public void play() throws RemoteException {
            mService.get().play(true);
        }
    }

    private static final class MusicPlayerHandler extends Handler {

        private final WeakReference<MediaService> mService;
        private float mCurrentVolume = 1.0f;

        private MusicPlayerHandler(final MediaService service, final Looper looper) {
            super(looper);
            mService = new WeakReference<MediaService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            final MediaService service = mService.get();
            if (service == null) {
                return;
            }
            super.handleMessage(msg);
        }

        private static final class MultiPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
            private final WeakReference<MediaService> mService;

            private MediaPlayer mCurrentMediaPlayer = new MediaPlayer();

            private MediaPlayer mNextMediaPlayer;

            private Handler mHandler;

            private boolean mIsInitialized = false;

            private String mNextMediaPath;

            private boolean isFirstLoad = true;


            private int sencondaryPosition = 0;

            private Handler handler = new Handler();

            public MultiPlayer(final MediaService service) {
                mService = new WeakReference<MediaService>(service);
                mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);
            }

            boolean mIsTrackPrepared = false;
            boolean mIsTrackNet = false;
            boolean mIsNextTrackPrepared = false;
            boolean mIsNextInitialized = false;
            boolean mIllegalState = false;

            /**
             * @param player
             * @param path
             * @return
             */
            public boolean setDataSourceImpl(MediaPlayer player, String path) {
                try {
                    player.reset();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    if (path.startsWith("content://")) {
                        player.setOnPreparedListener(null);
                        player.setDataSource(MyApplication.mContext, Uri.parse(path));
                        player.prepare();
                        player.setOnCompletionListener(this);
                    } else {
                        player.setDataSource(path);
//                        player.setOnPreparedListener(preparedListener);
                        player.prepareAsync();
                        mIsTrackNet = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.setOnErrorListener(this);
//                player.setOnBufferingUpdateListener(bufferingUpdateListener);
                return true;
            }

            @Override
            public void onCompletion(MediaPlayer mp) {

            }

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        }
    }
}
