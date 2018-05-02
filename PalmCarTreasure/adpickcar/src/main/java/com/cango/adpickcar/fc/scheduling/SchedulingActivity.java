package com.cango.adpickcar.fc.scheduling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;

/**
 * Created by dell on 2017/12/13.
 */

public class SchedulingActivity extends BaseActivity{

    private SchedulingFragment schedulingFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc_scheduling);

        schedulingFragment = (SchedulingFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fc_scheduling_container);
        if (CommUtil.checkIsNull(schedulingFragment)) {
            schedulingFragment = SchedulingFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_fc_scheduling_container, schedulingFragment);
            transaction.commit();
        }

        new SchedulingPresenter(schedulingFragment);
    }
}
