package com.cango.adpickcar.fc.everyday;


import android.content.Intent;
import android.view.View;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.fc.accountmanager.AccManagerActivity;
import com.cango.adpickcar.fc.download.DownloadActivity;
import com.cango.adpickcar.fc.main.fcmain.FcHomeVisitFragment;
import com.cango.adpickcar.fc.main.fcsweeklyscheduling.FcsWeeklySchedulingActivity;
import com.cango.adpickcar.fc.main.weeklyscheduling.WeeklySchedulingActivity;

import butterknife.OnClick;

public class EverydayFCSFragment extends BaseFragment implements EverydayFCSContract.IEverydayFCSView {

    @OnClick({R.id.space, R.id.space_follow, R.id.space_week})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.space:
                //true:fcs家访初访管理界面    false:fc周行程管理界面
                toOtherUi(WeeklySchedulingActivity.class, FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_FCORFCS, true);
                break;
            case R.id.space_follow:
                //true:fcs周行程管理界面    false:fcs家访跟进管理界面
                toOtherUi(FcsWeeklySchedulingActivity.class, FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_ISWEEKLY, false);
                break;
            case R.id.space_week:
                //true:fcs周行程管理界面    false:fcs家访跟进管理界面
                toOtherUi(FcsWeeklySchedulingActivity.class, FcHomeVisitFragment.TYPE_ADMINI_HOMEVISIT_ISWEEKLY, true);
                break;
        }
    }
    public static EverydayFCSFragment getInstance() {
        return new EverydayFCSFragment();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_everyday_fcs;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(EverydayFCSContract.IEverydayFCSPresenter presenter) {

    }

    @Override
    public void toOtherUi(Class tClass, String key, boolean value) {
        Intent intent = new Intent(getActivity(), tClass);
        intent.putExtra(key,value);
        startActivity(intent);
    }
}
