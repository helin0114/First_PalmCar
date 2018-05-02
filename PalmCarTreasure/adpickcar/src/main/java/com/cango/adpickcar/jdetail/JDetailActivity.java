package com.cango.adpickcar.jdetail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.model.DeliveryTaskList;
import com.cango.adpickcar.util.CommUtil;

public class JDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jdetail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        DeliveryTaskList.DataBean.CarDeliveryTaskListBean carDeliveryTaskListBean = getIntent().getParcelableExtra("CarDeliveryTaskListBean");
        int type = getIntent().getIntExtra("Type", -1);
        JDetailFragment jDetailFragment = (JDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fl_jdetail_container);
        if (CommUtil.checkIsNull(jDetailFragment)) {
            jDetailFragment = JDetailFragment.getInstance(carDeliveryTaskListBean,type);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_jdetail_container, jDetailFragment);
            transaction.commit();
        }
        new JDetailPresenter(jDetailFragment);
    }
}
