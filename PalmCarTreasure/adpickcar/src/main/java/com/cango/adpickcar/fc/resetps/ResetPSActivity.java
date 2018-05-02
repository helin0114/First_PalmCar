package com.cango.adpickcar.fc.resetps;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;

public class ResetPSActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_ps);

        ResetFragment resetFragment = (ResetFragment) getSupportFragmentManager().findFragmentById(R.id.fl_rest_container);
        if (CommUtil.checkIsNull(resetFragment)) {
            resetFragment = ResetFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_rest_container, resetFragment);
            transaction.commit();
        }
        new ResetPSPresenter(resetFragment);
    }
}
