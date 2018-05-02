package com.cango.palmcartreasure.trailer.complete;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.util.CommUtil;

/**
 * 拖车打点后的提交界面
 */
public class TrailerCompleteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_trailer_complete);

        String type = getIntent().getStringExtra(TrailerCompleteFragment.TYPE);
        String imgPath = getIntent().getStringExtra(TrailerCompleteFragment.IMG_PATH);
        double lat = getIntent().getDoubleExtra(TrailerCompleteFragment.LAT, 0);
        double lon = getIntent().getDoubleExtra(TrailerCompleteFragment.LON, 0);
        String province = getIntent().getStringExtra(TrailerCompleteFragment.PROVINCE);
        TypeTaskData.DataBean.TaskListBean taskListBean = getIntent().getParcelableExtra(TrailerCompleteFragment.TASKLISTBEAN);
        TrailerCompleteFragment trailerCompleteFragment = (TrailerCompleteFragment) getSupportFragmentManager().findFragmentById(R.id.fl_complete_contains);
        if (CommUtil.checkIsNull(trailerCompleteFragment)) {
            trailerCompleteFragment = TrailerCompleteFragment.getInstance(type,imgPath, lat, lon, province,taskListBean);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_complete_contains, trailerCompleteFragment);
            transaction.commit();
        }
        TrailerCompletePresenter presenter = new TrailerCompletePresenter(trailerCompleteFragment);
    }

//    @Override
//    public void onBackPressed() {
//
//    }
}
