package com.past.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.past.music.MyApplication;
import com.past.music.adapter.MyContentAdapter;
import com.past.music.entity.SongListEntity;
import com.past.music.event.CreateSongListEvent;
import com.past.music.pastmusic.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.recycle_layout)
    RecyclerView mMusicList = null;

    private MyContentAdapter mAdapter = null;

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
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_easy, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new MyContentAdapter(getActivity());
        mMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMusicList.setHasFixedSize(true);
        mMusicList.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CreateSongListEvent event) {
        List<SongListEntity> mList = MyApplication.songListDBService.query();
        mAdapter.setSongList(mList);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
