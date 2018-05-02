package com.cango.adpickcar.fc.location;

import com.cango.adpickcar.api.GPSService;
import com.cango.adpickcar.model.LocationQuery;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.net.RxSubscriber;
import com.cango.adpickcar.util.CommUtil;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/6/6.
 */

public class LocationPresenter implements LocationContract.Presenter {
    private LocationContract.View mView;
    GPSService mService;
    Subscription subscription;

    public LocationPresenter(LocationContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(GPSService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscription)) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void locationQuery(final boolean showRefreshLoadingUI, String userId, String IMEI, String startTime) {
        subscription = mService.locationQuery(userId, IMEI, startTime)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 需要在主线程执行
                        if (mView.isActive()) {
                            mView.showInfoIndicator(showRefreshLoadingUI);
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<LocationQuery>() {
                    @Override
                    protected void _onNext(LocationQuery o) {
                        if (mView.isActive()) {
                            mView.showInfoIndicator(false);
                            String code = o.getCode();
                            boolean isSuccess = "200".equals(code);
                            if (isSuccess) {
                                mView.showInfoSuccess(isSuccess, o);
                            } else
                                mView.showInfoNoData(o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showInfoIndicator(false);
                            mView.showInfoError();
                        }
                    }
                });
    }

}
