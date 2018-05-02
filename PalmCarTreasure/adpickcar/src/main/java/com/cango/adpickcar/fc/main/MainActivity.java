package com.cango.adpickcar.fc.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;

/**
 * Created by dell on 2017/12/11.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc_main);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fc_main_container);
        if (CommUtil.checkIsNull(mainFragment)) {
            mainFragment = MainFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_fc_main_container, mainFragment);
            transaction.commit();
        }
        new MainPresenter(mainFragment);
    }
}
