package com.past.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.past.music.MyApplication;
import com.past.music.entity.SongListEntity;
import com.past.music.pastmusic.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/16 13:39
 * 描述：
 * 备注：
 * =======================================================
 */
public class CreateSongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADLAYOUT = 0x01;
    public static final int SONGLISTLAYOUT = 0x02;
    public static final int EMPTYLAYOUT = 0x03;

    private List<SongListEntity> mList;
    private Context mContext;

    public CreateSongListAdapter(Context context) {
        this.mContext = context;
        mList = MyApplication.songListDBService.query();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADLAYOUT) {
            return new HeadLayout(LayoutInflater.from(parent.getContext()).inflate(R.layout.create_song_head_layout, parent, false));
        } else if (viewType == SONGLISTLAYOUT) {
            return new SongListLayout(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_favor_item_layout, parent, false));
        } else {
            return new EmptyLayout(LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongListLayout) {
            ((SongListLayout) holder).onBind(mList.get(position - 1));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size() == 0 ? 2 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADLAYOUT;
        } else {
            if (mList.size() == 0) {
                return EMPTYLAYOUT;
            } else {
                return SONGLISTLAYOUT;
            }
        }
    }

    class HeadLayout extends RecyclerView.ViewHolder {

        @BindView(R.id.create_song_list)
        ImageView mCreateSongList;

        @OnClick(R.id.create_song_list)
        void create() {
            Toast.makeText(mContext, "创建歌单", Toast.LENGTH_SHORT).show();
        }

        @BindView(R.id.tv_manage)
        TextView textView;

        @OnClick(R.id.tv_manage)
        void manage() {
            Toast.makeText(mContext, "管理歌单", Toast.LENGTH_SHORT).show();
        }

        public HeadLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SongListLayout extends RecyclerView.ViewHolder {
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
            Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show();
        }

        public SongListLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(SongListEntity songListEntity) {
            songList_pic.setImageURI(songListEntity.getList_pic());
            mPlayListTitle.setText(songListEntity.getName());
            mPlayListInfo.setText(songListEntity.getCreator() + songListEntity.getInfo());
        }
    }

    class EmptyLayout extends RecyclerView.ViewHolder {
        public EmptyLayout(View itemView) {
            super(itemView);
        }
    }
}
