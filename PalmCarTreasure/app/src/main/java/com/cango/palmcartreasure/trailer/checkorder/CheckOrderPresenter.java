package com.cango.palmcartreasure.trailer.checkorder;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.model.CheckOrderData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/9/6.
 */

public class CheckOrderPresenter implements CheckOrderContract.Presenter {
    CheckOrderContract.View mView;
    private TrailerTaskService mService;
    private Subscription mSubscription;

    public CheckOrderPresenter(CheckOrderContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(TrailerTaskService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(mSubscription)) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void getCheckOrderData(boolean showIndicatorUI, int userId, int agencyID, String applyCD, int caseID,int datasource) {
        if (mView.isActive()) {
            mView.showIndicator(showIndicatorUI);
        }
        mSubscription = mService.getCheckOrderData(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, applyCD, caseID,datasource)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<CheckOrderData>() {
                    @Override
                    protected void _onNext(CheckOrderData o) {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    mView.showNoData();
                                } else {
                                    if (o.getData().getQuestionList().size()>1){
                                        mView.showDataSuccess(o);
                                    }else {
                                        mView.showNoData();
                                    }
                                }
                            } else {
                                mView.showNoData();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            mView.showDataError(null);
                        }
                    }
                });
    }
}
