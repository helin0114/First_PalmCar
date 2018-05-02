package com.cango.application.home;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

import com.cango.application.MTApplication;
import com.cango.application.R;
import com.cango.application.base.BaseActivity;
import com.cango.application.util.CommUtil;

/**
 * 主页界面
 */
public class HomeActivity extends BaseActivity {

    private HomeContract.Presenter mHomePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
//        //取消状态栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar supportActionBar = getSupportActionBar();
        if (!CommUtil.checkIsNull(supportActionBar))
            supportActionBar.hide();

        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fl_home_contains);
        if (CommUtil.checkIsNull(homeFragment)) {
            homeFragment = HomeFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_home_contains, homeFragment);
            transaction.commit();
        }
        mHomePresenter = new HomePresenter(homeFragment);
    }
    @Override
    public void onBackPressed() {
        MTApplication.clearLastActivity();
        finish();
    }
}
