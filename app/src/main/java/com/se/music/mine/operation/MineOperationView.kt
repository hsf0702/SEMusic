package com.se.music.mine.operation

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
import com.se.music.activity.CollectedActivity
import com.se.music.base.BaseActivity
import com.se.music.base.BaseConfig
import com.se.music.base.kmvp.KBaseView
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.fragment.DownLoadFragment
import com.se.music.fragment.LocalMusicFragment
import com.se.music.fragment.RecentMusicFragment

/**
 * Author: gaojin
 * Time: 2018/5/7 下午4:22
 */
class MineOperationView(presenter: KMvpPresenter, viewId: Int, view: View) : KBaseView(presenter, viewId), View.OnClickListener {

    private var rootView: GridLayout? = null

    init {
        initView(view)
    }

    override fun createView(): View {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.mine_func_layout, null) as GridLayout
        return rootView!!
    }

    @Keep
    fun onDataChanged(cursor: Cursor) {

        val dataList = listOf(DataHolder(getContext()!!.resources.getString(R.string.mine_local_music)
                , cursor.count.toString(), R.drawable.item_music, R.id.local_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_down_music)
                , "2", R.drawable.item_download, R.id.download_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_recent_music)
                , "3", R.drawable.item_recent, R.id.recent_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_love_music)
                , "4", R.drawable.item_collect, R.id.love_music)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_love_singer)
                , "5", R.drawable.item_singer, R.id.love_singer)
                , DataHolder(getContext()!!.resources.getString(R.string.mine_buy_music)
                , "6", R.drawable.item_buy, R.id.buy_music))
        rootView!!.removeAllViews()
        addViewToGridLayout(rootView!!, dataList)
    }

    private fun addViewToGridLayout(container: ViewGroup, dataList: List<DataHolder>) {
        dataList.forEach {
            val itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_mine_item_view, container, false)
            val params: GridLayout.LayoutParams = itemView.layoutParams as GridLayout.LayoutParams
            params.width = BaseConfig.width / 3
            itemView.layoutParams = params

            val imageView: ImageView = itemView.findViewById(R.id.img_item)
            val itemName: TextView = itemView.findViewById(R.id.tv_item_name)
            val itemCount: TextView = itemView.findViewById(R.id.tv_item_count)
            itemName.text = it.itemName
            imageView.setBackgroundResource(it.drawablePic)
            itemCount.text = it.itemInfo

            itemView.setOnClickListener(this)
            itemView.id = it.id
            container.addView(itemView)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.local_music -> localMusic()
            R.id.download_music -> downloadMusic()
            R.id.recent_music -> recentMusic()
            R.id.love_music -> loveMusic()
            R.id.love_singer -> loveSinger()
            R.id.buy_music -> buyMusic()
        }
    }

    private fun localMusic() {
        val ft = (getActivity() as BaseActivity).supportFragmentManager.beginTransaction()
        ft.addToBackStack(null)
        ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        ft.add(R.id.content, LocalMusicFragment.newInstance(0)).commit()
    }

    private fun downloadMusic() {
        val ft = (getActivity() as BaseActivity).supportFragmentManager.beginTransaction()
        ft.addToBackStack(null)
        ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        ft.add(R.id.content, DownLoadFragment.newInstance()).commit()
    }

    private fun recentMusic() {
        val ft = (getActivity() as BaseActivity).supportFragmentManager.beginTransaction()
        ft.addToBackStack(null)
        ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        ft.add(R.id.content, RecentMusicFragment.newInstance()).commit()
    }

    private fun loveMusic() {
        val intent = Intent(getContext(), CollectedActivity::class.java)
        (getActivity() as BaseActivity).startActivityByX(intent, true)
    }

    private fun loveSinger() {
        val ft = (getActivity() as BaseActivity).supportFragmentManager.beginTransaction()
        ft.addToBackStack(null)
        ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        ft.add(R.id.content, LocalMusicFragment.newInstance(0)).commit()
    }

    private fun buyMusic() {
        Toast.makeText(getContext(), "测试", Toast.LENGTH_SHORT).show()
    }

    class DataHolder(var itemName: String
                     , var itemInfo: String
                     , var drawablePic: Int
                     , var id: Int)
}