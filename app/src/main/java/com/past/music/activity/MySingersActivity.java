package com.past.music.activity;

import android.os.Bundle;

import com.past.music.pastmusic.R;

public class MySingersActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的歌手");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_singers;
    }
}
