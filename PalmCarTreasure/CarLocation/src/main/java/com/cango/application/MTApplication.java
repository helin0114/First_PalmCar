package com.cango.application;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.cango.application.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created by cango on 2017/6/5.
 */

public class MTApplication extends Application {
    private static Context mContext;
    public static SPUtils mSPUtils;
    public static List<AppCompatActivity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
//        Logger.init("LLI").logLevel(LogLevel.NONE);
        mContext = getApplicationContext();
        mSPUtils = new SPUtils("cango_vps");
        BGASwipeBackManager.getInstance().init(this);
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void addActivity(AppCompatActivity activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        activityList.add(activity);
    }

    public static void clearActivitys() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            for (AppCompatActivity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        }
    }

    public static void clearLastActivity() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            activityList.remove(activityList.size() - 1);
        }
    }

    public static void clearExceptLastActivitys() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            for (int i = 0; i < activityList.size() - 1; i++) {
                if (activityList.get(i) != null) {
                    activityList.get(i).finish();
                }
            }
            AppCompatActivity activity = activityList.get(activityList.size() - 1);
            activityList.clear();
            activityList.add(activity);
        }
    }

    private static AppCompatActivity getLastActvity() {
        if (activityList == null || activityList.size() == 0) {
            return null;
        } else {
            return activityList.get(activityList.size() - 1);
        }
    }
}
