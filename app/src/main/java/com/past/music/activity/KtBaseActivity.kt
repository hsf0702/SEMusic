package com.past.music.activity

import android.content.*
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import butterknife.ButterKnife
import com.past.music.fragment.QuickControlsFragment
import com.past.music.pastmusic.IMediaAidlInterface
import com.past.music.pastmusic.R
import com.past.music.service.MediaService
import com.past.music.service.MusicPlayer
import com.past.music.utils.MConstants
import java.lang.ref.WeakReference
import java.util.*



/**
 * Creator：gaojin
 * date：2017/11/6 下午7:57
 */
open class KtBaseActivity : AppCompatActivity(), ServiceConnection {

    private var mToken: MusicPlayer.ServiceToken? = null
    var mService: IMediaAidlInterface? = null
    private var fragment: QuickControlsFragment? = null //底部播放控制栏
    private val mMusicListener = ArrayList<MusicStateListener>()
    private var mPlaybackStatus: PlaybackStatus? = null //BroadCastReceiver 接受播放状态变化等

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ButterKnife.bind(this)
        //每次新建一个activity都会连接一次，然后把上下文和Sc保存在一个WeakHashMap中
        mToken = MusicPlayer.bindToService(this, this)
        setStatusBar()
        mPlaybackStatus = PlaybackStatus(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(MediaService.PLAYSTATE_CHANGED)
        intentFilter.addAction(MediaService.META_CHANGED)
        intentFilter.addAction(MediaService.MUSIC_CHANGED)
        intentFilter.addAction(MediaService.QUEUE_CHANGED)
        intentFilter.addAction(MConstants.MUSIC_COUNT_CHANGED)
        intentFilter.addAction(MediaService.TRACK_PREPARED)
        intentFilter.addAction(MediaService.BUFFER_UP)
        intentFilter.addAction(MConstants.EMPTY_LIST)
        intentFilter.addAction(MediaService.MUSIC_CHANGED)
        intentFilter.addAction(MediaService.LRC_UPDATED)
        intentFilter.addAction(MConstants.PLAYLIST_COUNT_CHANGED)
        intentFilter.addAction(MediaService.MUSIC_LODING)
        registerReceiver(mPlaybackStatus, intentFilter)
        showQuickControl(true)
    }

    open fun setStatusBar() {}

    /**
     * @param show 显示或关闭底部播放控制栏
     */
    open fun showQuickControl(show: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        if (show) {
            if (fragment == null) {
                fragment = QuickControlsFragment.newInstance()
                ft.add(R.id.bottom_container, fragment).commitAllowingStateLoss()
            } else {
                ft.show(fragment).commitAllowingStateLoss()
            }
        } else {
            if (fragment != null)
                ft.hide(fragment).commitAllowingStateLoss()
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
    open fun updateTrack() {

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
            if (listener != null) {
                listener.reloadAdapter()
                listener.updatePlayInfo()
            }
        }
    }

    /**
     * fragment界面刷新
     */
    fun refreshUI() {
        for (listener in mMusicListener) {
            listener?.reloadAdapter()
        }
    }

    fun updateTime() {
        for (listener in mMusicListener) {
            listener?.updateTime()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService()
        mMusicListener.clear()
        unregisterReceiver(mPlaybackStatus)
    }

    fun unbindService() {
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken)
            mToken = null
        }
    }

    /**
     * X轴方向滑动打开Activity
     *
     * @param intent
     * @param isInFromRight
     */
    fun startActivityByX(intent: Intent, isInFromRight: Boolean) {
        this.startActivity(intent)
        if (isInFromRight) {
            this.overridePendingTransition(R.anim.empty, R.anim.empty)
        } else {
            this.overridePendingTransition(R.anim.empty, R.anim.empty)
        }
    }


    class PlaybackStatus(activity: KtBaseActivity) : BroadcastReceiver() {

        private val mReference: WeakReference<KtBaseActivity> = WeakReference(activity)


        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val baseActivity = mReference.get()
            if (baseActivity != null) {
                when (action) {
                    MediaService.META_CHANGED -> baseActivity.baseUpdatePlayInfo()
                    MediaService.PLAYSTATE_CHANGED -> {
                    }
                    MediaService.TRACK_PREPARED -> baseActivity.updateTime()
                    MediaService.BUFFER_UP -> baseActivity.updateBuffer(intent.getIntExtra("progress", 0))
                    MediaService.MUSIC_LODING -> baseActivity.loading(intent.getBooleanExtra("isloading", false))
                    MediaService.REFRESH -> {
                    }
                    MConstants.MUSIC_COUNT_CHANGED -> baseActivity.refreshUI()
                    MConstants.PLAYLIST_COUNT_CHANGED -> baseActivity.refreshUI()
                    MediaService.QUEUE_CHANGED -> baseActivity.updateQueue()
                    MediaService.TRACK_ERROR -> Toast.makeText(baseActivity, "错误了嘤嘤嘤", Toast.LENGTH_SHORT).show()
                    MediaService.MUSIC_CHANGED -> {
                        baseActivity.updateTrack()
                    }
                    MediaService.LRC_UPDATED -> baseActivity.updateLrc()
                }
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mService = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mService = IMediaAidlInterface.Stub.asInterface(service)
    }

}