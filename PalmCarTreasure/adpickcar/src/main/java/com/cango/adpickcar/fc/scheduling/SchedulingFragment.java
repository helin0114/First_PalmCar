package com.cango.adpickcar.fc.scheduling;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseAdapter;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.baseAdapter.OnLoadMoreListener;
import com.cango.adpickcar.customview.CalendarDialogFragment;
import com.cango.adpickcar.model.FcSchedulingInfo;
import com.cango.adpickcar.util.BarUtil;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.SizeUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/12/13.
 */

public class SchedulingFragment extends BaseFragment implements SchedulingContract.View,SwipeRefreshLayout.OnRefreshListener{
    private static final int PAGE_SIZE = 10;

    @BindView(R.id.layout_scheduling_title)
    Toolbar mToolbar;
    @BindView(R.id.tv_scheduling_allotment)
    TextView tvSchedulingAllotment;
    @BindView(R.id.fc_srl_scheduling)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fc_recyclerview_scheduling)
    RecyclerView mRecyclerView;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    private SchedulingActivity mActivity;
    private SchedulingContract.Presenter mPresenter;
    private SchedulingAdapter mSchedulingAdapter;
    private List<FcSchedulingInfo> mDatas;
    private CalendarDialogFragment mCalendarDialog;
    private boolean isLoadMore;
    private int mPageCount = 1, mTempPageCount = 2;

    @OnClick({R.id.tv_scheduling_allotment})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_scheduling_allotment:
                showCalendarDialog();
                break;
                default:
                    break;
        }
    }

    public static SchedulingFragment getInstance(){
        SchedulingFragment schedulingFragment = new SchedulingFragment();
        Bundle bundle = new Bundle();
        schedulingFragment.setArguments(bundle);
        return schedulingFragment;
    }


    @Override
    protected int initLayoutId() {
        return R.layout.fragment_fc_scheduling;
    }

    @Override
    protected void initView() {
        showLoadView(false);
//        BarUtil.setWindowStatusBarColor(mActivity,R.color.blue);

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

        initRecyclerView();
    }

    @Override
    protected void initData() {
        mActivity = (SchedulingActivity) getActivity();
    }

    private void initRecyclerView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mDatas = new ArrayList<>();
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mSchedulingAdapter = new SchedulingAdapter(mActivity, mDatas, true, new OnCheckBoxChangeListener() {
            @Override
            public void onChange() {
                changeTvAllotment();
            }
        });
        mSchedulingAdapter.setLoadingView(R.layout.load_loading_layout);
        mSchedulingAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                mPresenter.GetSchedulingData();
            }
        });
        mSchedulingAdapter.setOnItemClickListener(new OnBaseItemClickListener<FcSchedulingInfo>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, final FcSchedulingInfo data, final int position) {
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mSchedulingAdapter);

        onRefresh();
    }

    public void showLoadView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showError() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    @Override
    public void showNoData() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void openOtherUi() {
//        SnackbarUtils.showLongDisSnackBar(LayoutMain, "认证失败，请重新登录");
//        ADApplication.mSPUtils.clear();
//        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public void setPresenter(SchedulingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        showMainIndicator(false);
    }
    @Override
    public void showMainIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showSchedulingSuccess(List<FcSchedulingInfo> list) {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        if (isLoadMore) {
            mTempPageCount++;
            mSchedulingAdapter.setLoadMoreData(list);
        } else {
            mSchedulingAdapter.setNewDataNoError(list);
        }
        if (list.size() < PAGE_SIZE) {
            mSchedulingAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

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

    /**
     * 改变分配按钮字体颜色
     */
    private void changeTvAllotment(){
        boolean isChecked = false;
        for(FcSchedulingInfo item:mDatas){
            if(item.isChecked()){
                isChecked = true;
                break;
            }
        }
        if(isChecked){
            tvSchedulingAllotment.setTextColor(mActivity.getResources().getColor(R.color.fc_text_blue));
            tvSchedulingAllotment.setEnabled(true);
        }else{
            tvSchedulingAllotment.setTextColor(mActivity.getResources().getColor(R.color.ad666666));
            tvSchedulingAllotment.setEnabled(false);
        }
    }

    private interface OnCheckBoxChangeListener{
        void onChange();
    }

    private class SchedulingAdapter extends BaseAdapter<FcSchedulingInfo> {
        private OnCheckBoxChangeListener mOnCheckBoxChangeListener;

        public SchedulingAdapter(Context context, List<FcSchedulingInfo> datas, boolean isOpenLoadMore, OnCheckBoxChangeListener mOnCheckBoxChangeListener) {
            super(context, datas, isOpenLoadMore);
            this.mOnCheckBoxChangeListener = mOnCheckBoxChangeListener;
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.fc_tasks_item;
        }

        @Override
        protected void convert(BaseHolder holder, final FcSchedulingInfo data) {
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
//            TextView tvLabelThree = holder.getView(R.id.tv_fc_item_label_three);
            CheckBox fcCheckboxItem = holder.getView(R.id.fc_checkbox_group_item);

            fcCheckboxItem.setVisibility(View.VISIBLE);
            tvDistance.setVisibility(View.VISIBLE);
            tvDate.setVisibility(View.GONE);
            tvAllotment.setVisibility(View.GONE);

            fcCheckboxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!buttonView.isPressed())
                        return;
                    data.setChecked(isChecked);
                    mOnCheckBoxChangeListener.onChange();
                }
            });
            fcCheckboxItem.setChecked(data.isChecked());
        }
    }
}
