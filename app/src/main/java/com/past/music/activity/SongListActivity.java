package com.past.music.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.past.music.adapter.CreateSongListAdapter;
import com.past.music.pastmusic.R;

import butterknife.BindView;

public class SongListActivity extends ToolBarActivity {

    @BindView(R.id.create_song_list_recycle_view)
    RecyclerView mRecyclerView;

    CreateSongListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("歌单");
        mAdapter = new CreateSongListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_song_list;
    }
}
