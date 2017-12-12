package com.past.music.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.past.music.entity.MusicEntity
import com.past.music.pastmusic.R
import com.past.music.service.MusicPlayer
import com.past.music.utils.HandlerUtil
import com.past.music.utils.MusicUtils
import java.util.*

/**
 * Created by gaojin on 2017/12/8.
 */
class KtMusicListAdapter constructor(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val HEAD_LAYOUT = 0X01
    val CONTENT_LAYOUT = 0X02

    private var mList: ArrayList<MusicEntity>? = null
    private var playMusic: PlayMusic? = null
    private var handler: Handler? = null
    private var mContext: Context = context

    init {
        handler = HandlerUtil.getInstance(context)
    }

    //更新adpter的数据
    fun updateDataSet(list: ArrayList<MusicEntity>) {
        this.mList = list
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is CommonItemViewHolder) {

        } else {
            (holder as ListItemViewHolder).onBindData(mList!![position - 1], position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEAD_LAYOUT) {
            CommonItemViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.common_item, parent, false))
        } else {
            ListItemViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.fragment_musci_common_item, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (null != mList) mList!!.size + 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEAD_LAYOUT
        } else {
            CONTENT_LAYOUT
        }
    }


    internal inner class PlayMusic(var position: Int) : Runnable {
        /**
         * 运行在主线程
         */
        override fun run() {
            val list = LongArray(mList!!.size)
            val infos = hashMapOf<Long, MusicEntity>()
            for (i in mList!!.indices) {
                val info = mList!![i]
                list[i] = info.songId
                info.islocal = true
                info.albumData = MusicUtils.getAlbumArtUri(info.albumId.toLong()).toString() + ""
                infos.put(list[i], mList!![i])
            }
            if (position > -1)
                MusicPlayer.playAll(infos, list, position, false)
        }
    }

    internal inner class CommonItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var textView: TextView? = null
        var select: ImageView? = null

        init {
            textView = view.findViewById(R.id.play_all_number)
            select = view.findViewById(R.id.select)
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (playMusic != null)
                handler!!.removeCallbacks(playMusic)
            if (adapterPosition > -1) {
                playMusic = PlayMusic(0)
                handler!!.postDelayed(playMusic, 70)
            }
        }
    }

    internal inner class ListItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var mMusicName: TextView? = null
        var mMusicInfo: TextView? = null
        var mListButton: ImageView? = null

        init {
            mMusicName = view.findViewById(R.id.music_name)
            mMusicInfo = view.findViewById(R.id.music_info)
            mListButton = view.findViewById(R.id.viewpager_list_button)
            mListButton!!.setOnClickListener(this)
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v.id == R.id.viewpager_list_button) {
//            songOperationDialog = SongOperationDialog(mContext, mList.get(adapterPosition - 1), MConstants.MUSICOVERFLOW)
//            songOperationDialog.show()
            } else {
                if (playMusic != null)
                    handler!!.removeCallbacks(playMusic)
                if (adapterPosition > -1) {
                    playMusic = PlayMusic(adapterPosition - 1)
                    handler!!.postDelayed(playMusic, 70)
                }
            }
        }

        fun onBindData(musicEntity: MusicEntity, position: Int) {
            mMusicName!!.text = musicEntity.getMusicName()
            mMusicInfo!!.text = musicEntity.getArtist()
        }
    }

}