package com.past.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.past.music.Config.BaseConfig;
import com.past.music.adapter.MusicListAdapter;
import com.past.music.entity.Mp3Info;
import com.past.music.pastmusic.R;
import com.past.music.service.PlayerService;
import com.past.music.view.CircleImageView;

public class MainActivity extends SlidingFragmentActivity {

    private RecyclerView mRecycleView = null;
    private MusicListAdapter adapter = null;
    private CircleImageView mCircleImageView = null;
    private SlidingMenu mSlidingMenu = null;
    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_drawer_fragment);
        mRecycleView = (RecyclerView) findViewById(R.id.music_list);
        mCircleImageView = (CircleImageView) findViewById(R.id.circle_image);
        textView = (TextView) findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "hhhhhhh", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new MusicListAdapter(this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setAdapter(adapter);
        adapter.setClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Mp3Info mp3Info, int position) {
                Log.d("mp3Info-->", mp3Info.toString());
                Intent intent = new Intent();
                intent.putExtra(BaseConfig.URL, mp3Info.getUrl());
                intent.putExtra(BaseConfig.MSG, BaseConfig.PlayerMsg.PLAY_MSG);
                intent.setClass(MainActivity.this, PlayerService.class);
                startService(intent);       //启动服务
            }
        });

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            mCircleImageView.startAnimation(operatingAnim);
        }

        mSlidingMenu = getSlidingMenu();
        initSlidingMenu(mSlidingMenu);
    }

    public void initSlidingMenu(SlidingMenu slidingMenu) {
        slidingMenu.setMode(SlidingMenu.LEFT);//设置左右滑菜单
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置要使菜单滑动，触碰屏幕的范围
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);//设置阴影图片的宽度
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);//设置阴影图片
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//设置划出时主页面显示的剩余宽度
        slidingMenu.setFadeEnabled(true);//设置滑动时菜单的是否渐变     <span style="white-space:pre">              </span>slidingMenu.setFadeDegree(0.35F);//<span style="font-family: Helvetica, arial, freesans, clean, sans-serif;">设置</span>滑动时的渐变程度
//      slidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
        slidingMenu.toggle();//动态判断自动关闭或开启SlidingMenu
        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            public void onOpened() {

            }
        });
    }
}
