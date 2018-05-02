package com.cango.application.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.cango.application.R;
import com.cango.application.base.BaseActivity;
import com.cango.application.util.CommUtil;

/**
 * Created by cango on 2017/6/5.
 */

public class LoginActivity extends BaseActivity {
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fl_login_contains);
        if (CommUtil.checkIsNull(loginFragment)) {
            loginFragment = LoginFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_login_contains, loginFragment);
            transaction.commit();
        }
        mLoginPresenter = new LoginPresenter(loginFragment);
    }
}
