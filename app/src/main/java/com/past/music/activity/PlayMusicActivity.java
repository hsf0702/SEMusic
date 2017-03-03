package com.past.music.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.past.music.BaseActivity;
import com.past.music.pastmusic.R;

public class PlayMusicActivity extends BaseActivity {

    private ImageView mImageBg = null;
    private RelativeLayout mRlTotal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        mRlTotal = (RelativeLayout) findViewById(R.id.rl_total_layout);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }
}
