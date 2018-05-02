package com.cango.adpickcar.fc.main.weeklyscheduling;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseLazyFragment;
import com.cango.adpickcar.baseAdapter.BaseAdapter;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.baseAdapter.OnLoadMoreListener;
import com.cango.adpickcar.customview.CalendarNewFcDialogFragment;
import com.cango.adpickcar.fc.main.fcmain.FcHomeVisitFragment;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2018/4/11.
 */

public class WeeklySchedulingItemFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final int PAGE_SIZE = 10;

    @BindView(R.id.fc_srl_tasks)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fc_recyclerview_tasks)
    RecyclerView mRecyclerView;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.tv_arrangement_plan)
    TextView tvArrangementPlan;

    private WeeklySchedulingItemAdapter mWeeklySchedulingItemAdapter;
    private List<FcVisitTaskList.TaskListBean> mDatas;
    private int tasksType = -1;    //任务类型
    private boolean isLoadMore;
    private int mPageCount = 1, mTempPageCount = 2;
    //选择日期的dialog框
    private CalendarNewFcDialogFragment mCalendarNewFcDialogFragment;

    private WeeklySchedulingActivity mActivity;
    private WeeklySchedulingPresenter mPresenter;

    private boolean isFcOrFcs;

    //是否有选择框被选中
    public boolean hasCheckBoxChecked;

    @OnClick({R.id.tv_arrangement_plan})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_arrangement_plan:
                if(hasCheckBoxChecked){
                    showCalendarDialog();
                } else {
                    ToastUtils.showShort("请选择要分配的任务");
                }
                break;
            default:
                break;
        }
    }

    public static WeeklySchedulingItemFragment getInstance(int type, boolean isFcOrFcs){
        WeeklySchedulingItemFragment mWeeklySchedulingItemFragment = new WeeklySchedulingItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putBoolean(FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_FCORFCS,isFcOrFcs);
        mWeeklySchedulingItemFragment.setArguments(bundle);
        return mWeeklySchedulingItemFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fc_tasks_item;
    }

    @Override
    public void initData() {
        mActivity = (WeeklySchedulingActivity) getParentFragment().getActivity();
        tasksType = getArguments().getInt("type",-1);
        isFcOrFcs = getArguments().getBoolean(FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_FCORFCS);
        mPresenter = (WeeklySchedulingPresenter) ((WeeklySchedulingFragment) getParentFragment()).mPresenter;

        if(tasksType == WeeklySchedulingFragment.TYPE_SCHEDULING_NEW && !isFcOrFcs){
            tvArrangementPlan.setVisibility(View.VISIBLE);
        }else{
            tvArrangementPlan.setVisibility(View.GONE);
        }
        showLoadView(false);
        initRecyclerView();
    }

    public void showLoadView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    /**
     * 初始化任务列表
     */
    private void initRecyclerView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mDatas = new ArrayList<>();
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mDatas.add(new FcVisitTaskList.TaskListBean());
        mWeeklySchedulingItemAdapter = new WeeklySchedulingItemAdapter(mActivity, mDatas, true,
                new WeeklySchedulingCheckedListener() {
                    @Override
                    public void onChecked(boolean isChecked, int position) {
                        if(isChecked){
                            mDatas.get(position).setChecked(true);
                        } else {
                            mDatas.get(position).setChecked(false);
                        }
                        boolean hasChecked = false;
                        for(FcVisitTaskList.TaskListBean item : mDatas){
                            if(item.isChecked()){
                                hasCheckBoxChecked = true;
                                hasChecked = true;
                                break;
                            }
                        }
                        if(!hasChecked){
                            hasCheckBoxChecked = false;
                        }
                    }
                }, tasksType, isFcOrFcs);
        mWeeklySchedulingItemAdapter.setLoadingView(R.layout.load_loading_layout);
        mWeeklySchedulingItemAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                getData();
            }
        });
        mWeeklySchedulingItemAdapter.setOnItemClickListener(new OnBaseItemClickListener<FcVisitTaskList.TaskListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, final FcVisitTaskList.TaskListBean data, final int position) {
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mWeeklySchedulingItemAdapter);

//        onRefresh();
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        showMainIndicator(false);
        mDatas.clear();
        mWeeklySchedulingItemAdapter.setLoadingView(R.layout.load_loading_layout);
        mWeeklySchedulingItemAdapter.notifyDataSetChanged();
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        getData();
    }
    /**
     * 获取任务列表数据
     */
    public void getData(){
        if(tasksType == WeeklySchedulingFragment.TYPE_SCHEDULING_NEW){
            mPresenter.getVisitTaskListNew(ADApplication.mSPUtils.getString(Api.USERID));
        }else if(tasksType == WeeklySchedulingFragment.TYPE_SCHEDULING_APPROVAL){
            mPresenter.getVisitTaskListApproval(ADApplication.mSPUtils.getString(Api.USERID));
        }else if(tasksType == WeeklySchedulingFragment.TYPE_SCHEDULING_FEEDBACK){
            mPresenter.getVisitTaskListFeedBack(ADApplication.mSPUtils.getString(Api.USERID));
        }
    }

    public void showMainIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    public void showTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (data!=null && data.size() == 0){
            if (isLoadMore){
                mWeeklySchedulingItemAdapter.setLoadEndView(R.layout.load_end_layout);
            } else {
                showNoData();
            }
            return;
        }
        if (isLoadMore) {
            mTempPageCount++;
            mWeeklySchedulingItemAdapter.setLoadMoreData(data);
        } else {
            mWeeklySchedulingItemAdapter.setNewDataNoError(data);
        }
        if (data.size() < PAGE_SIZE) {
            mWeeklySchedulingItemAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    public void showError() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    public void showNoData() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    /**
     * 显示日历空间dialog
     */
    private void showCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarNewFcDialogFragment)) {
            mCalendarNewFcDialogFragment = new CalendarNewFcDialogFragment();
            mCalendarNewFcDialogFragment.setCalendarDilaogListener(new CalendarNewFcDialogFragment.CalendarDilaogListener() {
                @Override
                public void onCalendarClick(Date date) {
                    ToastUtils.showShort("onCalendarClick:"+date.toString());
                }
            });
        }
        if (mCalendarNewFcDialogFragment.isVisible()) {

        } else {
            mCalendarNewFcDialogFragment.show(getFragmentManager(), "CalendarDialog");
        }
    }

    public class WeeklySchedulingItemAdapter extends BaseAdapter<FcVisitTaskList.TaskListBean> {
        private WeeklySchedulingCheckedListener mWeeklySchedulingCheckedListener;
        private int tasksType;
        private boolean isFcOrFcs;
        private List<FcVisitTaskList.TaskListBean> datas;

        public WeeklySchedulingItemAdapter(Context context, List<FcVisitTaskList.TaskListBean> datas, boolean isOpenLoadMore,
                                           WeeklySchedulingCheckedListener mWeeklySchedulingCheckedListener, int tasksType, boolean isFcOrFcs) {
            super(context, datas, isOpenLoadMore);
            this.mWeeklySchedulingCheckedListener = mWeeklySchedulingCheckedListener;
            this.datas = datas;
            this.tasksType = tasksType;
            this.isFcOrFcs = isFcOrFcs;
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.fc_weekly_scheduling_item;
        }

        @Override
        protected void convert(final BaseHolder holder, final FcVisitTaskList.TaskListBean data) {
            TextView tvSchedulingDate = holder.getView(R.id.tv_scheduling_date);
            CheckBox mCheckBox = holder.getView(R.id.cb_weekly_scheduling_item);
            mCheckBox.setChecked(data.isChecked());
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mWeeklySchedulingCheckedListener.onChecked(isChecked, holder.getAdapterPosition());
                }
            });

            if(isFcOrFcs){
                if(tasksType == WeeklySchedulingFragment.TYPE_SCHEDULING_FEEDBACK){
                    mCheckBox.setVisibility(View.GONE);
                }else{
                    mCheckBox.setVisibility(View.VISIBLE);
                }
            } else {
                if(tasksType == WeeklySchedulingFragment.TYPE_SCHEDULING_NEW){
                    mCheckBox.setVisibility(View.VISIBLE);
                    tvSchedulingDate.setText("未安排\n行程");
                    tvSchedulingDate.setTextColor(mActivity.getResources().getColor(R.color.ad666666));
                    tvSchedulingDate.setTextSize(12);
                }else{
                    mCheckBox.setVisibility(View.GONE);
                    tvSchedulingDate.setText("4月1日");
                    tvSchedulingDate.setTextColor(mActivity.getResources().getColor(R.color.fc_text_blue));
                    tvSchedulingDate.setTextSize(14);
                }
            }
        }
    }

    /**
     * 选择框选择状态改变的监听
     */
    private interface WeeklySchedulingCheckedListener{
        void onChecked(boolean isChecked, int position);
    }
}
