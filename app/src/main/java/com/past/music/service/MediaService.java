package com.past.music.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.past.music.MyApplication;
import com.past.music.activity.MainActivity;
import com.past.music.entity.MusicEntity;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.IMediaAidlInterface;
import com.past.music.pastmusic.R;
import com.past.music.proxy.utils.MediaPlayerProxy;
import com.past.music.utils.SharePreferencesUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MediaService extends Service {

    private static final String TAG = "MediaService";
    public static final String PLAYSTATE_CHANGED = "com.past.music.play_state_changed";
    public static final String META_CHANGED = "com.past.music.meta_changed";
    public static final String MUSIC_CHANGED = "com.past.music.change_music";
    public static final String QUEUE_CHANGED = "com.past.music.queuechanged";
    public static final String TRACK_ERROR = "com.past.music.trackerror";

    public static final String TOGGLEPAUSE_ACTION = "com.past.music.togglepause";
    public static final String NEXT_ACTION = "com.past.music.next";
    public static final String STOP_ACTION = "com.past.music.stop";
    private static final String SHUTDOWN = "com.past.music.shutdown";
    public static final String BUFFER_UP = "com.past.music.bufferup";
    public static final String MUSIC_LODING = "com.past.music.loading";
    private static final String TRACK_NAME = "trackname";
    public static final int NEXT = 2;
    public static final int LAST = 3;
    private static final int IDCOLIDX = 0;
    private static final int TRACK_ENDED = 1;
    private static final int TRACK_WENT_TO_NEXT = 2;
    private static final int RELEASE_WAKELOCK = 3;
    private static final int SERVER_DIED = 4;
    private static final int FOCUSCHANGE = 5;
    private static final int FADEDOWN = 6;
    private static final int FADEUP = 7;
    public static final int SHUFFLE_NONE = 0;
    public static final int SHUFFLE_NORMAL = 1;
    public static final int SHUFFLE_AUTO = 2;
    public static final int REPEAT_NONE = 2;
    public static final int REPEAT_CURRENT = 1;
    public static final int REPEAT_ALL = 2;

    private static final int NOTIFY_MODE_NONE = 0;
    private static final int NOTIFY_MODE_FOREGROUND = 1;
    private static final int NOTIFY_MODE_BACKGROUND = 2;

    private static final int IDLE_DELAY = 5 * 60 * 1000;
    private static final long REWIND_INSTEAD_PREVIOUS_THRESHOLD = 3000;
    private int mCardId;

    private int mRepeatMode = REPEAT_ALL;
    private int mShuffleMode = SHUFFLE_NONE;

    private static final String[] PROJECTION = new String[]{
            "audio._id AS _id", MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
    };

    private static final String[] PROJECTION_MATRIX = new String[]{
            "_id", MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
    };


    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            handleCommandIntent(intent);
        }
    };

    private boolean isPlaying = false;
    private int mNotificationId = 1000;


    private Context mContext = null;
    //传进来的歌单
    private HashMap<Long, MusicEntity> mPlaylistInfo = new HashMap<>();
    //历史歌单
    private static LinkedList<Integer> mHistory = new LinkedList<>();
    //播放列表
    private ArrayList<MusicTrack> mPlaylist = new ArrayList<>(100);


    private MusicPlayerHandler mPlayerHandler;
    private HandlerThread mHandlerThread;
    private MultiPlayer mPlayer;

    private String mFileToPlay;
    private Cursor mCursor;
    private boolean mServiceInUse = false;
    private int mNotifyMode = NOTIFY_MODE_NONE;
    /**
     * 提供访问控制音量和钤声模式的操作
     */
    private AudioManager mAudioManager;
    private NotificationManager mNotificationManager;
    private AlarmManager mAlarmManager;
    private PendingIntent mShutdownIntent;
    private boolean mShutdownScheduled = false;
    /**
     * 当前播放音乐的下标
     */
    private int mPlayPos = -1;
    /**
     * 下一首歌的下标
     */
    private int mNextPlayPos = -1;
    private int mOpenFailedCounter = 0;
    private int mMediaMountedCount = 0;
    private int mServiceStartId = -1;
    private long mLastPlayedTime;
    private Bitmap mNoBit;
    private Notification mNotification;
    private long mNotificationPostTime = 0;
    private long mLastSeekPos = 0;
    private MediaPlayerProxy mProxy;
    private RequestPlayUrl mRequestUrl;
    private static Handler mUrlHandler;
    private final IBinder mBinder = new ServiceStub(this);
    private SharedPreferences mPreferences;
    private boolean mQueueIsSaveable = true;
    private boolean mPausedByTransientLossOfFocus = false;
    private MediaSession mSession;

    public MediaService() {
    }

    private Thread mGetUrlThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            mUrlHandler = new Handler();
            Looper.loop();
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        MyLog.i(TAG, "onBind");
        cancelShutdown();
        mServiceInUse = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mServiceInUse = false;
        if (isPlaying) {
            return true;
        } else if (mPlaylist.size() > 0 || mPlayerHandler.hasMessages(TRACK_ENDED)) {
            scheduleDelayedShutdown();
            return true;
        }
        stopSelf(mServiceStartId);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        cancelShutdown();
        mServiceInUse = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGetUrlThread.start();
        MyLog.i(TAG, "onCreate");
        mContext = this;
        mHandlerThread = new HandlerThread("MusicPlayerHandler", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mPlayerHandler = new MusicPlayerHandler(this, mHandlerThread.getLooper());
        mPlayer = new MultiPlayer(this);
        mPlayer.setHandler(mPlayerHandler);
        mPreferences = getSharedPreferences("Service", 0);

        final IntentFilter filter = new IntentFilter();
//        filter.addAction(SERVICECMD);
        filter.addAction(TOGGLEPAUSE_ACTION);
        filter.addAction(STOP_ACTION);
        filter.addAction(NEXT_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(mIntentReceiver, filter);


        final Intent shutdownIntent = new Intent(this, MediaService.class);
        shutdownIntent.setAction(SHUTDOWN);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mShutdownIntent = PendingIntent.getService(this, 0, shutdownIntent, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void setUpMediaSession() {
        mSession = new MediaSession(this, "pastmusic");
        mSession.setCallback(new MediaSession.Callback() {
            @Override
            public void onPause() {
                pause();
                mPausedByTransientLossOfFocus = false;
            }

            @Override
            public void onPlay() {
                play();
            }

            @Override
            public void onSeekTo(long pos) {
                seek(pos);
            }

            @Override
            public void onSkipToNext() {
                nextPlay(true);
            }

            @Override
            public void onSkipToPrevious() {
                prev(false);
            }

            @Override
            public void onStop() {
                pause();
                mPausedByTransientLossOfFocus = false;
                seek(0);
                releaseServiceUiAndStop();
            }
        });
        mSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }

    public long seek(long position) {
        if (mPlayer.isInitialized()) {
            if (position < 0) {
                position = 0;
            } else if (position > mPlayer.duration()) {
                position = mPlayer.duration();
            }
            long result = mPlayer.seek(position);
//            notifyChange(POSITION_CHANGED);
            return result;
        }
        return -1;
    }

    public long position() {
        if (mPlayer.isInitialized() && mPlayer.isTrackPrepared()) {
            try {
                return mPlayer.position();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int getRepeatMode() {
        return mRepeatMode;
    }

    public int getPreviousPlayPosition(boolean removeFromHistory) {
        synchronized (this) {
            if (mPlayPos > 0) {
                return mPlayPos - 1;
            } else {
                return mPlaylist.size() - 1;
            }
        }
    }

    private void openCurrent() {
        openCurrentAndMaybeNext(false, false);
    }

    public void prev(boolean forcePrevious) {
        synchronized (this) {
            boolean goPrevious = getRepeatMode() != REPEAT_CURRENT &&
                    (position() < REWIND_INSTEAD_PREVIOUS_THRESHOLD || forcePrevious);
            if (goPrevious) {
                int pos = getPreviousPlayPosition(true);
                if (pos < 0) {
                    return;
                }
                mNextPlayPos = mPlayPos;
                mPlayPos = pos;
                stop(false);
                openCurrent();
                play(false);
                notifyChange(META_CHANGED);
                notifyChange(MUSIC_CHANGED);
            } else {
                seek(0);
                play(false);
            }
        }
    }

    public void play() {
        play(true);
    }

    /**
     * 播放歌曲
     *
     * @param createNewNextSong 是否准备下一首歌
     */
    public void play(boolean createNewNextSong) {
        MyLog.i("play", "执行了play");
        if (createNewNextSong) {
            setNextTrack();
        } else {
            setNextTrack(mNextPlayPos);
        }
        if (mPlayer.isTrackPrepared()) {
            final long duration = mPlayer.duration();
            if (mRepeatMode != REPEAT_CURRENT && duration > 2000
                    && mPlayer.position() >= duration - 2000) {
                nextPlay(true);
            }
        }
        mPlayer.start();
//        mPlayerHandler.removeMessages(FADEDOWN);
//        mPlayerHandler.sendEmptyMessage(FADEUP);
//        cancelShutdown();
        setIsPlaying(true, true);
        updateNotification();
        notifyChange(META_CHANGED);
        MyLog.i(TAG, "play");
    }

    /**
     * 设置isPlaying标志位 并更新notification
     *
     * @param value
     * @param notify
     */
    private void setIsPlaying(boolean value, boolean notify) {
        if (isPlaying != value) {
            isPlaying = value;
            if (!isPlaying) {
                mLastPlayedTime = System.currentTimeMillis();
            }
            if (notify) {
                notifyChange(PLAYSTATE_CHANGED);
            }
        }
    }

    /**
     * 歌曲暂停
     */
    public void pause() {
        synchronized (this) {
            mPlayerHandler.removeMessages(FADEUP);
            mPlayer.pause();
            setIsPlaying(false, true);
            notifyChange(META_CHANGED);
        }
    }

    public void stop() {
        stop(true);
    }

    public void stop(final boolean goToIdle) {
        if (mPlayer.isInitialized()) {
            mPlayer.stop();
        }
        mFileToPlay = null;
        closeCursor();
        if (goToIdle) {
            setIsPlaying(false, false);
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(final int focusChange) {
            mPlayerHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };

    /**
     * 为下载的音乐更新游标
     *
     * @param context
     * @param uri
     */
    private void updateCursorForDownloadedFile(Context context, Uri uri) {
        synchronized (this) {
            closeCursor();
            //模拟一个cursor
            MatrixCursor cursor = new MatrixCursor(PROJECTION_MATRIX);
            String title = getValueForDownloadedFile(this, uri, MediaStore.Audio.Media.TITLE);
            cursor.addRow(new Object[]{null, null, null, title, null, null, null, null});
            mCursor = cursor;
            mCursor.moveToFirst();
        }
    }

    /**
     * 根据URI查找本地音乐文件的相应的column的值
     *
     * @param context
     * @param uri
     * @param column
     * @return
     */
    private String getValueForDownloadedFile(Context context, Uri uri, String column) {

        Cursor cursor = null;
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 打开游标并指向第一个
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @return
     */
    private Cursor openCursorAndGoToFirst(Uri uri, String[] projection,
                                          String selection, String[] selectionArgs) {
        Cursor c = getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (c == null) {
            return null;
        }
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        return c;
    }

    private synchronized void closeCursor() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

    /**
     * 根据Uri更新游标
     *
     * @param uri
     */
    private void updateCursor(final Uri uri) {
        synchronized (this) {
            closeCursor();
            mCursor = openCursorAndGoToFirst(uri, PROJECTION, null, null);
        }
    }

    /**
     * 根据ID更新游标
     *
     * @param trackId
     */
    private void updateCursor(final long trackId) {
        MusicEntity info = mPlaylistInfo.get(trackId);
        if (mPlaylistInfo.get(trackId) != null) {
            MatrixCursor cursor = new MatrixCursor(PROJECTION);
            cursor.addRow(new Object[]{info.songId, info.artist, info.albumName, info.musicName
                    , info.data, info.albumData, info.albumId, info.artistId});
            cursor.moveToFirst();
            mCursor = cursor;
            cursor.close();
        }
    }

    private void updateCursor(final String selection, final String[] selectionArgs) {
        synchronized (this) {
            closeCursor();
            mCursor = openCursorAndGoToFirst(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION, selection, selectionArgs);
        }
    }

    public void open(final HashMap<Long, MusicEntity> infos, final long[] list, final int position) {
        synchronized (this) {
            mPlaylistInfo = infos;
//            if (mShuffleMode == SHUFFLE_AUTO) {
//                mShuffleMode = SHUFFLE_NORMAL;
//            }
            final long oldId = getAudioId();
            final int listlength = list.length;
            boolean newlist = true;
            //是否新建一个播放列表
            if (mPlaylist.size() == listlength) {
                newlist = false;
                for (int i = 0; i < listlength; i++) {
                    if (list[i] != mPlaylist.get(i).mId) {
                        newlist = true;
                        break;
                    }
                }
            }
            if (newlist) {
                addToPlayList(list, -1);
//                notifyChange(QUEUE_CHANGED);
            }
            if (position >= 0) {
                mPlayPos = position;
            } else {
//                mPlayPos = mShuffler.nextInt(mPlaylist.size());
            }
            mHistory.clear();
            openCurrentAndNextPlay(true);
            if (oldId != getAudioId()) {
                notifyChange(META_CHANGED);
            }
        }
    }

    /**
     * 音乐加入播放列表
     *
     * @param list
     * @param position
     */
    private void addToPlayList(final long[] list, int position) {
        final int addlen = list.length;
        if (position < 0) {
            mPlaylist.clear();
            position = 0;
        }

        mPlaylist.ensureCapacity(mPlaylist.size() + addlen);
        if (position > mPlaylist.size()) {
            position = mPlaylist.size();
        }

        final ArrayList<MusicTrack> arrayList = new ArrayList<MusicTrack>(addlen);
        for (int i = 0; i < list.length; i++) {
            arrayList.add(new MusicTrack(list[i], i));
        }

        mPlaylist.addAll(position, arrayList);

        if (mPlaylist.size() == 0) {
            closeCursor();
            notifyChange(META_CHANGED);
        }
    }

    private void openCurrentAndNext() {
        openCurrentAndMaybeNext(false, true);
    }


    /**
     * 播放当前歌曲并且准备下一首
     *
     * @param play
     */
    private void openCurrentAndNextPlay(boolean play) {
        openCurrentAndMaybeNext(play, true);
    }

    private void openCurrentAndMaybeNext(final boolean play, final boolean openNext) {
        synchronized (this) {
            closeCursor();
            stop(false);
            boolean shutdown = false;
            if (mPlaylist.size() == 0 || mPlaylistInfo.size() == 0 && mPlayPos >= mPlaylist.size()) {
                clearPlayInfos();
                return;
            }
            final long id = mPlaylist.get(mPlayPos).mId;
            updateCursor(id);
//            getLrc(id);
            if (mPlaylistInfo.get(id) == null) {
                return;
            }
            if (!mPlaylistInfo.get(id).islocal) {
                if (mRequestUrl != null) {
                    mRequestUrl.stop();
                    mUrlHandler.removeCallbacks(mRequestUrl);
                }
                mRequestUrl = new RequestPlayUrl(id, play);
                mUrlHandler.postDelayed(mRequestUrl, 70);
            } else {
                while (true) {
                    if (mCursor != null && openFile(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + mCursor.getLong(IDCOLIDX))) {
                        break;
                    }
                    closeCursor();
                    if (mOpenFailedCounter++ < 10 && mPlaylist.size() > 1) {
                        final int pos = getNextPosition(false);
                        if (pos < 0) {
                            shutdown = true;
                            break;
                        }
                        mPlayPos = pos;
                        stop(false);
                        mPlayPos = pos;
                        updateCursor(mPlaylist.get(mPlayPos).mId);
                    } else {
                        mOpenFailedCounter = 0;
                        MyLog.w(TAG, "Failed to open file for playback");
                        break;
                    }
                }
            }
            if (shutdown) {
                scheduleDelayedShutdown();
                if (isPlaying) {
                    isPlaying = false;
                    notifyChange(PLAYSTATE_CHANGED);
                }
            } else if (openNext) {
                setNextTrack();
            }
        }
    }

    private void clearPlayInfos() {
        File file = new File(getCacheDir().getAbsolutePath() + "playlist");
        if (file.exists()) {
            file.delete();
        }
//        MusicPlaybackState.getInstance(this).clearQueue();
    }

    class RequestPlayUrl implements Runnable {
        private long id;
        private boolean play;
        private boolean stop;

        public RequestPlayUrl(long id, boolean play) {
            this.id = id;
            this.play = play;
        }

        public void stop() {
            stop = true;
        }

        @Override
        public void run() {
            try {
                String url = SharePreferencesUtils.getInstance(MediaService.this).getPlayLink(id);
                if (url == null) {
                    url = "http://ws.stream.qqmusic.qq.com/" + id + ".m4a?fromtag=46";
                    SharePreferencesUtils.getInstance(MediaService.this).setPlayLink(id, url);
                }
                if (url == null) {
                    MyLog.i(TAG + "在线播放", "下一首");
                    nextPlay(true);
                }

                if (!stop) {
                    mPlayer.setDataSource(url);
                }
                if (play && !stop) {
                    play();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startProxy() {
        if (mProxy == null) {
            mProxy = new MediaPlayerProxy(this);
            mProxy.init();
            mProxy.start();
        }
    }

    public int getShuffleMode() {
        return mShuffleMode;
    }

    private void notifyChange(final String what) {
        Intent intent = null;
        if (what.equals(META_CHANGED)) {
            intent = new Intent(META_CHANGED);
            sendStickyBroadcast(intent);
        } else if (what.equals(MUSIC_CHANGED)) {
//            play(true);
        } else if (what.equals(PLAYSTATE_CHANGED)) {
            updateNotification();
        } else if (what.equals(QUEUE_CHANGED)) {
            Intent intent1 = new Intent("com.past.music.emptyplaylist");
            intent.putExtra("showorhide", "show");
            sendBroadcast(intent1);
            saveQueue(true);
            if (isPlaying()) {
                if (mNextPlayPos >= 0 && mNextPlayPos < mPlaylist.size()
                        && getShuffleMode() != SHUFFLE_NONE) {
                    setNextTrack(mNextPlayPos);
                } else {
                    setNextTrack();
                }
                setNextTrack();
            }
        }

    }

    private void saveQueue(final boolean full) {
        if (!mQueueIsSaveable) {
            return;
        }
        final SharedPreferences.Editor editor = mPreferences.edit();
        if (full) {
//            mPlaybackStateStore.saveState(mPlaylist, mShuffleMode != SHUFFLE_NONE ? mHistory : null);
            if (mPlaylistInfo.size() > 0) {
                String temp = MyApplication.gsonInstance().toJson(mPlaylistInfo);
                try {
                    File file = new File(getCacheDir().getAbsolutePath() + "playlist");
                    RandomAccessFile ra = new RandomAccessFile(file, "rws");
                    ra.write(temp.getBytes());
                    ra.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            editor.putInt("cardid", mCardId);

        }
        editor.putInt("curpos", mPlayPos);
        if (mPlayer.isInitialized()) {
            editor.putLong("seekpos", mPlayer.position());
        }
//        editor.putInt("repeatmode", mRepeatMode);
//        editor.putInt("shufflemode", mShuffleMode);
        editor.apply();
    }

    /**
     * 获取下一首音乐的位置
     *
     * @param force
     * @return
     */
    private int getNextPosition(final boolean force) {
        if (mPlaylist == null || mPlaylist.isEmpty()) {
            return -1;
        } else {
            return mPlayPos + 1;
        }
    }

    private void setNextTrack() {
        setNextTrack(getNextPosition(false));
    }

    /**
     * 设置下一首将要播放的音乐的信息
     *
     * @param position
     */
    private void setNextTrack(int position) {
        mNextPlayPos = position;
        if (mNextPlayPos >= 0 && mPlaylist != null && mNextPlayPos < mPlaylist.size()) {
            final long id = mPlaylist.get(mNextPlayPos).mId;
            if (mPlaylistInfo.get(id) != null) {
                if (mPlaylistInfo.get(id).islocal) {
                    mPlayer.setNextDataSource(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + id);
                } else {
                    mPlayer.setNextDataSource(null);
                }
            }
        } else {
            mPlayer.setNextDataSource(null);
        }
    }


    public boolean openFile(final String path) {
        MyLog.i(TAG, "openFile: path = " + path);
        synchronized (this) {
            if (path == null) {
                return false;
            }
            if (mCursor == null) {
                Uri uri = Uri.parse(path);
                boolean shouldAddToPlaylist = true;
                long id = -1;
                try {
                    id = Long.valueOf(uri.getLastPathSegment());
                } catch (NumberFormatException ex) {
                }

                if (id != -1 && path.startsWith(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
                    updateCursor(uri);
                } else if (id != -1 && path.startsWith(
                        MediaStore.Files.getContentUri("external").toString())) {
                    updateCursor(id);
                } else if (path.startsWith("content://downloads/")) {
                    String mpUri = getValueForDownloadedFile(this, uri, "mediaprovider_uri");
                    if (!TextUtils.isEmpty(mpUri)) {
                        if (openFile(mpUri)) {
                            notifyChange(META_CHANGED);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        updateCursorForDownloadedFile(this, uri);
                        shouldAddToPlaylist = false;
                    }
                } else {
                    String where = MediaStore.Audio.Media.DATA + "=?";
                    String[] selectionArgs = new String[]{path};
                    updateCursor(where, selectionArgs);
                }
                try {
                    if (mCursor != null && shouldAddToPlaylist) {
                        mPlaylist.clear();
                        mPlaylist.add(new MusicTrack(mCursor.getLong(IDCOLIDX), -1));
                        notifyChange(QUEUE_CHANGED);
                        mPlayPos = 0;
                        mHistory.clear();
                    }
                } catch (final UnsupportedOperationException ex) {
                }
            }
            mFileToPlay = path;
            mPlayer.setDataSource(mFileToPlay);
            if (mPlayer.isInitialized()) {
                mOpenFailedCounter = 0;
                return true;
            }
            String trackName = getTrackName();
            if (TextUtils.isEmpty(trackName)) {
                trackName = path;
            }
            sendErrorMessage(trackName);
            stop(true);
            return false;
        }
    }

    private void sendErrorMessage(final String trackName) {
        final Intent intent = new Intent(TRACK_ERROR);
        intent.putExtra(TRACK_NAME, trackName);
        sendBroadcast(intent);
    }

    public boolean isTrackLocal() {
        synchronized (this) {
            MusicEntity info = mPlaylistInfo.get(getAudioId());
            if (info == null) {
                return true;
            }
            return info.islocal;
        }
    }

    public String getTrackName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE));
        }
    }

    public String getAlbumName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM));
        }
    }

    public String getArtistName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST));
        }
    }

    public HashMap<Long, MusicEntity> getPlaylistInfo() {
        synchronized (this) {
            return mPlaylistInfo;
        }
    }

    public long getAlbumId() {
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID));
        }
    }

    public long getArtistId() {
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID));
        }
    }

    public long getAudioId() {
        MusicTrack track = getCurrentTrack();
        if (track != null) {
            return track.mId;
        }

        return -1;
    }

    public MusicTrack getCurrentTrack() {
        return getTrack(mPlayPos);
    }

    public synchronized MusicTrack getTrack(int index) {
        if (index >= 0 && index < mPlaylist.size()) {
            return mPlaylist.get(index);
        }
        return null;
    }


    public String getAlbumPath() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.MIME_TYPE));
        }
    }

    /**
     * 获得播放音乐的队列
     *
     * @return
     */
    public long[] getQueue() {
        synchronized (this) {
            final int len = mPlaylist.size();
            final long[] list = new long[len];
            for (int i = 0; i < len; i++) {
                list[i] = mPlaylist.get(i).mId;
            }
            return list;
        }
    }

    /**
     * 获取播放队列的位置
     *
     * @return
     */
    public int getQueuePosition() {
        synchronized (this) {
            return mPlayPos;
        }
    }

    /**
     * 设置当前播放队列播放的歌曲
     *
     * @param index
     */
    public void setQueuePosition(int index) {
        synchronized (this) {
            mPlayPos = index;
            openCurrentAndNext();
            play(true);
            notifyChange(META_CHANGED);
        }
    }

    /**
     * 设置当前播放的位置mPlayPos
     *
     * @param nextPos
     */
    public void setAndRecordPlayPos(int nextPos) {
        synchronized (this) {
            mPlayPos = nextPos;
        }
    }

    public void nextPlay(final boolean force) {
        synchronized (this) {
            int pos = mNextPlayPos;
            if (pos < 0) {
                pos = getNextPosition(force);
            }
            if (pos < 0) {
                setIsPlaying(false, false);
                return;
            }
            stop(false);
            setAndRecordPlayPos(pos);
            openCurrentAndNextPlay(true);
            play();
            notifyChange(META_CHANGED);
        }
    }

    private void cancelShutdown() {
        if (mShutdownScheduled) {
            mAlarmManager.cancel(mShutdownIntent);
            mShutdownScheduled = false;
        }
    }

    private boolean recentlyPlayed() {
        return isPlaying() || System.currentTimeMillis() - mLastPlayedTime < IDLE_DELAY;
    }

    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
        if (TOGGLEPAUSE_ACTION.equals(action)) {
            if (isPlaying()) {
                pause();
            } else {
                play();
            }
        } else if (NEXT_ACTION.equals(action)) {
            nextPlay(true);
        } else if (STOP_ACTION.equals(action)) {
            pause();
            releaseServiceUiAndStop();
        }
    }

    private void releaseServiceUiAndStop() {
        if (isPlaying() || mPlayerHandler.hasMessages(TRACK_ENDED)) {
            return;
        }
        cancelNotification();
        mAudioManager.abandonAudioFocus(mAudioFocusListener);
        if (!mServiceInUse) {
            stopSelf(mServiceStartId);
        }
    }

    private void scheduleDelayedShutdown() {
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + IDLE_DELAY, mShutdownIntent);
        mShutdownScheduled = true;
    }

    private void updateNotification() {
        final int notifyMode;
        if (isPlaying()) {
            notifyMode = NOTIFY_MODE_FOREGROUND;
        } else {
            notifyMode = NOTIFY_MODE_NONE;
        }
//
//        if (mNotifyMode != notifyMode) {
//            if (mNotifyMode == NOTIFY_MODE_FOREGROUND) {
//                stopForeground(notifyMode == NOTIFY_MODE_NONE || notifyMode == NOTIFY_MODE_BACKGROUND);
//            } else if (notifyMode == NOTIFY_MODE_NONE) {
//                mNotificationManager.cancel(mNotificationId);
//                mNotificationPostTime = 0;
//            }
//        }
//        if (notifyMode == NOTIFY_MODE_FOREGROUND) {
//            MyLog.i(TAG, "NOTIFY_MODE_FOREGROUND");
////            startForeground(mNotificationId, getNotification());
//            mNotificationManager.notify(mNotificationId, getNotification());
//        } else if (notifyMode == NOTIFY_MODE_BACKGROUND) {
//            MyLog.i(TAG, "NOTIFY_MODE_BACKGROUND");
//            mNotificationManager.notify(mNotificationId, getNotification());
//        }

        if (notifyMode == mNotifyMode) {
            mNotificationManager.notify(mNotificationId, getNotification());
        } else {
            if (notifyMode == NOTIFY_MODE_FOREGROUND) {
                startForeground(mNotificationId, getNotification());
            } else {
                mNotificationManager.notify(mNotificationId, getNotification());
            }
        }
        mNotifyMode = notifyMode;
    }

    private void sendUpdateBuffer(int progress) {
        Intent intent = new Intent(BUFFER_UP);
        intent.putExtra("progress", progress);
        sendBroadcast(intent);
    }

    public void loading(boolean l) {
        Intent intent = new Intent(MUSIC_LODING);
        intent.putExtra("isloading", l);
        sendBroadcast(intent);
    }

    public void enqueue(final long[] list, final HashMap<Long, MusicEntity> map, final int action) {
        synchronized (this) {
            mPlaylistInfo.putAll(map);
            if (action == NEXT && mPlayPos + 1 < mPlaylist.size()) {
                addToPlayList(list, mPlayPos + 1);
                mNextPlayPos = mPlayPos + 1;
                notifyChange(QUEUE_CHANGED);
            } else {
                addToPlayList(list, Integer.MAX_VALUE);
                notifyChange(QUEUE_CHANGED);
            }
            if (mPlayPos < 0) {
                mPlayPos = 0;
                openCurrentAndNext();
                play();
                notifyChange(META_CHANGED);
            }
        }
    }

    private void cancelNotification() {
        stopForeground(true);
        mNotificationManager.cancel(hashCode());
        mNotificationManager.cancel(mNotificationId);
        mNotificationPostTime = 0;
        mNotifyMode = NOTIFY_MODE_NONE;
    }


    private Notification getNotification() {
        final RemoteViews remoteViews;
        final int PAUSE_FLAG = 0x1;
        final int NEXT_FLAG = 0x2;
        final int STOP_FLAG = 0x3;
        final String albumName = getAlbumName();
        final String artistName = getArtistName();
        final boolean isPlaying = isPlaying();

        remoteViews = new RemoteViews(this.getPackageName(), R.layout.remote_view);
        String text = TextUtils.isEmpty(albumName) ? artistName : artistName + " - " + albumName;
        remoteViews.setTextViewText(R.id.title, getTrackName());
        remoteViews.setTextViewText(R.id.text, text);

        //此处action不能是一样的 如果一样的 接受的flag参数只是第一个设置的值
        Intent pauseIntent = new Intent(TOGGLEPAUSE_ACTION);
        pauseIntent.putExtra("FLAG", PAUSE_FLAG);
        PendingIntent pausePIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        remoteViews.setImageViewResource(R.id.img_play, isPlaying ? R.drawable.icon_remote_pause : R.drawable.icon_remote_play);
        remoteViews.setOnClickPendingIntent(R.id.img_play, pausePIntent);

        Intent nextIntent = new Intent(NEXT_ACTION);
        nextIntent.putExtra("FLAG", NEXT_FLAG);
        PendingIntent nextPIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.img_next_play, nextPIntent);

        Intent preIntent = new Intent(STOP_ACTION);
        preIntent.putExtra("FLAG", STOP_FLAG);
        PendingIntent prePIntent = PendingIntent.getBroadcast(this, 0, preIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.img_cancel, prePIntent);

        final Intent mMainIntent = new Intent(this, MainActivity.class);
        PendingIntent mainIntent = PendingIntent.getActivity(this, 0, mMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (MyApplication.dbService.query(getArtistName().replace(";", "")) != null) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            Uri uri = Uri.parse(MyApplication.dbService.query(getArtistName().replace(";", "")));
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, MediaService.this);
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    // You can use the bitmap in only limited ways
                    // No need to do any cleanup.
                    if (bitmap != null) {
                        remoteViews.setImageViewBitmap(R.id.remote_img, bitmap);
//                        updateNotification();
                    }
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

                }
            }, CallerThreadExecutor.getInstance());
        }
        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }
        if (mNotification == null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContent(remoteViews)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(mainIntent)
                    .setWhen(mNotificationPostTime);
            mNotification = builder.build();
        } else {
            mNotification.contentView = remoteViews;
        }
        return mNotification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelNotification();
        mPlayerHandler.removeCallbacksAndMessages(null);
        mHandlerThread.quit();
        mPlayer.release();
        mPlayer = null;
        closeCursor();
        unregisterReceiver(mIntentReceiver);

    }

    private static final class ServiceStub extends IMediaAidlInterface.Stub {

        private final WeakReference<MediaService> mService;

        private ServiceStub(final MediaService service) {
            mService = new WeakReference<MediaService>(service);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mService.get().isPlaying();
        }

        @Override
        public void stop() throws RemoteException {
            mService.get().stop();
        }

        @Override
        public void pause() throws RemoteException {
            mService.get().pause();
        }

        @Override
        public void play() throws RemoteException {
            mService.get().play(true);
        }

        @Override
        public void nextPlay() throws RemoteException {
            mService.get().nextPlay(true);
        }

        @Override
        public void openFile(String path) throws RemoteException {
            mService.get().openFile(path);
        }

        @Override
        public void open(Map infos, long[] list, int position) throws RemoteException {
            mService.get().open((HashMap<Long, MusicEntity>) infos, list, position);
        }

        @Override
        public String getArtistName() throws RemoteException {
            return mService.get().getArtistName();
        }

        @Override
        public String getTrackName() throws RemoteException {
            return mService.get().getTrackName();
        }

        @Override
        public String getAlbumName() throws RemoteException {
            return null;
        }

        @Override
        public String getAlbumPath() throws RemoteException {
            return null;
        }

        @Override
        public String[] getAlbumPathtAll() throws RemoteException {
            return new String[0];
        }

        @Override
        public String getPath() throws RemoteException {
            return null;
        }

        @Override
        public Map getPlaylistInfo() throws RemoteException {
            return mService.get().getPlaylistInfo();
        }

        @Override
        public long[] getQueue() throws RemoteException {
            return mService.get().getQueue();
        }

        @Override
        public void setQueuePosition(int index) throws RemoteException {
            mService.get().setQueuePosition(index);
        }

        @Override
        public long getAudioId() throws RemoteException {
            return mService.get().getAudioId();
        }

        @Override
        public int getQueuePosition() throws RemoteException {
            return mService.get().getQueuePosition();
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            try {
                super.onTransact(code, data, reply, flags);
            } catch (final RuntimeException e) {
                e.printStackTrace();
                File file = new File(mService.get().getCacheDir().getAbsolutePath() + "/err/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                try {
                    PrintWriter writer = new PrintWriter(mService.get().getCacheDir().getAbsolutePath() + "/err/" + System.currentTimeMillis() + "_aidl.log");
                    e.printStackTrace(writer);
                    writer.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                throw e;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private static final class MultiPlayer implements MediaPlayer.OnErrorListener,
            MediaPlayer.OnCompletionListener {

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

        public void setDataSource(final String path) {

            mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
            if (mIsInitialized) {
                setNextDataSource(null);
            }
        }

        public void setNextDataSource(final String path) {
            mNextMediaPath = null;
            mIsNextInitialized = false;
            try {
                mCurrentMediaPlayer.setNextMediaPlayer(null);
            } catch (IllegalArgumentException e) {
                Log.i(TAG, "Next media player is current one, continuing");
            } catch (IllegalStateException e) {
                Log.e(TAG, "Media player not initialized!");
                return;
            }
            if (mNextMediaPlayer != null) {
                mNextMediaPlayer.release();
                mNextMediaPlayer = null;
            }
            if (path == null) {
                return;
            }
            mNextMediaPlayer = new MediaPlayer();
            mNextMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);
            mNextMediaPlayer.setAudioSessionId(getAudioSessionId());

            if (setNextDataSourceImpl(mNextMediaPlayer, path)) {
                mNextMediaPath = path;
                mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);

            } else {
                if (mNextMediaPlayer != null) {
                    mNextMediaPlayer.release();
                    mNextMediaPlayer = null;
                }
            }
        }

        boolean mIsTrackPrepared = false;
        boolean mIsTrackNet = false;
        boolean mIsNextTrackPrepared = false;
        boolean mIsNextInitialized = false;
        boolean mIllegalState = false;

        private boolean setDataSourceImpl(final MediaPlayer player, final String path) {
            mIsTrackNet = false;
            mIsTrackPrepared = false;
            try {
                player.reset();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                if (path.startsWith("content://")) {
                    player.setOnPreparedListener(null);
                    player.setDataSource(MyApplication.mContext, Uri.parse(path));
                    player.prepare();
                    mIsTrackPrepared = true;
                    player.setOnCompletionListener(this);

                } else {
                    player.setDataSource(path);
                    player.setOnPreparedListener(preparedListener);
                    player.prepareAsync();
                    mIsTrackNet = true;
                }
                if (mIllegalState) {
                    mIllegalState = false;
                }

            } catch (final IOException todo) {

                return false;
            } catch (final IllegalArgumentException todo) {

                return false;
            } catch (final IllegalStateException todo) {
                todo.printStackTrace();
                if (!mIllegalState) {
                    mCurrentMediaPlayer = null;
                    mCurrentMediaPlayer = new MediaPlayer();
                    mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);
                    mCurrentMediaPlayer.setAudioSessionId(getAudioSessionId());
                    setDataSourceImpl(mCurrentMediaPlayer, path);
                    mIllegalState = true;
                } else {
                    mIllegalState = false;
                    return false;
                }
            }

            player.setOnErrorListener(this);
            player.setOnBufferingUpdateListener(bufferingUpdateListener);
            return true;
        }

        private boolean setNextDataSourceImpl(final MediaPlayer player, final String path) {

            mIsNextTrackPrepared = false;
            try {
                player.reset();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                if (path.startsWith("content://")) {
                    player.setOnPreparedListener(preparedNextListener);
                    player.setDataSource(MyApplication.mContext, Uri.parse(path));
                    player.prepare();
                } else {
                    player.setDataSource(path);
                    player.setOnPreparedListener(preparedNextListener);
                    player.prepare();
                    mIsNextTrackPrepared = false;
                }
            } catch (final IOException todo) {
                return false;
            } catch (final IllegalArgumentException todo) {
                return false;
            }
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            return true;
        }

        public void setHandler(final Handler handler) {
            mHandler = handler;
        }


        public boolean isInitialized() {
            return mIsInitialized;
        }

        public boolean isTrackPrepared() {
            return mIsTrackPrepared;
        }


        public void start() {
            if (!mIsTrackNet) {
                mService.get().sendUpdateBuffer(100);
                sencondaryPosition = 100;
                mCurrentMediaPlayer.start();
            } else {
                sencondaryPosition = 0;
                mService.get().loading(true);
                handler.postDelayed(startMediaPlayerIfPrepared, 50);
            }
            mService.get().notifyChange(MUSIC_CHANGED);
        }

        MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (isFirstLoad) {
                    long seekpos = mService.get().mLastSeekPos;
                    seek(seekpos >= 0 ? seekpos : 0);
                    isFirstLoad = false;
                }
                mService.get().notifyChange(META_CHANGED);
                mp.setOnCompletionListener(MultiPlayer.this);
                mIsTrackPrepared = true;
            }
        };

        MediaPlayer.OnPreparedListener preparedNextListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsNextTrackPrepared = true;
            }
        };

        MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (sencondaryPosition != 100)
                    mService.get().sendUpdateBuffer(percent);
                sencondaryPosition = percent;
            }
        };

        Runnable setNextMediaPlayerIfPrepared = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (mIsNextTrackPrepared && mIsInitialized) {

//                    mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);
                } else if (count < 60) {
                    handler.postDelayed(setNextMediaPlayerIfPrepared, 100);
                }
                count++;
            }
        };

        Runnable startMediaPlayerIfPrepared = new Runnable() {

            @Override
            public void run() {
                if (mIsTrackPrepared) {
                    mCurrentMediaPlayer.start();
                    final long duration = duration();
                    if (mService.get().mRepeatMode != REPEAT_CURRENT && duration > 2000
                            && position() >= duration - 2000) {
                        mService.get().nextPlay(true);
                        Log.e("play to go", "");
                    }
                    mService.get().loading(false);
                } else {
                    handler.postDelayed(startMediaPlayerIfPrepared, 700);
                }
            }
        };

        public void stop() {
            handler.removeCallbacks(setNextMediaPlayerIfPrepared);
            handler.removeCallbacks(startMediaPlayerIfPrepared);
            mCurrentMediaPlayer.reset();
            mIsInitialized = false;
            mIsTrackPrepared = false;
        }

        public void release() {
            mCurrentMediaPlayer.release();
        }

        public void pause() {
            handler.removeCallbacks(startMediaPlayerIfPrepared);
            mCurrentMediaPlayer.pause();
        }

        public long duration() {
            if (mIsTrackPrepared) {
                return mCurrentMediaPlayer.getDuration();
            }
            return -1;
        }

        public long position() {
            if (mIsTrackPrepared) {
                try {
                    return mCurrentMediaPlayer.getCurrentPosition();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }

        public long secondPosition() {
            if (mIsTrackPrepared) {
                return sencondaryPosition;
            }
            return -1;
        }

        public long seek(final long whereto) {
            mCurrentMediaPlayer.seekTo((int) whereto);
            return whereto;
        }

        public void setVolume(final float vol) {
            try {
                mCurrentMediaPlayer.setVolume(vol, vol);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public int getAudioSessionId() {
            return mCurrentMediaPlayer.getAudioSessionId();
        }

        public void setAudioSessionId(final int sessionId) {
            mCurrentMediaPlayer.setAudioSessionId(sessionId);
        }

        @Override
        public boolean onError(final MediaPlayer mp, final int what, final int extra) {
            Log.w(TAG, "Music Server Error what: " + what + " extra: " + extra);
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    final MediaService service = mService.get();
                    final TrackErrorInfo errorInfo = new TrackErrorInfo(service.getAudioId(),
                            service.getTrackName());
                    mIsInitialized = false;
                    mIsTrackPrepared = false;
                    mCurrentMediaPlayer.release();
                    mCurrentMediaPlayer = new MediaPlayer();
                    mCurrentMediaPlayer.setWakeMode(service, PowerManager.PARTIAL_WAKE_LOCK);
                    Message msg = mHandler.obtainMessage(SERVER_DIED, errorInfo);
                    mHandler.sendMessageDelayed(msg, 2000);
                    return true;
                default:
                    break;
            }
            return false;
        }


        @Override
        public void onCompletion(final MediaPlayer mp) {
            Log.w(TAG, "completion");
            if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
                mCurrentMediaPlayer.release();
                mCurrentMediaPlayer = mNextMediaPlayer;
                mNextMediaPath = null;
                mNextMediaPlayer = null;
                mHandler.sendEmptyMessage(TRACK_WENT_TO_NEXT);
            } else {
//                mService.get().mWakeLock.acquire(30000);
                mHandler.sendEmptyMessage(TRACK_ENDED);
                mHandler.sendEmptyMessage(RELEASE_WAKELOCK);
            }
        }

    }

    private static final class TrackErrorInfo {
        public long mId;
        public String mTrackName;

        public TrackErrorInfo(long id, String trackName) {
            mId = id;
            mTrackName = trackName;
        }
    }

    private static final class MusicPlayerHandler extends Handler {

        private final WeakReference<MediaService> mService;

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
            synchronized (service) {
                switch (msg.what) {
                    case TRACK_WENT_TO_NEXT:
                        service.setAndRecordPlayPos(service.mNextPlayPos);
                        service.setNextTrack();
                        if (service.mCursor != null) {
                            service.mCursor.close();
                            service.mCursor = null;
                        }
                        service.updateCursor(service.mPlaylist.get(service.mPlayPos).mId);
                        service.notifyChange(META_CHANGED);
                        service.notifyChange(MUSIC_CHANGED);
                        service.updateNotification();
                        break;
                }
            }
        }
    }
}
