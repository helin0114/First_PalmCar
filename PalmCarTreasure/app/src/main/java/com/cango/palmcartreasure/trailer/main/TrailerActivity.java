package com.cango.palmcartreasure.trailer.main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.util.CommUtil;

/**
 * 首页界面
 */
public class TrailerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().setStatusBarColor(Color.WHITE);
//        }

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
        }
        setContentView(R.layout.activity_trailer);

        TrailerFragment trailerFragment = (TrailerFragment) getSupportFragmentManager().findFragmentById(R.id.fl_trailer_contains);
        boolean isFromSMS = false;
        if (getIntent() != null) {
            if (getIntent().hasExtra("isFromSMS")) {
                isFromSMS = getIntent().getBooleanExtra("isFromSMS", false);
            }
        }
        if (CommUtil.checkIsNull(trailerFragment)) {
            if (isFromSMS) {
                trailerFragment = trailerFragment.newInstance(isFromSMS);
            } else {
                trailerFragment = TrailerFragment.newInstance();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_trailer_contains, trailerFragment);
            transaction.commit();
        }
//        Logger.d("w = "+ScreenUtil.getScreenWidth(this)+"; h = "+ScreenUtil.getScreenHeight(this));
//        Logger.d("statusBar = "+ BarUtil.getStatusBarHeight(this)+"; navigationBar = "+BarUtil.getNavigationBarHeight(this));

    }

    @Override
    public void onBackPressed() {
        MtApplication.clearLastActivity();
        finish();
    }
}
