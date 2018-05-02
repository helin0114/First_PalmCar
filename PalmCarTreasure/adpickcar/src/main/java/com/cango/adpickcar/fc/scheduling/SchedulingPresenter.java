package com.cango.adpickcar.fc.scheduling;

import com.cango.adpickcar.api.FcSchedulingService;
import com.cango.adpickcar.model.FcSchedulingInfo;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.util.CommUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by dell on 2017/12/13.
 */

public class SchedulingPresenter implements SchedulingContract.Presenter {
    private SchedulingContract.View mView;
    private FcSchedulingService mService;
    private Subscription subscription;

    public SchedulingPresenter(SchedulingContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(FcSchedulingService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscription))
            subscription.unsubscribe();
    }

    @Override
    public void GetSchedulingData() {
        List<FcSchedulingInfo> mDatas = new ArrayList<>();
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mDatas.add(new FcSchedulingInfo());
        mView.showSchedulingSuccess(mDatas);

    }
}
