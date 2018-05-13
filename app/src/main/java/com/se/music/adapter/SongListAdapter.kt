package com.se.music.adapter

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.se.music.R
import com.se.music.entity.MusicEntity
import com.se.music.service.MusicPlayer
import com.se.music.utils.HandlerUtil
import com.se.music.utils.ImageUtils
import java.util.*

/**
 * Created by gaojin on 2017/12/14.
 */
class SongListAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context = context
    private var mList: List<MusicEntity>? = null
    private var mHandler: Handler? = null
    private var isLight = true

    constructor(context: Context, isLight: Boolean) : this(context) {
        mHandler = HandlerUtil.instance
        this.isLight = isLight
    }

    fun updateList(list: List<MusicEntity>) {
        this.mList = list
        notifyDataSetChanged()
    }

    init {
        mHandler = HandlerUtil.instance
    }

    override fun getItemCount(): Int {
        return if (mList == null) 0 else mList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as RecommendListHolder).onBind(mList!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return RecommendListHolder(LayoutInflater.from(mContext).inflate(R.layout.hot_list_item, parent, false))
    }

    internal inner class RecommendListHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var mImgSong: SimpleDraweeView = itemView.findViewById(R.id.hot_img_singer)
        var mTitle: TextView = itemView.findViewById(R.id.hot_singer_list_title)
        var mInfo: TextView = itemView.findViewById(R.id.hot_singer_list_info)
        var relativeLayout: RelativeLayout = itemView.findViewById(R.id.rl_hot_singer_item)
        var mLine: View = itemView.findViewById(R.id.line)

        fun more() {
        }

        init {
            itemView.setOnClickListener(this)
            itemView.findViewById<View>(R.id.three_more).setOnClickListener(this)
        }

        fun onBind(musicEntity: MusicEntity) {
            if (isLight) {
                mTitle.setTextColor(Color.WHITE)
                mLine.visibility = View.GONE
            } else {
                mTitle.setTextColor(Color.BLACK)
                relativeLayout.setBackgroundColor(Color.WHITE)
                mLine.visibility = View.VISIBLE
            }
            mTitle.text = musicEntity.musicName
            mInfo.text = musicEntity.artist
            ImageUtils.setImageSource(mContext, mImgSong, musicEntity)
        }

        override fun onClick(view: View) {
            if (view.id == R.id.three_more) {
                more()
            } else {
                mHandler!!.postDelayed(Runnable {
                    val infos = HashMap<Long, MusicEntity>()
                    val len = mList!!.size
                    val list = LongArray(len)
                    for (i in 0 until len) {
                        val info = mList!![i]
                        list[i] = info.songId
                        infos.put(list[i], info)
                    }
                    if (adapterPosition >= 0) {
                        MusicPlayer.playAll(infos, list, adapterPosition, false)
                    }
                }, 70)
            }
        }
    }
}