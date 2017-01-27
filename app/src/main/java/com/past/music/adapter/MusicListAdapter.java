package com.past.music.adapter;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.past.music.entity.Mp3Info;
import com.past.music.pastmusic.R;
import com.past.music.utils.Mp3Utils;

import java.util.List;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 上午12:53
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Mp3Info> mList = null;
    private Context mContext = null;

    public MusicListAdapter(Context context) {
        this.mContext = context;
        mList = Mp3Utils.getMp3Infos(context);
    }

    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view, Mp3Info mp3Info, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicDataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MusicDataHolder) holder).bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MusicDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mMusicName = null;
        private TextView mArtist = null;
        private RelativeLayout rootView = null;

        public MusicDataHolder(View itemView) {
            super(itemView);
            rootView = (RelativeLayout) itemView.findViewById(R.id.root_view);
            mMusicName = (TextView) itemView.findViewById(R.id.music_name);
            mArtist = (TextView) itemView.findViewById(R.id.artist);

            rootView.setOnClickListener(this);
        }

        public void bindView(Mp3Info mp3Info) {
            mMusicName.setText(mp3Info.getTitle());
            mArtist.setText(mp3Info.getArtist());
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(itemView, mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }
}
