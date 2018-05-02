package com.cango.adpickcar.fc.main.fcmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.fc.everyday.EverydayFCSFragment;
import com.cango.adpickcar.fc.everyday.EverydayFragment;
import com.cango.adpickcar.fc.fcs.FCSMainFragment;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.util.SnackbarUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2018/4/8.
 */

public class FcMainFragment extends BaseFragment implements FcMainContract.View{

    @BindView(R.id.tv_button_home_visits)
    TextView tvHomeVisits;
    @BindView(R.id.tv_button_home)
    TextView tvHome;
    @BindView(R.id.tv_button_mine)
    TextView tvMine;
    @BindView(R.id.layout_fc_main)
    RelativeLayout mMainLayout;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    public FcMainContract.Presenter mPresenter;
    private FcMainActivity mActivity;
    private FragmentTransaction ft;
    private FragmentManager fm;
    private Fragment mFcHomeVisitFragment;
    private Fragment mFcDailyFragment;
    private Fragment mFcMineFragment;

    public static FcMainFragment getInstance(){
        FcMainFragment mFcMainFragment = new FcMainFragment();
        Bundle bundle = new Bundle();
        mFcMainFragment.setArguments(bundle);
        return mFcMainFragment;
    }
    @OnClick({R.id.layout_home_visits, R.id.layout_daily, R.id.layout_mine})
    public void onClick(View view) {
        ft = fm.beginTransaction();
        switch (view.getId()){
            case R.id.layout_home_visits:
                ft.hide(mFcDailyFragment);
                ft.hide(mFcMineFragment);
                ft.show(mFcHomeVisitFragment);

                tvHomeVisits.setTextColor(mActivity.getResources().getColor(R.color.fc_text_main_blue));
                tvHomeVisits.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_main_on),null,null);
                tvHome.setTextColor(mActivity.getResources().getColor(R.color.ad888888));
                tvHome.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_daily_off),null,null);
                tvMine.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_my_off),null,null);
                tvMine.setTextColor(mActivity.getResources().getColor(R.color.ad888888));
                break;
            case R.id.layout_daily:
                ft.hide(mFcHomeVisitFragment);
                ft.hide(mFcMineFragment);
                ft.show(mFcDailyFragment);

                tvHomeVisits.setTextColor(mActivity.getResources().getColor(R.color.ad888888));
                tvHomeVisits.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_main_off),null,null);
                tvHome.setTextColor(mActivity.getResources().getColor(R.color.fc_text_main_blue));
                tvHome.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_daily_on),null,null);
                tvMine.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_my_off),null,null);
                tvMine.setTextColor(mActivity.getResources().getColor(R.color.ad888888));
                break;
            case R.id.layout_mine:
                ft.hide(mFcHomeVisitFragment);
                ft.hide(mFcDailyFragment);
                ft.show(mFcMineFragment);

                tvHomeVisits.setTextColor(mActivity.getResources().getColor(R.color.ad888888));
                tvHomeVisits.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_main_off),null,null);
                tvHome.setTextColor(mActivity.getResources().getColor(R.color.ad888888));
                tvHome.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_daily_off),null,null);
                tvMine.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_my_on),null,null);
                tvMine.setTextColor(mActivity.getResources().getColor(R.color.fc_text_main_blue));
                break;
                default:
                    break;
        }
        ft.commit();
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_fc_new_main;
    }

    @Override
    protected void initView() {
        showLoadView(false);

        //FCS
//        tvHomeVisits.setText(mActivity.getResources().getText(R.string.fcs_manager));
//        mFcHomeVisitFragment = new FCSMainFragment();
//        mFcDailyFragment = EverydayFCSFragment.getInstance();

        //FC
        mFcHomeVisitFragment = new FcHomeVisitFragment();
        mFcDailyFragment = EverydayFragment.getInstance();

        mFcMineFragment = new FcMineFragment();

        ft = fm.beginTransaction();
        ft.add(R.id.fl_container,mFcDailyFragment);
        ft.add(R.id.fl_container,mFcMineFragment);
        ft.add(R.id.fl_container,mFcHomeVisitFragment);
        ft.commit();
    }

    @Override
    protected void initData() {
        mActivity = (FcMainActivity) getActivity();
        fm = getChildFragmentManager();
    }

    @Override
    public void setPresenter(FcMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showLogout(boolean isSuccess, String message) {
        ((FcMineFragment)mFcMineFragment).showLogout();
        if (isSuccess) {
            ADApplication.mSPUtils.clear();
            startActivity(new Intent(mActivity, LoginActivity.class));
            mActivity.finish();
        }
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(mMainLayout, message);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void openOtherUi() {
        SnackbarUtils.showLongDisSnackBar(mMainLayout, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public void getHomeVisitDataSuccess() {
        ((FcHomeVisitFragment)mFcHomeVisitFragment).getHomeVisitDataSuccess();
    }

    @Override
    public void showHomeVisitError() {
        ((FcHomeVisitFragment)mFcHomeVisitFragment).showHomeVisitError();
    }

    @Override
    public void showHomeVisitNoData() {
        ((FcHomeVisitFragment)mFcHomeVisitFragment).showHomeVisitNoData();
    }
}
