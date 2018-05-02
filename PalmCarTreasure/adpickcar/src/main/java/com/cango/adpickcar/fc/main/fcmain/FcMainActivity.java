package com.cango.adpickcar.fc.main.fcmain;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;

/**
 * Created by dell on 2018/4/8.
 */

public class FcMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc_new_main);

        FcMainFragment mFcMainFragment = (FcMainFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fc_new_main_container);
        if (CommUtil.checkIsNull(mFcMainFragment)) {
            mFcMainFragment = FcMainFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_fc_new_main_container, mFcMainFragment);
            transaction.commit();
        }
        new FcMainPresenter(mFcMainFragment);
    }
}
