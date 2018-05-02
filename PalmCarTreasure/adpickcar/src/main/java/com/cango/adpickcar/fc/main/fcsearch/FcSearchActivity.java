package com.cango.adpickcar.fc.main.fcsearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;

/**
 * 搜索页
 */

public class FcSearchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc_search);

        FcSearchFragment mFcSearchFragment = (FcSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fc_search_container);
        if (CommUtil.checkIsNull(mFcSearchFragment)) {
            mFcSearchFragment = FcSearchFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_fc_search_container, mFcSearchFragment);
            transaction.commit();
        }
        new FcSearchPresenter(mFcSearchFragment);
    }
}
