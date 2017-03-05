package com.past.music.activity;

import android.os.Bundle;

import com.past.music.pastmusic.R;

public class RecentMusicActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("最近播放");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recent_music;
    }
}
