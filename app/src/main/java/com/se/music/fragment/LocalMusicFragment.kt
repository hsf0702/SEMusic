package com.se.music.fragment


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.adapter.LocalFragmentAdapter
import com.se.music.pastmusic.R
import com.se.music.utils.MConstants
import com.se.music.utils.MusicUtils
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [KtUiLocalMusicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocalMusicFragment : KtBaseFragment() {

    internal var mAdapter: LocalFragmentAdapter? = null
    private var mTabNames: List<String>? = null
    private var position: Int = 0
    private var mTabLayout: TabLayout? = null
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = arguments!!.getInt(POSITION)
        }
        val localCount = MusicUtils.queryMusic(context!!, MConstants.START_FROM_LOCAL).size
        val singerCount = MusicUtils.queryArtist(context!!).size
        val albumCount = MusicUtils.queryAlbums(context!!).size
        val folderCount = MusicUtils.queryFolder(context!!).size

        mTabNames = listOf(context!!.getString(R.string.local_music_song, localCount)
                , context!!.getString(R.string.local_music_singer, singerCount)
                , context!!.getString(R.string.local_music_album, albumCount)
                , context!!.getString(R.string.local_music_folder, folderCount))
    }

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val content = LayoutInflater.from(context).inflate(R.layout.fragment_local_music_kotlin, container, false)
        mTabLayout = content.findViewById(R.id.local_tab_layout)
        mViewPager = content.findViewById(R.id.local_view_pager)
        return content
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(context!!.getString(R.string.local_music_title))
        mAdapter = LocalFragmentAdapter(fm, mTabNames!!)
        mViewPager!!.adapter = mAdapter
        mViewPager!!.currentItem = position
        mTabLayout!!.setupWithViewPager(mViewPager)
        mAdapter!!.notifyDataSetChanged()
    }

    companion object {
        private val POSITION = "POSITION"

        fun newInstance(position: Int): LocalMusicFragment {
            val fragment = LocalMusicFragment()
            val args = Bundle()
            args.putInt(POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

}
