package com.past.music.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.past.music.Config.BaseConfig;
import com.past.music.adapter.MusicListAdapter;
import com.past.music.entity.Mp3Info;
import com.past.music.pastmusic.R;
import com.past.music.service.PlayerService;
import com.past.music.utils.Mp3Utils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MineFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //权限申请标志码
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x01;

    private String mParam1;
    private String mParam2;

    private RecyclerView mMusicList = null;
    private MusicListAdapter adapter = null;

    public MineFragment() {
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_easy, container, false);
        mMusicList = (RecyclerView) view.findViewById(R.id.recycle_music_list);
        adapter = new MusicListAdapter(getActivity());
        mMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMusicList.setHasFixedSize(true);
        mMusicList.setAdapter(adapter);
        adapter.setClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Mp3Info mp3Info, int position) {
                Log.d("mp3Info-->", mp3Info.toString());
                Intent intent = new Intent();
                intent.putExtra(BaseConfig.URL, mp3Info.getUrl());
                intent.putExtra(BaseConfig.MSG, BaseConfig.PlayerMsg.PLAY_MSG);
                intent.setClass(getActivity(), PlayerService.class);
                getActivity().startService(intent);       //启动服务
            }
        });

        getPermission();
        return view;
    }

    private void createObservable() {
        Observable<List<Mp3Info>> listObservable = Observable.just(getMusicList());

        listObservable.subscribe(new Observer<List<Mp3Info>>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Mp3Info> music) {
                adapter.setListItem(music);
            }
        });

    }

    private List<Mp3Info> getMusicList() {
        List<Mp3Info> mList;
        mList = Mp3Utils.getMp3Infos(getContext());
        return mList;
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            createObservable();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createObservable();
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
