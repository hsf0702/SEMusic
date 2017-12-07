package com.past.music.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.past.music.adapter.KtCreateSongListAdapter;
import com.past.music.pastmusic.R;

import butterknife.BindView;

public class SongListActivity extends ToolBarActivity {

    @BindView(R.id.create_song_list_recycle_view)
    RecyclerView mRecyclerView;

    KtCreateSongListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("歌单");
        mAdapter = new KtCreateSongListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.update();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_song_list;
    }
}
