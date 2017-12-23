package com.past.music.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.facebook.drawee.view.SimpleDraweeView
import com.neu.gaojin.MyOkHttpClient
import com.neu.gaojin.response.BaseCallback
import com.past.music.MyApplication
import com.past.music.activity.BaseActivity
import com.past.music.activity.CollectedActivity
import com.past.music.activity.SongListActivity
import com.past.music.activity.SongListInfoActivity
import com.past.music.api.AvatarRequest
import com.past.music.api.AvatarResponse
import com.past.music.entity.SongListEntity
import com.past.music.fragment.DownLoadFragment
import com.past.music.fragment.LocalMusicFragment
import com.past.music.fragment.RecentMusicFragment
import com.past.music.pastmusic.R
import com.past.music.utils.MConstants
import com.past.music.utils.MusicUtils
import com.past.music.widget.CircleImageView
import com.past.music.widget.MineItemView

/**
 * Created by gaojin on 2017/12/7.
 */
class MyContentAdapter constructor(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEAD_LAYOUT = 0X01
    private val FUNC_LAYOUT = 0X02
    private val FAVOR_TITLE_LAYOUT = 0X03
    private val FAVOR_ITEM_LAYOUT = 0X04
    private val MANAGE_SONG_LIST_LAYOUT = 0X05

    private val START = 3
    private var mList: List<SongListEntity>? = null
    private var mContext: Context = context

    init {
        mList = MyApplication.songListDBService.query()
    }

    fun setSongList(listItem: List<SongListEntity>) {
        this.mList = listItem
        notifyItemRangeChanged(START, listItem.size + 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEAD_LAYOUT -> HeadLayoutHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.mine_head_layout, parent, false))
            FUNC_LAYOUT -> FuncLayoutHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.mine_func_layout, parent, false))
            FAVOR_TITLE_LAYOUT -> FavorTitleLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.mine_favor_title_layout, parent, false))
            MANAGE_SONG_LIST_LAYOUT -> ManageSongList(LayoutInflater.from(parent!!.context).inflate(R.layout.manage_songlist_layout, parent, false))
            else -> FavorItemLayout(LayoutInflater.from(parent!!.context).inflate(R.layout.mine_favor_item_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is HeadLayoutHolder) {

        } else (holder as? FuncLayoutHolder)?.onBind() ?: if (holder is FavorTitleLayout) {

        } else (holder as? FavorItemLayout)?.onBind(mList!![position - 3])
    }

    override fun getItemCount(): Int {
        return if (mList == null) {
            4
        } else {
            mList!!.size + 4
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEAD_LAYOUT
        } else if (position == 1) {
            FUNC_LAYOUT
        } else if (position == 2) {
            FAVOR_TITLE_LAYOUT
        } else {
            if (position == mList!!.size + 3) {
                MANAGE_SONG_LIST_LAYOUT
            } else {
                FAVOR_ITEM_LAYOUT
            }
        }
    }

    internal inner class HeadLayoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mHeadAvatar: CircleImageView? = null
        private var mListenTime: TextView? = null
        private var mListenVip: TextView? = null
        private var mUserName: TextView? = null

        init {
            mHeadAvatar = itemView.findViewById(R.id.mine_head_avatar)
            mListenTime = itemView.findViewById(R.id.mine_head_listen_time)
            mListenVip = itemView.findViewById(R.id.mine_head_listen_vip)
            mUserName = itemView.findViewById(R.id.mine_head_user_name)
        }
    }

    internal inner class FuncLayoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var mLocalMusic: MineItemView? = null
        private var mDownMusic: MineItemView? = null
        private var mRecentMusic: MineItemView? = null
        private var mLoveMusic: MineItemView? = null
        private var mLoveSinger: MineItemView? = null
        private var mBuyMusic: MineItemView? = null

        init {
            mLocalMusic = itemView.findViewById(R.id.local_music)
            mDownMusic = itemView.findViewById(R.id.download_music)
            mRecentMusic = itemView.findViewById(R.id.recent_music)
            mLoveMusic = itemView.findViewById(R.id.love_music)
            mLoveSinger = itemView.findViewById(R.id.love_singer)
            mBuyMusic = itemView.findViewById(R.id.buy_music)

            mLocalMusic?.setOnClickListener(this)
            mDownMusic?.setOnClickListener(this)
            mRecentMusic?.setOnClickListener(this)
            mLoveMusic?.setOnClickListener(this)
            mLoveSinger?.setOnClickListener(this)
            mBuyMusic?.setOnClickListener(this)
        }

        fun onBind() {
            //            int singerCount = MusicUtils.queryArtist(mContext).size();
            //            int albumCount = MusicUtils.queryAlbums(mContext).size();
            //            int folderCount = MusicUtils.queryFolder(mContext).size();
            mLocalMusic!!.setmItemCount(MusicUtils.queryMusic(mContext, MConstants.START_FROM_LOCAL).size.toString())

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
            val ft = (mContext as BaseActivity).supportFragmentManager.beginTransaction()
            ft.addToBackStack(null)
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            ft.add(R.id.content, LocalMusicFragment.newInstance(0)).commit()
        }

        private fun downloadMusic() {
            val ft = (mContext as BaseActivity).supportFragmentManager.beginTransaction()
            ft.addToBackStack(null)
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            ft.add(R.id.content, DownLoadFragment.newInstance()).commit()
        }

        private fun recentMusic() {
            val ft = (mContext as BaseActivity).supportFragmentManager.beginTransaction()
            ft.addToBackStack(null)
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            ft.add(R.id.content, RecentMusicFragment.newInstance(0)).commit()
        }

        private fun loveMusic() {
            val intent = Intent(mContext, CollectedActivity::class.java)
            (mContext as BaseActivity).startActivityByX(intent, true)
        }

        private fun loveSinger() {
            val ft = (mContext as BaseActivity).supportFragmentManager.beginTransaction()
            ft.addToBackStack("KtUiLocalMusicFragment")
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            ft.add(R.id.content, LocalMusicFragment.newInstance(0)).commit()
        }

        private fun buyMusic() {
            Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show()
        }

    }

    internal inner class FavorTitleLayout(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mTitleLayout: RelativeLayout? = null
        private var mPlayListCount: TextView? = null

        init {
            mTitleLayout = itemView.findViewById(R.id.rl_favor_title)
            mPlayListCount = itemView.findViewById(R.id.playlist_count)
            mTitleLayout?.setOnClickListener(View.OnClickListener { favorTltle() })
        }


        private fun favorTltle() {
            val intent = Intent(mContext, SongListActivity::class.java)
            (mContext as BaseActivity).startActivityByX(intent, false)
        }
    }

    internal inner class ManageSongList(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val intent = Intent(mContext, SongListActivity::class.java)
            (mContext as BaseActivity).startActivityByX(intent, false)
        }
    }


    internal inner class FavorItemLayout(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var songListPic: SimpleDraweeView? = null
        private var mPlayListTitle: TextView? = null
        private var mPlayListInfo: TextView? = null
        private var mImgDown: ImageView? = null
        private var mItemLayout: RelativeLayout? = null

        private fun favorItem() {
            SongListInfoActivity.startActivity(mContext, mList!![adapterPosition - 3].getId(), mList!![adapterPosition - 3].getName())
        }

        init {
            songListPic = itemView.findViewById(R.id.img_album)
            mPlayListTitle = itemView.findViewById(R.id.play_list_title)
            mPlayListInfo = itemView.findViewById(R.id.play_list_info)
            mImgDown = itemView.findViewById(R.id.img_down)
            mItemLayout = itemView.findViewById(R.id.rl_favor_item)

            mItemLayout?.setOnClickListener(View.OnClickListener { favorItem() })
        }

        fun onBind(songListEntity: SongListEntity) {
            val musicEntity = MyApplication.musicInfoDBService.firstEntity(songListEntity.getId())
            if (musicEntity?.getAlbumPic() != null) {
                MyApplication.songListDBService.updatePic(songListEntity.getId(), musicEntity.getAlbumPic())
                songListPic!!.setImageURI(musicEntity.getAlbumPic())
            } else if (musicEntity != null) {
                if (MyApplication.imageDBService.query(musicEntity.getArtist().replace(";", "")) == null) {
                    val avatarRequest = AvatarRequest()
                    avatarRequest.artist = musicEntity.getArtist().replace(";", "")
                    MyOkHttpClient.getInstance(mContext).sendNet(avatarRequest, object : BaseCallback<AvatarResponse>() {
                        override fun onFailure(code: Int, error_msg: String) {

                        }

                        override fun onSuccess(statusCode: Int, response: AvatarResponse) {
                            MyApplication.imageDBService.insert(musicEntity.getArtist().replace(";", ""), response.artist.image[2].`_$Text112`)
                            MyApplication.songListDBService.updatePic(songListEntity.getId(), response.artist.image[3].`_$Text112`)
                            songListPic!!.setImageURI(response.artist.image[2].`_$Text112`)
                        }
                    })
                } else {
                    MyApplication.songListDBService.updatePic(songListEntity.getId(), MyApplication.imageDBService.query(musicEntity.getArtist().replace(";", "")))
                    songListPic!!.setImageURI(MyApplication.imageDBService.query(musicEntity.getArtist().replace(";", "")))
                }
            }
            mPlayListTitle!!.text = songListEntity.getName()
            mPlayListInfo!!.text = MyApplication.musicInfoDBService.getLocalCount(songListEntity.getId())
        }
    }

}