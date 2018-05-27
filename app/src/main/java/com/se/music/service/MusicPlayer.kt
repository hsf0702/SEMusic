package com.se.music.service

import android.app.Activity
import android.content.*
import android.os.IBinder
import android.os.RemoteException
import com.se.music.IMediaAidlInterface
import com.se.music.common.MusicEntity
import java.util.*

/**
 * Created by gaojin on 2018/3/8.
 * 界面和Service的桥   MusicPlayer维护着一个WeakHashMap  保存Context和ServiceConnection的键值对
 */
class MusicPlayer {

    companion object {
        var mService: IMediaAidlInterface? = null
        /**
         * 实现规范化Context到ServiceConnection的映射
         * 一般用weak reference引用的对象是有价值被cache, 而且很容易被重新被构建, 且很消耗内存的对象.
         * GC执行的时候就会回收weak reference
         */
        private var mConnectionMap: WeakHashMap<Context, ServiceConnect> = WeakHashMap()

        fun bindToService(context: Context, callback: ServiceConnection): ServiceToken? {

            //document:Return the parent activity if this view is an embedded child.
            //如果是内嵌的Activity则获得他所在activity的上下文
            var realActivity: Activity? = (context as Activity).parent
            if (realActivity == null) {
                realActivity = context
            }
            val contextWrapper = ContextWrapper(realActivity)
            contextWrapper.startService(Intent(contextWrapper, MediaService::class.java))
            val sc = ServiceConnect(callback, contextWrapper.applicationContext)
            if (contextWrapper.bindService(Intent().setClass(contextWrapper, MediaService::class.java), sc, Context.BIND_AUTO_CREATE)) {
                mConnectionMap[contextWrapper] = sc
                return ServiceToken(contextWrapper)
            }
            return null
        }

        fun unbindFromService(token: ServiceToken?) {
            if (token == null) {
                return
            }
            val mContextWrapper = token.mWrappedContext
            val sc = mConnectionMap.remove(mContextWrapper) ?: return
            mContextWrapper.unbindService(sc)
            if (mConnectionMap.isEmpty()) {
                mService = null
            }
        }

        /**
         * 播放或者暂停音乐
         */
        fun playOrPause() {
            try {
                if (mService != null) {
                    if (mService!!.isPlaying) {
                        mService!!.pause()
                    } else {
                        mService!!.play()
                    }
                }
            } catch (ignored: Exception) {
            }
        }

        fun getIsPlaying(): Boolean {
            if (mService != null) {
                try {
                    return mService!!.isPlaying
                } catch (ignored: RemoteException) {
                }
            }
            return false
        }

        fun getQueueSize(): Int {
            try {
                if (mService != null) {
                    return mService!!.queueSize
                } else {
                }
            } catch (ignored: RemoteException) {
            }
            return 0
        }

        fun getQueuePosition(): Int {
            try {
                if (mService != null) {
                    return mService!!.queuePosition
                }
            } catch (ignored: RemoteException) {
            }
            return 0
        }

        fun setQueuePosition(position: Int) {
            if (mService != null) {
                try {
                    mService!!.queuePosition = position
                } catch (ignored: RemoteException) {
                }

            }
        }

        fun getAlbumPath(): String? {
            if (mService != null) {
                try {
                    return mService!!.albumPath
                } catch (ignored: RemoteException) {
                }
            }
            return null
        }

        fun getAlbumPathAll(): Array<String>? {
            if (mService != null) {
                try {
                    return mService!!.albumPathtAll
                } catch (ignored: RemoteException) {
                }
            }
            return null
        }

        /**
         * 播放所有音乐
         *
         * @param infos        ID到音乐实体的映射
         * @param list         音乐ID的集合
         * @param position     当前播放的音乐的位置
         * @param forceShuffle
         */
        @Synchronized
        fun playAll(infos: HashMap<Long, MusicEntity>, list: LongArray?, position: Int, forceShuffle: Boolean) {
            var position = position
            if (list == null || list.isEmpty() || mService == null) {
                return
            }
            try {
                if (forceShuffle) {
                    mService!!.shuffleMode = MediaService.SHUFFLE_NORMAL
                }
                val currentId = mService!!.audioId
                val currentQueuePosition = getQueuePosition()
                if (position != -1) {
                    val playlist = getQueue()
                    if (Arrays.equals(list, playlist)) {
                        if (currentQueuePosition == position && currentId == list[position]) {
                            mService!!.play()
                            return
                        } else {
                            mService!!.queuePosition = position
                            return
                        }
                    }
                }
                if (position < 0) {
                    position = 0
                }
                mService!!.open(infos, list, position)
                mService!!.play()
            } catch (ignored: RemoteException) {
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

        }

        /**
         * 播放下一首音乐
         */
        fun nextPlay() {
            try {
                if (mService != null) {
                    mService!!.nextPlay()
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }

        fun position(): Long {
            if (mService != null) {
                try {
                    return mService!!.position()
                } catch (ignored: RemoteException) {
                } catch (ignored: IllegalStateException) {
                }
            }
            return 0
        }

        fun duration(): Long {
            if (mService != null) {
                try {
                    return mService!!.duration()
                } catch (ignored: RemoteException) {
                } catch (ignored: IllegalStateException) {
                }
            }
            return 0
        }

        fun seek(position: Long) {
            if (mService != null) {
                try {
                    mService!!.seek(position)
                } catch (ignored: RemoteException) {
                }
            }
        }

        fun getCurrentAlbumId(): Long {
            if (mService != null) {
                try {
                    return mService!!.albumId
                } catch (ignored: RemoteException) {
                    ignored.printStackTrace()
                }
            }
            return -1
        }

        fun isTrackLocal(): Boolean {
            try {
                if (mService != null) {
                    return mService!!.isTrackLocal
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

            return false
        }

        fun secondPosition(): Int {
            if (mService != null) {
                try {
                    return mService!!.secondPosition()
                } catch (ignored: RemoteException) {
                } catch (ignored: IllegalStateException) {
                }

            }
            return 0
        }

        fun previous(context: Context, force: Boolean) {
            val previous = Intent(context, MediaService::class.java)
            if (force) {
                previous.action = MediaService.PREVIOUS_FORCE_ACTION
            } else {
                previous.action = MediaService.PREVIOUS_ACTION
            }
            context.startService(previous)
        }

        fun cycleRepeat() {
            try {
                if (mService != null) {
                    if (mService!!.shuffleMode == MediaService.SHUFFLE_NORMAL) {
                        mService!!.shuffleMode = MediaService.SHUFFLE_NONE
                        mService!!.repeatMode = MediaService.REPEAT_CURRENT
                        return
                    } else {
                        when (mService!!.repeatMode) {
                            MediaService.REPEAT_CURRENT -> mService!!.repeatMode = MediaService.REPEAT_ALL
                            MediaService.REPEAT_ALL -> mService!!.shuffleMode = MediaService.SHUFFLE_NORMAL
                        }
                    }
                }
            } catch (ignored: RemoteException) {
                ignored.printStackTrace()
            }

        }

        fun getShuffleMode(): Int {
            if (mService != null) {
                try {
                    return mService!!.shuffleMode
                } catch (ignored: RemoteException) {
                    ignored.printStackTrace()
                }
            }
            return 0
        }

        fun setShuffleMode(mode: Int) {
            try {
                if (mService != null) {
                    mService!!.shuffleMode = mode
                }
            } catch (ignored: RemoteException) {
                ignored.printStackTrace()
            }
        }

        fun getRepeatMode(): Int {
            if (mService != null) {
                try {
                    return mService!!.repeatMode
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            return 0
        }


        /**
         * 获取当前播放的音乐的名字
         *
         * @return
         */
        fun getTrackName(): String? {
            try {
                if (mService != null) {
                    return mService!!.trackName
                }
            } catch (ignored: RemoteException) {
            }
            return null
        }

        /**
         * 获取当前音乐歌手的名字
         *
         * @return
         */
        fun getArtistName(): String? {
            try {
                if (mService != null) {
                    return mService!!.artistName
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            return null
        }

        fun getAlbumPic(): String? {
            try {
                if (mService != null) {
                    return mService!!.albumPic
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 返回播放列表ID到音乐实体的映射
         *
         * @return
         */
        fun getPlayinfos(): HashMap<Long, MusicEntity>? {
            try {
                if (mService != null) {
                    return mService!!.playlistInfo as HashMap<Long, MusicEntity>
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            return null
        }


        /**
         * 返回播放列表的ID
         *
         * @return
         */
        fun getQueue(): LongArray? {
            try {
                if (mService != null) {
                    return mService!!.queue
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            return null
        }

        fun setQueue(index: Int) {
            try {
                if (mService != null) {
                    mService!!.queuePosition = index
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        fun getCurrentAudioId(): Long {
            try {
                if (mService != null) {
                    return mService!!.audioId
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

            return 0
        }
    }

    class ServiceConnect(private val mCallback: ServiceConnection?, private val mContext: Context) : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = IMediaAidlInterface.Stub.asInterface(service)
            mCallback?.onServiceConnected(name, service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mCallback?.onServiceDisconnected(name)
            mService = null
        }
    }

    /**
     * mWrappedContext:Service和Activity的父类
     */
    class ServiceToken(var mWrappedContext: ContextWrapper)

}