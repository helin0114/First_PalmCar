package com.cango.adpickcar.fc.main.weeklyscheduling;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * true:fcs家访初访管理界面    false:fc周行程管理界面
 */

public class WeeklySchedulingFragment extends BaseFragment implements WeeklySchedulingContract.View{
    public static final int TYPE_SCHEDULING_NEW = 0;   //新任务
    public static final int TYPE_SCHEDULING_APPROVAL  = 1;    //待审批
    public static final int TYPE_SCHEDULING_FEEDBACK = 2; //待反馈

    @BindView(R.id.layout_tasks_title)
    Toolbar mToolbar;
    @BindView(R.id.fc_tl_tasks_label)
    TabLayout mTabLayout;
    @BindView(R.id.fc_vp_tasks_label)
    ViewPager mViewPager;
    @BindView(R.id.fc_weekly_scheduling_layout_main)
    RelativeLayout layoutMain;
    @BindView(R.id.iv_transfer)
    TextView ivTransfer;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_choose)
    RelativeLayout layoutChoose;
    @BindView(R.id.fl_shadow)
    FrameLayout flShadow;
    @BindView(R.id.rg_weeklly_choose)
    RadioGroup rgWeekllyChoose;
    @BindView(R.id.mcv_fcs_weekly_scheduling)
    MaterialCalendarView mcrWeeklyScheduling;
    @BindView(R.id.layout_title_choose)
    RelativeLayout layoutTitleChoose;
    @BindView(R.id.layout_name_choose)
    LinearLayout layoutNameChoose;

    private WeeklySchedulingActivity mActivity;
    private ArrayList<String> tabList = new ArrayList<>();
    private ArrayList<Fragment> fgList = new ArrayList<>();
    private MyAdapter mMyAdapter;
    public WeeklySchedulingContract.Presenter mPresenter;
    private WeeklySchedulingItemFragment mWeeklySchedulingItemFragmentNew;
    private WeeklySchedulingItemFragment mWeeklySchedulingItemFragmentApproval;
    private WeeklySchedulingItemFragment mWeeklySchedulingItemFragmentFeedback;
    //true:fcs家访初访管理界面    false:fc周行程管理界面
    private boolean isFcOrFcs;
    //日历上被选中的日期
    private CalendarDay mSelectDay;
    //点击转派后选择的名字
    private int titleCheckedId = -1;

    @OnClick({R.id.tv_title, R.id.fl_shadow, R.id.iv_transfer, R.id.tv_sure, R.id.fl_title_choose_shadow, R.id.rg_weeklly_choose, R.id.layout_name_choose})
    public void onClick(View view) {
        if(isFcOrFcs) {
            switch (view.getId()) {
                case R.id.iv_transfer:
                    //显示选名字和日期的阴影层
                    if(mWeeklySchedulingItemFragmentNew.hasCheckBoxChecked
                            || mWeeklySchedulingItemFragmentApproval.hasCheckBoxChecked) {
                        if (layoutChoose.getVisibility() == View.VISIBLE) {
                            layoutChoose.setVisibility(View.GONE);
                        } else {
                            layoutChoose.setVisibility(View.VISIBLE);
                            layoutTitleChoose.setVisibility(View.GONE);
                        }
                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up), null);
                    } else {
                        ToastUtils.showShort("请选择要分配的任务");
                    }
                    break;
                case R.id.fl_shadow:
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up), null);
                    layoutChoose.setVisibility(View.GONE);
                    break;
                case R.id.tv_title:
                    //显示选名字的阴影层
                    if (layoutTitleChoose.getVisibility() == View.VISIBLE) {
                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up), null);
                        layoutTitleChoose.setVisibility(View.GONE);
                    } else {
                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_down), null);
                        layoutTitleChoose.setVisibility(View.VISIBLE);
                        layoutChoose.setVisibility(View.GONE);
                    }
                    break;
                case R.id.tv_sure:
                    if (mSelectDay != null) {
                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up), null);
                        layoutTitleChoose.setVisibility(View.GONE);
                        layoutChoose.setVisibility(View.GONE);
                    } else if(titleCheckedId == -1){
                        ToastUtils.showShort("请选择分配的员工");
                    } else {
                        ToastUtils.showShort("请选择分配日期");
                    }
                    break;
                case R.id.fl_title_choose_shadow:
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up), null);
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
    }

    public static WeeklySchedulingFragment getInstance(boolean isFcOrFcs){
        WeeklySchedulingFragment mWeeklySchedulingFragment = new WeeklySchedulingFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_FCORFCS,isFcOrFcs);
        mWeeklySchedulingFragment.setArguments(bundle);
        return mWeeklySchedulingFragment;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_main_weekly_scheduling;
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
        //如果是fcs家访初访管理界面
        if(isFcOrFcs){
            ivTransfer.setVisibility(View.VISIBLE);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mActivity.getResources().getDrawable(R.drawable.weekly_schudeling_turn_up),null);
        }
        initCalendar();
        initTabLayout();
        initRadioGroup();
        initTitleName();
    }

    /**
     * 初始化viewpager
     */
    private void initTabLayout() {
        tabList.clear();
        fgList.clear();
        //如果是fcs家访初访管理界面
        if(isFcOrFcs){
            tvTitle.setText(mActivity.getResources().getString(R.string.first_home_manager));
            tabList.add(mActivity.getResources().getString(R.string.task_today));
            tabList.add(mActivity.getResources().getString(R.string.task_week));
            tabList.add(mActivity.getResources().getString(R.string.task_all));
        } else {
            tvTitle.setText(mActivity.getResources().getString(R.string.week_manager));
            tabList.add(mActivity.getResources().getString(R.string.task_new));
            tabList.add(mActivity.getResources().getString(R.string.task_approval));
            tabList.add(mActivity.getResources().getString(R.string.task_feedback));
        }
        mWeeklySchedulingItemFragmentNew = WeeklySchedulingItemFragment.getInstance(TYPE_SCHEDULING_NEW, isFcOrFcs);
        mWeeklySchedulingItemFragmentApproval = WeeklySchedulingItemFragment.getInstance(TYPE_SCHEDULING_APPROVAL, isFcOrFcs);
        mWeeklySchedulingItemFragmentFeedback = WeeklySchedulingItemFragment.getInstance(TYPE_SCHEDULING_FEEDBACK, isFcOrFcs);
        fgList.add(mWeeklySchedulingItemFragmentNew);
        fgList.add(mWeeklySchedulingItemFragmentApproval);
        fgList.add(mWeeklySchedulingItemFragmentFeedback);
        mMyAdapter = new MyAdapter(getChildFragmentManager(), tabList, fgList);
        mViewPager.setAdapter(mMyAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager, true);
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
        mRadioButton.setText("李顺发"+id);
        mRadioButton.setGravity(Gravity.CENTER);
        mRadioButton.setCompoundDrawablesWithIntrinsicBounds(null,
                null,null,null);
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
    @Override
    protected void initData() {
        mActivity = (WeeklySchedulingActivity) getActivity();
        isFcOrFcs = getArguments().getBoolean(FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_FCORFCS);
    }

    @Override
    public void setPresenter(WeeklySchedulingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

//    @Override
//    public void showLoadViewNew(boolean isShow) {
//        mWeeklySchedulingItemFragmentNew.showLoadView(isShow);
//    }
//
//    @Override
//    public void showLoadViewApproval(boolean isShow) {
//        mWeeklySchedulingItemFragmentApproval.showLoadView(isShow);
//    }
//
//    @Override
//    public void showLoadViewFeedBack(boolean isShow) {
//        mWeeklySchedulingItemFragmentFeedback.showLoadView(isShow);
//    }

    @Override
    public void openOtherUi() {
        SnackbarUtils.showLongDisSnackBar(layoutMain, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public void showNewError() {
        mWeeklySchedulingItemFragmentNew.showError();
    }

    @Override
    public void showNewNoData() {
        mWeeklySchedulingItemFragmentNew.showNoData();
    }

    @Override
    public void showNewTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        mWeeklySchedulingItemFragmentNew.showTasksSuccess(data);
    }

    @Override
    public void showApprovalError() {
        mWeeklySchedulingItemFragmentApproval.showError();
    }

    @Override
    public void showApprovalNoData() {
        mWeeklySchedulingItemFragmentApproval.showNoData();
    }

    @Override
    public void showApprovalTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        mWeeklySchedulingItemFragmentApproval.showTasksSuccess(data);
    }

    @Override
    public void showFeedBackError() {
        mWeeklySchedulingItemFragmentFeedback.showError();
    }

    @Override
    public void showFeedBackNoData() {
        mWeeklySchedulingItemFragmentFeedback.showNoData();
    }

    @Override
    public void showFeedBackTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        mWeeklySchedulingItemFragmentFeedback.showTasksSuccess(data);
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
