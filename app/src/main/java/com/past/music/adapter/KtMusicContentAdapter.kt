package com.past.music.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.drawee.view.SimpleDraweeView
import com.past.music.activity.NetSongListActivity
import com.past.music.activity.WebViewActivity
import com.past.music.api.SonglistBean
import com.past.music.pastmusic.R
import com.past.music.utils.FrescoImageLoader
import com.past.music.widget.IconView
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener
import java.util.ArrayList

/**
 * Created by gaojin on 2017/12/7.
 */
class KtMusicContentAdapter constructor(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val BANNER_LAYOUT = 0X01
    private val HOT_LIST_LAYOUT = 0X02
    private val RECOMMEND_SONGS_LAYOUT = 0X03
    private val FAVOR_ITEM_LAYOUT = 0X04

    private val mContext: Context = context
    private var mHotList: ArrayList<String>? = null
    private var mRecommendList: List<SonglistBean>? = null

    fun updateHotList(arrayList: ArrayList<String>) {
        this.mHotList = arrayList
        notifyItemChanged(1)
    }

    fun updateRecommendList(arrayList: List<SonglistBean>) {
        this.mRecommendList = arrayList
        notifyItemRangeChanged(2, 5)

    }


    override fun getItemCount(): Int {
        return if (mRecommendList == null) {
            2
        } else {
            7
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BANNER_LAYOUT -> BannerLayoutHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.music_banner_layout, parent, false))
            HOT_LIST_LAYOUT -> HotListHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.music_hot_list_layout, parent, false))
            else -> RecommendListHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.recommend_song_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as? RecommendListHolder)?.onBind(mRecommendList!![position - 2])
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> BANNER_LAYOUT
            1 -> HOT_LIST_LAYOUT
            in 2..6 -> RECOMMEND_SONGS_LAYOUT
            else -> 9
        }
    }


    internal inner class BannerLayoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnBannerListener {

        private val images = ArrayList<String>()
        var banner: Banner? = null

        init {
            images.add("https://y.gtimg.cn/music/photo_new/T003R720x288M000004YEQve3OwuKT.jpg")
            images.add("https://y.gtimg.cn/music/photo_new/T003R720x288M000002P5Ak50TPHdm.jpg")
            images.add("https://y.gtimg.cn/music/photo_new/T003R720x288M000001xMeNP2PGdLS.jpg")
            images.add("https://y.gtimg.cn/music/photo_new/T003R720x288M000001P2zAW0MPYpK.jpg")

            banner = itemView.findViewById(R.id.banner)

            //设置图片加载器
            banner?.setImageLoader(FrescoImageLoader())
            //设置图片集合
            banner?.setImages(images)
            //banner设置方法全部调用完毕时最后调用
            banner?.start()

            banner?.setOnBannerListener(this)
        }

        fun startAutoPlay() {
            banner!!.startAutoPlay()
        }

        fun stopAutoPlay() {
            banner!!.stopAutoPlay()
        }

        override fun OnBannerClick(position: Int) {
            WebViewActivity.startWebViewActivity(mContext, "", "https://y.qq.com/msa/226/0_3046.html?ADTAG=myqq&from=myqq&channel=10007100")
        }
    }

    internal inner class HotListHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var mIconView1: IconView? = null

        var mIconView2: IconView? = null

        var mIconView3: IconView? = null

        var mIconView4: IconView? = null

        var mIconView5: IconView? = null

        var mIconView6: IconView? = null

        fun click1() {
            NetSongListActivity.startActivity(mContext, "26", "巅峰榜 • 热歌")
        }

        fun click2() {
            NetSongListActivity.startActivity(mContext, "6", "巅峰榜 • 港台")
        }

        fun click3() {
            NetSongListActivity.startActivity(mContext, "5", "巅峰榜 • 内地")
        }

        fun click4() {
            NetSongListActivity.startActivity(mContext, "3", "巅峰榜 • 欧美")
        }

        fun click5() {
            NetSongListActivity.startActivity(mContext, "17", "巅峰榜 • 日本")
        }

        fun click6() {
            NetSongListActivity.startActivity(mContext, "28", "巅峰榜 • 网络歌曲")
        }

        init {
            ButterKnife.bind(this, itemView)

            mIconView1 = itemView.findViewById(R.id.icon_view_1)
            mIconView2 = itemView.findViewById(R.id.icon_view_2)
            mIconView3 = itemView.findViewById(R.id.icon_view_3)
            mIconView4 = itemView.findViewById(R.id.icon_view_4)
            mIconView5 = itemView.findViewById(R.id.icon_view_5)
            mIconView6 = itemView.findViewById(R.id.icon_view_6)

            mIconView1!!.setOnClickListener(this)
            mIconView2!!.setOnClickListener(this)
            mIconView3!!.setOnClickListener(this)
            mIconView4!!.setOnClickListener(this)
            mIconView5!!.setOnClickListener(this)
            mIconView6!!.setOnClickListener(this)

            mIconView1!!.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_26_300_201697348.jpg")
            mIconView1!!.setmTvDes("巅峰榜 • 热歌")

            mIconView2!!.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_6_300_201698289.jpg")
            mIconView2!!.setmTvDes("巅峰榜 • 港台")

            mIconView3!!.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_5_300_201712321.jpg")
            mIconView3!!.setmTvDes("巅峰榜 • 内地")

            mIconView4!!.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_3_300_200981920.jpg")
            mIconView4!!.setmTvDes("巅峰榜 • 欧美")

            mIconView5!!.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_17_300_200559528.jpg?")
            mIconView5!!.setmTvDes("巅峰榜 • 日本")

            mIconView6!!.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_28_300_201644796.jpg")
            mIconView6!!.setmTvDes("巅峰榜 • 网络歌曲")
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.icon_view_1 -> click1()
                R.id.icon_view_2 -> click2()
                R.id.icon_view_3 -> click3()
                R.id.icon_view_4 -> click4()
                R.id.icon_view_5 -> click5()
                R.id.icon_view_6 -> click6()
            }
        }
    }

    internal inner class RecommendListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mImgSong: SimpleDraweeView? = null
        var mTitle: TextView? = null
        var mInfo: TextView? = null

        init {
            mImgSong = itemView.findViewById(R.id.recommend_img_singer)
            mTitle = itemView.findViewById(R.id.recommend_singer_list_title)
            mInfo = itemView.findViewById(R.id.recommend_singer_list_info)
        }

        fun onBind(songlistBean: SonglistBean) {
            mTitle!!.text = songlistBean.songname
            mInfo!!.text = songlistBean.singername + " • " + "专辑"
            mImgSong!!.setImageURI(songlistBean.albumpic_big)
        }
    }

}