package com.cango.adpickcar.customview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cango.adpickcar.R;
import com.cango.adpickcar.util.ToastUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;

/**
 * 界面下方显示的日历控件
 */

public class CalendarNewFcDialogFragment extends DialogFragment implements OnDateSelectedListener {
    CalendarDay mSelectDay;
    Drawable selectDayEqualToday;
    OneDayDecorator mOneDayDecorator;
    private boolean isLimitDate = true;//是否限制显示的日期
    private int limitDateNum = 6;//限制日期的天数

    public static CalendarNewFcDialogFragment getInstance(boolean isLimitDate, int limitDateNum){
        CalendarNewFcDialogFragment mCalendarNewFcDialogFragment = new CalendarNewFcDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLimitDate", isLimitDate);
        bundle.putInt("limitDateNum", limitDateNum);
        mCalendarNewFcDialogFragment.setArguments(bundle);
        return mCalendarNewFcDialogFragment;
    }

    public void setCalendarDilaogListener(CalendarDilaogListener calendarDilaogListener) {
        this.mListener = calendarDilaogListener;
    }

    private CalendarDilaogListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_new_fc_calendar_dialog, container, false);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_calendar_sure);
        selectDayEqualToday = getActivity().getResources().getDrawable(R.drawable.calendar_selector);
        MaterialCalendarView calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);

        if(getArguments() != null){
            isLimitDate = getArguments().getBoolean("isLimitDate",true);
            limitDateNum = getArguments().getInt("limitDateNum", 6);
        }

        Calendar instance = Calendar.getInstance();
        instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);

        calendarView.setTileHeightDp(35);
        mOneDayDecorator = new OneDayDecorator();
        if(isLimitDate){
            Calendar maxCalendar=Calendar.getInstance();
            maxCalendar.add(Calendar.DAY_OF_MONTH,limitDateNum);
            calendarView.state().edit()
                    .setMinimumDate(instance.getTime())
                    .setMaximumDate(maxCalendar)
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            calendarView.addDecorators(
                    new PrimeDayDisableDecorator(),
                    mOneDayDecorator
            );
        }else{
            calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            calendarView.addDecorators(mOneDayDecorator);
        }
        calendarView.setOnDateChangedListener(this);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectDay!=null){
                    getDialog().dismiss();
                    mListener.onCalendarClick(mSelectDay.getDate());
                }else {
                    ToastUtils.showShort("请选择分配日期");
                }
            }
        });
        view.findViewById(R.id.layout_calendar_newfc_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        mSelectDay = date;
        widget.invalidateDecorators();
    }

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

    private class OneDayDecorator implements DayViewDecorator {
        public OneDayDecorator() {
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
    public interface CalendarDilaogListener{
        void onCalendarClick(Date date);
    }
}