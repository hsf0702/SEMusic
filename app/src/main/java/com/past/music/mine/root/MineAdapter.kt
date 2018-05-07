package com.past.music.mine.root

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.past.music.GlideApp
import com.past.music.entity.SongListEntity
import com.past.music.pastmusic.R
import com.past.music.utils.CollectionUtils

/**
 * Author: gaojin
 * Time: 2018/5/4 下午10:50
 */
class MineAdapter constructor(private var context: Context, private val list: List<SongListEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER = 0X01
    private val LIST = 0X02
    var header: View? = null

    init {
        header = LayoutInflater.from(context).inflate(R.layout.mine_header_view_layout, null)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER) {
            HeadItemHolder(header!!)
        } else {
            ListItemHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.mine_list_view_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ListItemHolder) {
            val songListEntity: SongListEntity = list[position + 1]
            GlideApp.with(context)
                    .load(songListEntity.list_pic)
                    .into(holder.imageView!!)

            holder.songListTitle!!.text = songListEntity.name
            holder.songListInfo!!.text = songListEntity.info
            holder.songListItem!!.setOnClickListener { Toast.makeText(context, "gj_jump", Toast.LENGTH_SHORT).show() }
        }
    }

    override fun getItemCount(): Int {
        return if (CollectionUtils.isEmpty(list)) {
            1
        } else {
            list.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER
        } else {
            LIST
        }
    }

    inner class HeadItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null
        var songListTitle: TextView? = null
        var songListInfo: TextView? = null
        var songListItem: View? = null

        init {
            imageView = itemView.findViewById(R.id.song_list_image)
            songListTitle = itemView.findViewById(R.id.play_list_title)
            songListInfo = itemView.findViewById(R.id.play_list_info)
            songListItem = itemView.findViewById(R.id.song_list_item)
        }
    }
}