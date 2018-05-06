package com.past.music.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.past.music.dialog.MusicQueueFragment;
import com.past.music.fragment.RoundFragment;
import com.past.music.lrc.DefaultLrcParser;
import com.past.music.lrc.LrcRow;
import com.past.music.lrc.LrcView;
import com.past.music.pastmusic.R;
import com.past.music.service.MediaService;
import com.past.music.service.MusicPlayer;
import com.past.music.utils.HandlerUtil;
import com.past.music.utils.ImageUtils;
import com.past.music.utils.MusicUtils;
import com.past.music.widget.AlbumViewPager;
import com.past.music.widget.PlayerSeekBar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayMusicActivity extends BaseActivity {

    private static final int NEXT_MUSIC = 0;
    private static final int PRE_MUSIC = 1;
    private static final int VIEWPAGER_SCROLL_TIME = 390;
    private static final int TIME_DELAY = 500;

    private Handler mPlayHandler;
    private PlayMusic mPlayThread;
    private ObjectAnimator mNeedleAnim, mRotateAnim;
    private AnimatorSet mAnimatorSet;
    private long lastAlbum;
    private Handler mHandler;
    private Bitmap mBitmap;
    private BitmapFactory.Options mNewOpts;
    private ActionBar ab;
    private FragmentAdapter mAdapter;
    private boolean isNextOrPreSetPage = false; //判断viewpager由手动滑动 还是setcruuentitem换页
    private WeakReference<View> mViewWeakReference = new WeakReference<View>(null);
    private View mActiveView;

    @BindView(R.id.playing_mode)
    ImageView mPlayMode;

    @OnClick(R.id.playing_mode)
    void changeMode() {
        MusicPlayer.Companion.cycleRepeat();
        updatePlaymode();
    }

    @BindView(R.id.playing_play)
    ImageView mPlay;

    @OnClick(R.id.playing_next)
    void next() {
        if (mRotateAnim != null) {
            mRotateAnim.end();
            mRotateAnim = null;
        }
        Message msg = new Message();
        msg.what = NEXT_MUSIC;
        mPlayHandler.sendMessage(msg);
    }

    @OnClick(R.id.playing_pre)
    void pre() {
        Message msg = new Message();
        msg.what = PRE_MUSIC;
        mPlayHandler.sendMessage(msg);

    }

    @OnClick(R.id.playing_play)
    void play() {
        if (MusicPlayer.Companion.getIsPlaying()) {
            mPlay.setImageResource(R.drawable.play_rdi_btn_pause);
        } else {
            mPlay.setImageResource(R.drawable.play_rdi_btn_play);
        }
        if (MusicPlayer.Companion.getQueueSize() != 0) {
            MusicPlayer.Companion.playOrPause();
        }
        mHandler.postDelayed(mUpdateProgress, 0);
    }

    @OnClick(R.id.playing_playlist)
    void play_list() {
        MusicQueueFragment.newInstance().show(getSupportFragmentManager(), "music_queue");
    }

    @BindView(R.id.play_seek)
    PlayerSeekBar mProgress;

    @BindView(R.id.music_duration_played)
    TextView mTimePlayed;

    @BindView(R.id.music_duration)
    TextView mDuration;

    @BindView(R.id.lrcview)
    LrcView mLrcView;

    @BindView(R.id.frame_content)
    FrameLayout mBackAlbum;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_pager)
    AlbumViewPager mViewPager;

    @BindView(R.id.lrcviewContainer)
    RelativeLayout mLrcViewContainer;

    @BindView(R.id.headerView)
    FrameLayout mAlbumLayout;

    @BindView(R.id.music_tool)
    LinearLayout mMusicTool;

    @BindView(R.id.tragetlrc)
    TextView mTryGetLrc;

    @BindView(R.id.needle)
    ImageView mNeedle;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        showQuickControl(false);
        onNewIntent(getIntent());

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.actionbar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        mNeedleAnim = ObjectAnimator.ofFloat(mNeedle, "rotation", -25, 0);
        mNeedleAnim.setDuration(200);
        mNeedleAnim.setRepeatMode(Animation.ABSOLUTE);
        mNeedleAnim.setInterpolator(new LinearInterpolator());


        mProgress.setIndeterminate(false);
        mProgress.setProgress(1);
        mProgress.setMax(1000);
        setSeekBarListener();
        setViewPager();
        initLrcView();

        mHandler = HandlerUtil.Companion.getInstance();
        mHandler.removeCallbacks(mUpAlbumRunnable);
        mHandler.postDelayed(mUpAlbumRunnable, 0);
        mPlayThread = new PlayMusic();
        mPlayThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastAlbum = -1;
        if (MusicPlayer.Companion.isTrackLocal())
            updateBuffer(100);
        else {
            updateBuffer(MusicPlayer.Companion.secondPosition());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void setStatusBar() {
    }

    @Override
    public void showQuickControl(boolean show) {
    }

    @SuppressLint("WrongConstant")
    @Override
    public void baseUpdatePlayInfo() {
        if (MusicPlayer.Companion.getQueueSize() == 0) {
            return;
        }
        Fragment fragment = (RoundFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
        if (fragment != null) {
            View v = fragment.getView();
            if (mViewWeakReference.get() != v && v != null) {
                ((ViewGroup) v).setAnimationCacheEnabled(false);
                if (mViewWeakReference != null)
                    mViewWeakReference.clear();
                mViewWeakReference = new WeakReference<View>(v);
                mActiveView = mViewWeakReference.get();
            }
        }
        if (mActiveView != null) {
            mRotateAnim = (ObjectAnimator) mActiveView.getTag(R.id.tag_animator);
        }
        mAnimatorSet = new AnimatorSet();
        if (MusicPlayer.Companion.getIsPlaying()) {
            mProgress.removeCallbacks(mUpdateProgress);
            mProgress.postDelayed(mUpdateProgress, 200);
            mPlay.setImageResource(R.drawable.play_rdi_btn_pause);
            if (mAnimatorSet != null && mRotateAnim != null && !mRotateAnim.isRunning()) {
                //修复从playactivity回到Main界面null
                if (mNeedleAnim == null) {
                    mNeedleAnim = ObjectAnimator.ofFloat(mNeedle, "rotation", -30, 0);
                    mNeedleAnim.setDuration(200);
                    mNeedleAnim.setRepeatMode(0);
                    mNeedleAnim.setInterpolator(new LinearInterpolator());
                }
                mAnimatorSet.play(mNeedleAnim).before(mRotateAnim);
                mAnimatorSet.start();
            }
        } else {
            mProgress.removeCallbacks(mUpdateProgress);
            mPlay.setImageResource(R.drawable.play_rdi_btn_play);
            if (mNeedleAnim != null) {
                mNeedleAnim.reverse();
                mNeedleAnim.end();
            }

            if (mRotateAnim != null && mRotateAnim.isRunning()) {
                mRotateAnim.cancel();
                float valueAvatar = (float) mRotateAnim.getAnimatedValue();
                mRotateAnim.setFloatValues(valueAvatar, 360f + valueAvatar);
            }
        }
        mDuration.setText(MusicUtils.Companion.makeShortTimeString(PlayMusicActivity.this.getApplication(), MusicPlayer.Companion.duration() / 1000));
        isNextOrPreSetPage = false;
        if (MusicPlayer.Companion.getQueuePosition() + 1 != mViewPager.getCurrentItem()) {
            mViewPager.setCurrentItem(MusicPlayer.Companion.getQueuePosition() + 1);
            isNextOrPreSetPage = true;
        }
    }

    @Override
    public void updateTrack() {
        mHandler.removeCallbacks(mUpAlbumRunnable);
        mHandler.postDelayed(mUpAlbumRunnable, 0);

        updateLrc();
        ab.setTitle(MusicPlayer.Companion.getTrackName());
        ab.setSubtitle(MusicPlayer.Companion.getArtistName());
        mDuration.setText(MusicUtils.Companion.makeShortTimeString(PlayMusicActivity.this.getApplication(), MusicPlayer.Companion.duration() / 1000));
    }

    @Override
    public void updateBuffer(int p) {
        mProgress.setSecondaryProgress(p * 10);
    }

    @Override
    public void loading(boolean l) {
        mProgress.setLoading(l);
    }

    private void initLrcView() {
        mLrcView.setOnSeekToListener(onSeekToListener);
        mLrcView.setOnLrcClickListener(onLrcClickListener);
        mViewPager.setOnSingleTouchListener(new AlbumViewPager.OnSingleTouchListener() {
            @Override
            public void onSingleTouch(View v) {
                if (mAlbumLayout.getVisibility() == View.VISIBLE) {
                    mAlbumLayout.setVisibility(View.INVISIBLE);
                    mLrcViewContainer.setVisibility(View.VISIBLE);
                    mMusicTool.setVisibility(View.INVISIBLE);
                }
            }
        });
        mLrcViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLrcViewContainer.getVisibility() == View.VISIBLE) {
                    mLrcViewContainer.setVisibility(View.INVISIBLE);
                    mAlbumLayout.setVisibility(View.VISIBLE);
                    mMusicTool.setVisibility(View.VISIBLE);
                }
            }
        });

        mTryGetLrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaService.TRY_GET_TRACKINFO);
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "正在获取信息", Toast.LENGTH_SHORT).show();
            }
        });

        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int v = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int mMaxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        mVolumeSeek.setMax(mMaxVol);
//        mVolumeSeek.setProgress(v);
//        mVolumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.ADJUST_SAME);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    LrcView.OnLrcClickListener onLrcClickListener = new LrcView.OnLrcClickListener() {

        @Override
        public void onClick() {

            if (mLrcViewContainer.getVisibility() == View.VISIBLE) {
                mLrcViewContainer.setVisibility(View.INVISIBLE);
                mAlbumLayout.setVisibility(View.VISIBLE);
                mMusicTool.setVisibility(View.VISIBLE);
            }
        }
    };

    LrcView.OnSeekToListener onSeekToListener = new LrcView.OnSeekToListener() {

        @Override
        public void onSeekTo(int progress) {
            MusicPlayer.Companion.seek(progress);
        }
    };

    @Override
    public void updateLrc() {
        List<LrcRow> list = getLrcRows();
        if (list != null && list.size() > 0) {
            mTryGetLrc.setVisibility(View.INVISIBLE);
            mLrcView.setLrcRows(list);
        } else {
            mTryGetLrc.setVisibility(View.VISIBLE);
            mLrcView.reset();
        }
    }

    private List<LrcRow> getLrcRows() {

        List<LrcRow> rows = null;
        InputStream is = null;
        try {
            is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/pastmusic/lrc/" + MusicPlayer.Companion.getCurrentAudioId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is == null) {
                return null;
            }
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            rows = DefaultLrcParser.getIstance().getLrcRows(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private void stopAnim() {
        mActiveView = null;
        if (mRotateAnim != null) {
            mRotateAnim.end();
            mRotateAnim = null;
        }
        if (mNeedleAnim != null) {
            mNeedleAnim.end();
            mNeedleAnim = null;
        }
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
            mAnimatorSet = null;
        }
    }

    private Runnable mUpAlbumRunnable = new Runnable() {
        @Override
        public void run() {
            if (MusicPlayer.Companion.getAlbumPic() == null) {

            } else {
                ImageRequest imageRequest = ImageRequestBuilder
                        .newBuilderWithSource(Uri.parse(MusicPlayer.Companion.getAlbumPic()))
                        .setProgressiveRenderingEnabled(true)
                        .build();
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, PlayMusicActivity.this);
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    public void onNewResultImpl(@Nullable Bitmap bitmap) {
                        // You can use the bitmap in only limited ways
                        // No need to do any cleanup.
                        if (bitmap != null) {
                            mBitmap = bitmap;
                            Drawable drawable = null;
                            drawable = ImageUtils.createBlurredImageFromBitmap(mBitmap, PlayMusicActivity.this.getApplication(), 3);
                            mBackAlbum.setBackground(drawable);
                        }
                    }

                    @Override
                    public void onFailureImpl(DataSource dataSource) {
                        // No cleanup required here.
                        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_disk_210);
                    }
                }, CallerThreadExecutor.getInstance());
            }
        }
    };

    private void setViewPager() {
        mViewPager.setOffscreenPageLimit(2);
        PlaybarPagerTransformer transformer = new PlaybarPagerTransformer();
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, transformer);
        // 改变viewpager动画时间
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            MyScroller mScroller = new MyScroller(mViewPager.getContext().getApplicationContext(), new LinearInterpolator());
            mField.set(mViewPager, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(final int pPosition) {
                if (pPosition < 1) { //首位之前，跳转到末尾（N）
                    MusicPlayer.Companion.setQueuePosition(MusicPlayer.Companion.getQueue().length);
                    mViewPager.setCurrentItem(MusicPlayer.Companion.getQueue().length, false);
                    isNextOrPreSetPage = false;
                    return;
                } else if (pPosition > MusicPlayer.Companion.getQueue().length) { //末位之后，跳转到首位（1）
                    MusicPlayer.Companion.setQueuePosition(0);
                    mViewPager.setCurrentItem(1, false); //false:不显示跳转过程的动画
                    isNextOrPreSetPage = false;
                    return;
                } else {

                    if (!isNextOrPreSetPage) {
                        if (pPosition < MusicPlayer.Companion.getQueuePosition() + 1) {
                            Message msg = new Message();
                            msg.what = PRE_MUSIC;
                            mPlayHandler.sendMessageDelayed(msg, TIME_DELAY);
                        } else if (pPosition > MusicPlayer.Companion.getQueuePosition() + 1) {
                            Message msg = new Message();
                            msg.what = NEXT_MUSIC;
                            mPlayHandler.sendMessageDelayed(msg, TIME_DELAY);

                        }
                    }
                }
                isNextOrPreSetPage = false;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int pState) {
            }
        });
    }

    public class MyScroller extends Scroller {
        private int animTime = VIEWPAGER_SCROLL_TIME;

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, animTime);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, animTime);
        }

        public void setmDuration(int animTime) {
            this.animTime = animTime;
        }
    }

    public class PlaybarPagerTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            if (position == 0) {
                if (MusicPlayer.Companion.getIsPlaying()) {
                    mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                    if (mRotateAnim != null && !mRotateAnim.isRunning() && mNeedleAnim != null) {
                        mAnimatorSet = new AnimatorSet();
                        mAnimatorSet.play(mNeedleAnim).before(mRotateAnim);
                        mAnimatorSet.start();
                    }
                }

            } else if (position == -1 || position == -2 || position == 1) {
                mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                if (mRotateAnim != null) {
                    mRotateAnim.setFloatValues(0);
                    mRotateAnim.end();
                    mRotateAnim = null;
                }
            } else {
                if (mNeedleAnim != null) {
                    mNeedleAnim.reverse();
                    mNeedleAnim.end();
                }
                mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                if (mRotateAnim != null) {
                    mRotateAnim.cancel();
                    float valueAvatar = (float) mRotateAnim.getAnimatedValue();
                    mRotateAnim.setFloatValues(valueAvatar, 360f + valueAvatar);
                }
            }
        }
    }

    class FragmentAdapter extends FragmentStatePagerAdapter {

        private int mChildCount = 0;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == MusicPlayer.Companion.getQueue().length + 1 || position == 0) {
                return RoundFragment.newInstance("");
            }
            return RoundFragment.newInstance(MusicPlayer.Companion.getAlbumPathAll()[position - 1]);
        }

        @Override
        public int getCount() {
            //左右各加一个
            return MusicPlayer.Companion.getQueue().length + 2;
        }


        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

    }

    private Runnable mUpdateProgress = new Runnable() {

        @Override
        public void run() {

            if (mProgress != null) {
                long position = MusicPlayer.Companion.position();
                long duration = MusicPlayer.Companion.duration();
                if (duration > 0 && duration < 627080716) {
                    mProgress.setProgress((int) (1000 * position / duration));
                    mTimePlayed.setText(MusicUtils.Companion.makeTimeString(position));
                }
                if (MusicPlayer.Companion.getIsPlaying()) {
                    mProgress.postDelayed(mUpdateProgress, 200);
                } else {
                    mProgress.removeCallbacks(mUpdateProgress);
                }
            }
        }
    };

    private void setSeekBarListener() {
        if (mProgress != null)
            mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    i = (int) (i * MusicPlayer.Companion.duration() / 1000);
                    mLrcView.seekTo(i, true, b);
                    if (b) {
                        MusicPlayer.Companion.seek((long) i);
                        mTimePlayed.setText(MusicUtils.Companion.makeTimeString(i));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopAnim();
        mProgress.removeCallbacks(mUpdateProgress);
    }

    private void updatePlaymode() {
        if (MusicPlayer.Companion.getShuffleMode() == MediaService.SHUFFLE_NORMAL) {
            mPlayMode.setImageResource(R.drawable.play_icn_shuffle);
            Toast.makeText(PlayMusicActivity.this.getApplication(), getResources().getString(R.string.random_play),
                    Toast.LENGTH_SHORT).show();
        } else {
            switch (MusicPlayer.Companion.getRepeatMode()) {
                case MediaService.REPEAT_ALL:
                    mPlayMode.setImageResource(R.drawable.play_icn_loop);
                    Toast.makeText(PlayMusicActivity.this.getApplication(), getResources().getString(R.string.loop_play),
                            Toast.LENGTH_SHORT).show();
                    break;
                case MediaService.REPEAT_CURRENT:
                    mPlayMode.setImageResource(R.drawable.play_icn_one);
                    Toast.makeText(PlayMusicActivity.this.getApplication(), getResources().getString(R.string.play_one),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    public class PlayMusic extends Thread {
        public void run() {
            if (Looper.myLooper() == null)
                Looper.prepare();
            mPlayHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case PRE_MUSIC:
                            MusicPlayer.Companion.previous(PlayMusicActivity.this, true);
                            break;
                        case NEXT_MUSIC:
                            MusicPlayer.Companion.nextPlay();
                            break;
                        case 3:
//                            MusicPlayer.CompanionsetQueuePosition(msg.arg1);
                            break;
                    }
                }
            };
            Looper.loop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayHandler.removeCallbacksAndMessages(null);
        mPlayHandler.getLooper().quit();
        mPlayHandler = null;

        mProgress.removeCallbacks(mUpdateProgress);
        stopAnim();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }
}
