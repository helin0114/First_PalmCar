package com.cango.adpickcar.customview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cango.adpickcar.R;
import com.cango.adpickcar.util.ToastUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Date;

/**
 * Created by dell on 2017/12/21.
 */

public class CalendarAccMgrFG extends DialogFragment implements OnDateSelectedListener {
    CalendarDay mSelectDay;
    Drawable selectDayEqualToday;
    private CalendarDilaogListener mListener;

    public void setCalendarDilaogListener(CalendarDilaogListener calendarDilaogListener) {
        this.mListener = calendarDilaogListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fg_calendar, container, false);
        Button btnStart = (Button) view.findViewById(R.id.btn_calendar_start);
        selectDayEqualToday = getActivity().getResources().getDrawable(R.drawable.calendar_selector);
        MaterialCalendarView calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);

//        Calendar instance = Calendar.getInstance();
//        instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);
//
//        Calendar maxCalendar=Calendar.getInstance();
//        maxCalendar.add(Calendar.DAY_OF_MONTH,6);

//        calendarView.state().edit()
//                .setMinimumDate(instance.getTime())
//                .setMaximumDate(maxCalendar)
//                .setCalendarDisplayMode(CalendarMode.MONTHS)
//                .commit();
        calendarView.addDecorators(
                new TodayDecorator(),
//                new MySelectorDecorator(getActivity()),
//                new PrimeDayDisableDecorator(),
                new OneDayDecorator()
        );
        calendarView.setOnDateChangedListener(this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectDay != null) {
                    getDialog().dismiss();
                    mListener.onCalendarClick(mSelectDay.getDate());
                } else {
                    ToastUtils.showShort(R.string.choose_tally_date);
                }
            }
        });
        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        mSelectDay = date;
//        mOneDayDecorator.setDate(date);
        widget.invalidateDecorators();
    }

    public class MySelectorDecorator implements DayViewDecorator {

        private final Drawable drawable;

        public MySelectorDecorator(Activity context) {
            drawable = context.getResources().getDrawable(R.drawable.calendar_selector);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
        }
    }

    public class TodayDecorator implements DayViewDecorator {

        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        public TodayDecorator() {
            today = CalendarDay.today();
            backgroundDrawable = getResources().getDrawable(R.drawable.fc_calendar_solid_bg);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
            view.setBackgroundDrawable(backgroundDrawable);
        }
    }

    public static class PrimeDayDisableDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.isBefore(new CalendarDay());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }

    public class OneDayDecorator implements DayViewDecorator {

        public OneDayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return mSelectDay != null && day.equals(mSelectDay);
        }

        @Override
        public void decorate(DayViewFacade view) {
//            view.addSpan(new StyleSpan(Typeface.BOLD));
//            view.addSpan(new RelativeSizeSpan(1.2f));
//            view.setBackgroundDrawable(selectDayEqualToday);

            view.addSpan(new ForegroundColorSpan(Color.parseColor("#000000")));
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.fc_calendar_selector_bg));
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
//        public void setDate(CalendarDay date) {
//            this.date = date;
//        }
    }

    public interface CalendarDilaogListener {
        void onCalendarClick(Date date);
    }
}