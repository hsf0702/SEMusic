package com.se.music.subpage.mine.local

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.se.music.R
import com.se.music.common.MusicEntity
import com.se.music.service.MusicPlayer
import com.se.music.utils.HandlerUtil
import java.util.*

/**
 * Created by gaojin on 2017/12/8.
 */
class MusicListAdapter constructor(private val context: Context, private val mList: ArrayList<MusicEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mHeadLayout = 0X01
    private val mContentLayout = 0X02

    private var playMusic: PlayMusic? = null
    private var handler: Handler? = null

    init {
        handler = HandlerUtil.instance
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommonItemViewHolder) {

        } else {
            (holder as ListItemViewHolder).onBindData(mList[position - 1])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == mHeadLayout) {
            CommonItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.common_item, parent, false))
        } else {
            ListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_musci_common_item, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return mList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            mHeadLayout
        } else {
            mContentLayout
        }
    }

    internal inner class PlayMusic(var position: Int) : Runnable {
        /**
         * 运行在主线程
         */
        override fun run() {
            val list = LongArray(mList.size)
            val infos = hashMapOf<Long, MusicEntity>()
            for (i in mList.indices) {
                val info = mList[i]
                list[i] = info.songId
                info.islocal = true
//                info.albumData = getAlbumArtUri(info.albumId.toLong()).toString()
                infos[list[i]] = mList[i]
            }
            if (position > -1)
                MusicPlayer.playAll(infos, list, position, false)
        }
    }

    inner class CommonItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
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

    inner class ListItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

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
            } else {
                if (playMusic != null)
                    handler!!.removeCallbacks(playMusic)
                if (adapterPosition > -1) {
                    playMusic = PlayMusic(adapterPosition - 1)
                    handler!!.postDelayed(playMusic, 70)
                }
            }
        }

        fun onBindData(musicEntity: MusicEntity) {
            mMusicName!!.text = musicEntity.musicName
            mMusicInfo!!.text = musicEntity.artist
        }
    }

}