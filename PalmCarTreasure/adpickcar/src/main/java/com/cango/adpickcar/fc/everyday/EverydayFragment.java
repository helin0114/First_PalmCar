package com.cango.adpickcar.fc.everyday;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.fc.accountmanager.AccManagerActivity;
import com.cango.adpickcar.fc.download.DownloadActivity;

import butterknife.OnClick;

public class EverydayFragment extends BaseFragment implements EverydayContract.IEverydayView {

    @OnClick({R.id.space,R.id.space_download})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.space:
                toOtherUi(AccManagerActivity.class);
                break;
            case R.id.space_download:
                toOtherUi(DownloadActivity.class);
                break;
        }
    }
    public static EverydayFragment getInstance(){
        return new EverydayFragment();
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_everyday;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(EverydayContract.IEverydayPresenter presenter) {

    }

    @Override
    public void toOtherUi(Class tClass) {
        startActivity(new Intent(getActivity(),tClass));
    }
}
