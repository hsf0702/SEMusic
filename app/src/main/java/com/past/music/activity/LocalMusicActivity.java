package com.past.music.activity;

import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.past.music.Config.BaseConfig;
import com.past.music.pastmusic.R;

public class LocalMusicActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("本地歌曲");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_local_music;
    }
}
