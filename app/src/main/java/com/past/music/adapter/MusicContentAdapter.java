package com.past.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.past.music.activity.SongListActivity;
import com.past.music.api.HotListResponse;
import com.past.music.pastmusic.R;
import com.past.music.utils.FrescoImageLoader;
import com.past.music.widget.IconView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/22 17:13
 * 描述：
 * 备注：
 * =======================================================
 */
public class MusicContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BANNER_LAYOUT = 0X01;
    private static final int HOT_LIST_LAYOUT = 0X02;
    private static final int RECOMMEND_SONGS_LAYOUT = 0X03;
    private static final int FAVOR_ITEM_LAYOUT = 0X04;

    private Context mContext = null;
    private ArrayList<String> mHotList = null;
    private List<HotListResponse.ShowapiResBodyBean.PagebeanBean.SonglistBean> mRecommendList = null;

    public MusicContentAdapter(Context context) {
        this.mContext = context;
    }

    public void updateHotList(ArrayList<String> arrayList) {
        this.mHotList = arrayList;
        notifyItemChanged(1);
    }

    public void updateRecommendList(List<HotListResponse.ShowapiResBodyBean.PagebeanBean.SonglistBean> arrayList) {
        this.mRecommendList = arrayList;
        notifyItemRangeChanged(2, 5);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BANNER_LAYOUT) {
            return new BannerLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_banner_layout, parent, false));
        } else if (viewType == HOT_LIST_LAYOUT) {
            return new HotListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_hot_list_layout, parent, false));
        } else if (viewType == RECOMMEND_SONGS_LAYOUT) {
            return new RecommendListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_song_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecommendListHolder) {
            ((RecommendListHolder) holder).onBind(mRecommendList.get(position - 2));
        }
    }

    @Override
    public int getItemCount() {
        if (mRecommendList == null) {
            return 2;
        } else {
            return 7;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER_LAYOUT;
        } else if (position == 1) {
            return HOT_LIST_LAYOUT;
        } else if (position >= 2 && position <= 6) {
            return RECOMMEND_SONGS_LAYOUT;
        } else {
            return 9;
        }
    }

    class BannerLayoutHolder extends RecyclerView.ViewHolder {

        private List<String> images = new ArrayList<>();

        @BindView(R.id.banner)
        Banner banner;

        public BannerLayoutHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            images.add("https://y.gtimg.cn/music/photo_new/T003R720x288M000004YEQve3OwuKT.jpg");
            images.add("https://y.gtimg.cn/music/photo_new/T003R720x288M000002P5Ak50TPHdm.jpg");
            images.add("https://y.gtimg.cn/music/photo_new/T003R720x288M000001xMeNP2PGdLS.jpg");
            images.add("https://y.gtimg.cn/music/photo_new/T003R720x288M000001P2zAW0MPYpK.jpg");

            //设置图片加载器
            banner.setImageLoader(new FrescoImageLoader());
            //设置图片集合
            banner.setImages(images);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }

        public void startAutoPlay() {
            banner.startAutoPlay();
        }

        public void stopAutoPlay() {
            banner.stopAutoPlay();
        }
    }

    class HotListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_view_1)
        IconView mIconView1;

        @OnClick(R.id.icon_view_1)
        void click1() {
            SongListActivity.startActivity(mContext, "26", "巅峰榜 • 热歌");
        }

        @BindView(R.id.icon_view_2)
        IconView mIconView2;

        @BindView(R.id.icon_view_3)
        IconView mIconView3;

        @BindView(R.id.icon_view_4)
        IconView mIconView4;

        @BindView(R.id.icon_view_5)
        IconView mIconView5;

        @BindView(R.id.icon_view_6)
        IconView mIconView6;

        public HotListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mIconView1.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_26_300_201697348.jpg");
            mIconView1.setmTvDes("巅峰榜 • 热歌");

            mIconView2.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_6_300_201698289.jpg");
            mIconView2.setmTvDes("巅峰榜 • 港台");

            mIconView3.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_5_300_201712321.jpg");
            mIconView3.setmTvDes("巅峰榜 • 内地");

            mIconView4.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_3_300_200981920.jpg");
            mIconView4.setmTvDes("巅峰榜 • 欧美");

            mIconView5.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_17_300_200559528.jpg?");
            mIconView5.setmTvDes("巅峰榜 • 日本");

            mIconView6.setmHotPic("https://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_27_300_201932121.jpg");
            mIconView6.setmTvDes("巅峰榜 • 新歌");
        }
    }

    class RecommendListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recommend_img_singer)
        SimpleDraweeView mImgSong;

        @BindView(R.id.recommend_singer_list_title)
        TextView mTitle;

        @BindView(R.id.recommend_singer_list_info)
        TextView mInfo;


        public RecommendListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(final HotListResponse.ShowapiResBodyBean.PagebeanBean.SonglistBean songlistBean) {
            mTitle.setText(songlistBean.getSongname());
            mInfo.setText(songlistBean.getSingername() + " • " + "专辑");
            mImgSong.setImageURI(songlistBean.getAlbumpic_big());
        }
    }

}
