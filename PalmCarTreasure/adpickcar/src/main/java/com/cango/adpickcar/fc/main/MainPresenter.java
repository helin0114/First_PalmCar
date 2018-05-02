package com.cango.adpickcar.fc.main;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.api.FcMainService;
import com.cango.adpickcar.model.BaseData;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.model.ServerTime;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.net.RxSubscriber;
import com.cango.adpickcar.util.CommUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dell on 2017/12/11.
 */

public class MainPresenter implements MainContract.Presenter{
    private MainContract.View mView;
    private FcMainService mService;
    private Subscription subscriptionLogout, subscriptionMainTasks;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(FcMainService.class);
    }
    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscriptionLogout))
            subscriptionLogout.unsubscribe();
        if (!CommUtil.checkIsNull(subscriptionMainTasks))
            subscriptionMainTasks.unsubscribe();
    }

    @Override
    public void logout(boolean showRefreshLoadingUI, final String UserID) {
        if (mView.isActive())
            mView.showLoadView(showRefreshLoadingUI);
            subscriptionLogout = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<BaseData>>() {
                    @Override
                    public Observable<BaseData> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("UserID", UserID);
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        paramsMap = new HashMap<>();
                        paramsMap.put("RequestContent", encrypt);
                        return mService.logout(paramsMap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseData>() {
                    @Override
                    protected void _onNext(BaseData o) {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            mView.showLogout(isSuccess, o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            mView.showLogout(false, null);
                        }
                    }
                });
    }

    @Override
    public void getVisitTaskList(final String UserID , final String PageIndex, final String PageSize, final String LAT, final String LON) {
        subscriptionMainTasks = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<FcVisitTaskList>>() {
                    @Override
                    public Observable<FcVisitTaskList> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        return mService.getVisitTaskList(UserID, PageIndex, PageSize, LAT, LON);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<FcVisitTaskList>() {
                    @Override
                    protected void _onNext(FcVisitTaskList o) {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            mView.showMainIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            if ("211".equals(o.getCode())) {
                                mView.updateApk();
                                return;
                            }
                            if (isSuccess) {
                                if (o.getData() != null) {
                                    mView.showTasksSuccess(o.getData());
                                } else {
                                    mView.showNoData();
                                }
                            } else {
                                mView.showNoData();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            mView.showMainIndicator(false);
                            mView.showError();
                        }
                    }
                });
    }
}
