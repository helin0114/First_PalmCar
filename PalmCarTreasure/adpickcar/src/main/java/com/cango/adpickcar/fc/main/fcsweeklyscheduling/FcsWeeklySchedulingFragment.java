package com.cango.adpickcar.fc.main.fcsweeklyscheduling;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseAdapter;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.baseAdapter.OnLoadMoreListener;
import com.cango.adpickcar.fc.main.fcmain.FcHomeVisitFragment;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.util.SizeUtil;
import com.cango.adpickcar.util.SnackbarUtils;
import com.cango.adpickcar.util.ToastUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * true:fcs周行程管理界面    false:fcs家访跟进管理界面
 */

public class FcsWeeklySchedulingFragment extends BaseFragment implements FcsWeeklySchedulingContract.View, SwipeRefreshLayout.OnRefreshListener{
    private static final int PAGE_SIZE = 10;

    @BindView(R.id.layout_tasks_title)
    Toolbar mToolbar;
    @BindView(R.id.fc_srl_fcs_weekly_scheduling)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fc_recyclerview_fcs_weekly_scheduling)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_choose)
    RelativeLayout layoutChoose;
    @BindView(R.id.fl_shadow)
    FrameLayout flShadow;
    @BindView(R.id.rg_weeklly_choose)
    RadioGroup rgWeekllyChoose;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.mcv_fcs_weekly_scheduling)
    MaterialCalendarView mcrWeeklyScheduling;
    @BindView(R.id.layout_title_choose)
    RelativeLayout layoutTitleChoose;
    @BindView(R.id.layout_name_choose)
    LinearLayout layoutNameChoose;
    @BindView(R.id.fc_weekly_scheduling_layout_main)
    RelativeLayout layoutMain;
    @BindView(R.id.iv_transfer)
    TextView ivTransfer;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    private FcsWeeklySchedulingActivity mActivity;
    public FcsWeeklySchedulingContract.Presenter mPresenter;
    private WeeklySchedulingItemAdapter mWeeklySchedulingItemAdapter;
    private List<FcVisitTaskList.TaskListBean> mDatas;
    private boolean isLoadMore;
    private int mPageCount = 1, mTempPageCount = 2;
    //日历上被选中的日期
    private CalendarDay mSelectDay;
    //点击转派后选择的名字
    private int titleCheckedId = -1;
    //true:fcs周行程管理界面    false:fcs家访跟进管理界面
    private boolean isFCSWeekly;
    //是否有选择框被选中
    private boolean hasCheckBoxChecked;

    @OnClick({R.id.tv_title, R.id.fl_shadow, R.id.iv_transfer, R.id.tv_sure, R.id.fl_title_choose_shadow, R.id.rg_weeklly_choose, R.id.layout_name_choose})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_transfer:
                //显示选名字和日期的阴影层
                if(hasCheckBoxChecked) {
                    if (layoutChoose.getVisibility() == View.VISIBLE) {
                        layoutChoose.setVisibility(View.GONE);
                    } else {
                        layoutChoose.setVisibility(View.VISIBLE);
                        layoutTitleChoose.setVisibility(View.GONE);
                    }
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up), null);
                }else{
                    ToastUtils.showShort("请选择要分配的任务");
                }
                break;
            case R.id.fl_shadow:
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up),null);
                layoutChoose.setVisibility(View.GONE);
                break;
            case R.id.tv_title:
                //显示选名字的阴影层
                if(layoutTitleChoose.getVisibility() == View.VISIBLE){
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up),null);
                    layoutTitleChoose.setVisibility(View.GONE);
                }else{
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_down),null);
                    layoutTitleChoose.setVisibility(View.VISIBLE);
                    layoutChoose.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_sure:
                if (mSelectDay!=null){
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up),null);
                    layoutTitleChoose.setVisibility(View.GONE);
                    layoutChoose.setVisibility(View.GONE);
                } else if(titleCheckedId == -1){
                    ToastUtils.showShort("请选择分配的员工");
                }  else {
                    ToastUtils.showShort("请选择分配日期");
                }
                break;
            case R.id.fl_title_choose_shadow:
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up),null);
                layoutTitleChoose.setVisibility(View.GONE);
                break;
            case R.id.rg_weeklly_choose:
                break;
            case R.id.layout_name_choose:
                break;
                default:
                    break;
        }
    }

    public static FcsWeeklySchedulingFragment getInstance(boolean isFCSWeekly){
        FcsWeeklySchedulingFragment mWeeklySchedulingFragment = new FcsWeeklySchedulingFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_ISWEEKLY, isFCSWeekly);
        mWeeklySchedulingFragment.setArguments(bundle);
        return mWeeklySchedulingFragment;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_main_fcs_weekly_scheduling;
    }

    @Override
    protected void initView() {
        showLoadView(false);

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
        //如果是家访跟进管理界面
        if(!isFCSWeekly){
            tvTitle.setText(mActivity.getResources().getString(R.string.visit_manager));
            ivTransfer.setVisibility(View.GONE);
        }
        initCalendar();
        initRecyclerView();
        initRadioGroup();
        initTitleName();
    }

    /**
     * 初始化日历控件
     */
    private void initCalendar(){
        Calendar instance = Calendar.getInstance();
        instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);

        Calendar maxCalendar=Calendar.getInstance();
        maxCalendar.add(Calendar.DAY_OF_MONTH,6);
        //设置日期控件的高度
        mcrWeeklyScheduling.setTileHeightDp(35);
        mcrWeeklyScheduling.state().edit()
                .setMinimumDate(instance.getTime())
                .setMaximumDate(maxCalendar)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //设置日历的背景样式
        mcrWeeklyScheduling.addDecorators(
                new PrimeDayDisableDecorator(),
                new SelectDayDecorator());
        mcrWeeklyScheduling.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mSelectDay = date;
                widget.invalidateDecorators();
            }
        });
    }

    /**
     * 动态生成radiogroup中的radiobutton
     */
    private void initRadioGroup() {
        for(int i=0;i<3;i++){
            rgWeekllyChoose.addView(createRadioButton(i), ViewGroup.LayoutParams.WRAP_CONTENT, SizeUtil.dp2px(mActivity, 33));
        }
        rgWeekllyChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                titleCheckedId = checkedId;
            }
        });
//        rgWeekllyChoose.check(0);
    }

    private RadioButton createRadioButton(int id){
        RadioButton mRadioButton = new RadioButton(mActivity);
        mRadioButton.setId(id);
        mRadioButton.setTextSize(14);
        mRadioButton.setTextColor(mActivity.getResources().getColor(R.color.ad333333));
        mRadioButton.setText("sssss"+id);
        mRadioButton.setCompoundDrawablePadding(SizeUtil.dp2px(mActivity, 17));
        return mRadioButton;
    }

    /**
     * 动态生成title中的显示名字的textview
     */
    private void initTitleName(){
        for(int i=0;i<3;i++){
            layoutNameChoose.addView(createTextView(i));
        }
    }

    private TextView createTextView(int id){
        TextView mTextView = new TextView(mActivity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                SizeUtil.dp2px(mActivity, 33));
        mTextView.setLayoutParams(params);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(14);
        mTextView.setTextColor(mActivity.getResources().getColor(R.color.ad333333));
        mTextView.setText("sssss"+id);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up), null);
                layoutTitleChoose.setVisibility(View.GONE);
            }
        });
        return mTextView;
    }

    /**
     * 获取数据
     */
    private void getData() {
        mPresenter.getVisitTaskListToday(ADApplication.mSPUtils.getString(Api.USERID));
    }

    /**
     * 限制选择日期的样式
     */
    private static class PrimeDayDisableDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.isBefore(new CalendarDay());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }

    /**
     * 日历选择样式
     */
    private class SelectDayDecorator implements DayViewDecorator {
        public SelectDayDecorator() {
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return mSelectDay != null && day.equals(mSelectDay);
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.parseColor("#000000")));
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.fc_calendar_selector_bg));
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
        mWeeklySchedulingItemAdapter = new WeeklySchedulingItemAdapter(mActivity, new FCSWeeklySchedulingCheckedListener() {
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
        }, mDatas, true, isFCSWeekly);
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
    protected void initData() {
        mActivity = (FcsWeeklySchedulingActivity) getActivity();
        isFCSWeekly = getArguments().getBoolean(FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_ISWEEKLY);
    }

    @Override
    public void showLoadView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void openOtherUi() {
        SnackbarUtils.showLongDisSnackBar(layoutMain, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public void showTodayError() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    @Override
    public void showTodayNoData() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(FcsWeeklySchedulingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTodayTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (data!=null && data.size() == 0){
            if (isLoadMore){
                mWeeklySchedulingItemAdapter.setLoadEndView(R.layout.load_end_layout);
            } else {
                showTodayNoData();
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

    public class WeeklySchedulingItemAdapter extends BaseAdapter<FcVisitTaskList.TaskListBean> {
        private boolean isFCSWeekly;
        private FCSWeeklySchedulingCheckedListener mFCSWeeklySchedulingCheckedListener;

        public WeeklySchedulingItemAdapter(Context context, FCSWeeklySchedulingCheckedListener mFCSWeeklySchedulingCheckedListener,
                                           List<FcVisitTaskList.TaskListBean> datas, boolean isOpenLoadMore, boolean isFCSWeekly) {
            super(context, datas, isOpenLoadMore);
            this.isFCSWeekly = isFCSWeekly;
            this.mFCSWeeklySchedulingCheckedListener = mFCSWeeklySchedulingCheckedListener;
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.fc_weekly_scheduling_item;
        }

        @Override
        protected void convert(final BaseHolder holder, final FcVisitTaskList.TaskListBean data) {
            TextView tvSchedulingDate = holder.getView(R.id.tv_scheduling_date);
            CheckBox mCheckBox = holder.getView(R.id.cb_weekly_scheduling_item);
            tvSchedulingDate.setText("4月1日");
            tvSchedulingDate.setTextColor(mActivity.getResources().getColor(R.color.fc_text_blue));
            tvSchedulingDate.setTextSize(14);
            if(isFCSWeekly){
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckBox.setChecked(data.isChecked());
                mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mFCSWeeklySchedulingCheckedListener.onChecked(isChecked, holder.getAdapterPosition());
                    }
                });
            }else{
                mCheckBox.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 选择框选择状态改变的监听
     */
    private interface FCSWeeklySchedulingCheckedListener{
        void onChecked(boolean isChecked, int position);
    }
}
