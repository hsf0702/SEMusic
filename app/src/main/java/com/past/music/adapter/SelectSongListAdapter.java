package com.past.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.past.music.MyApplication;
import com.past.music.activity.SelectSongListActivity;
import com.past.music.entity.MusicEntity;
import com.past.music.entity.SongListEntity;
import com.past.music.event.CreateSongListEvent;
import com.past.music.pastmusic.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/19 23:02
 * 描述：
 * 备注：
 * =======================================================
 */
public class SelectSongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SongListEntity> mList;
    private Context mContext;
    private MusicEntity musicEntity;

    public SelectSongListAdapter(Context context, MusicEntity musicEntity) {
        this.mContext = context;
        this.musicEntity = musicEntity;
        mList = MyApplication.songListDBService.query();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SongListLayout(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_favor_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SongListLayout) holder).onBind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
            MyApplication.musicInfoDBService.insert(musicEntity, mList.get(getAdapterPosition()).getId());
            EventBus.getDefault().post(new CreateSongListEvent());
            ((SelectSongListActivity) mContext).finish();
        }

        public SongListLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(SongListEntity songListEntity) {
            songList_pic.setImageURI(songListEntity.getList_pic());
            mPlayListTitle.setText(songListEntity.getName());
            mPlayListInfo.setText(MyApplication.musicInfoDBService.getLocalCount(songListEntity.getId()));
        }
    }
}
