package com.cango.application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.cango.application.api.Api;
import com.cango.application.base.BaseActivity;
import com.cango.application.home.HomeActivity;
import com.cango.application.login.LoginActivity;
import com.cango.application.util.AppUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("v" + AppUtils.getVersionName(this));

        if (MTApplication.mSPUtils.getInt(Api.USERID) != -1) {
            startDelay(HomeActivity.class);
        } else {
            startDelay(LoginActivity.class);
        }
//        boolean isShow = MTApplication.mSPUtils.getBoolean(Api.ISSHOWSTARTOVER);
//        if (!isShow) {
//            MTApplication.mSPUtils.put(Api.ISSHOWSTARTOVER, true);
////            startDelay(LaunchActivity.class);
//        } else {
//        int anInt = MTApplication.mSPUtils.getInt(Api.ROLE);
//        if (anInt != -1) {
//            if (anInt == Api.ADMIN_CODE) {
//            } else {
//            }
//            startDelay(HomeActivity.class);
//        } else {
//            startDelay(LoginActivity.class);
//        }
//        }
    }

    private void startDelay(final Class cls) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, cls);
                startActivity(intent);
                MTApplication.clearLastActivity();
                finish();
            }
        }, 2000);
    }

}
