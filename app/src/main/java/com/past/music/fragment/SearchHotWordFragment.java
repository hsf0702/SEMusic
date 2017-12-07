package com.past.music.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.past.music.api.SearchSongInfo;
import com.past.music.download.BMA;
import com.past.music.adapter.RecentSearchAdapter;
import com.past.music.pastmusic.R;
import com.past.music.utils.HttpUtil;
import com.past.music.utils.NetworkUtils;
import com.past.music.widget.WidgetController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/6/10 21:46
 * 描述：
 * 备注：
 * =======================================================
 */
public class SearchHotWordFragment extends KtAttachFragment implements View.OnClickListener, SearchWords {
    String[] texts = new String[10];
    ArrayList<TextView> views = new ArrayList<>();
    SearchWords searchWords;
    private RecentSearchAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isFromCache = true;

    private List searchResults = Collections.emptyList();
    private ArrayList<SearchSongInfo> songList = new ArrayList<>();
    View loadview;
    FrameLayout frameLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.load_framelayout, container, false);
        frameLayout = view.findViewById(R.id.loadframe);
        loadview = LayoutInflater.from(getMContext()).inflate(R.layout.loading, frameLayout, false);
        frameLayout.addView(loadview);
        loadWords();

        return view;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void loadWords() {
        new AsyncTask<Boolean, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Boolean... params) {
                if (NetworkUtils.isConnectInternet(getMContext())) {
                    isFromCache = false;
                }


                try {
                    JsonArray jsonArray = HttpUtil.getResposeJsonObject(BMA.Search.hotWord(), getMContext(), isFromCache).get("result").getAsJsonArray();
                    for (int i = 0; i < 10; i++) {
                        texts[i] = jsonArray.get(i).getAsJsonObject().get("word").getAsString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean load) {
                super.onPostExecute(load);
                if (!load && getMContext() == null) {

                    return;
                }
                View view = LayoutInflater.from(getMContext()).inflate(R.layout.fragment_search_hot_words, frameLayout, false);
                recyclerView = view.findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getMContext()));
                recyclerView.setHasFixedSize(true);
                adapter = new RecentSearchAdapter(getMContext());
                adapter.setListenter(SearchHotWordFragment.this);
                recyclerView.setAdapter(adapter);

                TextView text1 = view.findViewById(R.id.text1);
                TextView text2 = view.findViewById(R.id.text2);
                TextView text3 = view.findViewById(R.id.text3);
                TextView text4 = view.findViewById(R.id.text4);
                TextView text5 = view.findViewById(R.id.text5);
                TextView text6 = view.findViewById(R.id.text6);
                TextView text7 = view.findViewById(R.id.text7);
                TextView text8 = view.findViewById(R.id.text8);
                TextView text9 = view.findViewById(R.id.text9);
                TextView text10 = view.findViewById(R.id.text10);
                views.add(text1);
                views.add(text2);
                views.add(text3);
                views.add(text4);
                views.add(text5);
                views.add(text6);
                views.add(text7);
                views.add(text8);
                views.add(text9);
                views.add(text10);


                frameLayout.removeAllViews();
                frameLayout.addView(view);

                int w = getMContext().getResources().getDisplayMetrics().widthPixels;
                int xdistance = -1;
                int ydistance = 0;
                int distance = dip2px(getMContext(), 16);
                for (int i = 0; i < 10; i++) {
                    views.get(i).setOnClickListener(SearchHotWordFragment.this);
                    views.get(i).setText(texts[i]);
                    if (xdistance == -1) {
                        xdistance = 0;
                        WidgetController.setLayout(views.get(i), xdistance, ydistance);
                        continue;
                    }
                    xdistance += WidgetController.getWidth(views.get(i - 1)) + distance;
                    if (xdistance + WidgetController.getWidth(views.get(i)) + distance > w) {
                        xdistance = -1;
                        ydistance += 120;
                        i--;
                        continue;
                    }
                    WidgetController.setLayout(views.get(i), xdistance, ydistance);
                }
            }
        }.execute();

    }

    public void searchWords(SearchWords searchWords) {
        this.searchWords = searchWords;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text1:
                searchWords.onSearch(texts[0]);
                break;
            case R.id.text2:
                searchWords.onSearch(texts[1]);
                break;
            case R.id.text3:
                searchWords.onSearch(texts[2]);
                break;
            case R.id.text4:
                searchWords.onSearch(texts[3]);
                break;
            case R.id.text5:
                searchWords.onSearch(texts[4]);
                break;
            case R.id.text6:
                searchWords.onSearch(texts[5]);
                break;
            case R.id.text7:
                searchWords.onSearch(texts[6]);
                break;
            case R.id.text8:
                searchWords.onSearch(texts[7]);
                break;
            case R.id.text9:
                searchWords.onSearch(texts[8]);
                break;
            case R.id.text10:
                searchWords.onSearch(texts[9]);
                break;
        }
    }

    @Override
    public void onSearch(String t) {
        if (searchWords != null)
            searchWords.onSearch(t);
    }
}
