package com.cango.adpickcar.customview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

/**
 * 首页限制滑动的日历控件
 */

public class FcHomeMaterialCalendarView extends MaterialCalendarView {
    private float mLastY;//手指按下时的y轴坐标
    private float mLastX;//手指按下时的x轴坐标

    public FcHomeMaterialCalendarView(Context context) {
        this(context, null);
    }

    public FcHomeMaterialCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private SwipeRefreshLayout refreshLayout;

    /**
     * 传入外面的下拉刷新布局
     * @param refreshLayout
     */
    public void setSwipeRefreshLayout(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (refreshLayout != null)
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = ev.getY();
                    mLastX = ev.getX();
                    refreshLayout.setEnabled(false);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    refreshLayout.setEnabled(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //当手势判定为是左右滑动的时候，把下拉刷新禁用
                    if (Math.abs(ev.getX() - mLastX) > Math.abs(ev.getY() - mLastY)) {
                        refreshLayout.setEnabled(false);
                    } else {//当手势判定为是上下滑动的时候，让下拉刷新可用
                        refreshLayout.setEnabled(true);
                    }
                    break;
            }
        return super.dispatchTouchEvent(ev);
    }
}
