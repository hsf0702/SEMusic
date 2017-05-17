package com.past.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neu.gaojin.MyOkHttpClient;
import com.neu.gaojin.response.BaseCallback;
import com.past.music.adapter.MusicContentAdapter;
import com.past.music.api.HotListResponse;
import com.past.music.api.HotListResquest;
import com.past.music.api.SonglistBean;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MusicFragment extends Fragment {

    private static final String TAG = "MusicFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    @BindView(R.id.music_recycle_view)
    RecyclerView mMusicList = null;

    private List<String> images = new ArrayList<>();
    private List<SonglistBean> mRecommendList;

    private MusicContentAdapter mAdapter;

    public MusicFragment() {

    }

    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new MusicContentAdapter(getActivity());
        mMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMusicList.setHasFixedSize(true);
        mMusicList.setAdapter(mAdapter);
        HotListResquest hotListResquest = new HotListResquest("26");
        MyOkHttpClient.getInstance(getContext()).sendNet(hotListResquest, new BaseCallback<HotListResponse>() {
            @Override
            public void onSuccess(int statusCode, HotListResponse response) {
                if (response.getShowapi_res_code() == 0) {
                    mRecommendList = response.getShowapi_res_body().getPagebean().getSonglist();
                    mAdapter.updateRecommendList(mRecommendList);
                } else {
                    MyLog.i(TAG, "请求失败" + response.getShowapi_res_code() + response.getShowapi_res_error());
                }
            }

            @Override
            public void onFailure(int code, String error_msg) {
                int a = 10;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
//        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
//        banner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
