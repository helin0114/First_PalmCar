package com.cango.adpickcar.fc.main.fcsweeklyscheduling;

import com.cango.adpickcar.api.FcTasksService;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.util.CommUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 任务列表
 */

public class FcsWeeklySchedulingPresenter implements FcsWeeklySchedulingContract.Presenter {

    private FcsWeeklySchedulingContract.View mView;
    private FcTasksService mService;
    private Subscription subscriptionToday, subscriptionWeek, subscriptionHis;

    public FcsWeeklySchedulingPresenter(FcsWeeklySchedulingContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(FcTasksService.class);
    }
    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscriptionToday))
            subscriptionToday.unsubscribe();
        if (!CommUtil.checkIsNull(subscriptionWeek))
            subscriptionWeek.unsubscribe();
        if (!CommUtil.checkIsNull(subscriptionHis))
            subscriptionHis.unsubscribe();
    }

    @Override
    public void getVisitTaskListToday(final String UserID) {
//        if (mView.isActive() && !isLoadMore) {
//            mView.showLoadViewNew(true);
//        }
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
//                            mView.showLoadViewNew(false);
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
            mView.showTodayTasksSuccess(data);
//                                } else {
//                                    mView.showNewNoData();
//                                }
//                            } else {
//                                mView.showNewNoData();
//                            }
        }
//                    }
//
//                    @Override
//                    protected void _onError() {
//                        if (mView.isActive()) {
//                            mView.showLoadViewNew(false);
//                            mView.showNewError();
//                        }
//                    }
//                });
    }
}
