package com.past.music.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.past.music.database.provider.DownFileStore;
import com.past.music.entity.DownloadDBEntity;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;
import com.past.music.download.DownService;
import com.past.music.utils.MConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/6/8 16:54
 * 描述：
 * 备注：
 * =======================================================
 */
public class DownFragment extends BaseFragment {


    public static DownFragment newInstance() {
        DownFragment fragment = new DownFragment();
        return fragment;
    }

    private ArrayList<DownloadDBEntity> mList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private DownLoadAdapter adapter;
    private DownFileStore downFileStore;
    private DownStatus downStatus;
    private int downPosition = -1;
    private String TAG = "DownFragment";
    private boolean d = true;
    public Activity mContext;

    @BindView(R.id.down_start_all)
    LinearLayout allStart;

    @BindView(R.id.down_pause_all)
    LinearLayout allStop;

    @BindView(R.id.down_clear_all)
    LinearLayout clear;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @OnClick(R.id.down_start_all)
    void start_all() {
        Intent intent = new Intent(getContext(), DownService.class);
        intent.setAction(DownService.START_ALL_DOWNTASK);
        intent.setPackage(MConstants.PACKAGE);
        mContext.startService(intent);
    }

    @OnClick(R.id.down_pause_all)
    void pause_all() {
        Intent intent = new Intent(getContext(), DownService.class);
        intent.setAction(DownService.PAUSE_ALLTASK);
        intent.setPackage(MConstants.PACKAGE);
        mContext.startService(intent);
    }

    @OnClick(R.id.down_clear_all)
    void clear_all() {
        Intent intent = new Intent(getContext(), DownService.class);
        intent.setAction(DownService.CANCLE_ALL_DOWNTASK);
        intent.setPackage(MConstants.PACKAGE);
        mContext.startService(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_down, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DownLoadAdapter(null, null);
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        reload();
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        downStatus = new DownStatus();
        IntentFilter f = new IntentFilter();
        f.addAction(DownService.TASK_STARTDOWN);
        f.addAction(DownService.UPDATE_DOWNSTAUS);
        f.addAction(DownService.TASKS_CHANGED);
        mContext.registerReceiver(downStatus, new IntentFilter(f));
    }

    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(downStatus);
    }


    private void reload() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                downFileStore = DownFileStore.getInstance(mContext);
                mList = downFileStore.getDownLoadedListAllDowning();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.update(mList, DownService.getPrepareTasks());
            }
        }.execute();
    }


    class DownLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList mList;
        private ArrayList<String> currentTaskList;
        private long completed = 0;
        private long totalsize = -1;


        public DownLoadAdapter(ArrayList list, ArrayList<String> currentTaskList) {
            mList = list;
            this.currentTaskList = currentTaskList;
        }

        public void update(ArrayList list, ArrayList<String> currentTaskList) {
            mList = list;
            this.currentTaskList = currentTaskList;
            completed = 0;
            totalsize = -1;
            notifyDataSetChanged();
        }

        public void notifyItem(long completed, long total) {
            this.completed = completed;
            if (total != -1)
                this.totalsize = total;
            notifyItemChanged(downPosition);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_down_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            boolean isCurrent = false;
            boolean isPreparing = false;
            final DownloadDBEntity task = (DownloadDBEntity) mList.get(position);
            ((ItemViewHolder) holder).title.setText(task.getFileName());

            if (currentTaskList.size() > 0) {
                isCurrent = currentTaskList.get(0).equals(task.getDownloadId());
                if (isCurrent) {
                    downPosition = position;
                }
                if (currentTaskList.contains(task.getDownloadId())) {
                    isPreparing = true;
                }
            }
            if (isCurrent) {
                completed = completed > task.getCompletedSize() ? completed : task.getCompletedSize();
                totalsize = totalsize > task.getTotalSize() ? totalsize : task.getTotalSize();
                if (completed == 0 || totalsize == -1) {
                    ((ItemViewHolder) holder).count.setText("正在计算大小文件大小");
                    ((ItemViewHolder) holder).progressBar.setVisibility(View.GONE);
                } else {
                    ((ItemViewHolder) holder).count.setText((float) (Math.round((float) completed / (1024 * 1024) * 10)) / 10 + "M/" +
                            (float) (Math.round((float) totalsize / (1024 * 1024) * 10)) / 10 + "M");
                    if (((ItemViewHolder) holder).progressBar.getVisibility() != View.VISIBLE)
                        ((ItemViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                    if (totalsize > 0)
                        ((ItemViewHolder) holder).progressBar.setProgress((int) (100 * completed / totalsize));
                }
            } else if (isPreparing) {
                ((ItemViewHolder) holder).progressBar.setVisibility(View.GONE);
                ((ItemViewHolder) holder).count.setText(task.getArtist() + "-" + task.getFileName());
            } else {
                ((ItemViewHolder) holder).progressBar.setVisibility(View.GONE);
                ((ItemViewHolder) holder).count.setText("已经暂停，点击继续下载");
            }

            final boolean isPreparing1 = isPreparing;
            ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPreparing1) {
                        Intent intent = new Intent(DownService.PAUSE_TASK);
                        intent.setPackage(MConstants.PACKAGE);
                        intent.putExtra("downloadid", task.getDownloadId());
                        mContext.startService(intent);
                    } else {
                        Intent intent = new Intent(DownService.RESUME_START_DOWNTASK);
                        intent.setPackage(MConstants.PACKAGE);
                        intent.putExtra("downloadid", task.getDownloadId());
                        mContext.startService(intent);
                    }

                }
            });

            ((ItemViewHolder) holder).clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext).setTitle("要清除下载吗")
                            .setPositiveButton(mContext.getString(R.string.sure), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DownService.CANCLE_DOWNTASK);
                                    intent.putExtra("downloadid", task.getDownloadId());
                                    intent.setPackage(MConstants.PACKAGE);
                                    mContext.startService(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });


        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView draweeView;
            ImageView downloaded, clear;
            TextView title, count, artist;
            ProgressBar progressBar;

            public ItemViewHolder(View itemView) {
                super(itemView);
                draweeView = itemView.findViewById(R.id.down_img);
                title = itemView.findViewById(R.id.down_top_text);
                count = itemView.findViewById(R.id.down_count);
                clear = itemView.findViewById(R.id.down_single_clear);
                artist = itemView.findViewById(R.id.down_artist);
                downloaded = itemView.findViewById(R.id.downloaded);
                progressBar = itemView.findViewById(R.id.down_progress);
                progressBar.setMax(100);

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        }


    }

    private class DownStatus extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DownService.UPDATE_DOWNSTAUS:
                    adapter.notifyItem(intent.getLongExtra("completesize", 0), intent.getLongExtra("totalsize", -1));
                    break;
                case DownService.TASK_STARTDOWN:
                    adapter.notifyItem(intent.getLongExtra("completesize", 0), intent.getLongExtra("totalsize", -1));
                    break;
                case DownService.TASKS_CHANGED:
                    reload();
                    MyLog.i("111111", " reload()");
                    break;

            }
        }
    }
}
