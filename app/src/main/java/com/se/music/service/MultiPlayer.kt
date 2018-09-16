package com.se.music.service

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
 */

class MultiPlayer(service: MediaService) : MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private val mService: WeakReference<MediaService> = WeakReference(service)
    var isTrackPrepared = false
    var mIsTrackNet = false
    var mIsNextTrackPrepared = false
    var mIsNextInitialized = false
    var mIllegalState = false
    var preparedNextListener: MediaPlayer.OnPreparedListener = MediaPlayer.OnPreparedListener { mIsNextTrackPrepared = true }
    private var mCurrentMediaPlayer: MediaPlayer? = MediaPlayer()
    private var mNextMediaPlayer: MediaPlayer? = null
    private var mHandler: Handler? = null
    var isInitialized = false
    private var mNextMediaPath: String? = null
    private var isFirstLoad = true
    private var preparedListener: MediaPlayer.OnPreparedListener = MediaPlayer.OnPreparedListener { mp ->
        if (isFirstLoad) {
            val seekpos = mService.get()!!.mLastSeekPos
            seek(seekpos)
            isFirstLoad = false
        }
        mService.get()!!.notifyChange(META_CHANGED)
        mp.setOnCompletionListener(this)
        isTrackPrepared = true
    }
    var sencondaryPosition = 0
    private var bufferingUpdateListener: MediaPlayer.OnBufferingUpdateListener = MediaPlayer.OnBufferingUpdateListener { mp, percent ->
        if (sencondaryPosition != 100)
            mService.get()!!.sendUpdateBuffer(percent)
        sencondaryPosition = percent
    }
    private val handler = Handler()
    private var setNextMediaPlayerIfPrepared: Runnable = object : Runnable {
        internal var count = 0

        override fun run() {
            if (mIsNextTrackPrepared && isInitialized) {
                mCurrentMediaPlayer!!.setNextMediaPlayer(mNextMediaPlayer)
            } else if (count < 60) {
                handler.postDelayed(this, 100)
            }
            count++
        }
    }
    private var startMediaPlayerIfPrepared: Runnable = object : Runnable {
        override fun run() {
            if (isTrackPrepared) {
                mCurrentMediaPlayer!!.start()
                val duration = duration()
                if (mService.get()!!.mRepeatMode != MediaService.REPEAT_CURRENT && duration > 2000
                        && position() >= duration - 2000) {
                    mService.get()!!.nextPlay(true)
                    Log.e("play to go", "")
                }
                mService.get()!!.loading(false)
            } else {
                handler.postDelayed(this, 700)
            }
        }
    }

    var audioSessionId: Int
        get() = mCurrentMediaPlayer!!.audioSessionId
        set(sessionId) {
            mCurrentMediaPlayer!!.audioSessionId = sessionId
        }

    init {
        mCurrentMediaPlayer!!.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK)
    }

    fun setDataSource(path: String) {
        isInitialized = setDataSourceImpl(mCurrentMediaPlayer!!, path)
        if (isInitialized) {
            setNextDataSource(null)
        }
    }

    fun setNextDataSource(path: String?) {
        mNextMediaPath = null
        mIsNextInitialized = false
        try {
            mCurrentMediaPlayer!!.setNextMediaPlayer(null)
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
            mCurrentMediaPlayer!!.setNextMediaPlayer(mNextMediaPlayer)

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
            player.setAudioStreamType(AudioManager.STREAM_MUSIC)
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
                mCurrentMediaPlayer = null
                mCurrentMediaPlayer = MediaPlayer()
                mCurrentMediaPlayer!!.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK)
                mCurrentMediaPlayer!!.audioSessionId = audioSessionId
                setDataSourceImpl(mCurrentMediaPlayer!!, path)
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
            player.setAudioStreamType(AudioManager.STREAM_MUSIC)
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
            mService.get()!!.sendUpdateBuffer(100)
            sencondaryPosition = 100
            mCurrentMediaPlayer!!.start()
        } else {
            sencondaryPosition = 0
            mService.get()!!.loading(true)
            handler.postDelayed(startMediaPlayerIfPrepared, 50)
        }
        mService.get()!!.notifyChange(MUSIC_CHANGED)
    }

    fun stop() {
        handler.removeCallbacks(setNextMediaPlayerIfPrepared)
        handler.removeCallbacks(startMediaPlayerIfPrepared)
        mCurrentMediaPlayer!!.reset()
        isInitialized = false
        isTrackPrepared = false
    }

    fun release() {
        mCurrentMediaPlayer!!.release()
    }

    fun pause() {
        handler.removeCallbacks(startMediaPlayerIfPrepared)
        mCurrentMediaPlayer!!.pause()
    }

    fun duration(): Long {
        return if (isTrackPrepared) {
            mCurrentMediaPlayer!!.duration.toLong()
        } else -1
    }

    fun position(): Long {
        if (isTrackPrepared) {
            try {
                return mCurrentMediaPlayer!!.currentPosition.toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return -1
    }

    fun secondPosition(): Long {
        return if (isTrackPrepared) {
            sencondaryPosition.toLong()
        } else -1
    }

    fun seek(whereto: Long): Long {
        mCurrentMediaPlayer!!.seekTo(whereto.toInt())
        return whereto
    }

    fun setVolume(vol: Float) {
        try {
            mCurrentMediaPlayer!!.setVolume(vol, vol)
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
                mCurrentMediaPlayer!!.release()
                mCurrentMediaPlayer = MediaPlayer()
                mCurrentMediaPlayer!!.setWakeMode(service, PowerManager.PARTIAL_WAKE_LOCK)
                val msg = mHandler!!.obtainMessage(MediaService.SERVER_DIED, errorInfo)
                mHandler!!.sendMessageDelayed(msg, 2000)
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
            mCurrentMediaPlayer!!.release()
            mCurrentMediaPlayer = mNextMediaPlayer
            mNextMediaPath = null
            mNextMediaPlayer = null
            mHandler!!.sendEmptyMessage(MediaService.TRACK_WENT_TO_NEXT)
        } else {
            //                mService.get().mWakeLock.acquire(30000);
            mHandler!!.sendEmptyMessage(MediaService.TRACK_ENDED)
            mHandler!!.sendEmptyMessage(MediaService.RELEASE_WAKELOCK)
        }
    }
}