package com.past.music.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.MyApplication
import com.past.music.adapter.MyContentAdapter
import com.past.music.event.CreateSongListEvent
import com.past.music.pastmusic.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Creator：gaojin
 * date：2017/11/3 下午9:38
 */
class MineFragment : Fragment() {

    companion object {

        fun newInstance(): MineFragment {
            val fragment = MineFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    var mMusicList: RecyclerView? = null
    private var mAdapter: MyContentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_mine, container, false)
        mAdapter = MyContentAdapter(activity)
        mMusicList = view.findViewById(R.id.recycle_layout)
        mMusicList!!.layoutManager = LinearLayoutManager(activity)
        mMusicList!!.setHasFixedSize(true)
        mMusicList!!.adapter = mAdapter
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CreateSongListEvent) {
        val mList = MyApplication.songListDBService.query()
        mAdapter!!.setSongList(mList)
    }

}