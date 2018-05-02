package com.cango.adpickcar.fc.trajectory;


import com.cango.adpickcar.api.GPSService;
import com.cango.adpickcar.model.CarTrackQuery;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.net.RxSubscriber;
import com.cango.adpickcar.util.CommUtil;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/6/8.
 */

public class TrajectoryPresenter implements TrajectoryContract.Presenter {
    private TrajectoryContract.View mView;
    private GPSService mService;
    private Subscription subscription;
    public TrajectoryPresenter(TrajectoryContract.View view) {
        mView=view;
        mView.setPresenter(this);
        mService= NetManager.getInstance().create(GPSService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscription)){
            subscription.unsubscribe();
        }
    }

    @Override
    public void carTrackQuery(boolean showRefreshLoadingUI, int userId, String IMEI, String startTime, String endTime, int dependMinute) {
        if (mView.isActive()){
            mView.showInfoIndicator(showRefreshLoadingUI);
        }
        subscription = mService.carTrackQuery(userId,IMEI,startTime,endTime,dependMinute)
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
