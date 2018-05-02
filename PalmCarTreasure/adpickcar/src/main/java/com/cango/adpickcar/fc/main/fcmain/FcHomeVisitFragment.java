package com.cango.adpickcar.fc.main.fcmain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cango.adpickcar.CustomQRCodeActivity;
import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseAdapter;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.customview.CalendarNewFcDialogFragment;
import com.cango.adpickcar.customview.FcDotSpan;
import com.cango.adpickcar.customview.FcHomeMaterialCalendarView;
import com.cango.adpickcar.customview.SlidingButtonView;
import com.cango.adpickcar.fc.main.fcsearch.FcSearchActivity;
import com.cango.adpickcar.fc.main.weeklyscheduling.WeeklySchedulingActivity;
import com.cango.adpickcar.fc.tasks.TasksActivity;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.ScreenUtil;
import com.cango.adpickcar.util.ToastUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.CalendarUtils;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2018/4/8.
 */

public class FcHomeVisitFragment extends BaseFragment implements OnDateSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    //true:fc初次家访界面         false:fc家访跟进界面
    public static String TYPE_FIRSTORNOT_HOMEVISIT = "type_firstornot_homevisit";

    //true:fcs家访初访管理界面    false:fc周行程管理界面
    public static String TYPE_ADMINI_HOMEVISIT_FCORFCS = "type_admini_homevisit_fcorfcs";

    //true:fcs周行程管理界面    false:fcs家访跟进管理界面
    public static String TYPE_ADMINI_HOMEVISIT_ISWEEKLY = "type_admini_homevisit_isweekly";

    @BindView(R.id.mcv_main_home_visit)
    FcHomeMaterialCalendarView mCalendarView;
    @BindView(R.id.tv_calendar_title)
    TextView tvCalendarTitle;
    @BindView(R.id.rv_main_home_visit)
    RecyclerView mRecyclerView;
    @BindView(R.id.fc_srl_home_visit)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private DateFormatTitleFormatter formatter = new DateFormatTitleFormatter();//日期转换格式
    private FcMainActivity mActivity;
    //日历上被选中的日期
    private CalendarDay mSelectDay;
    //左滑的recyclerView
    private NormalRecyclerViewAdapter adapter;
    //选择日期的dialog框
//    private CalendarDialogFragment mCalendarDialog;
    private CalendarNewFcDialogFragment mCalendarNewFcDialogFragment;

    @OnClick({R.id.layout_main_first_home, R.id.layout_main_home_visit, R.id.layout_main_weekly_travel, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_main_first_home:
                Intent intent1 = new Intent(mActivity, TasksActivity.class);
                intent1.putExtra(TYPE_FIRSTORNOT_HOMEVISIT, true);//初次家访界面
                mActivity.startActivity(intent1);
                break;
            case R.id.layout_main_home_visit:
                Intent intent2 = new Intent(mActivity, TasksActivity.class);
                intent2.putExtra(TYPE_FIRSTORNOT_HOMEVISIT, false);//家访跟进界面
                mActivity.startActivity(intent2);
                break;
            case R.id.layout_main_weekly_travel:
                Intent intent3 = new Intent(mActivity, WeeklySchedulingActivity.class);
                mActivity.startActivity(intent3);
                break;
            case R.id.iv_search:
//                mActivity.startActivity(new Intent(mActivity, FcSearchActivity.class));

                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this).setCaptureActivity(CustomQRCodeActivity.class);
                integrator.addExtra("from", true);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.initiateScan();
                break;
            default:
                break;
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_main_home_visit;
    }

    @Override
    protected void initView() {
        //设置显示今天日期的月份
        tvCalendarTitle.setText(getCalendarString(formatter.format(mCalendarView.getCurrentDate()).toString()));

        //设置日期控件的当前默认选中时间
//        mCalendarView.setSelectedDate(new Date());
        //设置日期控件的高度
        mCalendarView.setTileHeightDp(35);
        mCalendarView.setSwipeRefreshLayout(mSwipeRefreshLayout);

        //得到当前日期在一周内的所在position，例如周末就应该是7（因为周一在第一个周日最后一个）
        int todayWeek = CalendarUtils.getDayOfWeek(CalendarDay.today().getCalendar()) - 1;
        if (todayWeek == 0) {
            todayWeek = 7;
        }
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.DAY_OF_MONTH, (-7 - todayWeek + 1));
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.DAY_OF_MONTH, 7 + 7 - todayWeek);

        //设置日历的常用设置
        mCalendarView.state().edit()
                // 设置你的日历 第一天是周一还是周一
                .setFirstDayOfWeek(Calendar.MONDAY)
                // 设置你的日历的最小的月份
                .setMinimumDate(minCalendar)
                // 同最小 设置最大
                .setMaximumDate(maxCalendar)
                .setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        //设置日期的title不可见
        mCalendarView.setTopbarVisible(false);
        //监听日期的月份改变
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                //日期的月份改变 title的月份也要改变
                tvCalendarTitle.setText(getCalendarString(formatter.format(date).toString()));
            }
        });
        //设置带有红点的日期
        HashSet<CalendarDay> dates = new HashSet<>();
        dates.add(CalendarDay.from(stringToDate("20180419")));
        dates.add(CalendarDay.from(stringToDate("20180420")));
        dates.add(CalendarDay.from(stringToDate("20180423")));

        mCalendarView.setOnDateChangedListener(this);

        //设置日历的背景样式
        mCalendarView.addDecorators(
                new EventDecorator(dates),
                new TodayDecorator(),
                new SelectDayDecorator());

        initRecycleView();
    }

    /**
     * 日期转换（“20180409”转换为date类型）
     *
     * @param strTime
     * @return
     */
    private Date stringToDate(String strTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        tvCalendarTitle.setText(getCalendarString(formatter.format(date).toString()));
        mSelectDay = date;
        widget.invalidateDecorators();
    }

    /**
     * 日历红点样式
     */
    private class EventDecorator implements DayViewDecorator {
        private HashSet<CalendarDay> dates;

        public EventDecorator(Collection<CalendarDay> dates) {
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new FcDotSpan(8, Color.parseColor("#ff0000")));
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
     * 日历今天的日期的样式
     */
    private class TodayDecorator implements DayViewDecorator {
        private final CalendarDay today;

        public TodayDecorator() {
            today = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.fc_calendar_solid_bg));
        }
    }

    /**
     * 初始化recycleview
     */
    private void initRecycleView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));//这里用线性显示 类似于listview
        List<String> mDatas = new ArrayList<>();
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        adapter = new NormalRecyclerViewAdapter(mActivity, mDatas, false, new IonSlidingViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.showShort("onItemClick:" + position);
            }

            @Override
            public void onDeleteBtnCilck(View view, int position) {
                showCalendarDialog();
//                adapter.removeData(position);
//                adapter.notifyDataSetChanged();
            }
        });
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    public void showHomeVisitIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void onRefresh() {
        getHomeVisitDataSuccess();
    }

    public void getHomeVisitDataSuccess() {
        showHomeVisitIndicator(false);
    }

    public void showHomeVisitError() {
        showHomeVisitIndicator(false);
    }

    public void showHomeVisitNoData() {
        showHomeVisitIndicator(false);
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
                    ToastUtils.showShort("onCalendarClick:" + date.toString());
                }
            });
        }
        if (mCalendarNewFcDialogFragment.isVisible()) {

        } else {
            mCalendarNewFcDialogFragment.show(getFragmentManager(), "CalendarDialog");
        }
    }

    /**
     * 设置日期的title的字样和字体
     *
     * @param date
     * @return
     */
    private SpannableString getCalendarString(String date) {
        SpannableString spannableString = new SpannableString(date);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString.length() - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#13a6c1")), 0, spannableString.length() - 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    protected void initData() {
        mActivity = (FcMainActivity) getParentFragment().getActivity();
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                ToastUtils.showShort("二维码异常");
            } else {
                ToastUtils.showShort(result.getContents());
                Logger.d(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 滑动删除的recyclerview的adapter
     */
    private class NormalRecyclerViewAdapter extends BaseAdapter<String> implements SlidingButtonView.IonSlidingButtonListener {
        private Context mContext;
        private IonSlidingViewClickListener mIDeleteBtnClickListener;
        private List<String> mDatas = new ArrayList<>();
        private SlidingButtonView mMenu = null;

        public NormalRecyclerViewAdapter(Context mContext, List<String> mDatas, boolean isOpenLoadMore, IonSlidingViewClickListener mIDeleteBtnClickListener) {
            super(mContext, mDatas, isOpenLoadMore);
            this.mContext = mContext;
            this.mDatas = mDatas;
            this.mIDeleteBtnClickListener = mIDeleteBtnClickListener;
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.layout_item;
        }

        @Override
        protected void convert(final BaseHolder holder, final String data) {
            TextView btnDelete = holder.getView(R.id.tv_delete);
            TextView textView = holder.getView(R.id.text);
            ViewGroup layoutContent = holder.getView(R.id.layout_content);
            // 设置内容布局的宽为屏幕宽度
            layoutContent.getLayoutParams().width = ScreenUtil.getScreenWidth(mContext);
            layoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否有删除菜单打开
                    if (menuIsOpen()) {
                        closeMenu();//关闭菜单
                    } else {
                        int n = holder.getLayoutPosition();
                        mIDeleteBtnClickListener.onItemClick(v, n);
                    }
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
                }
            });
            ((SlidingButtonView) holder.getView(R.id.sliding_button)).setSlidingButtonListener(NormalRecyclerViewAdapter.this);
        }

        public void removeData(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        //删除菜单打开信息接收
        @Override
        public void onMenuIsOpen(View view) {
            mMenu = (SlidingButtonView) view;
        }

        /**
         * 滑动或者点击了Item监听
         *
         * @param slidingButtonView
         */
        @Override
        public void onDownOrMove(SlidingButtonView slidingButtonView) {
            if (menuIsOpen()) {
//                if(mMenu != slidingButtonView){
                closeMenu();
//                }
            }
        }

        //关闭菜单
        public void closeMenu() {
            mMenu.closeMenu();
            mMenu = null;
        }

        //判断是否有菜单打开
        public Boolean menuIsOpen() {
            if (mMenu != null) {
                return true;
            }
            return false;
        }
    }

    private interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }
}
