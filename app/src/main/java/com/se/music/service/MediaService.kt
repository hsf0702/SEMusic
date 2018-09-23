package com.se.music.service

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.media.AudioManager
import android.media.audiofx.AudioEffect
import android.media.session.MediaSession
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.util.Log
import android.widget.RemoteViews
import com.se.music.R
import com.se.music.activity.MainActivity
import com.se.music.entity.MusicEntity
import com.se.music.provider.database.provider.RecentStore
import com.se.music.singleton.GsonFactory
import java.io.File
import java.io.RandomAccessFile
import java.lang.ref.WeakReference
import java.util.*

/**
 *Author: gaojin
 *Time: 2018/5/13 下午11:17
 */

class MediaService : Service() {

    companion object {
        const val TAG = "MediaService"

        const val ID_INDEX = 0
        const val TRACK_ENDED = 1
        const val TRACK_WENT_TO_NEXT = 2
        const val RELEASE_WAKELOCK = 3
        const val SERVER_DIED = 4
        const val FOCUS_CHANGE = 5
        const val FADE_DOWN = 6
        const val FADE_UP = 7
        const val LRC_DOWNLOADED = -10

        /**
         * 单曲循环
         */
        const val REPEAT_CURRENT = 1

        /**
         * 顺序播放
         */
        const val REPEAT_ALL = 2

        /**
         * 随机播放
         */
        const val REPEAT_SHUFFLER = 3

        const val MAX_HISTORY_SIZE = 1000

        const val LRC_PATH = "/semusic/lrc/"

        private const val SHUTDOWN = "com.se.music.shutdown"
        private const val TRACK_NAME = "trackname"
        private const val NOTIFY_MODE_NONE = 0
        private const val NOTIFY_MODE_FOREGROUND = 1
        private const val IDLE_DELAY = 5 * 60 * 1000
    }

    //handler
    private lateinit var mUrlHandler: Handler
    private lateinit var mLrcHandler: Handler
    private lateinit var mPlayerHandler: MusicPlayerHandler
    private lateinit var mHandlerThread: HandlerThread
    private lateinit var mPlayer: MultiPlayer
    private lateinit var mSession: MediaSession

    //public field
    var isPlaying = false
    var mRepeatMode = REPEAT_ALL
    var mLastSeekPos: Long = 0

    private val mShuffler = Shuffler.instance
    /**
     * 历史歌单
     */
    private val mHistory = LinkedList<Int>()
    private val mBinder = ServiceStub(this)
    private val mCardId: Int = 0
    private val mNotificationId = 1000
    /**
     * 传进来的歌单
     */
    @SuppressLint("UseSparseArrays")
    private var mPlaylistInfo = HashMap<Long, MusicEntity>()
    /**
     * 当前播放列表
     */
    private val mPlaylist = ArrayList<MusicTrack>(100)
    private var mAutoShuffleList: LongArray? = null
    private val mAudioFocusListener = AudioManager.OnAudioFocusChangeListener { focusChange -> mPlayerHandler.obtainMessage(FOCUS_CHANGE, focusChange, 0).sendToTarget() }
    private var mFileToPlay: String? = null
    private var mCursor: Cursor? = null
    private var mServiceInUse = false
    private var mNotifyMode = NOTIFY_MODE_NONE
    /**
     * 提供访问控制音量和钤声模式的操作
     */
    private var mAudioManager: AudioManager? = null
    private var mNotificationManager: NotificationManager? = null
    private var mAlarmManager: AlarmManager? = null
    private var mShutdownIntent: PendingIntent? = null
    private var mShutdownScheduled = false
    /**
     * 当前播放音乐的下标
     */
    private var mPlayPos = -1
    /**
     * 下一首歌的下标
     */
    private var mNextPlayPos = -1
    private var mOpenFailedCounter = 0
    private val mServiceStartId = -1
    private var mLastPlayedTime: Long = 0
    private var mNotification: Notification? = null
    private var mNotificationPostTime: Long = 0
    private var mPreferences: SharedPreferences? = null
    private var mPausedByTransientLossOfFocus = false
    private var mRecentStore: RecentStore? = null
    private val mIntentReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            handleCommandIntent(intent)
        }
    }
    private val mLrcThread = Thread(Runnable {
        Looper.prepare()
        mLrcHandler = Handler()
        Looper.loop()
    })

    private val mGetUrlThread = Thread(Runnable {
        Looper.prepare()
        mUrlHandler = Handler()
        Looper.loop()
    })

    override fun onBind(intent: Intent?): IBinder {
        cancelShutdown()
        mServiceInUse = true
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mServiceInUse = false
        if (isPlaying) {
            return true
        } else if (mPlaylist.size > 0 || mPlayerHandler.hasMessages(TRACK_ENDED)) {
            scheduleDelayedShutdown()
            return true
        }
        stopSelf(mServiceStartId)
        return true
    }

    override fun onRebind(intent: Intent?) {
        cancelShutdown()
        mServiceInUse = true
    }

    override fun onCreate() {
        super.onCreate()
        mGetUrlThread.start()
        mLrcThread.start()
        mHandlerThread = HandlerThread("MusicPlayerHandler", android.os.Process.THREAD_PRIORITY_BACKGROUND)
        mHandlerThread.start()
        mPlayerHandler = MusicPlayerHandler(this, mHandlerThread.looper)
        mPlayer = MultiPlayer(this)
        mPlayer.setHandler(mPlayerHandler)
        mPreferences = getSharedPreferences("Service", 0)
        mRecentStore = RecentStore.instance

        val filter = IntentFilter()
        filter.addAction(TOGGLE_PAUSE_ACTION)
        filter.addAction(STOP_ACTION)
        filter.addAction(NEXT_ACTION)
        filter.addAction(PREVIOUS_ACTION)
        filter.addAction(PREVIOUS_FORCE_ACTION)
        filter.addAction(REPEAT_ACTION)
        filter.addAction(SHUFFLE_ACTION)
        filter.addAction(TRY_GET_TRACK_INFO)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(SEND_PROGRESS)
        registerReceiver(mIntentReceiver, filter)

        val shutdownIntent = Intent(this, MediaService::class.java)
        shutdownIntent.action = SHUTDOWN

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mShutdownIntent = PendingIntent.getService(this, 0, shutdownIntent, 0)
    }

    private fun setUpMediaSession() {
        mSession = MediaSession(this, "pastmusic")
        mSession.setCallback(object : MediaSession.Callback() {
            override fun onPause() {
                pause()
                mPausedByTransientLossOfFocus = false
            }

            override fun onPlay() {
                play()
            }

            override fun onSeekTo(pos: Long) {
                seek(pos)
            }

            override fun onSkipToNext() {
                nextPlay()
            }

            override fun onSkipToPrevious() {
                previous()
            }

            override fun onStop() {
                pause()
                mPausedByTransientLossOfFocus = false
                seek(0)
                releaseServiceUiAndStop()
            }
        })
        mSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS)
    }

    fun seek(position: Long): Long {
        var result: Long = -1
        if (mPlayer.isInitialized) {
            if (position < 0) {
                result = mPlayer.seek(0)
            } else if (position > mPlayer.duration()) {
                result = mPlayer.seek(mPlayer.duration())
            }
            notifyChange(POSITION_CHANGED)
        }
        return result
    }

    fun position(): Long {
        if (mPlayer.isInitialized && mPlayer.isTrackPrepared) {
            try {
                return mPlayer.position()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return -1
    }

    fun getSecondPosition(): Int {
        return if (mPlayer.isInitialized) {
            mPlayer.secondaryPosition
        } else -1
    }

    fun getRepeatMode(): Int {
        return mRepeatMode
    }

    fun setRepeatMode(repeatmode: Int) {
        synchronized(this) {
            mRepeatMode = repeatmode
            setNextTrack()
            saveQueue(false)
            notifyChange(REPEAT_MODE_CHANGED)
        }
    }

    private fun getPreviousPlayPosition(removeFromHistory: Boolean): Int {
        synchronized(this) {
            if (mRepeatMode == REPEAT_SHUFFLER) {
                val historySize = mHistory.size
                if (historySize == 0) {
                    return -1
                }
                val pos = mHistory[historySize - 1]
                if (removeFromHistory) {
                    mHistory.removeAt(historySize - 1)
                }
                return pos
            } else {
                return if (mPlayPos > 0) {
                    mPlayPos - 1
                } else {
                    mPlaylist.size - 1
                }
            }
        }
    }

    private fun openCurrent() {
        openCurrentAndMaybeNext(false)
    }

    fun previous() {
        synchronized(this) {
            val goPrevious = getRepeatMode() != REPEAT_CURRENT
            if (goPrevious) {
                val pos = getPreviousPlayPosition(true)
                if (pos < 0) {
                    return
                }
                mNextPlayPos = mPlayPos
                mPlayPos = pos
                stop(false)
                openCurrent()
                play(false)
                notifyChange(META_CHANGED)
                notifyChange(MUSIC_CHANGED)
            } else {
                seek(0)
                play(false)
            }
        }
    }

    fun play() {
        play(true)
    }

    /**
     * 播放歌曲
     *
     * @param createNewNextTrack 是否准备下一首歌
     */
    fun play(createNewNextTrack: Boolean) {
        val intent = Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId())
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
        sendBroadcast(intent)

        if (createNewNextTrack) {
            setNextTrack()
        } else {
            setNextTrack(mNextPlayPos)
        }
        if (mPlayer.isTrackPrepared) {
            val duration = mPlayer.duration()
            if (mRepeatMode != REPEAT_CURRENT && duration > 2000
                    && mPlayer.position() >= duration - 2000) {
                nextPlay()
            }
        }
        mPlayer.start()
        mPlayerHandler.removeMessages(FADE_DOWN)
        mPlayerHandler.sendEmptyMessage(FADE_UP)
        setIsPlaying(true, true)
        cancelShutdown()
        updateNotification()
        notifyChange(META_CHANGED)
    }

    /**
     * 设置isPlaying标志位 并更新notification
     *
     * @param value
     * @param notify
     */
    private fun setIsPlaying(value: Boolean, notify: Boolean) {
        if (isPlaying != value) {
            isPlaying = value
            if (!isPlaying) {
                mLastPlayedTime = System.currentTimeMillis()
            }
            if (notify) {
                notifyChange(PLAY_STATE_CHANGED)
            }
        }
    }

    /**
     * 歌曲暂停
     */
    fun pause() {
        synchronized(this) {
            mPlayerHandler.removeMessages(FADE_UP)
            mPlayer.pause()
            setIsPlaying(false, true)
            notifyChange(META_CHANGED)
        }
    }

    fun stop() {
        stop(true)
    }

    private fun stop(goToIdle: Boolean) {
        if (mPlayer.isInitialized) {
            mPlayer.stop()
        }
        mFileToPlay = null
        closeCursor()
        if (goToIdle) {
            setIsPlaying(false, false)
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
    private fun getValueForDownloadedFile(context: Context, uri: Uri, column: String): String? {

        var cursor: Cursor? = null
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return null
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
    private fun openCursorAndGoToFirst(uri: Uri, projection: Array<String>,
                                       selection: String?, selectionArgs: Array<String>?): Cursor? {
        val c = contentResolver.query(uri, projection, selection, selectionArgs, null)
                ?: return null
        if (!c.moveToFirst()) {
            c.close()
            return null
        }
        return c
    }

    @Synchronized
    private fun closeCursor() {
        if (mCursor != null) {
            mCursor!!.close()
            mCursor = null
        }
    }

    /**
     * 根据Uri更新游标
     *
     * @param uri
     */
    private fun updateCursor(uri: Uri) {
        synchronized(this) {
            closeCursor()
            mCursor = openCursorAndGoToFirst(uri, PROJECTION, null, null)
        }
    }

    /**
     * 根据ID更新游标
     *
     * @param trackId
     */
    private fun updateCursor(trackId: Long) {
        val info = mPlaylistInfo[trackId]
        if (mPlaylistInfo[trackId] != null) {
            val cursor = MatrixCursor(PROJECTION)
            cursor.addRow(arrayOf(info!!.songId, info.artist, info.albumName, info.musicName, info.data, info.albumData, info.albumId, info.artistId, info.albumPic))
            cursor.moveToFirst()
            mCursor = cursor
            cursor.close()
        }
    }

    private fun updateCursor(selection: String, selectionArgs: Array<String>) {
        synchronized(this) {
            closeCursor()
            mCursor = openCursorAndGoToFirst(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION, selection, selectionArgs)
        }
    }

    fun open(infos: HashMap<Long, MusicEntity>, list: LongArray, position: Int) {
        synchronized(this) {
            mPlaylistInfo = infos
            val oldId = getAudioId()
            val listLength = list.size
            var newList = true
            if (mPlaylist.size == listLength) {
                newList = false
                for (i in 0 until listLength) {
                    if (list[i] != mPlaylist[i].mId) {
                        newList = true
                        break
                    }
                }
            }
            if (newList) {
                addToPlayList(list, -1)
                notifyChange(QUEUE_CHANGED)
            }
            mPlayPos = if (position >= 0) {
                position
            } else {
                mShuffler.nextInt(mPlaylist.size)
            }
            mHistory.clear()
            openCurrentAndMaybeNext(true)
            if (oldId != getAudioId()) {
                notifyChange(META_CHANGED)
            }
        }
    }

    /**
     * 音乐加入播放列表
     *
     * @param list
     * @param position
     */
    private fun addToPlayList(list: LongArray, position: Int) {
        var index = position
        val addLen = list.size
        if (index < 0) {
            mPlaylist.clear()
            index = 0
        }

        mPlaylist.ensureCapacity(mPlaylist.size + addLen)
        if (index > mPlaylist.size) {
            index = mPlaylist.size
        }

        val arrayList = ArrayList<MusicTrack>(addLen)
        for (i in list.indices) {
            arrayList.add(MusicTrack(list[i], i))
        }

        mPlaylist.addAll(index, arrayList)

        if (mPlaylist.size == 0) {
            closeCursor()
            notifyChange(META_CHANGED)
        }
    }

    private fun openCurrentAndNext() {
        openCurrentAndMaybeNext(true)
    }

    /**
     * 播放当前歌曲并且准备下一首
     *
     * @param openNext
     */
    private fun openCurrentAndMaybeNext(openNext: Boolean) {
        synchronized(this) {
            closeCursor()
            stop(false)
            var shutdown = false

            if (mPlaylist.size == 0 || mPlaylistInfo.size == 0 && mPlayPos >= mPlaylist.size) {
                clearPlayInfos()
                return
            }
            val id = mPlaylist[mPlayPos].mId
            updateCursor(id)
            getLrc(id)
            if (mPlaylistInfo[id] == null) {
                return
            }
            if (!mPlaylistInfo[id]!!.islocal) {
                //在线歌曲
            } else {
                while (true) {
                    if (mCursor != null
                            && openFile(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString() + "/"
                                    + mCursor!!.getLong(ID_INDEX))) {
                        break
                    }
                    closeCursor()
                    if (mOpenFailedCounter++ < 10 && mPlaylist.size > 1) {
                        val pos = getNextPosition()
                        if (pos < 0) {
                            shutdown = true
                            break
                        }
                        mPlayPos = pos
                        stop(false)
                        mPlayPos = pos
                        updateCursor(mPlaylist[mPlayPos].mId)
                    } else {
                        mOpenFailedCounter = 0
                        Log.w(TAG, "Failed to open file for playback")
                        shutdown = true
                        break
                    }
                }
            }

            if (shutdown) {
                scheduleDelayedShutdown()
                if (isPlaying) {
                    isPlaying = false
                    notifyChange(PLAY_STATE_CHANGED)
                }
            } else if (openNext) {
                setNextTrack()
            }
        }
    }

    private fun clearPlayInfos() {
        val file = File(cacheDir.absolutePath + "playlist")
        if (file.exists()) {
            file.delete()
        }
    }

    fun duration(): Long {
        return if (mPlayer.isInitialized && mPlayer.isTrackPrepared) {
            mPlayer.duration()
        } else -1
    }

    fun notifyChange(what: String) {
        if (SEND_PROGRESS == what) {
            val intent = Intent(what)
            intent.putExtra("position", position())
            intent.putExtra("duration", duration())
            sendStickyBroadcast(intent)
            return
        }
        if (what == POSITION_CHANGED) {
            return
        }
        val intent = Intent(what)
        intent.putExtra("id", getAudioId())
        intent.putExtra("artist", getArtistName())
        intent.putExtra("album", getAlbumName())
        intent.putExtra("track", getTrackName())
        intent.putExtra("playing", isPlaying)
        intent.putExtra("albumuri", getAlbumPath())
        intent.putExtra("islocal", isTrackLocal())
        sendStickyBroadcast(intent)
        val musicIntent = Intent(intent)
        musicIntent.action = what.replace(TIMBER_PACKAGE_NAME, MUSIC_PACKAGE_NAME)
        sendStickyBroadcast(musicIntent)
        if (what == META_CHANGED) {
            mRecentStore!!.addSongId(getAudioId())
        } else if (what == QUEUE_CHANGED) {
            val intent1 = Intent("com.past.music.emptyplaylist")
            intent.putExtra("showorhide", "show")
            sendBroadcast(intent1)
            saveQueue(true)
            if (isPlaying) {
                if (mNextPlayPos >= 0 && mNextPlayPos < mPlaylist.size) {
                    setNextTrack(mNextPlayPos)
                } else {
                    setNextTrack()
                }
            }
        } else {
            saveQueue(false)
        }
        if (what == PLAY_STATE_CHANGED) {
            updateNotification()
        }
    }

    private fun saveQueue(full: Boolean) {

        val editor = mPreferences!!.edit()
        if (full) {
            //            mPlaybackStateStore.saveState(mPlaylist, mShuffleMode != SHUFFLE_NONE ? mHistory : null);
            if (mPlaylistInfo.size > 0) {
                val temp = GsonFactory.instance.toJson(mPlaylistInfo)
                try {
                    val file = File(cacheDir.absolutePath + "playlist")
                    val ra = RandomAccessFile(file, "rws")
                    ra.write(temp.toByteArray())
                    ra.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            editor.putInt("cardid", mCardId)

        }
        editor.putInt("curpos", mPlayPos)
        if (mPlayer.isInitialized) {
            editor.putLong("seekpos", mPlayer.position())
        }
        editor.putInt("repeatmode", mRepeatMode)
        editor.apply()
    }

    /**
     * 获取下一首音乐的位置
     *
     * @param force
     * @return
     */
    private fun getNextPosition(): Int {
        if (mPlaylist.isEmpty()) {
            return -1
        }
        if (mRepeatMode == REPEAT_CURRENT) {
            return if (mPlayPos < 0) {
                0
            } else mPlayPos
        } else if (mRepeatMode == REPEAT_SHUFFLER) {
            doAutoShuffleUpdate()
            return mPlayPos + 1
        } else if (mRepeatMode == REPEAT_ALL) {
            if (mPlayPos >= mPlaylist.size - 1) {
                if (mRepeatMode == REPEAT_ALL) {
                    return 0
                }
                return -1
            } else {
                return mPlayPos + 1
            }
        } else {
            return -1
        }
    }

    private fun setNextTrack() {
        setNextTrack(getNextPosition())
    }

    /**
     * 设置下一首将要播放的音乐的信息
     *
     * @param position
     */
    private fun setNextTrack(position: Int) {
        mNextPlayPos = position
        if (mNextPlayPos >= 0 && mNextPlayPos < mPlaylist.size) {
            val id = mPlaylist[mNextPlayPos].mId
            if (mPlaylistInfo[id] != null) {
                if (mPlaylistInfo[id]!!.islocal) {
                    mPlayer.setNextDataSource(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString() + "/" + id)
                } else {
                    mPlayer.setNextDataSource(null)
                }
            }
        } else {
            mPlayer.setNextDataSource(null)
        }
    }

    fun openFile(path: String?): Boolean {
        synchronized(this) {
            if (path == null) {
                return false
            }
            if (mCursor == null) {
                val uri = Uri.parse(path)
                var shouldAddToPlaylist = true
                var id: Long = -1
                try {
                    id = java.lang.Long.valueOf(uri.lastPathSegment)
                } catch (ignored: NumberFormatException) {
                }

                if (!(id == (-1).toLong() || !path.startsWith(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()))) {
                    updateCursor(uri)
                } else {
                    if (id != (-1).toLong() && path.startsWith(
                                    MediaStore.Files.getContentUri("external").toString())) {
                        updateCursor(id)
                    } else if (path.startsWith("content://downloads/")) {
                        val mpUri = getValueForDownloadedFile(this, uri, "mediaprovider_uri")
                        if (!TextUtils.isEmpty(mpUri)) {
                            return if (openFile(mpUri)) {
                                notifyChange(META_CHANGED)
                                true
                            } else {
                                false
                            }
                        } else {
                            //                        updateCursorForDownloadedFile(this, localMusicUri);
                            shouldAddToPlaylist = false
                        }
                    } else {
                        val where = MediaStore.Audio.Media.DATA + "=?"
                        val selectionArgs = arrayOf(path)
                        updateCursor(where, selectionArgs)
                    }
                }
                try {
                    if (mCursor != null && shouldAddToPlaylist) {
                        mPlaylist.clear()
                        mPlaylist.add(MusicTrack(mCursor!!.getLong(ID_INDEX), -1))
                        notifyChange(QUEUE_CHANGED)
                        mPlayPos = 0
                        mHistory.clear()
                    }
                } catch (ignored: UnsupportedOperationException) {
                }

            }
            mFileToPlay = path
            mPlayer.setDataSource(mFileToPlay!!)
            if (mPlayer.isInitialized) {
                mOpenFailedCounter = 0
                return true
            }
            var trackName = getTrackName()
            if (TextUtils.isEmpty(trackName)) {
                trackName = path
            }
            sendErrorMessage(trackName!!)
            stop(true)
            return false
        }
    }

    private fun sendErrorMessage(trackName: String) {
        val intent = Intent(TRACK_ERROR)
        intent.putExtra(TRACK_NAME, trackName)
        sendBroadcast(intent)
    }

    fun isTrackLocal(): Boolean {
        synchronized(this) {
            val info = mPlaylistInfo[getAudioId()] ?: return true
            return info.islocal
        }
    }

    fun getTrackName(): String? {
        synchronized(this) {
            return if (mCursor == null) {
                null
            } else mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE))
        }
    }

    fun getAlbumPic(): String? {
        synchronized(this) {
            return if (mCursor == null) {
                null
            } else mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(ALBUM_PIC))
        }
    }

    fun getAlbumName(): String? {
        synchronized(this) {
            return if (mCursor == null) {
                null
            } else mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM))
        }
    }

    fun getArtistName(): String? {
        synchronized(this) {
            return if (mCursor == null) {
                null
            } else mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST))
        }
    }

    fun getQueueSize(): Int {
        synchronized(this) {
            return mPlaylist.size
        }
    }

    fun getPlaylistInfo(): HashMap<Long, MusicEntity> {
        synchronized(this) {
            return mPlaylistInfo
        }
    }

    fun getAudioSessionId(): Int {
        synchronized(this) {
            return mPlayer.audioSessionId
        }
    }

    fun getAlbumId(): Long {
        synchronized(this) {
            return if (mCursor == null) {
                -1
            } else mCursor!!.getLong(mCursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID))
        }
    }

    fun getAlbumPathAll(): Array<String?> {
        synchronized(this) {
            try {
                val len = mPlaylistInfo.size
                val albums = arrayOfNulls<String>(len)
                val queue = getQueue()
                for (i in 0 until len) {
                    albums[i] = mPlaylistInfo[queue[i]]!!.albumData
                }
                return albums
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return arrayOf()
        }
    }

    fun getAlbumPicAll(): Array<String?> {
        synchronized(this) {
            try {
                val len = mPlaylistInfo.size
                val albums = arrayOfNulls<String>(len)
                val queue = getQueue()
                for (i in 0 until len) {
                    albums[i] = mPlaylistInfo[queue[i]]!!.albumPic
                }
                return albums
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return arrayOf()
        }
    }

    fun getArtistId(): Long {
        synchronized(this) {
            return if (mCursor == null) {
                -1
            } else mCursor!!.getLong(mCursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID))
        }
    }

    fun getAudioId(): Long {
        val track = getCurrentTrack()
        return track?.mId ?: -1
    }

    fun getCurrentTrack(): MusicTrack? {
        return getTrack(mPlayPos)
    }

    @Synchronized
    fun getTrack(index: Int): MusicTrack? {
        return if (index >= 0 && index < mPlaylist.size) {
            mPlaylist[index]
        } else null
    }

    fun getAlbumPath(): String? {
        synchronized(this) {
            return if (mCursor == null) {
                null
            } else mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.MIME_TYPE))
        }
    }

    /**
     * 获得播放音乐的队列
     *
     * @return
     */
    fun getQueue(): LongArray {
        synchronized(this) {
            val len = mPlaylist.size
            val list = LongArray(len)
            for (i in 0 until len) {
                list[i] = mPlaylist[i].mId
            }
            return list
        }
    }

    /**
     * 获取播放队列的位置
     *
     * @return
     */
    fun getQueuePosition(): Int {
        synchronized(this) {
            return mPlayPos
        }
    }

    /**
     * 设置当前播放队列播放的歌曲
     *
     * @param index
     */
    fun setQueuePosition(index: Int) {
        synchronized(this) {
            stop(false)
            mPlayPos = index
            openCurrentAndNext()
            play()
            notifyChange(META_CHANGED)
        }
    }

    /**
     * 设置当前播放的位置mPlayPos
     *
     * @param nextPos
     */
    fun setAndRecordPlayPos(nextPos: Int) {
        synchronized(this) {
            mPlayPos = nextPos
        }
    }

    fun nextPlay() {
        synchronized(this) {
            var pos = mNextPlayPos
            if (pos < 0) {
                pos = getNextPosition()
            }
            if (pos < 0) {
                setIsPlaying(false, false)
                return
            }
            stop(false)
            setAndRecordPlayPos(pos)
            openCurrentAndMaybeNext(true)
            play()
            notifyChange(MUSIC_CHANGED)
        }
    }

    private fun getLrc(id: Long) {
        val info = mPlaylistInfo[id]
        val lrc = Environment.getExternalStorageDirectory().absolutePath + LRC_PATH
        var file = File(lrc)
        if (!file.exists()) {
            //不存在就建立此目录
            val r = file.mkdirs()
        }
        file = File(lrc + id)
        if (!file.exists()) {
            //获取歌词
        }
    }

    private fun cancelShutdown() {
        if (mShutdownScheduled) {
            mAlarmManager!!.cancel(mShutdownIntent)
            mShutdownScheduled = false
        }
    }

    private fun recentlyPlayed(): Boolean {
        return isPlaying || System.currentTimeMillis() - mLastPlayedTime < IDLE_DELAY
    }

    private fun handleCommandIntent(intent: Intent) {
        val action = intent.action
        if (TOGGLE_PAUSE_ACTION == action) {
            if (isPlaying) {
                pause()
            } else {
                play()
            }
        } else if (NEXT_ACTION == action) {
            nextPlay()
        } else if (STOP_ACTION == action) {
            pause()
            releaseServiceUiAndStop()
        } else if (REPEAT_ACTION == action) {
            cycleRepeat()
        } else if (SHUFFLE_ACTION == action) {
            //待定
        } else if (TRY_GET_TRACK_INFO == action) {
            getLrc(mPlaylist[mPlayPos].mId)
        }
    }

    private fun releaseServiceUiAndStop() {
        if (isPlaying || mPlayerHandler!!.hasMessages(TRACK_ENDED)) {
            return
        }
        cancelNotification()
        mAudioManager!!.abandonAudioFocus(mAudioFocusListener)
        if (!mServiceInUse) {
            stopSelf(mServiceStartId)
        }
    }

    private fun scheduleDelayedShutdown() {
        mAlarmManager!!.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + IDLE_DELAY, mShutdownIntent)
        mShutdownScheduled = true
    }

    private fun updateNotification() {
        val notifyMode: Int = if (isPlaying) {
            NOTIFY_MODE_FOREGROUND
        } else {
            NOTIFY_MODE_NONE
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
            mNotificationManager!!.notify(mNotificationId, getNotification())
        } else {
            if (notifyMode == NOTIFY_MODE_FOREGROUND) {
                startForeground(mNotificationId, getNotification())
            } else {
                mNotificationManager!!.notify(mNotificationId, getNotification())
            }
        }
        mNotifyMode = notifyMode
    }

    fun sendUpdateBuffer(progress: Int) {
        val intent = Intent(BUFFER_UP)
        intent.putExtra("progress", progress)
        sendBroadcast(intent)
    }

    fun loading(l: Boolean) {
        val intent = Intent(MUSIC_LOADING)
        intent.putExtra("isloading", l)
        sendBroadcast(intent)
    }

    fun removeTrack(id: Long): Int {
        var numberMoved = 0
        synchronized(this) {
            var i = 0
            while (i < mPlaylist.size) {
                if (mPlaylist[i].mId == id) {
                    numberMoved += removeTracksInternal(i, i)
                    i--
                }
                i++
            }

            mPlaylistInfo.remove(id)
        }


        if (numberMoved > 0) {
            notifyChange(QUEUE_CHANGED)
        }
        return numberMoved
    }

    private fun removeTracksInternal(first: Int, last: Int): Int {
        var first = first
        var last = last
        synchronized(this) {
            when {
                last < first -> return 0
                first < 0 -> first = 0
                last >= mPlaylist.size -> last = mPlaylist.size - 1
            }

            var gotonext = false
            if (mPlayPos in first..last) {
                mPlayPos = first
                gotonext = true
            } else if (mPlayPos > last) {
                mPlayPos -= last - first + 1
            }
            val numToRemove = last - first + 1

            if (first == 0 && last == mPlaylist.size - 1) {
                mPlayPos = -1
                mNextPlayPos = -1
                mPlaylist.clear()
                mHistory.clear()
            } else {
                for (i in 0 until numToRemove) {
                    mPlaylistInfo.remove(mPlaylist[first].mId)
                    mPlaylist.removeAt(first)

                }

                val positionIterator = mHistory.listIterator()
                while (positionIterator.hasNext()) {
                    val pos = positionIterator.next()
                    if (pos in first..last) {
                        positionIterator.remove()
                    } else if (pos > last) {
                        positionIterator.set(pos - numToRemove)
                    }
                }
            }
            if (gotonext) {
                if (mPlaylist.size == 0) {
                    stop(true)
                    mPlayPos = -1
                    closeCursor()
                } else {
                    if (mPlayPos >= mPlaylist.size) {
                        mPlayPos = 0
                    }
                    val wasPlaying = isPlaying
                    stop(false)
                    openCurrentAndNext()
                    if (wasPlaying) {
                        play()
                    }
                }
                notifyChange(META_CHANGED)
            }
            return last - first + 1
        }
    }

    private fun cancelNotification() {
        stopForeground(true)
        mNotificationManager!!.cancel(hashCode())
        mNotificationManager!!.cancel(mNotificationId)
        mNotificationPostTime = 0
        mNotifyMode = NOTIFY_MODE_NONE
    }

    private fun doAutoShuffleUpdate() {
        var notify = false
        if (mPlayPos > 10) {
            removeTracks(0, mPlayPos - 9)
            notify = true
        }
        val toAdd = 7 - (mPlaylist.size - if (mPlayPos < 0) -1 else mPlayPos)
        for (i in 0 until toAdd) {
            var lookBack = mHistory.size
            var idx: Int
            while (true) {
                idx = mShuffler.nextInt(mAutoShuffleList!!.size)
                if (!wasRecentlyUsed(idx, lookBack)) {
                    break
                }
                lookBack /= 2
            }
            mHistory.add(idx)
            if (mHistory.size > MAX_HISTORY_SIZE) {
                mHistory.removeAt(0)
            }
            mPlaylist.add(MusicTrack(mAutoShuffleList!![idx], -1))
            notify = true
        }
        if (notify) {
            notifyChange(QUEUE_CHANGED)
        }
    }

    private fun removeTracks(first: Int, last: Int): Int {
        val numberMoved = removeTracksInternal(first, last)
        if (numberMoved > 0) {
            notifyChange(QUEUE_CHANGED)
        }
        return numberMoved
    }

    private fun wasRecentlyUsed(idx: Int, lookbacksize: Int): Boolean {
        var lookbacksize = lookbacksize
        if (lookbacksize == 0) {
            return false
        }
        val histsize = mHistory.size
        if (histsize < lookbacksize) {
            lookbacksize = histsize
        }
        val maxidx = histsize - 1
        for (i in 0 until lookbacksize) {
            val entry = mHistory[maxidx - i].toLong()
            if (entry == idx.toLong()) {
                return true
            }
        }
        return false
    }

    private fun cycleRepeat() {
        if (mRepeatMode == REPEAT_ALL) {
            setRepeatMode(REPEAT_CURRENT)
        } else {
            setRepeatMode(REPEAT_ALL)
        }
    }


    private fun getNotification(): Notification {
        val remoteViews = RemoteViews(this.packageName, R.layout.remote_view)
        val PAUSE_FLAG = 0x1
        val NEXT_FLAG = 0x2
        val STOP_FLAG = 0x3
        val albumName = getAlbumName()
        val artistName = getArtistName()
        val isPlaying = isPlaying

        val text = if (TextUtils.isEmpty(albumName)) artistName else "$artistName - $albumName"
        remoteViews.setTextViewText(R.id.title, getTrackName())
        remoteViews.setTextViewText(R.id.text, text)

        //此处action不能是一样的 如果一样的 接受的flag参数只是第一个设置的值
        val pauseIntent = Intent(TOGGLE_PAUSE_ACTION)
        pauseIntent.putExtra("FLAG", PAUSE_FLAG)
        val pausePIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0)
        remoteViews.setImageViewResource(R.id.img_play, if (isPlaying) R.drawable.icon_remote_pause else R.drawable.icon_remote_play)
        remoteViews.setOnClickPendingIntent(R.id.img_play, pausePIntent)

        val nextIntent = Intent(NEXT_ACTION)
        nextIntent.putExtra("FLAG", NEXT_FLAG)
        val nextPIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0)
        remoteViews.setOnClickPendingIntent(R.id.img_next_play, nextPIntent)

        val preIntent = Intent(STOP_ACTION)
        preIntent.putExtra("FLAG", STOP_FLAG)
        val prePIntent = PendingIntent.getBroadcast(this, 0, preIntent, 0)
        remoteViews.setOnClickPendingIntent(R.id.img_cancel, prePIntent)

        val mMainIntent = Intent(this, MainActivity::class.java)
        val mainIntent = PendingIntent.getActivity(this, 0, mMainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (mNotificationPostTime == 0L) {
            mNotificationPostTime = System.currentTimeMillis()
        }
        if (mNotification == null) {
            val builder = NotificationCompat.Builder(this)
                    .setContent(remoteViews)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(mainIntent)
                    .setWhen(mNotificationPostTime)
            mNotification = builder.build()
        } else {
            mNotification!!.contentView = remoteViews
        }
        return mNotification!!
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelNotification()
        mPlayerHandler.removeCallbacksAndMessages(null)
        mHandlerThread.quit()
        mPlayer.release()
        closeCursor()
        unregisterReceiver(mIntentReceiver)

    }

    private class MusicPlayerHandler constructor(service: MediaService, looper: Looper) : Handler(looper) {
        private val mService: WeakReference<MediaService> = WeakReference(service)
        override fun handleMessage(msg: Message) {
            val service = mService.get() ?: return
            synchronized(service) {
                when (msg.what) {
                    TRACK_WENT_TO_NEXT -> {
                        service.setAndRecordPlayPos(service.mNextPlayPos)
                        service.setNextTrack()
                        if (service.mCursor != null) {
                            service.mCursor!!.close()
                            service.mCursor = null
                        }
                        service.updateCursor(service.mPlaylist[service.mPlayPos].mId)
                        service.notifyChange(META_CHANGED)
                        service.notifyChange(MUSIC_CHANGED)
                        service.updateNotification()
                    }
                    TRACK_ENDED -> if (service.mRepeatMode == REPEAT_CURRENT) {
                        service.seek(0)
                        service.play()
                    } else {
                        service.nextPlay()
                    }
                    LRC_DOWNLOADED -> service.notifyChange(LRC_UPDATED)
                    else -> {
                    }
                }
            }
        }
    }
}