package com.cango.adpickcar.fc.main.fcsearch;

import com.cango.adpickcar.api.FcMainService;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.util.CommUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by dell on 2018/4/24.
 */

public class FcSearchPresenter implements FcSearchContract.Presenter{
    private FcSearchContract.View mView;
    private FcMainService mService;
    private Subscription subscription;

    public FcSearchPresenter(FcSearchContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(FcMainService.class);
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
    public void getSearchResultTaskList(String UserID, String PageIndex, String PageSize) {
        if (mView.isActive()) {
            mView.showLoadView(true);
        }
//        subscriptionToday = mService.getServerTime()
//                .flatMap(new Func1<ServerTime, Observable<FcVisitTaskList>>() {
//                    @Override
//                    public Observable<FcVisitTaskList> call(ServerTime serverTime) {
//                        boolean isSuccess = serverTime.getCode().equals("200");
//                        if (isSuccess) {
//                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
//                        }
//                        return mService.getVisitTaskListToday(UserID);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxSubscriber<FcVisitTaskList>() {
//                    @Override
//                    protected void _onNext(FcVisitTaskList o) {
        if (mView.isActive()) {
                            mView.showLoadView(false);
//                            boolean isSuccess = o.getCode().equals("200");
//                            if (CommUtil.handingCodeLogin(o.getCode())) {
//                                mView.openOtherUi();
//                                return;
//                            }
//                            if (isSuccess) {
//                                if (o.getData() != null) {
            List<FcVisitTaskList.TaskListBean> data = new ArrayList<>();
            data.add(new FcVisitTaskList.TaskListBean());
            data.add(new FcVisitTaskList.TaskListBean());
            data.add(new FcVisitTaskList.TaskListBean());
            data.add(new FcVisitTaskList.TaskListBean());
            data.add(new FcVisitTaskList.TaskListBean());
            data.add(new FcVisitTaskList.TaskListBean());
            data.add(new FcVisitTaskList.TaskListBean());
            data.add(new FcVisitTaskList.TaskListBean());
            data.add(new FcVisitTaskList.TaskListBean());
            mView.showTasksSuccess(data);
//                                } else {
//                                    mView.showTodayNoData();
//                                }
//                            } else {
//                                mView.showTodayNoData();
//                            }
        }
//                    }
//
//                    @Override
//                    protected void _onError() {
//                        if (mView.isActive()) {
//                            mView.showLoadViewToday(false);
//                            mView.showTodayError();
//                        }
//                    }
//                });
    }
}