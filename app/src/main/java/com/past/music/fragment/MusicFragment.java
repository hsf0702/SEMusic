package com.past.music.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.past.music.Config.BaseConfig;
import com.past.music.adapter.GridViewAdapter;
import com.past.music.api.Test;
import com.past.music.pastmusic.R;
import com.past.music.utils.FrescoImageLoader;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.youth.banner.Banner;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MusicFragment extends Fragment {

    private static final String TAG = "MusicFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Subscription mSubscription;
    private String mParam1;
    private String mParam2;

    private Banner banner;
    private GridView mGridView;
    private List<String> images = new ArrayList<>();
    private List<Test.ShowapiResBodyBean.PagebeanBean.SonglistBean> mData = new ArrayList<>();

    private GridViewAdapter mGridViewAdapter;

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
        images.add("http://cimg2.163.com/catchimg/20090930/8458904_45.jpg");
        images.add("http://img1.imgtn.bdimg.com/it/u=2119707315,3199660736&fm=23&gp=0.jpg");
        images.add("http://img1.imgtn.bdimg.com/it/u=2504464883,3611462034&fm=23&gp=0.jpg");
        images.add("http://www.qqai.net/uploads/i_2_192535384x1019546146_21.jpg");
        Log.i(TAG, "111" + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
        banner = (Banner) view.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new FrescoImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridViewAdapter = new GridViewAdapter(getContext(), mData);
        mGridView.setAdapter(mGridViewAdapter);

        Map<String, String> params = new HashMap<>();
        params.put("showapi_appid", BaseConfig.APPID);
        params.put("showapi_sign", BaseConfig.SECRET);
        params.put("topid", "5");
        MyOkHttp.get().post(getContext(), BaseConfig.QQ_MUSIC_URL, params, new GsonResponseHandler<Test>() {

            @Override
            public void onFailure(int statusCode, String error_msg) {
            }

            @Override
            public void onSuccess(int statusCode, Test response) {
                for (int i = 0; i < 6; i++) {
                    mData.add(response.getShowapi_res_body().getPagebean().getSonglist().get(i));
                }
                mGridViewAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
