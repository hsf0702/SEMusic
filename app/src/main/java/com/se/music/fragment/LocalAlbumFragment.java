package com.se.music.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.se.music.R;
import com.se.music.base.BaseFragment;
import com.se.music.common.entity.AlbumEntity;
import com.se.music.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalAlbumFragment extends BaseFragment {

    @BindView(R.id.local_album_recycle)
    RecyclerView mRecyclerView;

    private List<AlbumEntity> list = new ArrayList<>();

    private LocalAlbumFragmentAdapter mAdapter;

    public LocalAlbumFragment() {
    }

    public static LocalAlbumFragment newInstance() {
        LocalAlbumFragment fragment = new LocalAlbumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_album, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new LocalAlbumFragmentAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    //更新adapter界面
    @Override
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                list.addAll(MusicUtils.Companion.queryAlbums(getContext()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private class LocalAlbumFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SingerItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.singer_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SingerItemViewHolder) holder).onBindData(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class SingerItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_singer)
        SimpleDraweeView simpleDraweeView;

        @BindView(R.id.singer_list_title)
        TextView mTitle;

        @BindView(R.id.singer_list_info)
        TextView mInfo;

        public SingerItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.rl_singer_item)
        void click() {
        }

        public void onBindData(AlbumEntity albumEntity) {
            mTitle.setText(albumEntity.getAlbumName());
            mInfo.setText(albumEntity.getNumberOfSongs() + "首");
            simpleDraweeView.setImageURI(albumEntity.getAlbumArt() + "");
        }
    }
}
