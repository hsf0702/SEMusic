package com.past.music.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.past.music.database.provider.RecentStore
import com.past.music.dialog.SongOperationDialog
import com.past.music.entity.MusicEntity
import com.past.music.pastmusic.R
import com.past.music.service.MusicPlayer
import com.past.music.utils.HandlerUtil
import com.past.music.utils.MConstants
import com.past.music.utils.MusicUtils
import com.past.music.utils.recent.Song
import com.past.music.utils.recent.SongLoader
import com.past.music.utils.recent.TopTracksLoader
import java.util.*

/**
 * Created by gaojin on 2017/12/14.
 */
class RecentMusicFragment : KtBaseFragment() {
    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val content = LayoutInflater.from(context).inflate(R.layout.activity_recent_music, container, false)
        mRecyclerView = content.findViewById(R.id.recent_music_recycle)
        return content
    }

    internal var mRecyclerView: RecyclerView? = null

    private var mAdapter: Adapter? = null
    private var mList: List<Song>? = null
    private var recentStore: RecentStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recentStore = RecentStore.getInstance(context)
        val recentloader = TopTracksLoader(context, TopTracksLoader.QueryType.RecentSongs)
        val recentsongs = SongLoader.getSongsForCursor(TopTracksLoader.getCursor())
        val songCountInt = recentsongs.size
        mList = recentsongs
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LoadSongs().execute("")
        return super.onCreateView(inflater, container, savedInstanceState)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTitle(context!!.getString(R.string.recent_music_title))
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.setHasFixedSize(true)
    }

    //异步加载recyclerview界面
    private inner class LoadSongs : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            mAdapter = Adapter(mList)
            return "Executed"
        }

        override fun onPostExecute(result: String) {
            mRecyclerView!!.adapter = mAdapter
        }

        override fun onPreExecute() {

        }
    }

    inner class Adapter(private var mList: List<Song>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val handler: Handler? = null
        internal var songOperationDialog: SongOperationDialog? = null

        init {
            if (mList == null) {
                throw IllegalArgumentException("model Data must not be null")
            }
        }

        //更新adpter的数据
        fun updateDataSet(list: List<Song>) {
            this.mList = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == HEAD_LAYOUT) {
                CommonItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.common_item, parent, false))
            } else {
                ListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_musci_common_item, parent, false))
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is CommonItemViewHolder) {
            } else {
                (holder as ListItemViewHolder).onBindData(mList!![position - 1], position)
            }
        }

        override fun getItemCount(): Int {
            return if (null != mList) mList!!.size + 1 else 0
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) {
                HEAD_LAYOUT
            } else {
                CONTENT_LAYOUT
            }
        }

        internal inner class CommonItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

            private var textView: TextView? = null
            private var select: ImageView? = null

            init {
                textView = view.findViewById(R.id.play_all_number)
                select = view.findViewById(R.id.select)
                view.setOnClickListener(this)
            }


            override fun onClick(v: View) {
                HandlerUtil.getInstance(context).postDelayed({
                    val list = LongArray(mList!!.size)
                    val infos = HashMap<Long, MusicEntity>()
                    for (i in mList!!.indices) {
                        val info = MusicUtils.getMusicInfo(context!!, mList!![i].id)
                        list[i] = info!!.songId
                        info.islocal = true
                        info.albumData = MusicUtils.getAlbumArtUri(info.albumId.toLong()).toString() + ""
                        infos.put(list[i], info)
                    }
                    MusicPlayer.playAll(infos, list, 0, false)
                }, 70)
            }
        }

        internal inner class ListItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

            private var mMusicName: TextView? = null
            private var mMusicInfo: TextView? = null
            private var mViewPagerButton: ImageView? = null

            private fun setListButton() {
                val musicEntity = MusicUtils.getMusicInfo(context!!, mList!![adapterPosition - 1].id)
                songOperationDialog = SongOperationDialog(context, musicEntity, MConstants.MUSICOVERFLOW)
                songOperationDialog!!.show()
            }

            init {
                mMusicName = itemView.findViewById(R.id.music_name)
                mMusicInfo = itemView.findViewById(R.id.music_info)
                mViewPagerButton = itemView.findViewById(R.id.viewpager_list_button)
                mViewPagerButton!!.setOnClickListener { setListButton() }
                view.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                HandlerUtil.getInstance(context).postDelayed({
                    val list = LongArray(mList!!.size)
                    val infos = HashMap<Long, MusicEntity>()
                    for (i in mList!!.indices) {
                        val info = MusicUtils.getMusicInfo(context!!, mList!![i].id)
                        list[i] = info!!.songId
                        info.islocal = true
                        info.albumData = MusicUtils.getAlbumArtUri(info.albumId.toLong()).toString() + ""
                        infos.put(list[i], info)
                    }
                    if (adapterPosition > 0)
                        MusicPlayer.playAll(infos, list, adapterPosition - 1, false)
                }, 70)
            }

            fun onBindData(song: Song, position: Int) {
                mMusicName!!.text = song.title
                mMusicInfo!!.text = song.artistName
            }
        }
    }

    companion object {
        val HEAD_LAYOUT = 0X01
        val CONTENT_LAYOUT = 0X02

        fun newInstance(position: Int): RecentMusicFragment {
            val fragment = RecentMusicFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}