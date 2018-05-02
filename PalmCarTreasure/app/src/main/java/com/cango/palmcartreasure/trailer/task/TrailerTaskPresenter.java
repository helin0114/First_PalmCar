package com.cango.palmcartreasure.trailer.task;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/4/7.
 */

public class TrailerTaskPresenter implements TaskContract.Presenter {
    private TaskContract.View mTaskView;
    private TrailerTaskService mTaskService;
    private Subscription subscription1,subscription2;
    public TrailerTaskPresenter(TaskContract.View taskView) {
        mTaskView = taskView;
        mTaskView.setPresenter(this);
        mTaskService = NetManager.getInstance().create(TrailerTaskService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscription1))
            subscription1.unsubscribe();
        if (!CommUtil.checkIsNull(subscription2))
            subscription2.unsubscribe();
    }

    @Override
    public void openDetailTask(TypeTaskData.DataBean.TaskListBean taskListBean) {
        mTaskView.showTaskDetailUi(taskListBean);
    }

    @Override
    public void loadTasks(String type, double lat, double lon, boolean showRefreshLoadingUI, int pageCount, int pageSize, String gpsStatus) {
        if (mTaskView.isActive()){
            mTaskView.showTasksIndicator(showRefreshLoadingUI);
        }
        Observable<TypeTaskData> taskDataObservable = null;
        int userId = MtApplication.mSPUtils.getInt(Api.USERID);
        if (type.equals(TrailerTasksFragment.NEW_TASK)) {
            taskDataObservable = mTaskService.getNewTaskList(userId, lat, lon,pageCount,pageSize);
        } else if (type.equals(TrailerTasksFragment.INPROGRESS_TASK)) {
            taskDataObservable = mTaskService.getTaskInprogressList(userId, lat, lon,pageCount,pageSize);
        } else if (type.equals(TrailerTasksFragment.DONE_TASK)) {
            taskDataObservable = mTaskService.getTaskDoneList(userId, lat, lon,pageCount,pageSize);
        } else {
            //search
            taskDataObservable = mTaskService.taskQuery(userId, "", "",0,pageCount,pageSize, gpsStatus);
        }
        subscription1 = taskDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TypeTaskData>() {
                    @Override
                    protected void _onNext(TypeTaskData o) {
                        if (mTaskView.isActive()){
                            mTaskView.showTasksIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    mTaskView.showTasksError();
                                } else {
                                    if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0)
                                        mTaskView.showTasks(o.getData().getTaskList());
                                    else {
                                        mTaskView.showNoTasks();
                                    }
                                }
                            } else
                                mTaskView.showTasksError();
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mTaskView.isActive()) {
                            mTaskView.showTasksIndicator(false);
                            mTaskView.showTasksError();
                        }
                    }
                });
    }

    @Override
    public void taskQuery(boolean showRefreshLoadingUI,String type, String applyCD, String customerName, int timeFlag, int pageIndex, int pageSize, String gpsStatus) {
        if (mTaskView.isActive()){
            mTaskView.showTasksIndicator(showRefreshLoadingUI);
        }
        subscription2 = mTaskService.taskQuery(MtApplication.mSPUtils.getInt(Api.USERID),applyCD,customerName,timeFlag,pageIndex,pageSize, gpsStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TypeTaskData>() {
                    @Override
                    protected void _onNext(TypeTaskData o) {
                        if (mTaskView.isActive()){
                            mTaskView.showTasksIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    mTaskView.showTasksError();
                                } else {
                                    if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0)
                                        mTaskView.showTasks(o.getData().getTaskList());
                                    else {
                                        mTaskView.showNoTasks();
                                    }
                                }
                            } else
                                mTaskView.showTasksError();
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mTaskView.isActive()) {
                            mTaskView.showTasksIndicator(false);
                            mTaskView.showTasksError();
                        }
                    }
                });
    }
}
