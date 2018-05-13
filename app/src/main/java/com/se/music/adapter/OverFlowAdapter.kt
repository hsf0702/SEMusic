package com.se.music.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.se.music.R
import com.se.music.entity.OverFlowItem

/**
 * Created by gaojin on 2017/12/13.
 */
class OverFlowAdapter(context: Context, list: List<OverFlowItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener!!.onItemClick(v!!, v.tag as String)
        }
    }

    private val mList: List<OverFlowItem> = list
    private val mContext: Context = context
    private var mOnItemClickListener: OnRecyclerViewItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.music_flow_layout, parent, false)
        val vh = ListItemViewHolder(view)
        //将创建的View注册点击事件
        view.setOnClickListener(this)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val mInfo = mList[position]
        (holder as ListItemViewHolder).icon.setImageResource(mInfo.avatar)
        holder.title.text = mInfo.title
        //设置tag
        holder.itemView.tag = position.toString() + ""
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.mOnItemClickListener = listener
    }

    inner class ListItemViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var icon: ImageView = view.findViewById(R.id.pop_list_view)
        var title: TextView = view.findViewById(R.id.pop_list_item)
    }

    //定义接口
    interface OnRecyclerViewItemClickListener {
        fun onItemClick(view: View, data: String)
    }
}