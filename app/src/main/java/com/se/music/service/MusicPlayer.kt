package com.se.music.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.se.music.IMediaAidlInterface
import com.se.music.entity.MusicEntity
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
        private var mConnectionMap: WeakHashMap<Context, SeServiceConnection> = WeakHashMap()

        fun bindToService(context: Context): ServiceToken? {
            context.startService(Intent(context, MediaService::class.java))
            val serviceConnection = SeServiceConnection()
            if (context.bindService(Intent().setClass(context, MediaService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)) {
                mConnectionMap[context] = serviceConnection
                return ServiceToken(context)
            }
            return null
        }

        fun unbindFromService(token: ServiceToken) {
            val context = token.context
            val serviceConnection = mConnectionMap.remove(context) ?: return
            context.unbindService(serviceConnection)
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
            return mService?.isPlaying ?: false
        }

        fun getQueueSize(): Int {
            return mService?.queueSize ?: 0
        }

        fun getQueuePosition(): Int {
            return mService?.queuePosition ?: 0
        }

        fun setQueuePosition(position: Int) {
            mService?.queuePosition = position
        }

        fun getAlbumPath(): String? {
            return mService?.albumPath
        }

        fun getAlbumPathAll(): Array<String>? {
            return mService?.albumPathtAll
        }

        /**
         * 播放所有音乐
         *
         * @param infos        ID到音乐实体的映射
         * @param list         音乐ID的集合
         * @param position     当前播放的音乐的位置
         */
        @Synchronized
        fun playAll(infos: HashMap<Long, MusicEntity>, list: LongArray, position: Int) {
            if (list.isEmpty() || mService == null) {
                return
            }
            try {
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
                    mService!!.open(infos, list, 0)
                } else {
                    mService!!.open(infos, list, position)
                }
                mService!!.play()
            } catch (ignored: RemoteException) {
                ignored.printStackTrace()
            }
        }

        /**
         * 播放下一首音乐
         */
        fun nextPlay() {
            mService?.nextPlay()
        }

        fun position(): Long {
            return mService?.position() ?: 0
        }

        fun duration(): Long {
            return mService?.duration() ?: 0
        }

        fun seek(position: Long) {
            mService?.seek(position)
        }

        fun getCurrentAlbumId(): Long {
            return mService?.albumId ?: -1
        }

        fun isTrackLocal(): Boolean {
            return mService?.isTrackLocal ?: false
        }

        fun secondPosition(): Int {
            return mService?.secondPosition() ?: 0
        }

        fun previous(context: Context, force: Boolean) {
            val previous = Intent(context, MediaService::class.java)
            if (force) {
                previous.action = PREVIOUS_FORCE_ACTION
            } else {
                previous.action = PREVIOUS_ACTION
            }
            context.startService(previous)
        }

        /**
         * 获取循环状态
         */
        fun getRepeatMode(): Int {
            return mService?.repeatMode ?: MediaService.REPEAT_ALL
        }

        fun setRepeatMode(repeatMode: Int) {
            mService?.repeatMode = repeatMode
        }


        /**
         * 获取当前播放的音乐的名字
         *
         * @return
         */
        fun getTrackName(): String? {
            return mService?.trackName
        }

        /**
         * 获取当前音乐歌手的名字
         *
         * @return
         */
        fun getArtistName(): String? {
            return mService?.artistName
        }

        fun getAlbumPic(): String? {
            return mService?.albumPic
        }

        /**
         * 返回播放列表ID到音乐实体的映射
         *
         * @return
         */
        fun getPlayinfos(): HashMap<Long, MusicEntity>? {
            return mService?.playlistInfo as HashMap<Long, MusicEntity>
        }

        /**
         * 返回播放列表的ID
         *
         * @return
         */
        fun getQueue(): LongArray? {
            return mService?.queue
        }

        fun setQueue(index: Int) {
            mService?.queuePosition = index
        }

        fun getCurrentAudioId(): Long {
            return mService?.audioId ?: 0
        }
    }

    class SeServiceConnection : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = IMediaAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }
    }

    /**
     * ServiceToken
     */
    class ServiceToken(var context: Context)
}