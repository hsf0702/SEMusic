package com.past.music.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.neu.gaojin.MyOkHttpClient;
import com.neu.gaojin.response.BaseCallback;
import com.past.music.adapter.SongListAdapter;
import com.past.music.api.HotListResponse;
import com.past.music.api.HotListResquest;
import com.past.music.api.SonglistBean;
import com.past.music.entity.MusicEntity;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/10 11:47
 * 描述：热门歌单界面
 * 备注：
 * =======================================================
 */

public class NetSongListActivity extends BaseActivity {

    public static final String TAG = "NetSongListActivity";
    public static final String TOPID = "TOPID";
    public static final String TITLE = "TITLE";

    @BindView(R.id.rl_hot_list)
    RelativeLayout relativeLayout;

    @BindView(R.id.head_image)
    SimpleDraweeView headView;

    @BindView(R.id.nested_recycle_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.hot_list_title)
    TextView mTitle;

    @OnClick(R.id.hot_list_back)
    void back() {
        finish();
    }

    @BindView(R.id.parent_view)
    CoordinatorLayout mCoordinatorLayout;

    private List<SonglistBean> mHotList;
    private List<MusicEntity> mList = new ArrayList<>();
    private SongListAdapter adapter = null;


    public static void startActivity(Context context, String topid, String title) {
        Intent intent = new Intent(context, NetSongListActivity.class);
        intent.putExtra(TOPID, topid);
        intent.putExtra(TITLE, title);
        ((BaseActivity) context).startActivityByX(intent, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_net);
        adapter = new SongListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String topid = intent.getStringExtra(TOPID);
        String title = intent.getStringExtra(TITLE);
        mTitle.setText(title);
        HotListResquest hotListResquest = new HotListResquest(topid);
        MyOkHttpClient.getInstance(this).sendNet(hotListResquest, new BaseCallback<HotListResponse>() {
            @Override
            public void onSuccess(int statusCode, HotListResponse response) {
                if (response.getShowapi_res_code() == 0) {
                    mHotList = response.getShowapi_res_body().getPagebean().getSonglist();
                    headView.setImageURI(mHotList.get(0).getAlbumpic_big());
                    String color_hex = Integer.toHexString(response.getShowapi_res_body().getPagebean().getColor());
                    String color = null;
                    if (color_hex.length() < 6) {
                        color = color_hex;
                        for (int i = 0; i < 6 - color_hex.length(); i++) {
                            color = "0" + color;
                        }
                        color = "#ff" + color;
                    } else if (color_hex.length() == 6) {
                        color = "#ff" + Integer.toHexString(response.getShowapi_res_body().getPagebean().getColor());
                    }
                    MyLog.i(TAG, color + "---" + response.getShowapi_res_body().getPagebean().getColor());
                    mRecyclerView.setBackgroundColor(Color.parseColor(color));
                    relativeLayout.setBackgroundColor(Color.parseColor(color));

                    for (int i = 0; i < mHotList.size(); i++) {
                        MusicEntity musicInfo = new MusicEntity();
                        musicInfo.songId = mHotList.get(i).getSongid();
                        musicInfo.musicName = mHotList.get(i).getSongname();
                        musicInfo.artist = mHotList.get(i).getSingername();
                        musicInfo.islocal = false;
                        musicInfo.albumName = mHotList.get(i).getAlbummid();
                        musicInfo.albumId = mHotList.get(i).getAlbumid();
                        musicInfo.artistId = mHotList.get(i).getSingerid();
                        musicInfo.albumPic = mHotList.get(i).getAlbumpic_big();
                        mList.add(musicInfo);
                    }
                    adapter.updateList(mList);
                }
            }

            @Override
            public void onFailure(int code, String error_msg) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
    }
}
