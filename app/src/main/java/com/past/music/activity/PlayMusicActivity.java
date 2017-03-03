package com.past.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.past.music.Config.BaseConfig;
import com.past.music.entity.Mp3Info;
import com.past.music.pastmusic.R;
import com.past.music.service.PlayerService;

public class PlayMusicActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBackAlbum, mPlayingmode, mControl, mNext, mPre, mPlaylist, mCmt, mFav, mDown, mMore, mNeedle, mPlay;
    private RelativeLayout mRlTotal = null;
    private Mp3Info mMp3Info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        initView();
        initListener();
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initView() {
        mNext = (ImageView) findViewById(R.id.playing_next);
        mPre = (ImageView) findViewById(R.id.playing_pre);
        mPlay = (ImageView) findViewById(R.id.playing_play);
    }

    private void initListener() {
        mNext.setOnClickListener(this);
        mPre.setOnClickListener(this);
        mPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.playing_pre:
                break;
            case R.id.playing_play:
                intent = new Intent();
                intent.putExtra(BaseConfig.URL, mMp3Info.getUrl());
                intent.putExtra(BaseConfig.MSG, BaseConfig.PlayerMsg.PLAY_MSG);
                intent.setClass(this, PlayerService.class);
                startService(intent);       //启动服务
                break;
            case R.id.playing_next:
                break;
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }
}
