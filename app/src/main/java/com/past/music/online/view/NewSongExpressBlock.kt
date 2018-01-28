package com.past.music.online.view

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
import com.past.music.online.model.ExpressInfoModel
import com.past.music.pastmusic.R
import com.past.music.retrofit.MusicRetrofit
import com.past.music.retrofit.callback.CallLoaderCallbacks
import com.past.music.utils.IdUtils
import com.past.music.widget.GridItemDecoration
import com.past.music.widget.RecommendItemView
import retrofit2.Call

/**
 * Created by gaojin on 2018/1/6.
 */
class NewSongExpressBlock : LinearLayout, OnLineRefreshListener {

    override fun refresh(fragmentManager: FragmentManager?, loaderManager: LoaderManager) {
        loaderManager.initLoader(IdUtils.GET_EXPRESS_SONG, null, buildExpressInfoCallBack())
    }

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
        View.inflate(context, R.layout.online_new_song_express_block, this)
        initView()
    }

    private fun initView() {
        iconEnter = findViewById(R.id.express_icon_enter)
        recommendRecycleView = findViewById(R.id.express_recycle_view)
        recommendRecycleView!!.layoutManager = GridLayoutManager(context, 3)

        val mDividerItemDecoration = GridItemDecoration(context, LinearLayoutManager.HORIZONTAL, 3)
        mDividerItemDecoration.setDrawable(context.resources.getDrawable(R.drawable.transparent_divider))
        recommendRecycleView!!.addItemDecoration(mDividerItemDecoration)
        recommendRecycleView!!.isNestedScrollingEnabled = false
    }

    private fun buildExpressInfoCallBack(): CallLoaderCallbacks<ExpressInfoModel> {
        return object : CallLoaderCallbacks<ExpressInfoModel>(context!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<ExpressInfoModel> {
                return MusicRetrofit.getInstance().getExpressInfo()
            }

            override fun onSuccess(loader: Loader<*>, data: ExpressInfoModel) {
                recommendRecycleView!!.adapter = ExpressGridAdapter(context, data)
            }


            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("RecommendSongListBlock", throwable.toString())
            }

        }
    }

    class ExpressGridAdapter(context: Context, data: ExpressInfoModel) : RecyclerView.Adapter<RecommendViewHolder>() {
        private var context: Context = context
        private val ITEMCOUNT = 3
        private var mData: ExpressInfoModel = data
        private val imgUrl = "http://y.gtimg.cn/music/photo_new/T002R150x150M000%s.jpg?max_age=2592000"
        override fun onBindViewHolder(holderRecommend: RecommendViewHolder?, position: Int) {
            when (position) {
                0 -> {
                    val description = StringBuilder()
                    val songData = mData.new_album?.data?.song_list?.get(0)
                    songData?.singer?.forEach {
                        description.append(it.name)
                        description.append("&")
                    }
                    if (description.isNotEmpty()) {
                        description.replace(description.length - 1, description.length, "")
                        description.append(" - ")
                    }
                    description.append(songData?.name)

                    holderRecommend?.recommendView?.setImageView(String.format(imgUrl, songData?.album?.mid))
                            ?.setDescription(description.toString())
                }
                1 -> {
                    val albumData = mData.new_song?.data?.album_list?.get(0)
                    val description = StringBuilder()
                    description.append(albumData?.album?.name)
                    if (description.isNotEmpty()) {
                        description.append(" - ")
                    }
                    albumData?.author?.forEach {
                        description.append(it.name)
                        description.append("&")
                    }

                    if (description.isNotEmpty()) {
                        description.replace(description.length - 1, description.length, "")
                    }
                    holderRecommend?.recommendView?.setImageView(String.format(imgUrl, albumData?.album?.mid))
                            ?.setDescription(description.toString())
                }
                2 -> {
                    val albumData = mData.new_song?.data?.album_list?.get(1)
                    val description = StringBuilder()
                    description.append(albumData?.album?.name)
                    if (description.isNotEmpty()) {
                        description.append(" - ")
                    }
                    albumData?.author?.forEach {
                        description.append(it.name)
                        description.append("&")
                    }

                    if (description.isNotEmpty()) {
                        description.replace(description.length - 1, description.length, "")
                    }
                    holderRecommend?.recommendView?.setImageView(String.format(imgUrl, albumData?.album?.mid))
                            ?.setDescription(description.toString())
                }
            }
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