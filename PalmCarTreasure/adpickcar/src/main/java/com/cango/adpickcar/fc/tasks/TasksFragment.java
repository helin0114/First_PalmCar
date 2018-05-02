package com.cango.adpickcar.fc.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.fc.main.fcmain.FcHomeVisitFragment;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.util.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * true:初次家访页面  false:家访跟进页面
 */

public class TasksFragment extends BaseFragment implements TasksContract.View{
    public static final int TYPE_TASKS_TODAY = 0;   //今日任务
    public static final int TYPE_TASKS_WEEK = 1;    //本周任务
    public static final int TYPE_TASKS_ALL = 2; //全部任务

    @BindView(R.id.layout_tasks_title)
    Toolbar mToolbar;
    @BindView(R.id.fc_task_layout_main)
    RelativeLayout LayoutMain;
    @BindView(R.id.fc_tl_tasks_label)
    TabLayout mTabLayout;
    @BindView(R.id.fc_vp_tasks_label)
    ViewPager mViewPager;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private TasksActivity mActivity;
    private ArrayList<String> tabList = new ArrayList<>();
    private ArrayList<Fragment> fgList = new ArrayList<>();
    private MyAdapter mMyAdapter;
    public TasksContract.Presenter mPresenter;
    private TasksItemFragment mTasksItemFragmentToday;
    private TasksItemFragment mTasksItemFragmentWeek;
    private TasksItemFragment mTasksItemFragmentAll;

    //是不是初次家访页面
    private boolean firstOrNo;//true:初次家访页面  false:家访跟进页面


    public static TasksFragment getInstance(boolean firstOrNo){
        TasksFragment tasksFragment = new TasksFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FcHomeVisitFragment.TYPE_FIRSTORNOT_HOMEVISIT,firstOrNo);
        tasksFragment.setArguments(bundle);
        return tasksFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_fc_tasks;
    }

    @Override
    protected void initView() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_title_back);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        initTabLayout();
    }

    @Override
    protected void initData() {
        mActivity = (TasksActivity) getActivity();
        firstOrNo = getArguments().getBoolean(FcHomeVisitFragment.TYPE_FIRSTORNOT_HOMEVISIT);
    }

    private void initTabLayout() {
        tabList.clear();
        fgList.clear();
        tabList.add(mActivity.getResources().getString(R.string.task_today));
        if(firstOrNo){
            tvTitle.setText(mActivity.getResources().getString(R.string.first_home));
            tabList.add(mActivity.getResources().getString(R.string.task_week));
        }else{
            tvTitle.setText(mActivity.getResources().getString(R.string.home_visit_follow));
            tabList.add(mActivity.getResources().getString(R.string.task_threeday));
        }
        tabList.add(mActivity.getResources().getString(R.string.task_all));
        mTasksItemFragmentToday = TasksItemFragment.getInstance(TYPE_TASKS_TODAY);
        mTasksItemFragmentWeek = TasksItemFragment.getInstance(TYPE_TASKS_WEEK);
        mTasksItemFragmentAll = TasksItemFragment.getInstance(TYPE_TASKS_ALL);
        fgList.add(mTasksItemFragmentToday);
        fgList.add(mTasksItemFragmentWeek);
        fgList.add(mTasksItemFragmentAll);
        mMyAdapter = new MyAdapter(getChildFragmentManager(), tabList, fgList);
        mViewPager.setAdapter(mMyAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }


    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void openOtherUi() {
        SnackbarUtils.showLongDisSnackBar(LayoutMain, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public void showTodayError() {
        mTasksItemFragmentToday.showError();
    }

    @Override
    public void showTodayNoData() {
        mTasksItemFragmentToday.showNoData();
    }

    @Override
    public void showTodayTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        mTasksItemFragmentToday.showTasksSuccess(data);
    }

    @Override
    public void showWeekError() {
        mTasksItemFragmentWeek.showError();
    }

    @Override
    public void showWeekNoData() {
        mTasksItemFragmentWeek.showNoData();
    }

    @Override
    public void showWeekTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        mTasksItemFragmentWeek.showTasksSuccess(data);
    }

    @Override
    public void showAllError() {
        mTasksItemFragmentAll.showError();
    }

    @Override
    public void showAllNoData() {
        mTasksItemFragmentAll.showNoData();
    }

    @Override
    public void showAllTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        mTasksItemFragmentAll.showTasksSuccess(data);
    }


    public class MyAdapter extends FragmentStatePagerAdapter {

        private ArrayList<String> titleList;
        private ArrayList<Fragment> fragmentList;

        public MyAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
