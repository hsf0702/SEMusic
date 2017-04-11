package com.past.music.adapter;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.past.music.activity.BaseActivity;
import com.past.music.activity.CollectedActivity;
import com.past.music.activity.DownloadActivity;
import com.past.music.activity.LocalMusicActivity;
import com.past.music.activity.MySingersActivity;
import com.past.music.activity.RecentMusicActivity;
import com.past.music.entity.Mp3Info;
import com.past.music.pastmusic.R;
import com.past.music.utils.MConstants;
import com.past.music.utils.MusicUtils;
import com.past.music.widget.CircleImageView;
import com.past.music.widget.MineItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/4 下午3:17
 * 描述：我的Fragment界面的adapter
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
public class MyContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEAD_LAYOUT = 0X01;
    private static final int FUNC_LAYOUT = 0X02;
    private static final int FAVOR_TITLE_LAYOUT = 0X03;
    private static final int FAVOR_ITEM_LAYOUT = 0X04;

    private List<Mp3Info> mList = new ArrayList<>();
    private Context mContext = null;

    public MyContentAdapter(Context context) {
        this.mContext = context;
    }

    public void setListItem(List<Mp3Info> listItem) {
        mList.clear();
        mList.addAll(listItem);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_LAYOUT) {
            return new HeadLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_head_layout, parent, false));
        } else if (viewType == FUNC_LAYOUT) {
            return new FuncLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_func_layout, parent, false));
        } else if (viewType == FAVOR_TITLE_LAYOUT) {
            return new FavorTitleLayout(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_favor_title_layout, parent, false));
        } else {
            return new FavorItemLayout(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_favor_item_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadLayoutHolder) {

        } else if (holder instanceof FuncLayoutHolder) {
            ((FuncLayoutHolder) holder).onBind();
        } else if (holder instanceof FavorTitleLayout) {

        } else {

        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_LAYOUT;
        } else if (position == 1) {
            return FUNC_LAYOUT;
        } else if (position == 2) {
            return FAVOR_TITLE_LAYOUT;
        } else {
            return FAVOR_ITEM_LAYOUT;
        }
    }

    class HeadLayoutHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mine_head_avatar)
        CircleImageView mHeadAvatar;

        @BindView(R.id.mine_head_listen_time)
        TextView mListenTime;

        @BindView(R.id.mine_head_listen_vip)
        TextView mListenVip;

        @BindView(R.id.mine_head_user_name)
        TextView mUserName;

        public HeadLayoutHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FuncLayoutHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.local_music)
        MineItemView mLocalMusic;

        @BindView(R.id.download_music)
        MineItemView mDownMusic;

        @BindView(R.id.recent_music)
        MineItemView mRecentMusic;

        @BindView(R.id.love_music)
        MineItemView mLoveMusic;

        @BindView(R.id.love_singer)
        MineItemView mLoveSinger;

        @BindView(R.id.buy_music)
        MineItemView mBuyMusic;

        @OnClick(R.id.local_music)
        void localMusic() {
            Intent intent = new Intent(mContext, LocalMusicActivity.class);
            ((BaseActivity) mContext).startActivityByX(intent, true);
        }

        @OnClick(R.id.download_music)
        void downloadMusic() {
            Intent intent = new Intent(mContext, DownloadActivity.class);
            ((BaseActivity) mContext).startActivityByX(intent, true);
        }

        @OnClick(R.id.recent_music)
        void recentMusic() {
            Intent intent = new Intent(mContext, RecentMusicActivity.class);
            ((BaseActivity) mContext).startActivityByX(intent, true);
        }

        @OnClick(R.id.love_music)
        void loveMusic() {
            Intent intent = new Intent(mContext, CollectedActivity.class);
            ((BaseActivity) mContext).startActivityByX(intent, true);
        }

        @OnClick(R.id.love_singer)
        void loveSinger() {
            Intent intent = new Intent(mContext, MySingersActivity.class);
            ((BaseActivity) mContext).startActivityByX(intent, true);
        }

        @OnClick(R.id.buy_music)
        void buyMusic() {
            Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show();
        }

        public FuncLayoutHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind() {
            int localCount = MusicUtils.queryMusic(mContext, MConstants.START_FROM_LOCAL).size();
//            int singerCount = MusicUtils.queryArtist(mContext).size();
//            int albumCount = MusicUtils.queryAlbums(mContext).size();
//            int folderCount = MusicUtils.queryFolder(mContext).size();

            mLocalMusic.setmItemCount(String.valueOf(localCount));

        }
    }

    class FavorTitleLayout extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_favor_title)
        RelativeLayout mTitleLayout;

        @BindView(R.id.playlist_count)
        TextView mPlayListCount;

        @OnClick(R.id.rl_favor_title)
        void favorTltle() {
            Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show();
        }

        public FavorTitleLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FavorItemLayout extends RecyclerView.ViewHolder {

        @BindView(R.id.img_album)
        ImageView mAlbum;

        @BindView(R.id.play_list_title)
        TextView mPlayListTitle;

        @BindView(R.id.play_list_info)
        TextView mPlayListInfo;

        @BindView(R.id.img_down)
        ImageView mImgDown;

        @BindView(R.id.rl_favor_item)
        RelativeLayout mItemLayout;

        @OnClick(R.id.rl_favor_item)
        void favorItem() {
            Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show();
        }

        public FavorItemLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
