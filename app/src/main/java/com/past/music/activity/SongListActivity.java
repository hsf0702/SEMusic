package com.past.music.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.library.StatusBarUtil;
import com.neu.gaojin.MyOkHttpClient;
import com.neu.gaojin.response.BaseCallback;
import com.past.music.adapter.SongListAdapter;
import com.past.music.api.HotListResponse;
import com.past.music.api.HotListResquest;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;

import java.util.List;

import butterknife.BindView;

public class SongListActivity extends BaseActivity {

    public static final String TAG = "SongListActivity";
    public static final String TOPID = "TOPID";
    public static final String TITLE = "TITLE";
    @BindView(R.id.view_need_offset)
    View mViewNeedOffset;

    @BindView(R.id.head_image)
    SimpleDraweeView headView;

    @BindView(R.id.nested_recycle_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.parent_view)
    CoordinatorLayout mCoordinatorLayout;

    private List<HotListResponse.ShowapiResBodyBean.PagebeanBean.SonglistBean> mHotList;
    private SongListAdapter adapter = null;


    public static void startActivity(Context context, String topid, String title) {
        Intent intent = new Intent(context, SongListActivity.class);
        intent.putExtra(TOPID, topid);
        intent.putExtra(TITLE, title);
        ((BaseActivity) context).startActivityByX(intent, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        adapter = new SongListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setBackgroundColor(0x422281);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String topid = intent.getStringExtra(TOPID);
        String title = intent.getStringExtra(TITLE);
        setTitle(title);
        HotListResquest hotListResquest = new HotListResquest(topid);
        MyOkHttpClient.getInstance(this).sendNet(hotListResquest, new BaseCallback<HotListResponse>() {
            @Override
            public void onSuccess(int statusCode, HotListResponse response) {
                if (response.getShowapi_res_code() == 0) {
                    mHotList = response.getShowapi_res_body().getPagebean().getSonglist();
                    headView.setImageURI(mHotList.get(0).getAlbumpic_big());
                    MyLog.i(TAG, mHotList.size() + "ff");
                    adapter.updateList(mHotList);
//                    mCoordinatorLayout.setBackgroundColor();
                    // TODO: 2017/4/24
//                    mRecyclerView.setBackgroundColor(Color.parseColor("0x" + Integer.toHexString(response.getShowapi_res_body().getPagebean().getColor())));

//                    mRecyclerView.setBackgroundColor(response.getShowapi_res_body().getPagebean().getColor());

                }
            }

            @Override
            public void onFailure(int code, String error_msg) {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        mViewNeedOffset = findViewById(R.id.view_need_offset);
        StatusBarUtil.setTranslucentForImageView(this, 0, mViewNeedOffset);
    }
}
