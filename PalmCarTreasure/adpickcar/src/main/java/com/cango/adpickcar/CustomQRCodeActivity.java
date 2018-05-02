package com.cango.adpickcar;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.BarUtil;
import com.cango.adpickcar.util.CommUtil;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class CustomQRCodeActivity extends BaseActivity {

    Toolbar mToolbar;
    LinearLayout llFlash;
    ImageView ivFlash;
    TextView tvTitle;
    boolean isOn = false;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_qrcode);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_qrcode);
        llFlash = (LinearLayout) findViewById(R.id.ll_flash);
        ivFlash = (ImageView) findViewById(R.id.iv_flash);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);

        if (!hasFlash()) {
            llFlash.setVisibility(View.GONE);
        }

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        } else {
//        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        int statusBarHeight = BarUtil.getStatusBarHeight(this);
        int actionBarHeight = BarUtil.getActionBarHeight(this);
//        if (Build.VERSION.SDK_INT >= 21) {
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
//            mToolbar.setLayoutParams(layoutParams);
//            mToolbar.setPadding(0, statusBarHeight, 0, 0);
//        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomQRCodeActivity.this.onBackPressed();
            }
        });

        llFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOn){
                    isOn = false;
                    ivFlash.setImageResource(R.drawable.flashoff);
                    barcodeScannerView.setTorchOff();
                }else {
                    isOn = true;
                    ivFlash.setImageResource(R.drawable.flashon);
                    barcodeScannerView.setTorchOn();
                }
            }
        });

        //如果from =true的话说明是fc的一维码扫描，如果是false的话是ad的二维码扫描
        boolean from = getIntent().getBooleanExtra("from",false);
        if (from){
            mToolbar.setBackgroundColor(Color.WHITE);
            tvTitle.setTextColor(getResources().getColor(R.color.ad333333));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_title_back);
        }else {
            if (Build.VERSION.SDK_INT >= 21) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
                mToolbar.setLayoutParams(layoutParams);
                mToolbar.setPadding(0, statusBarHeight, 0, 0);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
