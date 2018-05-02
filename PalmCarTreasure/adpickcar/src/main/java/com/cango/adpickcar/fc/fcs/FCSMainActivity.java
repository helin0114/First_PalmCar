package com.cango.adpickcar.fc.fcs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.fc.everyday.EverydayFCSFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FCSMainActivity extends BaseActivity {
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.iv_everyday)
    ImageView ivEveryday;
    @BindView(R.id.tv_everyday)
    TextView tvEveryday;
    @BindView(R.id.iv_my)
    ImageView ivMy;
    @BindView(R.id.tv_my)
    TextView tvMy;

    @OnClick({R.id.view_manager, R.id.view_everyday, R.id.view_my})
    public void onClick(View view) {
        ft = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.view_manager:
                ft.hide(everydayFCSFragment);
//                ft.hide(fcMineFragment);
                ft.show(fcsMainFragment);
                ivMain.setImageResource(R.drawable.icon_main_on);
                tvMain.setTextColor(mOnColor);
                ivEveryday.setImageResource(R.drawable.icon_daily_off);
                tvEveryday.setTextColor(mOffColor);
                ivMy.setImageResource(R.drawable.icon_my_off);
                tvMy.setTextColor(mOffColor);
                break;
            case R.id.view_everyday:
                ft.hide(fcsMainFragment);
//                ft.hide(fcMineFragment);
                ft.show(everydayFCSFragment);
                ivMain.setImageResource(R.drawable.icon_main_off);
                tvMain.setTextColor(mOffColor);
                ivEveryday.setImageResource(R.drawable.icon_daily_on);
                tvEveryday.setTextColor(mOnColor);
                ivMy.setImageResource(R.drawable.icon_my_off);
                tvMy.setTextColor(mOffColor);
                break;
            case R.id.view_my:
                ft.hide(everydayFCSFragment);
                ft.hide(fcsMainFragment);
//                ft.show(fcMineFragment);
                ivMain.setImageResource(R.drawable.icon_main_off);
                tvMain.setTextColor(mOffColor);
                ivEveryday.setImageResource(R.drawable.icon_daily_off);
                tvEveryday.setTextColor(mOffColor);
                ivMy.setImageResource(R.drawable.icon_my_on);
                tvMy.setTextColor(mOnColor);
                break;
        }
        ft.commit();
    }

    private FragmentManager fm;
    private FragmentTransaction ft;
    private FCSMainFragment fcsMainFragment;
    private EverydayFCSFragment everydayFCSFragment;
    //    private FcMineFragment fcMineFragment;
    private int mOnColor, mOffColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcsmain);
        ButterKnife.bind(this);
        mOnColor = Color.parseColor("#0db7d1");
        mOffColor = Color.parseColor("#888888");
        fm = getSupportFragmentManager();
        fcsMainFragment =FCSMainFragment.newInstance();
        everydayFCSFragment = EverydayFCSFragment.getInstance();
//        fcMineFragment = new FcMineFragment();

        ft = fm.beginTransaction();
//        ft.add(R.id.fl_fcs_main, fcMineFragment);
        ft.add(R.id.fl_fcs_main, everydayFCSFragment);
        ft.add(R.id.fl_fcs_main, fcsMainFragment);
        ft.commit();
        ivMain.setImageResource(R.drawable.icon_main_on);
        tvMain.setTextColor(mOnColor);
        ivEveryday.setImageResource(R.drawable.icon_daily_off);
        tvEveryday.setTextColor(mOffColor);
        ivMy.setImageResource(R.drawable.icon_my_off);
        tvMy.setTextColor(mOffColor);

    }
}
