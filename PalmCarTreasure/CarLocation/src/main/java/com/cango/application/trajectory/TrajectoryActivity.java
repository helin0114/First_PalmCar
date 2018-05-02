package com.cango.application.trajectory;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.application.R;
import com.cango.application.base.BaseActivity;
import com.cango.application.location.LocationFragment;
import com.cango.application.location.LocationPresenter;
import com.cango.application.util.CommUtil;

import java.util.Date;

/**
 * 查看轨迹界面
 */
public class TrajectoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);

        TrajectoryFragment trajectoryFragment = (TrajectoryFragment) getSupportFragmentManager().findFragmentById(R.id.fl_trajectory_contains);
        String imei = getIntent().getStringExtra(TrajectoryFragment.IMEI);
        Date startTime= (Date) getIntent().getSerializableExtra(TrajectoryFragment.START_TIME);
        Date endTime = (Date) getIntent().getSerializableExtra(TrajectoryFragment.END_TIME);
        if (CommUtil.checkIsNull(trajectoryFragment)) {
            trajectoryFragment = TrajectoryFragment.newInstance(imei,startTime,endTime);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_trajectory_contains, trajectoryFragment);
            transaction.commit();
        }
        TrajectoryPresenter locationPresenter = new TrajectoryPresenter(trajectoryFragment);
    }
}
