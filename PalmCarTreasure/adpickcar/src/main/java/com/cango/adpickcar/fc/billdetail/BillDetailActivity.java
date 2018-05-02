package com.cango.adpickcar.fc.billdetail;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cango.adpickcar.R;
import com.cango.adpickcar.fc.accountmanager.AccManagerFragment;
import com.cango.adpickcar.util.CommUtil;

public class BillDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        BillDetailFragment fragment = (BillDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fl_bill_detail_container);
        if (CommUtil.checkIsNull(fragment)) {
            fragment = BillDetailFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_bill_detail_container, fragment);
            transaction.commit();
        }
    }
}
