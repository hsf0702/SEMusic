package com.past.music.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.past.music.adapter.MusicListAdapter;
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

    private MusicListAdapter mAdapter;
    ArrayList<MusicEntity> musicList = new ArrayList<>();


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
        mAdapter = new MusicListAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        refreshAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                musicList.clear();
                musicList.addAll(MusicUtils.queryMusic(mContext, MConstants.START_FROM_LOCAL));
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
