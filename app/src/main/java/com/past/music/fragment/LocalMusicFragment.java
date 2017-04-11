package com.past.music.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.past.music.adapter.LocalMusicAdapter;
import com.past.music.entity.MusicEntity;
import com.past.music.pastmusic.R;
import com.past.music.utils.MConstants;
import com.past.music.utils.MusicUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private LocalMusicAdapter mAdapter;


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
        mAdapter = new LocalMusicAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    //重写更新adapter的方法
    @Override
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
}
