package com.cango.adpickcar.fc.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.cango.adpickcar.customview.CalendarDialogFragment;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.util.CommUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 任务列表任务分类fragment(懒加载)
 */

public class TasksItemFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener{
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

    private TasksItemAdapter mTasksItemAdapter;
    private List<FcVisitTaskList.TaskListBean> mDatas;
    private int tasksType = -1;    //任务类型
    private boolean isLoadMore;
    private int mPageCount = 1, mTempPageCount = 2;

    private TasksActivity mActivity;
    private TasksContract.Presenter mPresenter;

    public static TasksItemFragment getInstance(int type){
        TasksItemFragment tasksItemFragment = new TasksItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        tasksItemFragment.setArguments(bundle);
        return tasksItemFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fc_tasks_item;
    }

    @Override
    public void initData() {
        mActivity = (TasksActivity) getParentFragment().getActivity();
        tasksType = getArguments().getInt("type",-1);
        mPresenter = ((TasksFragment) getParentFragment()).mPresenter;
        showLoadView(false);
        initRecyclerView();
    }

    /**
     * 获取任务列表数据
     */
    public void getData(){
        if(tasksType == TasksFragment.TYPE_TASKS_TODAY){
            mPresenter.getVisitTaskListToday(ADApplication.mSPUtils.getString(Api.USERID));
        }else if(tasksType == TasksFragment.TYPE_TASKS_WEEK){
            mPresenter.getVisitTaskListWeek(ADApplication.mSPUtils.getString(Api.USERID));
        }else if(tasksType == TasksFragment.TYPE_TASKS_ALL){
            mPresenter.getVisitTaskListAll(ADApplication.mSPUtils.getString(Api.USERID));
        }
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
        mTasksItemAdapter = new TasksItemAdapter(mActivity, mDatas, true, this, tasksType);
        mTasksItemAdapter.setLoadingView(R.layout.load_loading_layout);
        mTasksItemAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
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
        mTasksItemAdapter.setOnItemClickListener(new OnBaseItemClickListener<FcVisitTaskList.TaskListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, final FcVisitTaskList.TaskListBean data, final int position) {
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mTasksItemAdapter);

//        onRefresh();
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        showMainIndicator(false);
        mDatas.clear();
        mTasksItemAdapter.setLoadingView(R.layout.load_loading_layout);
        mTasksItemAdapter.notifyDataSetChanged();
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        getData();
    }

    /**
     * 下拉刷新动画
     * @param active
     */
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
                mTasksItemAdapter.setLoadEndView(R.layout.load_end_layout);
            } else {
                showNoData();
            }
            return;
        }
        if (isLoadMore) {
            mTempPageCount++;
            mTasksItemAdapter.setLoadMoreData(data);
        } else {
            mTasksItemAdapter.setNewDataNoError(data);
        }
        if (data.size() < PAGE_SIZE) {
            mTasksItemAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    public void showLoadView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
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

    private class TasksItemAdapter extends BaseAdapter<FcVisitTaskList.TaskListBean> {
        private TasksItemFragment tasksItemFragment;
        private int tasksType;

        public TasksItemAdapter(Context context, List<FcVisitTaskList.TaskListBean> datas, boolean isOpenLoadMore, TasksItemFragment tasksItemFragment, int tasksType) {
            super(context, datas, isOpenLoadMore);
            this.tasksItemFragment = tasksItemFragment;
            this.tasksType = tasksType;
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.fc_tasks_item;
        }

        @Override
        protected void convert(BaseHolder holder, final FcVisitTaskList.TaskListBean data) {
            TextView tvId = holder.getView(R.id.fc_item_id);
            TextView tvOverdueTerm = holder.getView(R.id.tv_overdue_term);
            TextView tvOverdueDay = holder.getView(R.id.tv_overdue_day);
            TextView tvOverdueMoney = holder.getView(R.id.tv_overdue_money);
            TextView tvName = holder.getView(R.id.tv_fc_tasks_item_name);
            TextView tvPlate = holder.getView(R.id.tv_fc_tasks_item_plate);
            TextView tvAllotment = holder.getView(R.id.tv_tasks_item_allotment);
            TextView tvDistance = holder.getView(R.id.tv_fc_tasks_item_distance);
            TextView tvDate = holder.getView(R.id.tv_fc_tasks_item_date);
            TextView tvLabelOne = holder.getView(R.id.tv_fc_item_label_one);
            TextView tvLabelTwo = holder.getView(R.id.tv_fc_item_label_two);
            TextView tvLabelSubmit = holder.getView(R.id.tv_fc_item_label_submit);
            if(tasksType == TasksFragment.TYPE_TASKS_ALL){
                tvLabelSubmit.setVisibility(View.GONE);
            }else{
                tvLabelSubmit.setVisibility(View.VISIBLE);
            }
//            tvId.setText(data.getApplyCD());
//            tvOverdueTerm.setText(data.getDueTerms());
//            tvOverdueDay.setText(data.getOverDueDays());
//            tvOverdueMoney.setText(data.getDueAmount());
//            tvName.setText(data.getCustomerName());
//            tvPlate.setText(data.getLicensePlateNO());
//            tvDistance.setText(data.getDistance());
//            if(tasksType == TasksFragment.TYPE_TASKS_TODAY){
//                tvDistance.setVisibility(View.VISIBLE);
//                tvDate.setVisibility(View.GONE);
//            }else{
//                tvDistance.setVisibility(View.GONE);
//                tvDate.setVisibility(View.VISIBLE);
//            }
//            if(tasksType == TasksFragment.TYPE_TASKS_ALL){
//                tvAllotment.setVisibility(View.GONE);
//            }else{
//                tvAllotment.setVisibility(View.VISIBLE);
//                tvAllotment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showCalendarDialog();
//                    }
//                });
//            }
        }

        private CalendarDialogFragment mCalendarDialog;
        /**
         * 显示日历空间dialog
         */
        private void showCalendarDialog() {
            if (CommUtil.checkIsNull(mCalendarDialog)) {
                mCalendarDialog = new CalendarDialogFragment();
                mCalendarDialog.setCalendarDilaogListener(new CalendarDialogFragment.CalendarDilaogListener() {
                    @Override
                    public void onCalendarClick(Date date) {
                    }
                });
            }
            if (mCalendarDialog.isVisible()) {

            } else {
                mCalendarDialog.show(getFragmentManager(), "CalendarDialog");
            }
        }

        private void closeCalendarDialog() {
            if (CommUtil.checkIsNull(mCalendarDialog)) {

            } else {
                if (mCalendarDialog.isVisible()) {
                    mCalendarDialog.dismiss();
                }
            }
        }
    }
}
