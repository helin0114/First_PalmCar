package com.cango.adpickcar.detail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.SoftHideKeyBoardUtil;
import com.orhanobut.logger.Logger;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("onCreate");
        setContentView(R.layout.activity_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        CarTakeTaskList.DataBean.CarTakeTaskListBean carTakeTaskListBean = getIntent().getParcelableExtra("CarTakeTaskListBean");
        int type = getIntent().getIntExtra("Type", -1);
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fl_detail_container);
        if (CommUtil.checkIsNull(detailFragment)) {
            detailFragment = DetailFragment.getInstance(carTakeTaskListBean, type);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_detail_container, detailFragment);
            transaction.commit();
        }
        new DetailPresenter(detailFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Logger.d("onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
