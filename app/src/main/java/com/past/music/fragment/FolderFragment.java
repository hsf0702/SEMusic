package com.past.music.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.past.music.entity.FolderEntity;
import com.past.music.pastmusic.R;
import com.past.music.utils.MusicUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaojin on 2017/4/7.
 */

public class FolderFragment extends BaseFragment {

    @BindView(R.id.local_folder_recycle)
    RecyclerView mRecyclerView;

    FolderAdapter mAdapter;
    ArrayList<FolderEntity> list = new ArrayList<>();

    public static FolderFragment newInstance() {
        return new FolderFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new FolderAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        reloadAdapter();
        return view;
    }

    @Override
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                list.addAll(MusicUtils.Companion.queryFolder(getContext()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    class FolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((FolderViewHolder) holder).onBind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.folder_name)
        TextView mFolderName;

        @BindView(R.id.folder_info)
        TextView mFolderInfo;

        public FolderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(FolderEntity folderEntity) {
            mFolderName.setText(folderEntity.getFolder_name());
            mFolderInfo.setText(folderEntity.getFolder_count() + "," + folderEntity.getFolder_path());
        }
    }
}
