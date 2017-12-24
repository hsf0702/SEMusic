package com.past.music.online

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.adapter.MusicContentAdapter
import com.past.music.online.model.HallModel
import com.past.music.pastmusic.R
import com.past.music.retrofit.MusicRetrofit
import com.past.music.retrofit.callback.CallLoaderCallbacks
import com.past.music.utils.IdUtils
import retrofit2.Call


class MusicFragment : Fragment() {

    private var mMusicList: RecyclerView? = null
    private var mAdapter: MusicContentAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mMusicList = view!!.findViewById(R.id.music_recycle_view)
        mAdapter = MusicContentAdapter(context)
        mMusicList!!.layoutManager = LinearLayoutManager(context)
        mMusicList!!.setHasFixedSize(true)
        mMusicList!!.adapter = mAdapter

        loaderManager.initLoader(IdUtils.GET_MUSIC_HALL, null, buildHallCallBack())

    }


    /**
     * 请求轮播图片
     */
    private fun buildHallCallBack(): CallLoaderCallbacks<HallModel> {
        return object : CallLoaderCallbacks<HallModel>(context) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<HallModel> {
                return MusicRetrofit.getInstance().getMusicHall()
            }

            override fun onSuccess(loader: Loader<*>, data: HallModel) {
                mAdapter!!.updateBanner(data.data?.slider)
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("MusicFragment", throwable.toString())
            }
        }
    }


    companion object {
        fun newInstance(): MusicFragment {
            return MusicFragment()
        }
    }
}