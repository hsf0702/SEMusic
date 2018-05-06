package com.past.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.past.music.entity.MusicEntity;
import com.past.music.entity.OverFlowItem;
import com.past.music.pastmusic.R;

import java.util.List;

/**
 * Created by gaojin on 2018/2/21.
 */
public class MusicFlowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<OverFlowItem> mList;
    private MusicEntity musicInfo;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public MusicFlowAdapter(Context context, List<OverFlowItem> list, MusicEntity info) {
        mList = list;
        musicInfo = info;
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_flow_layout, parent, false);
        ListItemViewHolder vh = new ListItemViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OverFlowItem minfo = mList.get(position);
        ((ListItemViewHolder) holder).icon.setImageResource(minfo.getAvatar());
        ((ListItemViewHolder) holder).title.setText(minfo.getTitle());
        ((ListItemViewHolder) holder).itemView.setTag(position + "");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        ListItemViewHolder(View view) {
            super(view);
            this.icon = view.findViewById(R.id.pop_list_view);
            this.title = view.findViewById(R.id.pop_list_item);

        }


    }

}
