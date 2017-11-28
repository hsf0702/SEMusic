package com.past.music.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.drawee.view.SimpleDraweeView
import com.past.music.MyApplication
import com.past.music.activity.CreateSongListActivity
import com.past.music.activity.SongListInfoActivity
import com.past.music.entity.SongListEntity
import com.past.music.pastmusic.R

/**
 * Created by gaojin on 2017/11/20.
 */
class KtCreateSongListAdapter constructor(context: Context) : RecyclerView.Adapter<BaseViewHolder<SongListEntity>>() {

    private val HEADLAYOUT = 0x01
    private val SONGLISTLAYOUT = 0x02
    private val EMPTYLAYOUT = 0x03

    private var mList = MyApplication.songListDBService.query()

    override fun onBindViewHolder(holder: BaseViewHolder<SongListEntity>?, position: Int) {
        (holder as? SongListLayout)?.onBind(mList[position - 1], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<SongListEntity> {
        return when (viewType) {
            HEADLAYOUT -> HeadLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.create_song_head_layout, parent, false))
            SONGLISTLAYOUT -> SongListLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.mine_favor_item_layout, parent, false))
            else -> EmptyLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.empty_layout, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (mList.size == 0) 2 else mList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADLAYOUT
        } else {
            if (mList.size == 0) {
                EMPTYLAYOUT
            } else {
                SONGLISTLAYOUT
            }
        }
    }

    fun update() {
        mList = MyApplication.songListDBService.query()
        notifyDataSetChanged()
    }


    internal inner class HeadLayout(itemView: View) : BaseViewHolder<SongListEntity>(itemView) {

        var mCreateSongList: ImageView? = null
        var textView: TextView? = null

        init {
            mCreateSongList = itemView.findViewById(R.id.create_song_list)
            textView = itemView.findViewById(R.id.tv_manage)
        }

        override fun onBind(t: SongListEntity, position: Int) {
        }
    }


    internal inner class SongListLayout(itemView: View) : BaseViewHolder<SongListEntity>(itemView) {
        override fun onBind(t: SongListEntity, position: Int) {
        }
    }

    internal inner class EmptyLayout(itemView: View) : BaseViewHolder<SongListEntity>(itemView) {
        override fun onBind(t: SongListEntity, position: Int) {
        }
    }
}