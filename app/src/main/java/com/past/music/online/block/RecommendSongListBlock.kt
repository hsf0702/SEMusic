package com.past.music.online.block

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.past.music.pastmusic.R
import com.past.music.widget.RecommendDividerItemDecoration
import com.past.music.widget.RecommendItemView


/**
 * Created by gaojin on 2017/12/31.
 */
class RecommendSongListBlock : LinearLayout {

    private var recommendRecycleView: RecyclerView? = null
    private var iconEnter: View? = null
    private var mDividerItemDecoration: RecommendDividerItemDecoration? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.online_recommend_song_list_block, this)
        initView()
    }

    private fun initView() {
        iconEnter = findViewById(R.id.recommend_icon_enter)
        recommendRecycleView = findViewById(R.id.recommend_recycle_view)
        recommendRecycleView!!.layoutManager = GridLayoutManager(context, 3)
        recommendRecycleView!!.adapter = RecommendGridAdapter(context)

        mDividerItemDecoration = RecommendDividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        mDividerItemDecoration!!.setDrawable(context.resources.getDrawable(R.drawable.transparent_divider))

        recommendRecycleView!!.addItemDecoration(mDividerItemDecoration)

    }

    class RecommendGridAdapter(context: Context) : RecyclerView.Adapter<RecommendViewHolder>() {
        private var context: Context = context
        private val ITEMCOUNT = 6
        override fun onBindViewHolder(holderRecommend: RecommendViewHolder?, position: Int) {
            holderRecommend?.recommendView?.setImageView("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_26_300_201697348.jpg")
                    ?.setDescription("进图违反了开发进阶软工")
        }

        override fun getItemCount(): Int {
            return ITEMCOUNT
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecommendViewHolder {
            return RecommendViewHolder(RecommendItemView(context))
        }
    }

    class RecommendViewHolder(itemView: RecommendItemView) : RecyclerView.ViewHolder(itemView) {
        var recommendView: RecommendItemView = itemView
    }
}