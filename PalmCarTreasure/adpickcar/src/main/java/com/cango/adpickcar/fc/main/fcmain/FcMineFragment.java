package com.cango.adpickcar.fc.main.fcmain;

import android.content.Intent;
import android.view.View;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.fc.download.DownloadActivity;
import com.cango.adpickcar.fc.message.MessageActivity;

import butterknife.OnClick;

/**
 * Created by dell on 2018/4/8.
 */

public class FcMineFragment extends BaseFragment {

    private FcMainActivity mActivity;
    private boolean isDoLogout = true;
    private FcMainPresenter mPresenter;

    @OnClick({R.id.layout_about, R.id.layout_password, R.id.layout_download, R.id.layout_signout, R.id.layout_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_about:
                startActivity(new Intent(mActivity, com.cango.adpickcar.fc.about.DocActivity.class));
                break;
            case R.id.layout_password:
                startActivity(new Intent(mActivity, com.cango.adpickcar.fc.resetps.ResetPSActivity.class));
                break;
            case R.id.layout_download:
                startActivity(new Intent(mActivity, DownloadActivity.class));
                break;
            case R.id.layout_signout:
                if (isDoLogout) {
                    isDoLogout = false;
                    mPresenter.logout(true, ADApplication.mSPUtils.getString(Api.USERID));
                }
                break;
            case R.id.layout_message:
                startActivity(new Intent(mActivity, MessageActivity.class));
                break;
            default:
                break;
        }
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_main_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mActivity = (FcMainActivity)getActivity();
        mPresenter = (FcMainPresenter) ((FcMainFragment)getParentFragment()).mPresenter;
    }
    public void showLogout(){
        isDoLogout = true;
    }
}
