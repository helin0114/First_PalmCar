package com.cango.adpickcar.fc.main.fcsweeklyscheduling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.fc.main.fcmain.FcHomeVisitFragment;
import com.cango.adpickcar.util.CommUtil;

/**
 * true:fcs周行程管理界面    false:fcs家访跟进管理界面
 */

public class FcsWeeklySchedulingActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc_weekly_scheduling);

        FcsWeeklySchedulingFragment mFcsWeeklySchedulingFragment = (FcsWeeklySchedulingFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fc_weeklyscheduling_container);
        if (CommUtil.checkIsNull(mFcsWeeklySchedulingFragment)) {
            mFcsWeeklySchedulingFragment = FcsWeeklySchedulingFragment.getInstance(getIntent().getBooleanExtra(FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_ISWEEKLY, false));
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_fc_weeklyscheduling_container, mFcsWeeklySchedulingFragment);
            transaction.commit();
        }
        new FcsWeeklySchedulingPresenter(mFcsWeeklySchedulingFragment);
    }
}
