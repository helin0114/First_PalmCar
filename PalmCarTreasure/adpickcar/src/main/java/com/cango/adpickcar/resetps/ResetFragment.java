package com.cango.adpickcar.resetps;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.util.BarUtil;
import com.cango.adpickcar.util.SnackbarUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetFragment extends BaseFragment implements ResetPSContract.View {

    public static ResetFragment getInstance() {
        ResetFragment resetFragment = new ResetFragment();
        Bundle bundle = new Bundle();
        resetFragment.setArguments(bundle);
        return resetFragment;
    }

    @BindView(R.id.toolbar_reset)
    Toolbar mToolbar;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.layout_reset)
    View layoutReset;

    @OnClick({R.id.btn_login_signin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_signin:
                if (isDoChangePassword) {
                    isDoChangePassword = false;
                    mPresenter.resetPS(true, ADApplication.mSPUtils.getString(Api.USERID), etPassword.getText().toString().trim(),
                            etNewPassword.getText().toString().trim(), etConfirmPassword.getText().toString().trim());
                }
                break;
        }
    }

    ResetPSActivity mActivity;
    private ResetPSContract.Presenter mPresenter;
    private boolean isDoChangePassword = true;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_reset;
    }

    @Override
    protected void initView() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        showResetIndicator(false);
    }

    @Override
    protected void initData() {
        mActivity = (ResetPSActivity) getActivity();
    }

    @Override
    public void setPresenter(ResetPSContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showResetIndicator(boolean active) {
        if (active)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showResetError() {
        isDoChangePassword = true;
    }

    @Override
    public void showResetSuccess(boolean isSuccess, String message) {
        isDoChangePassword = true;
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(layoutReset, message);
        if (isSuccess) {
            startActivity(new Intent(mActivity, LoginActivity.class));
//            mActivity.finish();
        }
    }

    @Override
    public void openOtherUi() {
//        ToastUtils.showShort("认证失败，请重新登录");
        SnackbarUtils.showLongDisSnackBar(layoutReset, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
