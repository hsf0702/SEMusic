package com.past.music.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;

import butterknife.BindView;

public class WebViewActivity extends ToolBarActivity {

    public static final String TAG = "WebViewActivity";
    public static final String TITLE = "WebView_TITLE";
    public static final String WEBURL = "WebView_WEBURL";

    private String mWebUrl = null;
    private String mTitle;

    @BindView(R.id.web_view)
    WebView mWebView;

    public static void startWebViewActivity(Context context, String title, String webUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(WEBURL, webUrl);
        ((BaseActivity) context).startActivityByX(intent, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebSettings setting = mWebView.getSettings();
        //是否支持和JS交互
        setting.setJavaScriptEnabled(true);
        //是否支持缩放
        setting.setSupportZoom(false);
        //是否显示缩放工具
        setting.setBuiltInZoomControls(false);
        onNewIntent(getIntent());
        MyLog.i(TAG, mWebUrl);
        mWebView.loadUrl(mWebUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (title != null && !title.isEmpty()) {
                    setTitle(title);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWebUrl = intent.getStringExtra(WEBURL);
        mTitle = intent.getStringExtra(TITLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
