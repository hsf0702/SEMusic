package com.past.music.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.past.music.entity.MusicEntity;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;
import com.past.music.utils.HandlerUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/23 19:51
 * 描述：热门歌单适配器
 * 备注：
 * =======================================================
 */
public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MusicEntity> mList;
    private Handler mHandler;

    public SongListAdapter(Context context) {
        this.mContext = context;
        mHandler = HandlerUtil.getInstance(context);
    }

    public void updateList(List<MusicEntity> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendListHolder(LayoutInflater.from(mContext).inflate(R.layout.hot_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecommendListHolder) holder).onBind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class RecommendListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.hot_img_singer)
        SimpleDraweeView mImgSong;

        @BindView(R.id.hot_singer_list_title)
        TextView mTitle;

        @BindView(R.id.hot_singer_list_info)
        TextView mInfo;

        @BindView(R.id.rl_hot_singer_item)
        RelativeLayout relativeLayout;


        public RecommendListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onBind(final MusicEntity musicEntity) {
            mTitle.setText(musicEntity.getMusicName());
            mTitle.setTextColor(0xffffffff);
            mInfo.setText(musicEntity.getArtist() + " • " + "专辑");
            mImgSong.setImageURI(musicEntity.getAlbumData());
        }

        @Override
        public void onClick(View view) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    HashMap<Long, MusicEntity> infos = new HashMap<Long, MusicEntity>();
//                    int len = 10;
                    int len = mList.size();
                    long[] list = new long[len];
                    for (int i = 0; i < len; i++) {
                        MusicEntity info = mList.get(i);
                        list[i] = info.songId;
                        infos.put(list[i], info);
                    }
                    if (getAdapterPosition() > 0)
                        MusicPlayer.playAll(infos, list, getAdapterPosition(), false);
                }
            }, 70);
        }
    }
}
