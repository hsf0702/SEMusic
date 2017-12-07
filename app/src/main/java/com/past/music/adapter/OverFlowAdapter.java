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
 * Created by wm on 2016/2/21.
 */
public class OverFlowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<OverFlowItem> mList;
    private List<MusicEntity> musicInfos;
    private Context mContext;
    private Context activity;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public OverFlowAdapter(Context activity, List<OverFlowItem> list, List<MusicEntity> info) {
        mList = list;
        musicInfos = info;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_flow_layout, parent, false);
        ListItemViewHolder vh = new ListItemViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
        //return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_flow_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OverFlowItem minfo = mList.get(position);
        ((ListItemViewHolder) holder).icon.setImageResource(minfo.getAvatar());
        ((ListItemViewHolder) holder).title.setText(minfo.getTitle());
        //设置tag
        ((ListItemViewHolder) holder).itemView.setTag(position + "");

    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //定义接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView title;

        ListItemViewHolder(View view) {
            super(view);
            this.icon = view.findViewById(R.id.pop_list_view);
            this.title = view.findViewById(R.id.pop_list_item);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

    }

}
