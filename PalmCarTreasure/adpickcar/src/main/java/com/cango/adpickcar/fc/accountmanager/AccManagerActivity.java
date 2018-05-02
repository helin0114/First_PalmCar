package com.cango.adpickcar.fc.accountmanager;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.fc.download.DownloadFragment;
import com.cango.adpickcar.fc.download.DownloadPresenter;
import com.cango.adpickcar.util.CommUtil;

public class AccManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_manager);
        AccManagerFragment fragment = (AccManagerFragment) getSupportFragmentManager().findFragmentById(R.id.fl_acc_manager_container);
        if (CommUtil.checkIsNull(fragment)) {
            fragment = AccManagerFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_acc_manager_container, fragment);
            transaction.commit();
        }
    }
}
