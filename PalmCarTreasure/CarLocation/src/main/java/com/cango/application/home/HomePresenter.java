package com.cango.application.home;

import com.cango.application.api.GPSService;
import com.cango.application.model.ImeiQuery;
import com.cango.application.net.NetManager;
import com.cango.application.net.RxSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/6/6.
 */

public class HomePresenter implements HomeContract.Presenter {
    HomeContract.View mView;
    GPSService mService;
    public HomePresenter(HomeContract.View view) {
        mView=view;
        mView.setPresenter(this);
        mService= NetManager.getInstance().create(GPSService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void getImeiQuery(boolean showRefreshLoadingUI,int userId, String IMEI) {
        if (mView.isActive()){
            mView.showInfoIndicator(showRefreshLoadingUI);
        }
        mService.ImeiQuery(userId,IMEI)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<ImeiQuery>() {
                    @Override
                    protected void _onNext(ImeiQuery o) {
                        if (mView.isActive()){
                            mView.showInfoIndicator(false);
                            int code = o.getCode();
                            boolean isSuccess= code == 0;
                            if (isSuccess){
                                mView.showInfoSuccess(isSuccess,o);
                            }else
                                mView.showInfoNoData(o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()){
                            mView.showInfoIndicator(false);
                            mView.showInfoError();
                        }
                    }
                });
    }
}
