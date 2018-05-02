package com.cango.adpickcar.login;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;
import com.orhanobut.logger.Logger;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fl_login_container);
        if (CommUtil.checkIsNull(loginFragment)) {
            loginFragment = LoginFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_login_container, loginFragment);
            transaction.commit();
        }
        new LoginPresenter(loginFragment);
        ADApplication.clearExceptLastActivitys();
        Logger.d(ADApplication.activityList.size());
    }
}
