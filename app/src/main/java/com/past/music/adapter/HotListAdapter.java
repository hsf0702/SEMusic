package com.past.music.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.past.music.pastmusic.R;
import com.past.music.widget.IconView;

import java.util.ArrayList;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/22 20:36
 * 描述：
 * 备注：
 * =======================================================
 */
public class HotListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mList;

    public HotListAdapter(Context context, ArrayList<String> arrayList) {
        this.mContext = context;
        this.mList = arrayList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        IconView iconView = null;
        if (view == null) {
            iconView = new IconView(mContext);
        } else {
            iconView = (IconView) view;
        }
        // 设置GridView的显示的个子的间距
        iconView.setLayoutParams(new GridView.LayoutParams(10, 10));
        iconView.setmTvDes("你的青春，肯定会有周杰伦");
        iconView.setBackgroundResource(R.drawable.avatar);
        return iconView;
    }
}
