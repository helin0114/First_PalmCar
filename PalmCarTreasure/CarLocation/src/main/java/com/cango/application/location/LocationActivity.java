package com.cango.application.location;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.application.R;
import com.cango.application.base.BaseActivity;
import com.cango.application.home.HomeFragment;
import com.cango.application.home.HomePresenter;
import com.cango.application.model.ImeiQuery;
import com.cango.application.util.CommUtil;

/**
 * 定位界面
 */
public class LocationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        LocationFragment locationFragment = (LocationFragment) getSupportFragmentManager().findFragmentById(R.id.fl_location_contains);
        ImeiQuery.DataBean dataBean=getIntent().getParcelableExtra("DataBean");
        if (CommUtil.checkIsNull(locationFragment)) {
            locationFragment = LocationFragment.newInstance(dataBean);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_location_contains, locationFragment);
            transaction.commit();
        }
        LocationPresenter locationPresenter = new LocationPresenter(locationFragment);
    }
}
