package com.se.music.mine.root

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.se.music.GlideApp
import com.se.music.R
import com.se.music.provider.MetaData

/**
 * Author: gaojin
 * Time: 2018/5/4 下午10:50
 */
class MineAdapter constructor(private var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER = 0X01
    private val LIST = 0X02
    var header: View? = null
    var cursor: Cursor? = null
    var currentCursor = true

    init {
        header = LayoutInflater.from(context).inflate(R.layout.mine_header_view_layout, null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER) {
            HeadItemHolder(header!!)
        } else {
            ListItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.mine_list_view_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListItemHolder) {
            if (position == 1) {
                cursor!!.moveToFirst()
                currentCursor = true
            }
            if (!currentCursor) {
                return
            }
            GlideApp.with(context)
                    .load(cursor!!.getString(MetaData.SongList.PIC_INDEX))
                    .placeholder(R.drawable.placeholder_disk)
                    .into(holder.imageView!!)

            holder.songListTitle!!.text = cursor!!.getString(MetaData.SongList.NAME_INDEX)
            holder.songListInfo!!.text = cursor!!.getString(MetaData.SongList.INFO_INDEX)
            holder.songListItem!!.setOnClickListener { Toast.makeText(context, "gj_jump", Toast.LENGTH_SHORT).show() }
            currentCursor = cursor!!.moveToNext()
        }
    }

    override fun getItemCount(): Int {
        return if (cursor == null) {
            1
        } else {
            cursor!!.count + 1
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
            songListTitle = itemView.findViewById(R.id.song_list_name)
            songListInfo = itemView.findViewById(R.id.song_list_content)
            songListItem = itemView.findViewById(R.id.song_list_item)
        }
    }
}