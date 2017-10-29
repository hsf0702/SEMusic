package com.past.music.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import com.past.music.fragment.AppMainFragment;
import com.past.music.pastmusic.R;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    //权限申请标志码
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x01;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.content, AppMainFragment.Companion.newInstance()).commitAllowingStateLoss();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }

    @Override
    protected void setStatusBar() {
    }
}
