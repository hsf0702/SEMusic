package com.past.music.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.adapter.MusicContentAdapter
import com.past.music.api.SonglistBean
import com.past.music.pastmusic.R


class KtMusicFragment : Fragment() {

    private var mMusicList: RecyclerView? = null
    private var mRecommendList: List<SonglistBean>? = null
    private var mAdapter: MusicContentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = MusicContentAdapter(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMusicList = view.findViewById(R.id.music_recycle_view)
        mMusicList!!.layoutManager = LinearLayoutManager(activity)
        mMusicList!!.setHasFixedSize(true)
        mMusicList!!.adapter = mAdapter
    }

    companion object {
        fun newInstance(): KtMusicFragment {
            val fragment = KtMusicFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}