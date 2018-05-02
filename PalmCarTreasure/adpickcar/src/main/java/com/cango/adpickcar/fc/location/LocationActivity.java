package com.cango.adpickcar.fc.location;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;

/**
 * 定位界面
 */
public class LocationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        LocationFragment locationFragment = (LocationFragment) getSupportFragmentManager().findFragmentById(R.id.fl_location_contains);
        String IMEI = getIntent().getStringExtra("IMEI");
        if (CommUtil.checkIsNull(locationFragment)) {
            locationFragment = LocationFragment.newInstance(IMEI);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_location_contains, locationFragment);
            transaction.commit();
        }
        new LocationPresenter(locationFragment);
    }
}
