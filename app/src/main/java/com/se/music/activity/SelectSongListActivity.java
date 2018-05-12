package com.se.music.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.se.music.adapter.SelectSongListAdapter;
import com.se.music.entity.MusicEntity;
import com.se.music.pastmusic.R;
import com.se.music.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectSongListActivity extends AppCompatActivity {

    public static final String MUSIC_ENTITY = "MusicEntity";


    @BindView(R.id.add_album)
    SimpleDraweeView imgAlbum;

    @BindView(R.id.add_song_title)
    TextView mSongName;

    @BindView(R.id.add_song_info)
    TextView mSongInfo;

    @BindView(R.id.song_list_recycle_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.cancel)
    void cancel() {
        finish();
    }

    @OnClick(R.id.new_list)
    void newList() {
        Intent intent = new Intent(this, CreateSongListActivity.class);
        this.startActivity(intent);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    private MusicEntity selectMusicEntity;

    public static void startActivity(Context context, MusicEntity musicEntity) {
        Intent intent = new Intent(context, SelectSongListActivity.class);
        intent.putExtra(MUSIC_ENTITY, musicEntity);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    SelectSongListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song_list);

        setStatusBar();
        ButterKnife.bind(this);
        onNewIntent(getIntent());
        adapter = new SelectSongListAdapter(this, selectMusicEntity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        selectMusicEntity = intent.getParcelableExtra(MUSIC_ENTITY);
        if (selectMusicEntity != null) {
            ImageUtils.setImageSource(this, imgAlbum, selectMusicEntity);
            mSongName.setText(selectMusicEntity.getMusicName());
            mSongInfo.setText(selectMusicEntity.getArtist() + "•" + selectMusicEntity.getAlbumName());
        }

    }

    protected void setStatusBar() {
    }


    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }
}
