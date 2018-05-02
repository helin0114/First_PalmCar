package com.cango.adpickcar.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cango.adpickcar.ADApplication;
import com.orhanobut.logger.Logger;

/**
 * Created by cango on 2017/9/18.
 * 做一些通用的设置
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ADApplication.addActivity(this);
//        Logger.d(ADApplication.activityList.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ADApplication.clearLastActivity();
//        Logger.d(ADApplication.activityList.size());
    }
}
