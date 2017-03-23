package com.past.music.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * 作者：gaojin
 * 日期：2017/3/4 下午3:17
 * 描述：
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */

public class LocalMusicFragment extends BaseFragment {


    @BindView(R.id.local_music_recycle)
    RecyclerView mRecyclerView;

    private LocalMusicFragmentAdapter mAdapter;


    public static LocalMusicFragment newInstance(String param1, String param2) {
        LocalMusicFragment fragment = new LocalMusicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LocalMusicFragment() {
       
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new LocalMusicFragmentAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        reloadAdapter();
        return view;
    }

    //更新adapter界面
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                ArrayList<MusicEntity> musicList = (ArrayList) MusicUtils.queryMusic(mContext, MConstants.START_FROM_LOCAL);
                mAdapter.updateDataSet(musicList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }


    class LocalMusicFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public static final int HEAD_LAYOUT = 0X01;
        public static final int CONTENT_LAYOUT = 0X02;

        ArrayList<MusicEntity> mList;
        PlayMusic playMusic;
        Handler handler;

        public LocalMusicFragmentAdapter() {
            handler = HandlerUtil.getInstance(mContext);
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
            return (null != mList ? mList.size() + 1 : 0);
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

}
