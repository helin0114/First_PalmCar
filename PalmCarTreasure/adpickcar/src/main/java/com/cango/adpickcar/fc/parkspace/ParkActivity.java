package com.cango.adpickcar.fc.parkspace;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cango.adpickcar.R;
import com.cango.adpickcar.fc.download.DownloadFragment;
import com.cango.adpickcar.fc.download.DownloadPresenter;
import com.cango.adpickcar.util.CommUtil;

public class ParkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);
        ParkFragment fragment = (ParkFragment) getSupportFragmentManager().findFragmentById(R.id.fl_park_container);
        if (CommUtil.checkIsNull(fragment)) {
            fragment = ParkFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_park_container, fragment);
            transaction.commit();
        }
        new ParkPresenter(fragment);
    }
}
