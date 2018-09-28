package com.se.music.base

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.se.music.R
import com.se.music.base.listener.MusicStateListener
import com.se.music.main.QuickControlsFragment
import com.se.music.service.*
import com.se.music.utils.setTransparentForWindow
import java.lang.ref.WeakReference
import java.util.*


/**
 * Creator：gaojin
 * date：2017/11/6 下午7:57
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private var mToken: MusicPlayer.ServiceToken? = null
    private var fragment: QuickControlsFragment? = null //底部播放控制栏
    private val mMusicListener = ArrayList<MusicStateListener>()
    private lateinit var mPlaybackStatus: PlaybackStatus

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mToken = MusicPlayer.bindToService(this)
        mPlaybackStatus = PlaybackStatus(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(PLAY_STATE_CHANGED)
        intentFilter.addAction(META_CHANGED)
        intentFilter.addAction(MUSIC_CHANGED)
        intentFilter.addAction(QUEUE_CHANGED)
        intentFilter.addAction(TRACK_PREPARED)
        intentFilter.addAction(BUFFER_UP)
        intentFilter.addAction(MUSIC_CHANGED)
        intentFilter.addAction(LRC_UPDATED)
        intentFilter.addAction(MUSIC_LOADING)
        intentFilter.addAction(EMPTY_LIST)
        intentFilter.addAction(PLAYLIST_COUNT_CHANGED)
        intentFilter.addAction(MUSIC_COUNT_CHANGED)
        registerReceiver(mPlaybackStatus, intentFilter)

        setTransparentForWindow(this)
    }

    /**
     * @param show 显示或关闭底部播放控制栏
     */
    open fun showQuickControl(show: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        if (show) {
            if (fragment == null) {
                fragment = QuickControlsFragment.newInstance()
                ft.add(R.id.bottom_container, fragment).commit()
            } else {
                ft.show(fragment).commit()
            }
        } else {
            if (fragment != null)
                ft.hide(fragment).commit()
        }
    }

    /**
     * @param p 更新歌曲缓冲进度值，p取值从0~100
     */
    open fun updateBuffer(p: Int) {

    }

    /**
     * @param l 歌曲是否加载中
     */
    open fun loading(l: Boolean) {

    }

    /**
     * 更新播放队列
     */
    open fun updateQueue() {

    }

    /**
     * 歌曲切换
     */
    open fun musicChanged() {

    }

    open fun updateLrc() {

    }

    fun setMusicStateListenerListener(status: MusicStateListener?) {
        if (status === this) {
            throw UnsupportedOperationException("Override the method, don't add a listener")
        }

        if (status != null) {
            mMusicListener.add(status)
        }
    }

    fun removeMusicStateListenerListener(status: MusicStateListener?) {
        if (status != null) {
            mMusicListener.remove(status)
        }
    }

    /**
     * 更新歌曲状态信息
     */
    open fun baseUpdatePlayInfo() {
        for (listener in mMusicListener) {
            listener.reloadAdapter()
            listener.updatePlayInfo()
        }
    }

    /**
     * fragment界面刷新
     */
    fun refreshUI() {
        for (listener in mMusicListener) {
            listener.reloadAdapter()
        }
    }

    fun updateTime() {
        for (listener in mMusicListener) {
            listener.updateTime()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService()
        mMusicListener.clear()
        unregisterReceiver(mPlaybackStatus)
    }

    private fun unbindService() {
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken!!)
            mToken = null
        }
    }

    class PlaybackStatus(activity: BaseActivity) : BroadcastReceiver() {

        private val mReference: WeakReference<BaseActivity> = WeakReference(activity)

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val baseActivity = mReference.get()
            if (baseActivity != null) {
                when (action) {
                    META_CHANGED -> baseActivity.baseUpdatePlayInfo()
                    PLAY_STATE_CHANGED -> {
                    }
                    TRACK_PREPARED -> baseActivity.updateTime()
                    BUFFER_UP -> baseActivity.updateBuffer(intent.getIntExtra("progress", 0))
                    MUSIC_LOADING -> baseActivity.loading(intent.getBooleanExtra("isloading", false))
                    REFRESH -> {
                    }
                    MUSIC_COUNT_CHANGED -> baseActivity.refreshUI()
                    PLAYLIST_COUNT_CHANGED -> baseActivity.refreshUI()
                    QUEUE_CHANGED -> baseActivity.updateQueue()
                    TRACK_ERROR -> Toast.makeText(baseActivity, "错误了嘤嘤嘤", Toast.LENGTH_SHORT).show()
                    MUSIC_CHANGED -> baseActivity.musicChanged()
                    LRC_UPDATED -> baseActivity.updateLrc()
                }
            }
        }
    }

}