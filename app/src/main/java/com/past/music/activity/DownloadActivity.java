package com.past.music.activity;

import android.os.Bundle;

import com.past.music.pastmusic.R;

public class DownloadActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("下载歌曲");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }
}
