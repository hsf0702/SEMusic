package com.se.music.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.se.music.activity.SelectSongListActivity
import com.se.music.database.provider.MusicInfoDBService
import com.se.music.database.provider.SongListDBService
import com.se.music.entity.MusicEntity
import com.se.music.entity.SongListEntity
import com.se.music.pastmusic.R
import org.greenrobot.eventbus.EventBus

/**
 * Created by gaojin on 2017/12/14.
 */
class SelectSongListAdapter(context: Context, musicEntity: MusicEntity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mList: List<SongListEntity> = SongListDBService.instance.query()
    private val mContext: Context = context
    private val musicEntity: MusicEntity = musicEntity

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return SongListLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.mine_favor_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as SongListLayout).onBind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    internal inner class SongListLayout(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var songListPic: SimpleDraweeView = itemView.findViewById(R.id.img_album)
        private var mPlayListTitle: TextView = itemView.findViewById(R.id.play_list_title)
        private var mPlayListInfo: TextView = itemView.findViewById(R.id.play_list_info)
        private var mImgDown: ImageView = itemView.findViewById(R.id.img_down)
        var mItemLayout: RelativeLayout = itemView.findViewById(R.id.rl_favor_item)

        private fun favorItem() {
            MusicInfoDBService.instance.insert(musicEntity, mList[adapterPosition].id!!)
            (mContext as SelectSongListActivity).finish()
        }

        init {
            mItemLayout.setOnClickListener { favorItem() }
        }

        fun onBind(songListEntity: SongListEntity) {
            songListPic.setImageURI(songListEntity.listPic)
            mPlayListTitle.text = songListEntity.name
            mPlayListInfo.text = MusicInfoDBService.instance.getLocalCount(songListEntity.id!!)
        }
    }
}