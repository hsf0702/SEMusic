package com.past.music.online.subfragment

import android.os.Bundle
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.past.music.Config.BaseConfig
import com.past.music.GlideApp
import com.past.music.fragment.UiBaseFragment
import com.past.music.online.model.SingerModel
import com.past.music.pastmusic.R
import com.past.music.retrofit.MusicRetrofit
import com.past.music.retrofit.callback.CallLoaderCallbacks
import com.past.music.utils.CollectionUtils
import com.past.music.utils.IdUtils
import com.past.music.widget.CircleImageView
import retrofit2.Call

/**
 * Created by gaojin on 2018/1/3.
 */
class OnLineSingerFragment : UiBaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_online_singer
    }

    private var recycleView: RecyclerView? = null
    private var singerList: List<SingerModel.Data.Singer>? = null
    private var singerAdapter: SingerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTitle("歌手")
        recycleView = view.findViewById(R.id.online_singer)
        recycleView!!.layoutManager = LinearLayoutManager(activity)
        recycleView!!.setHasFixedSize(true)
        singerAdapter = SingerAdapter()
        recycleView!!.adapter = singerAdapter

        loaderManager.initLoader(IdUtils.GET_SINGER_LIST, null, buildSingerCallback())
    }


    private fun buildSingerCallback(): CallLoaderCallbacks<SingerModel> {
        return object : CallLoaderCallbacks<SingerModel>(context!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<SingerModel> {
                return MusicRetrofit.getInstance().getSinger(100, 1)
            }

            override fun onSuccess(loader: Loader<*>, data: SingerModel) {
                singerList = data.data?.list
                singerAdapter!!.notifyDataSetChanged()
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("OnLineSingerFragment", throwable.toString())
            }
        }
    }

    inner class SingerAdapter : RecyclerView.Adapter<ItemViewHolder>() {
        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            holder?.singerName?.text = singerList!![position].Fsinger_name
            GlideApp.with(context!!).load(String.format(BaseConfig.picBaseUrl, singerList!![position].Fsinger_mid)).into(holder?.singerAvatar!!)
        }

        override fun getItemCount(): Int {
            return if (CollectionUtils.isEmpty(singerList)) {
                0
            } else {
                singerList!!.size
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
            return ItemViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.online_singer_item, parent, false))
        }
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var singerAvatar: CircleImageView? = null
        var singerName: TextView? = null
        var iconEnter: ImageView? = null

        init {
            singerAvatar = itemView.findViewById(R.id.singer_avatar)
            singerName = itemView.findViewById(R.id.singer_name)
            iconEnter = itemView.findViewById(R.id.icon_enter)
        }
    }


    companion object {
        fun newInstance(): OnLineSingerFragment {
            return OnLineSingerFragment()
        }
    }
}