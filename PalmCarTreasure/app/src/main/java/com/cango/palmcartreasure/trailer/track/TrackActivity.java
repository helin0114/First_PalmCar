package com.cango.palmcartreasure.trailer.track;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.util.CommUtil;

import java.util.Date;

public class TrackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

        }

        TrackFragment trackFragment = (TrackFragment) getSupportFragmentManager().findFragmentById(R.id.fl_track_contains);
        String imei = getIntent().getStringExtra(TrackFragment.IMEI);
        Date startTime = (Date) getIntent().getSerializableExtra(TrackFragment.START_TIME);
        Date endTime = (Date) getIntent().getSerializableExtra(TrackFragment.END_TIME);
        if (CommUtil.checkIsNull(trackFragment)) {
            trackFragment = TrackFragment.newInstance(imei, startTime, endTime);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_track_contains, trackFragment);
            transaction.commit();
        }
        TrackPresenter locationPresenter = new TrackPresenter(trackFragment);
    }
}
