package com.past.music.fragment


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.fragment.adapter.LocalFragmentAdapter
import com.past.music.pastmusic.R
import com.past.music.utils.MConstants
import com.past.music.utils.MusicUtils
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [KT_LocalMusicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KT_LocalMusicFragment : KT_BaseFragment() {

    internal var mAdapter: LocalFragmentAdapter? = null
    private val mTabNames = ArrayList<String>()
    private var position: Int = 0
    private var mTabLayout: TabLayout? = null
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = arguments.getInt(POSITION)
        }
        val localCount = MusicUtils.queryMusic(context, MConstants.START_FROM_LOCAL).size
        val singerCount = MusicUtils.queryArtist(context).size
        val albumCount = MusicUtils.queryAlbums(context).size
        val folderCount = MusicUtils.queryFolder(context).size
        mTabNames.add("歌曲 " + localCount)
        mTabNames.add("歌手 " + singerCount)
        mTabNames.add("专辑 " + albumCount)
        mTabNames.add("文件夹 " + folderCount)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        mTabLayout = view.findViewById(R.id.local_tab_layout)
        mViewPager = view.findViewById(R.id.local_view_pager)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("本地音乐")
        mTabLayout!!.tabGravity = TabLayout.GRAVITY_CENTER
        val fm = activity.supportFragmentManager
        mAdapter = LocalFragmentAdapter(fm, mTabNames)
        mViewPager!!.adapter = mAdapter
        mViewPager!!.currentItem = position
        mTabLayout!!.setupWithViewPager(mViewPager)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_local_music_kotlin
    }

    companion object {
        private val POSITION = "POSITION"

        fun newInstance(position: Int): KT_LocalMusicFragment {
            val fragment = KT_LocalMusicFragment()
            val args = Bundle()
            args.putInt(POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
