package com.past.music.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.past.music.dialog.SongOperationDialog;
import com.past.music.entity.MusicEntity;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;
import com.past.music.utils.HandlerUtil;
import com.past.music.utils.MConstants;
import com.past.music.utils.MusicUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/6/6 10:35
 * 描述：
 * 备注：
 * =======================================================
 */

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEAD_LAYOUT = 0X01;
    public static final int CONTENT_LAYOUT = 0X02;

    private ArrayList<MusicEntity> mList;
    private PlayMusic playMusic;
    private Handler handler;
    private Context mContext;
    SongOperationDialog songOperationDialog;

    public MusicListAdapter(Context context) {
        this.mContext = context;
        handler = HandlerUtil.getInstance(context);
    }

    //更新adpter的数据
    public void updateDataSet(ArrayList<MusicEntity> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_LAYOUT) {
            return new CommonItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item, parent, false));
        } else {
            return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_musci_common_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CommonItemViewHolder) {

        } else {
            ((ListItemViewHolder) holder).onBindData(mList.get(position - 1), position);
        }
    }

    @Override
    public int getItemCount() {
        return null != mList ? mList.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_LAYOUT;
        } else {
            return CONTENT_LAYOUT;
        }
    }

    class CommonItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.play_all_number)
        TextView textView;

        @BindView(R.id.select)
        ImageView select;

        @OnClick(R.id.select)
        void select() {
        }

        CommonItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (playMusic != null)
                handler.removeCallbacks(playMusic);
            if (getAdapterPosition() > -1) {
                playMusic = new PlayMusic(0);
                handler.postDelayed(playMusic, 70);
            }
        }
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.music_name)
        TextView mMusicName;

        @BindView(R.id.music_info)
        TextView mMusicInfo;

        @BindView(R.id.viewpager_list_button)
        ImageView mListButton;

        @OnClick(R.id.viewpager_list_button)
        void setmListButton() {
            songOperationDialog = new SongOperationDialog(mContext, mList.get(getAdapterPosition() - 1), MConstants.MUSICOVERFLOW);
            songOperationDialog.show();
        }

        ListItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (playMusic != null)
                handler.removeCallbacks(playMusic);
            if (getAdapterPosition() > -1) {
                playMusic = new PlayMusic(getAdapterPosition() - 1);
                handler.postDelayed(playMusic, 70);
            }
        }

        public void onBindData(MusicEntity musicEntity, int position) {
            mMusicName.setText(musicEntity.getMusicName());
            mMusicInfo.setText(musicEntity.getArtist());
        }
    }


    class PlayMusic implements Runnable {
        int position;

        public PlayMusic(int position) {
            this.position = position;
        }

        /**
         * 运行在主线程
         */
        @Override
        public void run() {
            long[] list = new long[mList.size()];
            HashMap<Long, MusicEntity> infos = new HashMap();
            for (int i = 0; i < mList.size(); i++) {
                MusicEntity info = mList.get(i);
                list[i] = info.songId;
                info.islocal = true;
                info.albumData = MusicUtils.getAlbumArtUri(info.albumId) + "";
                infos.put(list[i], mList.get(i));
            }
            if (position > -1)
                MusicPlayer.playAll(infos, list, position, false);
        }
    }
}
