package com.past.music;/**
 * Created by gaojin on 2017/1/26.
 */

import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.past.music.pastmusic.R;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 上午12:38
 * 版本：
 * 描述：baseActivity
 * 备注：
 * =======================================================
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }
}
