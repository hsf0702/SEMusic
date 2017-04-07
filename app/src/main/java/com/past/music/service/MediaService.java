package com.past.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
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
import android.text.TextUtils;
import android.util.Log;

import com.past.music.MyApplication;
import com.past.music.entity.MusicEntity;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.IMediaAidlInterface;

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
    private static final int IDCOLIDX = 0;
    private static final int TRACK_ENDED = 1;
    private static final int TRACK_WENT_TO_NEXT = 2;
    private static final int RELEASE_WAKELOCK = 3;
    private static final int SERVER_DIED = 4;
    private static final int FOCUSCHANGE = 5;
    private static final int FADEDOWN = 6;
    private static final int FADEUP = 7;

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

    private boolean isPlaying = false;


    private Context mContext = null;
    //传进来的歌单
    private HashMap<Long, MusicEntity> mPlaylistInfo = new HashMap<>();

    /**
     * 播放列表
     */
    private ArrayList<MusicTrack> mPlaylist = new ArrayList<MusicTrack>(100);


    /**
     * 提供访问控制音量和钤声模式的操作
     */
    private AudioManager mAudioManager;
    private MusicPlayerHandler mPlayerHandler;
    private HandlerThread mHandlerThread;

    private MultiPlayer mPlayer;
    private String mFileToPlay;
    private Cursor mCursor;

    private int mCardId;

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
        SystemClock.sleep(6000);
        mContext = this;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mHandlerThread = new HandlerThread("MusicPlayerHandler", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mPlayerHandler = new MusicPlayerHandler(this, mHandlerThread.getLooper());
        mPlayer = new MultiPlayer(this);
        mPlayer.setHandler(mPlayerHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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
//        setIsSupposedToBePlaying(true, true);
//        cancelShutdown();
//        updateNotification();
        isPlaying = true;
        notifyChange(META_CHANGED);
        MyLog.i(TAG, "play");
    }

    /**
     * 歌曲暂停
     */
    public void pause() {
        synchronized (this) {
            mPlayerHandler.removeMessages(FADEUP);
            mPlayer.pause();
            isPlaying = false;
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
//                        stop(false);
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
        }
        if (what.equals(MUSIC_CHANGED)) {
            play(true);
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
            return false;
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

    public long getAudioId() {
        synchronized (this) {
            return mPlaylist.get(mPlayPos).mId;
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
                isPlaying = false;
                return;
            }
            setAndRecordPlayPos(pos);
            openCurrentAndNextPlay(true);
            play(true);
            notifyChange(META_CHANGED);
        }
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
                        service.notifyChange(MUSIC_CHANGED);
                        break;
                }
            }
        }
    }
}
