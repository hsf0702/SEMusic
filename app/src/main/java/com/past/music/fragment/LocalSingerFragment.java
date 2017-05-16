package com.past.music.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.neu.gaojin.MyOkHttpClient;
import com.neu.gaojin.response.BaseSuccessCallback;
import com.past.music.MyApplication;
import com.past.music.activity.PlayListInfoActivity;
import com.past.music.api.AvatarRequest;
import com.past.music.api.AvatarResponse;
import com.past.music.entity.ArtistEntity;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;
import com.past.music.utils.MConstants;
import com.past.music.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LocalSingerFragment extends BaseFragment {

    @BindView(R.id.local_singer_recycle)
    RecyclerView mRecyclerView;

    private List<ArtistEntity> list = new ArrayList<>();

    private LocalSingerFragmentAdapter mAdapter;

    public static LocalSingerFragment newInstance() {
        LocalSingerFragment fragment = new LocalSingerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_singer, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new LocalSingerFragmentAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                list.clear();
                list.addAll(MusicUtils.queryArtist(mContext));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private class LocalSingerFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

        @OnClick(R.id.rl_singer_item)
        void click() {
            PlayListInfoActivity.startActivity(getContext(), list.get(getAdapterPosition()).artist_name
                    , String.valueOf(list.get(getAdapterPosition()).artist_id), MConstants.START_FROM_ARTIST);
        }

        public SingerItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBindData(final ArtistEntity artistEntity) {
            mTitle.setText(artistEntity.artist_name);
            mInfo.setText(artistEntity.getNumber_of_tracks() + "首");
            AvatarRequest avatarRequest = new AvatarRequest();
            avatarRequest.setArtist(artistEntity.getArtist_name().replace(";", " "));
            if (MyApplication.imageDBService.query(artistEntity.getArtist_name().replace(";", "")) == null) {
                MyOkHttpClient.getInstance(getContext()).sendNet(avatarRequest, new BaseSuccessCallback<AvatarResponse>() {
                    @Override
                    public void onSuccess(int statusCode, final AvatarResponse response) {
                        MyLog.i("onSuccess", statusCode + "");
                        MyApplication.imageDBService.insert(artistEntity.getArtist_name().replace(";", ""), response.getArtist().getImage().get(2).get_$Text112());
                        simpleDraweeView.setImageURI(response.getArtist().getImage().get(2).get_$Text112());
                    }
                });
            } else {
                simpleDraweeView.setImageURI(MyApplication.imageDBService.query(artistEntity.getArtist_name()));
            }
        }
    }
}
