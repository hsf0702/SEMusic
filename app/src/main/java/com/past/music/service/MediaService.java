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
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.past.music.utils.ImageUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MediaService extends Service {

    private static final String TAG = "MediaService";
    public static final String PLAYSTATE_CHANGED = "com.past.music.play_state_changed";
    public static final String META_CHANGED = "com.past.music.meta_changed";
    public static final String MUSIC_CHANGED = "com.past.music.change_music";

    public static final String TOGGLEPAUSE_ACTION = "com.past.music.togglepause";
    public static final String NEXT_ACTION = "com.past.music.next";
    public static final String STOP_ACTION = "com.past.music.stop";
    private static final String SHUTDOWN = "com.past.music.shutdown";
    private static final int IDCOLIDX = 0;
    private static final int TRACK_ENDED = 1;
    private static final int TRACK_WENT_TO_NEXT = 2;
    private static final int RELEASE_WAKELOCK = 3;
    private static final int SERVER_DIED = 4;
    private static final int FOCUSCHANGE = 5;
    private static final int FADEDOWN = 6;
    private static final int FADEUP = 7;

    private static final int NOTIFY_MODE_NONE = 0;
    private static final int NOTIFY_MODE_FOREGROUND = 1;
    private static final int NOTIFY_MODE_BACKGROUND = 2;

    private static final int IDLE_DELAY = 5 * 60 * 1000;

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

    /**
     * 播放列表
     */
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

    /**
     * 上次播放的时间
     */
    private long mLastPlayedTime;
    private Bitmap mNoBit;
    private Notification mNotification;
    private long mNotificationPostTime = 0;

    private final IBinder mBinder = new ServiceStub(this);

    public MediaService() {
    }

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
    public void onCreate() {
        super.onCreate();
        MyLog.i(TAG, "onCreate");
        SystemClock.sleep(6000);
        mContext = this;
        mHandlerThread = new HandlerThread("MusicPlayerHandler", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mPlayerHandler = new MusicPlayerHandler(this, mHandlerThread.getLooper());
        mPlayer = new MultiPlayer(this);
        mPlayer.setHandler(mPlayerHandler);

        // Initialize the intent filter and each action
        final IntentFilter filter = new IntentFilter();
//        filter.addAction(SERVICECMD);
        filter.addAction(TOGGLEPAUSE_ACTION);
        filter.addAction(STOP_ACTION);
        filter.addAction(NEXT_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // Attach the broadcast listener
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

    public void play() {
        play(true);
    }

    /**
     * 播放歌曲
     *
     * @param createNewNextSong 是否准备下一首歌
     */
    public void play(boolean createNewNextSong) {
        int status = mAudioManager.requestAudioFocus(mAudioFocusListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (status != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }
//        final Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
//        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
//        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
//        sendBroadcast(intent);
//
//        mAudioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(),
//                MediaButtonIntentReceiver.class.getName()));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            mSession.setActive(true);
        if (createNewNextSong) {
            setNextTrack();
        } else {
            setNextTrack(mNextPlayPos);
        }
//        if (mPlayer.isTrackPrepared()) {
//            final long duration = mPlayer.duration();
//            if (mRepeatMode != REPEAT_CURRENT && duration > 2000
//                    && mPlayer.position() >= duration - 2000) {
//                gotoNext(true);
//            }
//        }
        mPlayer.start();
//        mPlayerHandler.removeMessages(FADEDOWN);
//        mPlayerHandler.sendEmptyMessage(FADEUP);
//        cancelShutdown();
        updateNotification();
        setIsPlaying(true, true);
        notifyChange(META_CHANGED);
        MyLog.i(TAG, "play");
    }

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
        if (mPlayer.isInitialized()) {
            mPlayer.stop();
        }
        mFileToPlay = null;
        closeCursor();
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
     * 根据URI查找本地音乐文件的相应的column名称
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
//            final long oldId = getAudioId();
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
//            mHistory.clear();
            openCurrentAndNextPlay(true);
//            if (oldId != getAudioId()) {
//                notifyChange(META_CHANGED);
//            }
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
        }
    }

    private void openCurrentAndNext() {
        openCurrentAndMaybeNext(false, true);
    }


    private void openCurrentAndNextPlay(boolean play) {
        openCurrentAndMaybeNext(play, true);
    }

    private void openCurrentAndMaybeNext(final boolean play, final boolean openNext) {
        synchronized (this) {
            closeCursor();
//            stop(false);
            boolean shutdown = false;
            if (mPlaylist.size() == 0 || mPlaylistInfo.size() == 0 && mPlayPos >= mPlaylist.size()) {
//                clearPlayInfos();
                return;
            }
            final long id = mPlaylist.get(mPlayPos).mId;
            updateCursor(id);
//            getLrc(id);
            if (mPlaylistInfo.get(id) == null) {
                return;
            }
            if (!mPlaylistInfo.get(id).islocal) {
//                if (mRequestUrl != null) {
//                    mRequestUrl.stop();
//                    mUrlHandler.removeCallbacks(mRequestUrl);
//                }
//                mRequestUrl = new RequestPlayUrl(id, play);
//                mUrlHandler.postDelayed(mRequestUrl, 70);
            } else {
                while (true) {
                    if (mCursor != null && openFile(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/"
                            + mCursor.getLong(IDCOLIDX))) {
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
                        stop();
                        mPlayPos = pos;
                        updateCursor(mPlaylist.get(mPlayPos).mId);
                    } else {
                        mOpenFailedCounter = 0;
                        MyLog.w(TAG, "Failed to open file for playback");
                        break;
                    }
                }
            }
        }
    }

    private void notifyChange(final String what) {
        Intent intent = null;
        if (what.equals(META_CHANGED)) {
            intent = new Intent(META_CHANGED);
            sendStickyBroadcast(intent);
        } else if (what.equals(MUSIC_CHANGED)) {
            play(true);
        } else if (what.equals(PLAYSTATE_CHANGED)) {
            updateNotification();
        }

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

                if (id != -1 && path.startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
                    updateCursor(uri);
                } else if (id != -1 && path.startsWith(MediaStore.Files.getContentUri("external").toString())) {
                    updateCursor(id);
                } else if (path.startsWith("content://downloads/")) {
                    String mpUri = getValueForDownloadedFile(this, uri, "mediaprovider_uri");
                    if (!TextUtils.isEmpty(mpUri)) {
                        if (openFile(mpUri)) {
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
                        mPlayPos = 0;
                    }
                } catch (final UnsupportedOperationException ex) {
                }
            }

            mFileToPlay = path;
            mPlayer.setDataSource(mFileToPlay);
            if (mPlayer.ismIsInitialized()) {
                mOpenFailedCounter = 0;
                return true;
            }
            stop();
            return false;
        }
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
        synchronized (this) {
            return mPlaylist.get(mPlayPos).mId;
        }
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

    public void nextPlay() {
        MyLog.i(TAG, "nextPlay");
        synchronized (this) {
            int pos = mNextPlayPos;
            if (pos < 0) {
                pos = getNextPosition(true);
            }
            if (pos < 0) {
                setIsPlaying(false, false);
                return;
            }
            setAndRecordPlayPos(pos);
            openCurrentAndNextPlay(true);
            play(true);
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
            nextPlay();
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
        if (isPlaying) {
            notifyMode = NOTIFY_MODE_FOREGROUND;
        } else if (recentlyPlayed()) {
            notifyMode = NOTIFY_MODE_BACKGROUND;
        } else {
            notifyMode = NOTIFY_MODE_NONE;
        }

        if (mNotifyMode != notifyMode) {
            if (mNotifyMode == NOTIFY_MODE_FOREGROUND) {
                stopForeground(notifyMode == NOTIFY_MODE_NONE || notifyMode == NOTIFY_MODE_BACKGROUND);
            } else if (notifyMode == NOTIFY_MODE_NONE) {
                mNotificationManager.cancel(mNotificationId);
            }
        }
        if (notifyMode == NOTIFY_MODE_FOREGROUND) {
            startForeground(mNotificationId, getNotification());

        } else if (notifyMode == NOTIFY_MODE_BACKGROUND) {
            mNotificationManager.notify(mNotificationId, getNotification());
        }
        mNotifyMode = notifyMode;
    }

    private void cancelNotification() {
        stopForeground(true);
        mNotificationManager.cancel(hashCode());
        mNotificationManager.cancel(mNotificationId);
        mNotificationPostTime = 0;
        mNotifyMode = NOTIFY_MODE_NONE;
    }


    private Notification getNotification() {
        RemoteViews remoteViews;
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

        final Bitmap bitmap = ImageUtils.getArtworkQuick(this, getArtistId(), 160, 160);
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.image, bitmap);
            mNoBit = null;
        } else if (!isTrackLocal()) {
            if (mNoBit != null) {
                remoteViews.setImageViewBitmap(R.id.image, mNoBit);
                mNoBit = null;
            } else {
                Uri uri = null;
                if (getAlbumPath() != null) {
                    try {
                        uri = Uri.parse(getAlbumPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (getAlbumPath() == null || uri == null) {
                    mNoBit = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_disk_210);
                    updateNotification();
                } else {
                    ImageRequest imageRequest = ImageRequestBuilder
                            .newBuilderWithSource(uri)
                            .setProgressiveRenderingEnabled(true)
                            .build();
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    DataSource<CloseableReference<CloseableImage>>
                            dataSource = imagePipeline.fetchDecodedImage(imageRequest, MediaService.this);
                    dataSource.subscribe(new BaseBitmapDataSubscriber() {

                        @Override
                        public void onNewResultImpl(@Nullable Bitmap bitmap) {
                            // You can use the bitmap in only limited ways
                            // No need to do any cleanup.
                            if (bitmap != null) {
                                mNoBit = bitmap;
                            }
                            updateNotification();
                        }

                        @Override
                        public void onFailureImpl(DataSource dataSource) {
                            // No cleanup required here.
                            mNoBit = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_disk_210);
                            updateNotification();
                        }
                    }, CallerThreadExecutor.getInstance());
                }
            }
        } else {
            remoteViews.setImageViewResource(R.id.image, R.drawable.placeholder_disk_210);
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
            mService.get().nextPlay();
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

        public boolean ismIsInitialized() {
            return mIsInitialized;
        }

        public boolean ismIsTrackNet() {
            return mIsTrackNet;
        }

        public boolean ismIsNextTrackPrepared() {
            return mIsNextTrackPrepared;
        }

        public boolean ismIsNextInitialized() {
            return mIsNextInitialized;
        }

        public boolean ismIllegalState() {
            return mIllegalState;
        }

        public MultiPlayer(final MediaService service) {
            mService = new WeakReference<MediaService>(service);
            mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);
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

        public void setDataSource(final String path) {

            mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
            if (mIsInitialized) {
//                setNextDataSource(null);
            }
        }

        public void setNextDataSource(final String path) {
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
            mIsInitialized = setNextDataSourceImpl(mNextMediaPlayer, path);
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
                    player.prepareAsync();
                    mIsTrackNet = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.setOnErrorListener(this);
            return true;
        }

        private boolean setNextDataSourceImpl(final MediaPlayer player, final String path) {
            mIsNextTrackPrepared = false;
            try {
                player.reset();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                if (path.startsWith("content://")) {
                    player.setDataSource(MyApplication.mContext, Uri.parse(path));
                    player.prepare();
                } else {
                    player.setDataSource(path);
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

        public void start() {
            if (!mIsTrackNet) {
                sencondaryPosition = 100;
                mCurrentMediaPlayer.start();
            } else {
                mCurrentMediaPlayer.start();
            }
        }

        public void stop() {
//            handler.removeCallbacks(setNextMediaPlayerIfPrepared);
//            handler.removeCallbacks(startMediaPlayerIfPrepared);
            mCurrentMediaPlayer.reset();
            mIsInitialized = false;
            mIsTrackPrepared = false;
        }

        public void pause() {
//            handler.removeCallbacks(startMediaPlayerIfPrepared);
            MyLog.i(TAG, "执行了pause");
            mCurrentMediaPlayer.pause();
        }

        public void release() {
            mCurrentMediaPlayer.release();
        }


        @Override
        public void onCompletion(MediaPlayer mp) {
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

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
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
