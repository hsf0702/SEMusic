package com.past.music.adapter;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.neu.gaojin.MyOkHttpClient;
import com.neu.gaojin.response.BaseCallback;
import com.past.music.MyApplication;
import com.past.music.activity.CollectedActivity;
import com.past.music.activity.KtBaseActivity;
import com.past.music.activity.RecentMusicActivity;
import com.past.music.activity.SongListActivity;
import com.past.music.activity.SongListInfoActivity;
import com.past.music.api.AvatarRequest;
import com.past.music.api.AvatarResponse;
import com.past.music.entity.MusicEntity;
import com.past.music.entity.SongListEntity;
import com.past.music.fragment.KtDownLoadFragment;
import com.past.music.fragment.KtLocalMusicFragment;
import com.past.music.pastmusic.R;
import com.past.music.utils.MConstants;
import com.past.music.utils.MusicUtils;
import com.past.music.widget.CircleImageView;
import com.past.music.widget.MineItemView;

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
    private static final int MANAGE_SONG_LIST_LAYOUT = 0X05;

    private List<SongListEntity> mList;
    private Context mContext = null;

    public MyContentAdapter(Context context) {
        this.mContext = context;
        mList = MyApplication.songListDBService.query();
    }

    public void setSongList(List<SongListEntity> listItem) {
        this.mList = listItem;
        notifyItemRangeChanged(3, listItem.size() + 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_LAYOUT) {
            return new HeadLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_head_layout, parent, false));
        } else if (viewType == FUNC_LAYOUT) {
            return new FuncLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_func_layout, parent, false));
        } else if (viewType == FAVOR_TITLE_LAYOUT) {
            return new FavorTitleLayout(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_favor_title_layout, parent, false));
        } else if (viewType == MANAGE_SONG_LIST_LAYOUT) {
            return new ManageSongList(LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_songlist_layout, parent, false));
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

        } else if (holder instanceof FavorItemLayout) {
            ((FavorItemLayout) holder).onBind(mList.get(position - 3));
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 4;
        } else {
            return mList.size() + 4;
        }
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
            if (position == mList.size() + 3) {
                return MANAGE_SONG_LIST_LAYOUT;
            } else {
                return FAVOR_ITEM_LAYOUT;
            }
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
            FragmentTransaction ft = ((KtBaseActivity) mContext).getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
            ft.add(R.id.content, KtLocalMusicFragment.Companion.newInstance(0)).commitAllowingStateLoss();

        }

        @OnClick(R.id.download_music)
        void downloadMusic() {
            FragmentTransaction ft = ((KtBaseActivity) mContext).getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
            ft.add(R.id.content, KtDownLoadFragment.Companion.newInstance()).commitAllowingStateLoss();
        }

        @OnClick(R.id.recent_music)
        void recentMusic() {
            Intent intent = new Intent(mContext, RecentMusicActivity.class);
            ((KtBaseActivity) mContext).startActivityByX(intent, true);
        }

        @OnClick(R.id.love_music)
        void loveMusic() {
            Intent intent = new Intent(mContext, CollectedActivity.class);
            ((KtBaseActivity) mContext).startActivityByX(intent, true);
        }

        @OnClick(R.id.love_singer)
        void loveSinger() {
            FragmentTransaction ft = ((KtBaseActivity) mContext).getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("KtLocalMusicFragment");
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
            ft.add(R.id.content, KtLocalMusicFragment.Companion.newInstance(0)).commitAllowingStateLoss();
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
            Intent intent = new Intent(mContext, SongListActivity.class);
            ((KtBaseActivity) mContext).startActivityByX(intent, false);
        }

        public FavorTitleLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FavorItemLayout extends RecyclerView.ViewHolder {

        @BindView(R.id.img_album)
        SimpleDraweeView songList_pic;

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
            SongListInfoActivity.startActivity(mContext, mList.get(getAdapterPosition() - 3).getId(), mList.get(getAdapterPosition() - 3).getName());
        }

        public FavorItemLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(final SongListEntity songListEntity) {
            final MusicEntity musicEntity = MyApplication.musicInfoDBService.firstEntity(songListEntity.getId());
            if (musicEntity != null && musicEntity.getAlbumPic() != null) {
                MyApplication.songListDBService.updatePic(songListEntity.getId(), musicEntity.getAlbumPic());
                songList_pic.setImageURI(musicEntity.getAlbumPic());
            } else if (musicEntity != null) {
                if (MyApplication.imageDBService.query(musicEntity.getArtist().replace(";", "")) == null) {
                    AvatarRequest avatarRequest = new AvatarRequest();
                    avatarRequest.setArtist(musicEntity.getArtist().replace(";", ""));
                    MyOkHttpClient.getInstance(mContext).sendNet(avatarRequest, new BaseCallback<AvatarResponse>() {
                        @Override
                        public void onFailure(int code, String error_msg) {

                        }

                        @Override
                        public void onSuccess(int statusCode, final AvatarResponse response) {
                            MyApplication.imageDBService.insert(musicEntity.getArtist().replace(";", ""), response.getArtist().getImage().get(2).get_$Text112());
                            MyApplication.songListDBService.updatePic(songListEntity.getId(), response.getArtist().getImage().get(3).get_$Text112());
                            songList_pic.setImageURI(response.getArtist().getImage().get(2).get_$Text112());
                        }
                    });
                } else {
                    MyApplication.songListDBService.updatePic(songListEntity.getId(), MyApplication.imageDBService.query(musicEntity.getArtist().replace(";", "")));
                    songList_pic.setImageURI(MyApplication.imageDBService.query(musicEntity.getArtist().replace(";", "")));
                }
            }
            mPlayListTitle.setText(songListEntity.getName());
            mPlayListInfo.setText(MyApplication.musicInfoDBService.getLocalCount(songListEntity.getId()));
        }
    }

    class ManageSongList extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ManageSongList(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, SongListActivity.class);
            ((KtBaseActivity) mContext).startActivityByX(intent, false);
        }
    }
}
