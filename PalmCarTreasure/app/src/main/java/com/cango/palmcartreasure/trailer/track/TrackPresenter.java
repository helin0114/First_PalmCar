package com.cango.palmcartreasure.trailer.track;

import com.cango.palmcartreasure.api.GPSService;
import com.cango.palmcartreasure.model.CarTrackQuery;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/6/8.
 */

public class TrackPresenter implements TrackContract.Presenter {
    private TrackContract.View mView;
    private GPSService mService;

    public TrackPresenter(TrackContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(GPSService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void carTrackQuery(boolean showRefreshLoadingUI, int userId, String IMEI, String startTime, String endTime, int dependMinute) {
        if (mView.isActive()) {
            mView.showInfoIndicator(showRefreshLoadingUI);
        }
        mService.carTrackQuery(userId, IMEI, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<CarTrackQuery>() {
                    @Override
                    protected void _onNext(CarTrackQuery o) {
                        if (mView.isActive()) {
                            mView.showInfoIndicator(false);
                            int code = o.getCode();
                            boolean isSuccess = code == 0;
                            if (isSuccess && o.getData() != null && o.getData().getTrackList() != null && o.getData().getTrackList().size() > 0) {
                                mView.showInfoSuccess(isSuccess, o.getData());
                            } else {
                                mView.showInfoDataError();
                            }
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
