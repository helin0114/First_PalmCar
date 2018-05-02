package com.cango.application.trajectory;

import com.cango.application.api.GPSService;
import com.cango.application.model.CarTrackQuery;
import com.cango.application.net.NetManager;
import com.cango.application.net.RxSubscriber;

import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/6/8.
 */

public class TrajectoryPresenter implements TrajectoryContract.Presenter {
    private TrajectoryContract.View mView;
    private GPSService mService;
    public TrajectoryPresenter(TrajectoryContract.View view) {
        mView=view;
        mView.setPresenter(this);
        mService= NetManager.getInstance().create(GPSService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void carTrackQuery(boolean showRefreshLoadingUI, int userId, String IMEI, String startTime, String endTime, int dependMinute) {
        if (mView.isActive()){
            mView.showInfoIndicator(showRefreshLoadingUI);
        }
        mService.carTrackQuery(userId,IMEI,startTime,endTime,dependMinute)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<CarTrackQuery>() {
                    @Override
                    protected void _onNext(CarTrackQuery o) {
                        if (mView.isActive()){
                            mView.showInfoIndicator(false);
                            int code = o.getCode();
                            boolean isSuccess=code==0;
                            if (isSuccess&&o.getData()!=null&&o.getData().getTrackList()!=null&&o.getData().getTrackList().size()>0){
                                mView.showInfoSuccess(isSuccess,o.getData());
                            }else {
                                mView.showInfoDataError();
                            }
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
