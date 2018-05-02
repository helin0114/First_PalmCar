package com.cango.adpickcar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.BarUtil;
import com.cango.adpickcar.util.CommUtil;
import com.wang.avi.AVLoadingIndicatorView;

import static com.cango.adpickcar.R.id.gank_detail_loading;

public class DocActivity extends BaseActivity {

    Toolbar mToolbar;
    WebView mWebView;
    ProgressBar mProgressBar;
    AVLoadingIndicatorView mLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_doc);
        mWebView = (WebView) findViewById(R.id.gank_detail_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.gank_detail_progress);
        mLoading = (AVLoadingIndicatorView) findViewById(gank_detail_loading);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        int statusBarHeight = BarUtil.getStatusBarHeight(this);
        int actionBarHeight = BarUtil.getActionBarHeight(this);
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocActivity.this.onBackPressed();
            }
        });

        initWebView();
    }

    @Override
    public void onDestroy() {
        if (!CommUtil.checkIsNull(mWebView))
            mWebView.destroy();
        super.onDestroy();
    }

    private void initWebView() {
        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // KITKAT
        {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (!CommUtil.checkIsNull(mProgressBar)){
                    mProgressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!CommUtil.checkIsNull(mLoading)){
                    mLoading.hide();
                }
                settings.setBlockNetworkImage(false);
            }
        });
        mWebView.loadUrl(Api.BASE_URL+Api.ABOUT_CONTENT);
    }
}
