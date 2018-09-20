package com.se.music.mine.operation

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.support.annotation.Keep
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.se.music.R
import com.se.music.base.BaseConfig
import com.se.music.base.mvp.BaseView
import com.se.music.base.mvp.MvpPresenter
import com.se.music.main.MainFragment
import com.se.music.subpage.mine.DownLoadFragment
import com.se.music.subpage.mine.RecentMusicFragment
import com.se.music.subpage.mine.local.LocalMusicContainerFragment
import com.se.music.subpage.mine.love.CollectedActivity
import com.se.music.utils.startFragment

/**
 * Author: gaojin
 * Time: 2018/5/7 下午4:22
 */
class MineOperationView(presenter: MvpPresenter, viewId: Int, view: View) : BaseView(presenter, viewId), View.OnClickListener {

    private lateinit var rootView: GridLayout
    private val preFragmentTag = MainFragment.TAG

    init {
        initView(view)
    }

    @SuppressLint("InflateParams")
    override fun createView(): View {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.mine_func_layout, null) as GridLayout
        addViewToGridLayout(rootView)
        return rootView
    }

    @Keep
    fun onDataChanged(cursor: Cursor) {
        val dataList = listOf(DataHolder(getContext()!!.resources.getString(R.string.mine_local_music)
                , cursor.count.toString(), R.drawable.ic_my_music_local_song, R.id.local_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_down_music)
                , "2", R.drawable.ic_my_music_download_song, R.id.download_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_recent_music)
                , "3", R.drawable.ic_my_music_recent_playlist, R.id.recent_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_love_music)
                , "4", R.drawable.ic_my_music_my_favorite, R.id.love_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_buy_music)
                , "5", R.drawable.ic_my_music_paid_songs, R.id.buy_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_running_radio)
                , null, R.drawable.ic_my_music_running_radio, R.id.running_radio))
        bindDataToView(dataList)
    }

    private fun addViewToGridLayout(container: ViewGroup) {
        for (i in 0..5) {
            val itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_mine_item_view, container, false)
            val params: GridLayout.LayoutParams = itemView.layoutParams as GridLayout.LayoutParams
            params.width = BaseConfig.width / 3
            itemView.layoutParams = params
            container.addView(itemView)
        }
    }

    private fun bindDataToView(list: List<DataHolder>) {
        list.forEachIndexed { index, dataHolder ->
            val itemView = rootView.getChildAt(index)
            val imageView: ImageView = itemView.findViewById(R.id.img_item)
            val itemName: TextView = itemView.findViewById(R.id.tv_item_name)
            val itemCount: TextView = itemView.findViewById(R.id.tv_item_count)
            itemView.id = dataHolder.id
            itemView.setOnClickListener(this)
            itemName.text = dataHolder.itemName
            imageView.setImageDrawable(getActivity()!!.getDrawable(dataHolder.drawablePic))
            itemCount.text = dataHolder.itemInfo
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.local_music -> localMusic()
            R.id.download_music -> downloadMusic()
            R.id.recent_music -> recentMusic()
            R.id.love_music -> loveMusic()
            R.id.running_radio -> runningRadio()
            R.id.buy_music -> buyMusic()
        }
    }

    private fun localMusic() {
        startFragment(getPage(), LocalMusicContainerFragment.newInstance(0), preFragmentTag)
    }

    private fun downloadMusic() {
        startFragment(getPage(), DownLoadFragment.newInstance(), preFragmentTag)
    }

    private fun recentMusic() {
        startFragment(getPage(), RecentMusicFragment.newInstance(), preFragmentTag)
    }

    private fun loveMusic() {
        val intent = Intent(getContext(), CollectedActivity::class.java)
        getActivity()?.startActivity(intent)
    }

    private fun runningRadio() {
        startFragment(getPage(), LocalMusicContainerFragment.newInstance(0), preFragmentTag)
    }

    private fun buyMusic() {
        Toast.makeText(getContext(), "测试", Toast.LENGTH_SHORT).show()
    }

    class DataHolder(var itemName: String
                     , var itemInfo: String?
                     , var drawablePic: Int
                     , var id: Int)
}