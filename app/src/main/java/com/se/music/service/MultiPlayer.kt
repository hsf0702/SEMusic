package com.se.music.service

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.PowerManager
import android.util.Log
import com.se.music.singleton.ApplicationSingleton
import java.io.IOException
import java.lang.ref.WeakReference

/**
 *Author: gaojin
 *Time: 2018/9/16 下午7:34
 *播放器实现
 */

class MultiPlayer(service: MediaService) : MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    var isTrackPrepared = false
    var mIsNextTrackPrepared = false
    /**
     * 播放器是否初始化
     */
    var isInitialized = false
    var secondaryPosition = 0
    var audioSessionId: Int
        get() = mCurrentMediaPlayer.audioSessionId
        set(sessionId) {
            mCurrentMediaPlayer.audioSessionId = sessionId
        }

    //两个播放器
    private var mCurrentMediaPlayer: MediaPlayer = MediaPlayer()
    private var mNextMediaPlayer: MediaPlayer? = null

    private val mService: WeakReference<MediaService> = WeakReference(service)
    private var mIsTrackNet = false
    private var mIsNextInitialized = false
    private var mIllegalState = false
    private var preparedNextListener: MediaPlayer.OnPreparedListener = MediaPlayer.OnPreparedListener { mIsNextTrackPrepared = true }
    private var mHandler: Handler? = null
    private var mNextMediaPath: String? = null
    private var isFirstLoad = true
    private val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setFlags(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
            .build()
    private var preparedListener: MediaPlayer.OnPreparedListener = MediaPlayer.OnPreparedListener { mp ->
        if (isFirstLoad) {
            val seekPos = mService.get()!!.mLastSeekPos
            seek(seekPos)
            isFirstLoad = false
        }
        mService.get()!!.notifyChange(META_CHANGED)
        mp.setOnCompletionListener(this)
        isTrackPrepared = true
    }
    private var bufferingUpdateListener: MediaPlayer.OnBufferingUpdateListener = MediaPlayer.OnBufferingUpdateListener { _, percent ->
        if (secondaryPosition != 100)
            mService.get()!!.sendUpdateBuffer(percent)
        secondaryPosition = percent
    }
    private val handler = Handler()
    private var setNextMediaPlayerIfPrepared: Runnable = object : Runnable {
        var count = 0
        override fun run() {
            if (mIsNextTrackPrepared && isInitialized) {
                mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer)
            } else if (count < 60) {
                handler.postDelayed(this, 100)
            }
            count++
        }
    }
    private var startMediaPlayerIfPrepared: Runnable = object : Runnable {
        override fun run() {
            if (isTrackPrepared) {
                mCurrentMediaPlayer.start()
                val duration = duration()
                if (mService.get()?.mRepeatMode != MediaService.REPEAT_CURRENT && duration > 2000
                        && position() >= duration - 2000) {
                    mService.get()?.nextPlay()
                }
                mService.get()?.loading(false)
            } else {
                handler.postDelayed(this, 700)
            }
        }
    }

    init {
        mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK)
    }

    fun setDataSource(path: String) {
        isInitialized = setDataSourceImpl(mCurrentMediaPlayer, path)
        if (isInitialized) {
            setNextDataSource(null)
        }
    }

    fun setNextDataSource(path: String?) {
        mNextMediaPath = null
        mIsNextInitialized = false
        try {
            mCurrentMediaPlayer.setNextMediaPlayer(null)
        } catch (ignored: IllegalArgumentException) {
            Log.i(MediaService.TAG, "Next media player is current one, continuing")
        } catch (ignored: IllegalStateException) {
            Log.e(MediaService.TAG, "Media player not initialized!")
            return
        }

        if (mNextMediaPlayer != null) {
            mNextMediaPlayer!!.release()
            mNextMediaPlayer = null
        }
        if (path == null) {
            return
        }
        mNextMediaPlayer = MediaPlayer()
        mNextMediaPlayer!!.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK)
        mNextMediaPlayer!!.audioSessionId = audioSessionId

        if (setNextDataSourceImpl(mNextMediaPlayer!!, path)) {
            mNextMediaPath = path
            mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer)

        } else {
            if (mNextMediaPlayer != null) {
                mNextMediaPlayer!!.release()
                mNextMediaPlayer = null
            }
        }
    }

    private fun setDataSourceImpl(player: MediaPlayer, path: String): Boolean {
        mIsTrackNet = false
        isTrackPrepared = false
        try {
            player.reset()
            player.setAudioAttributes(audioAttributes)
            if (path.startsWith("content://")) {
                player.setOnPreparedListener(null)
                player.setDataSource(ApplicationSingleton.instance!!, Uri.parse(path))
                player.prepare()
                isTrackPrepared = true
                player.setOnCompletionListener(this)

            } else {
                player.setDataSource(path)
                player.setOnPreparedListener(preparedListener)
                player.prepareAsync()
                mIsTrackNet = true
            }
            if (mIllegalState) {
                mIllegalState = false
            }

        } catch (ignored: IOException) {
            return false
        } catch (ignored: IllegalArgumentException) {
            return false
        } catch (todo: IllegalStateException) {
            todo.printStackTrace()
            if (!mIllegalState) {
                mCurrentMediaPlayer = MediaPlayer()
                mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK)
                mCurrentMediaPlayer.audioSessionId = audioSessionId
                setDataSourceImpl(mCurrentMediaPlayer, path)
                mIllegalState = true
            } else {
                mIllegalState = false
                return false
            }
        }

        player.setOnErrorListener(this)
        player.setOnBufferingUpdateListener(bufferingUpdateListener)
        return true
    }

    private fun setNextDataSourceImpl(player: MediaPlayer, path: String): Boolean {

        mIsNextTrackPrepared = false
        try {
            player.reset()
            player.setAudioAttributes(audioAttributes)
            if (path.startsWith("content://")) {
                player.setOnPreparedListener(preparedNextListener)
                player.setDataSource(ApplicationSingleton.instance!!, Uri.parse(path))
                player.prepare()
            } else {
                player.setDataSource(path)
                player.setOnPreparedListener(preparedNextListener)
                player.prepare()
                mIsNextTrackPrepared = false
            }
        } catch (ignored: IOException) {
            return false
        } catch (ignored: IllegalArgumentException) {
            return false
        }

        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)
        return true
    }

    fun setHandler(handler: Handler) {
        mHandler = handler
    }

    fun start() {
        if (!mIsTrackNet) {
            mService.get()?.sendUpdateBuffer(100)
            secondaryPosition = 100
            mCurrentMediaPlayer.start()
        } else {
            secondaryPosition = 0
            mService.get()?.loading(true)
            handler.postDelayed(startMediaPlayerIfPrepared, 50)
        }
        mService.get()?.notifyChange(MUSIC_CHANGED)
    }

    fun stop() {
        handler.removeCallbacks(setNextMediaPlayerIfPrepared)
        handler.removeCallbacks(startMediaPlayerIfPrepared)
        mCurrentMediaPlayer.reset()
        isInitialized = false
        isTrackPrepared = false
    }

    fun release() {
        mCurrentMediaPlayer.release()
    }

    fun pause() {
        handler.removeCallbacks(startMediaPlayerIfPrepared)
        mCurrentMediaPlayer.pause()
    }

    fun duration(): Long {
        return if (isTrackPrepared) {
            mCurrentMediaPlayer.duration.toLong()
        } else -1
    }

    fun position(): Long {
        if (isTrackPrepared) {
            try {
                return mCurrentMediaPlayer.currentPosition.toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return -1
    }

    fun secondPosition(): Long {
        return if (isTrackPrepared) {
            secondaryPosition.toLong()
        } else -1
    }

    fun seek(whereto: Long): Long {
        mCurrentMediaPlayer.seekTo(whereto.toInt())
        return whereto
    }

    fun setVolume(vol: Float) {
        try {
            mCurrentMediaPlayer.setVolume(vol, vol)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        Log.w(MediaService.TAG, "Music Server Error what: $what extra: $extra")
        when (what) {
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                val service = mService.get()
                val errorInfo = TrackErrorInfo(service!!.getAudioId(),
                        service.getTrackName())
                isInitialized = false
                isTrackPrepared = false
                mCurrentMediaPlayer.release()
                mCurrentMediaPlayer = MediaPlayer()
                mCurrentMediaPlayer.setWakeMode(service, PowerManager.PARTIAL_WAKE_LOCK)
                val msg = mHandler?.obtainMessage(MediaService.SERVER_DIED, errorInfo)
                mHandler?.sendMessageDelayed(msg, 2000)
                return true
            }
            else -> {
            }
        }
        return false
    }


    override fun onCompletion(mp: MediaPlayer) {
        Log.w(MediaService.TAG, "completion")
        if (mp === mCurrentMediaPlayer && mNextMediaPlayer != null) {
            mCurrentMediaPlayer.release()
            mCurrentMediaPlayer = mNextMediaPlayer!!
            mNextMediaPath = null
            mNextMediaPlayer = null
            mHandler?.sendEmptyMessage(MediaService.TRACK_WENT_TO_NEXT)
        } else {
            mHandler?.sendEmptyMessage(MediaService.TRACK_ENDED)
            mHandler?.sendEmptyMessage(MediaService.RELEASE_WAKELOCK)
        }
    }
}