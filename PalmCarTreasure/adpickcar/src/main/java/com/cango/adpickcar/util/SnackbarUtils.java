package com.cango.adpickcar.util;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.cango.adpickcar.ADApplication;

/**
 * Created by dell on 2017/12/1.
 * snackbar工具类
 */

public class SnackbarUtils {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private SnackbarUtils(){
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 安全地显示自定义SnackBar(带自定义按钮)
     * @param view          父布局对象
     * @param messageTips   提示文本
     * @param messageDis    点击文本
     * @param duration      显示时间
     * @param listener      点击监听
     */
    public static void showSnackBar(final View view, final CharSequence messageTips,
                                    final CharSequence messageDis, final int duration, final View.OnClickListener listener) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view,messageTips,duration)
                        .setAction(messageDis, listener)
                        .show();
            }
        });
    }

    /**
     * 安全地显示自定义SnackBar(不带按钮)
     * @param view         父布局对象
     * @param messageTips  提示文本
     * @param duration     显示时间
     */
    public static void showSnackBar(final View view, final CharSequence messageTips, final int duration) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view,messageTips,duration)
                        .setAction("", null)
                        .show();
            }
        });
    }

    /**
     * 安全地显示长时间SnackBar（自带“消失”按钮）
     * @param view         父布局对象
     * @param messageTips  提示文本
     */
    public static void showLongDisSnackBar(final View view, final CharSequence messageTips) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view,messageTips,Snackbar.LENGTH_LONG)
                        .setAction("消失", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        });
    }

    /**
     * 安全地显示长时间SnackBar（自带“消失”按钮）
     * @param view         父布局对象
     * @param resId        提示文本id
     */
    public static void showLongDisSnackBar(final View view, final int resId) {
        showLongDisSnackBar(view, ADApplication.getmContext().getResources().getText(resId).toString());
    }

    /**
     * 安全地显示短时间SnackBar（自带“消失”按钮）
     * @param view         父布局对象
     * @param messageTips  提示文本
     */
    public static void showShortDisSnackBar(final View view, final CharSequence messageTips) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view,messageTips,Snackbar.LENGTH_SHORT)
                        .setAction("消失",  new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        });
    }
    /**
     * 安全地显示短时间SnackBar（自带“消失”按钮）
     * @param view         父布局对象
     * @param resId        提示文本id
     */
    public static void showShortDisSnackBar(final View view, final int resId) {
        showShortDisSnackBar(view, ADApplication.getmContext().getResources().getText(resId).toString());
    }
}
