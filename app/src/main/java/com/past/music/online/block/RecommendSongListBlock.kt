package com.past.music.online.block

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.past.music.online.listener.OnLineRefreshListener
import com.past.music.online.model.RecommendListModel
import com.past.music.pastmusic.R
import com.past.music.retrofit.MusicRetrofit
import com.past.music.retrofit.callback.CallLoaderCallbacks
import com.past.music.utils.CollectionUtils
import com.past.music.utils.IdUtils
import com.past.music.widget.GridItemDecoration
import com.past.music.widget.RecommendItemView
import retrofit2.Call


/**
 * Created by gaojin on 2017/12/31.
 */
class RecommendSongListBlock : LinearLayout, OnLineRefreshListener {

    private var recommendRecycleView: RecyclerView? = null
    private var iconEnter: View? = null

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

        val mDividerItemDecoration = GridItemDecoration(context, LinearLayoutManager.HORIZONTAL, 3)
        mDividerItemDecoration.setDrawable(context.resources.getDrawable(R.drawable.transparent_divider))
        recommendRecycleView!!.addItemDecoration(mDividerItemDecoration)

    }

    override fun refresh(fragmentManager: FragmentManager?, loaderManager: LoaderManager) {
        loaderManager.initLoader(IdUtils.GET_RECOMMEND_LIST, null, buildRecommendListCallBack())
    }

    private fun buildRecommendListCallBack(): CallLoaderCallbacks<RecommendListModel> {
        return object : CallLoaderCallbacks<RecommendListModel>(context!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<RecommendListModel> {
                return MusicRetrofit.getInstance().getRecommendList()
            }

            override fun onSuccess(loader: Loader<*>, data: RecommendListModel) {
                recommendRecycleView!!.adapter = RecommendGridAdapter(context, data.recomPlaylist?.data?.v_hot)
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("RecommendSongListBlock", throwable.toString())
            }
        }
    }

    class RecommendGridAdapter(context: Context, v_hot: List<RecommendListModel.VHotBean>?) : RecyclerView.Adapter<RecommendViewHolder>() {
        private var context: Context = context
        private val ITEMCOUNT = 6
        private var hotList = v_hot
        override fun onBindViewHolder(holderRecommend: RecommendViewHolder?, position: Int) {
            val vHotBean = hotList?.get(position)
            holderRecommend?.recommendView?.setImageView(vHotBean?.cover!!)
                    ?.setDescription(vHotBean.title!!)
        }

        override fun getItemCount(): Int {
            return when {
                CollectionUtils.isEmpty(hotList) -> 0
                hotList!!.size < 6 -> hotList!!.size
                else -> ITEMCOUNT
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecommendViewHolder {
            return RecommendViewHolder(RecommendItemView(context))
        }
    }

    class RecommendViewHolder(itemView: RecommendItemView) : RecyclerView.ViewHolder(itemView) {
        var recommendView: RecommendItemView = itemView
    }
}