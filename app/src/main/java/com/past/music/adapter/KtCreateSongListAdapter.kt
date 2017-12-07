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
import com.facebook.drawee.view.SimpleDraweeView
import com.past.music.MyApplication
import com.past.music.activity.CreateSongListActivity
import com.past.music.activity.SongListInfoActivity.startActivity
import com.past.music.entity.SongListEntity
import com.past.music.pastmusic.R

/**
 * Created by gaojin on 2017/12/7.
 */
class KtCreateSongListAdapter constructor(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val HEADLAYOUT = 0x01
    val SONGLISTLAYOUT = 0x02
    val EMPTYLAYOUT = 0x03

    private var mList: List<SongListEntity>? = null
    private val mContext: Context = context

    init {
        mList = MyApplication.songListDBService.query()
    }

    fun update() {
        mList = MyApplication.songListDBService.query()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as? SongListLayout)?.onBind(mList!![position - 1])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADLAYOUT -> HeadLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.create_song_head_layout, parent, false))
            SONGLISTLAYOUT -> SongListLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.mine_favor_item_layout, parent, false))
            else -> EmptyLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.empty_layout, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (mList!!.isEmpty()) 2 else mList!!.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADLAYOUT
        } else {
            if (mList!!.isEmpty()) {
                EMPTYLAYOUT
            } else {
                SONGLISTLAYOUT
            }
        }
    }

    internal inner class HeadLayout(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var mCreateSongList: ImageView? = null
        var manageView: TextView? = null

        private fun create() {
            val intent = Intent(mContext, CreateSongListActivity::class.java)
            mContext.startActivity(intent)
            (mContext as Activity).overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out)
        }

        private fun manage() {
            Toast.makeText(mContext, "管理歌单", Toast.LENGTH_SHORT).show()
        }

        init {
            mCreateSongList = itemView.findViewById(R.id.create_song_list)
            manageView = itemView.findViewById(R.id.tv_manage)

            mCreateSongList?.setOnClickListener(this)
            manageView?.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.create_song_list -> create()
                else -> manage()
            }
        }

    }

    internal inner class SongListLayout(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var songListPic: SimpleDraweeView? = null
        var mPlayListTitle: TextView? = null
        var mPlayListInfo: TextView? = null
        var mImgDown: ImageView? = null
        var mItemLayout: RelativeLayout? = null

        private fun favorItem() {
            startActivity(mContext, mList!![adapterPosition - 1].getId(), mList!![adapterPosition - 1].getName())
        }

        init {
            songListPic = itemView.findViewById(R.id.img_album)
            mPlayListTitle = itemView.findViewById(R.id.play_list_title)
            mPlayListInfo = itemView.findViewById(R.id.play_list_info)
            mImgDown = itemView.findViewById(R.id.img_down)
            mItemLayout = itemView.findViewById(R.id.rl_favor_item)

            mItemLayout?.setOnClickListener(View.OnClickListener { favorItem() })
        }

        fun onBind(songListEntity: SongListEntity) {
            songListPic!!.setImageURI(songListEntity.getList_pic())
            mPlayListTitle!!.text = songListEntity.getName()
            mPlayListInfo!!.text = MyApplication.musicInfoDBService.getLocalCount(songListEntity.getId())
        }
    }

    internal inner class EmptyLayout(itemView: View) : RecyclerView.ViewHolder(itemView)

}