package com.cango.palmcartreasure.trailer.taskpictures;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailFragment;
import com.cango.palmcartreasure.util.CommUtil;

/**
 * Created by dell on 2018/3/14.
 */

public class TaskPicturesActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskpictures);

        TypeTaskData.DataBean.TaskListBean taskListBean = getIntent().getParcelableExtra(TaskDetailFragment.TASKLISTBEAN);
        TaskPicturesFragment mTaskPicturesFragment = (TaskPicturesFragment) getSupportFragmentManager().findFragmentById(R.id.fl_taskpictures_fg);
        if (CommUtil.checkIsNull(mTaskPicturesFragment)) {
            mTaskPicturesFragment = TaskPicturesFragment.newInstance(taskListBean);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_taskpictures_fg, mTaskPicturesFragment);
            transaction.commit();
        }
        new TaskPicturesPresenter(mTaskPicturesFragment);
    }
}
