package com.cango.adpickcar.fc.main.weeklyscheduling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.fc.main.fcmain.FcHomeVisitFragment;
import com.cango.adpickcar.util.CommUtil;

/**
 * true:fcs家访初访管理界面    false:fc周行程管理界面
 */

public class WeeklySchedulingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc_weekly_scheduling);

        WeeklySchedulingFragment mWeeklySchedulingFragment = (WeeklySchedulingFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fc_weeklyscheduling_container);
        if (CommUtil.checkIsNull(mWeeklySchedulingFragment)) {
            mWeeklySchedulingFragment = WeeklySchedulingFragment.getInstance(getIntent().getBooleanExtra(FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_FCORFCS,false));
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_fc_weeklyscheduling_container, mWeeklySchedulingFragment);
            transaction.commit();
        }
        new WeeklySchedulingPresenter(mWeeklySchedulingFragment);
    }
}
