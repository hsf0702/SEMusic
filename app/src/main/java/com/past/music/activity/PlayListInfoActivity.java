package com.past.music.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.past.music.adapter.MusicListAdapter;
import com.past.music.entity.MusicEntity;
import com.past.music.pastmusic.R;
import com.past.music.utils.MConstants;
import com.past.music.utils.MusicUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class PlayListInfoActivity extends ToolBarActivity implements MConstants {

    private static final String TITLE = "title";
    private static final String MARK = "mark";
    private static final String SEARCHID = "searchid";

    private String mTitle = null;
    private int mMark = -1;
    private String searchId = null;

    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    private MusicListAdapter mAdapter;

    public static void startActivity(Context context, String title, String searchId, int mark) {
        Intent intent = new Intent(context, PlayListInfoActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(MARK, mark);
        intent.putExtra(SEARCHID, searchId);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.empty, R.anim.empty);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
        setTitle(mTitle);
        mAdapter = new MusicListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        reloadAdapter(mMark);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mTitle = intent.getStringExtra(TITLE);
        mMark = intent.getIntExtra(MARK, -1);
        searchId = intent.getStringExtra(SEARCHID);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_list_info;
    }

    //更新adapter界面
    public void reloadAdapter(final int mark) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                ArrayList<MusicEntity> musicList = null;
                switch (mark) {
                    case START_FROM_ARTIST:
                        musicList = (ArrayList) MusicUtils.queryMusic(PlayListInfoActivity.this, searchId, MConstants.START_FROM_ARTIST);
                        break;
                    case START_FROM_ALBUM:
                        musicList = (ArrayList) MusicUtils.queryMusic(PlayListInfoActivity.this, searchId, START_FROM_ALBUM);
                        break;
                }
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
