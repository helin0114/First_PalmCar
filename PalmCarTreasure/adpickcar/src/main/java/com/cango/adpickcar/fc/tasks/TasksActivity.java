package com.cango.adpickcar.fc.tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.fc.main.fcmain.FcHomeVisitFragment;
import com.cango.adpickcar.util.CommUtil;

/**
 * true:初次家访页面  false:家访跟进页面
 */

public class TasksActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc_tasks);

        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fc_tasks_container);
        if (CommUtil.checkIsNull(tasksFragment)) {
            tasksFragment = TasksFragment.getInstance(getIntent().getBooleanExtra(FcHomeVisitFragment.TYPE_FIRSTORNOT_HOMEVISIT,false));
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_fc_tasks_container, tasksFragment);
            transaction.commit();
        }
        new TasksPresenter(tasksFragment);
    }
}
