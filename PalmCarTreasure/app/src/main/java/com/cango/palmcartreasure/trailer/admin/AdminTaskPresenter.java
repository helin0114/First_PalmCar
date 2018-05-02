package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.AdminService;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.TaskAbandonRequest;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/4/28.
 */

public class AdminTaskPresenter implements AdminTasksContract.Presenter {
    private AdminTasksContract.View mAdminView;
    private AdminService mService;
    private Subscription subscription1,subscription2,subscription3,subscription4,subscription5,subscription6;
    //从条件查询过来的
    private Subscription subscription7;

    public AdminTaskPresenter(AdminTasksContract.View adminView) {
        mAdminView = adminView;
        mAdminView.setPresenter(this);
        mService = NetManager.getInstance().create(AdminService.class);
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
        if (!CommUtil.checkIsNull(subscription3))
            subscription3.unsubscribe();
        if (!CommUtil.checkIsNull(subscription4))
            subscription4.unsubscribe();
        if (!CommUtil.checkIsNull(subscription5))
            subscription5.unsubscribe();
        if (!CommUtil.checkIsNull(subscription6))
            subscription6.unsubscribe();
        if (!CommUtil.checkIsNull(subscription7))
            subscription7.unsubscribe();
    }

    @Override
    public void loadAdminTasks(String type, double lat, double lon, boolean showRefreshLoadingUI, int pageCount, int pageSize,
                               String applyId,String mobile,String plateNo, String gpsStatus) {
        if (showRefreshLoadingUI) {
            if (mAdminView.isActive())
                mAdminView.showAdminTasksIndicator(showRefreshLoadingUI);
        } else {

        }
        if (type.equals(AdminTasksFragment.GROUP)) {
            subscription1 = mService.getGroupTaskCount(MtApplication.mSPUtils.getInt(Api.USERID),
                    lat, lon)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<GroupTaskCount>() {
                        @Override
                        protected void _onNext(GroupTaskCount o) {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                int code = o.getCode();
                                if (code == 0) {
                                    if (o.getData() == null) {
                                        mAdminView.showAdminTasksError();
                                    } else {
//                                        mAdminView.showAdminTasks(o.getData().getTaskCountList());
                                        if (CommUtil.checkIsNull(o.getData())) {
                                            mAdminView.showAdminTasksError();
                                        } else {
                                            if (!CommUtil.checkIsNull(o.getData().getTaskCountList()) && o.getData().getTaskCountList().size() > 0)
                                                mAdminView.showAdminTasks(o.getData().getTaskCountList());
                                            else {
                                                mAdminView.showNoAdminTasks();
                                            }
                                        }
                                    }
                                } else
                                    mAdminView.showAdminTasksError();
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                mAdminView.showAdminTasksError();
                            }
                        }
                    });
        } else if (type.equals(AdminTasksFragment.TASK)) {

        } else if (type.equals(AdminTasksFragment.ADMIN_UNABSORBED)) {
            subscription2 = mService.getTaskManageList(MtApplication.mSPUtils.getInt(Api.USERID),
                    lat, lon, pageCount, pageSize,applyId,mobile,plateNo,gpsStatus)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TaskManageList>() {
                        @Override
                        protected void _onNext(TaskManageList o) {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                int code = o.getCode();
                                if (code == 0) {
//                                    mAdminView.showAdminUnabsorbedTasks(o.getData().getTaskList());
                                    if (o.getData() == null) {
                                        mAdminView.showAdminTasksError();
                                    } else {
                                        if (CommUtil.checkIsNull(o.getData())) {
                                            mAdminView.showAdminTasksError();
                                        } else {
                                            if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0)
                                                mAdminView.showAdminUnabsorbedTasks(o.getData().getTaskList());
                                            else {
                                                mAdminView.showNoAdminTasks();
                                            }
                                        }
                                    }
                                } else
                                    mAdminView.showAdminTasksError();
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mAdminView.isActive()) {
                                mAdminView.showAdminTasksIndicator(false);
                                mAdminView.showAdminTasksError();
                            }
                        }
                    });
        }

    }

    @Override
    public void loadGroupTasks(int[] userIds, double lat, double lon, boolean showRefreshLoadingUI, int pageCount, int pageSize) {
        if (showRefreshLoadingUI) {
            if (mAdminView.isActive())
                mAdminView.showAdminTasksIndicator(showRefreshLoadingUI);
        } else {

        }
//        mService.getGroupTaskQuery(MtApplication.mSPUtils.getInt(Api.USERID), userIds, MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LAT),
//                MtApplication.mSPUtils.getFloat(Api.LOGIN_LAST_LON), pageCount, pageSize)
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("groupIDList", userIds);
        objectMap.put("LAT", lat);
        objectMap.put("LON", lon);
        objectMap.put("pageIndex", pageCount);
        objectMap.put("pageSize", pageSize);
        subscription3 = mService.getGroupTaskQuery(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<GroupTaskQuery>() {
                    @Override
                    protected void _onNext(GroupTaskQuery o) {
                        if (mAdminView.isActive()) {
                            mAdminView.showAdminTasksIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    mAdminView.showAdminTasksError();
                                } else {
                                    if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0)
                                        mAdminView.showAdminGroupTasks(o.getData().getTaskList());
                                    else {
                                        mAdminView.showNoAdminTasks();
                                    }
                                }
                            } else
                                mAdminView.showAdminTasksError();
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()) {
                            mAdminView.showAdminTasksIndicator(false);
                            mAdminView.showAdminTasksError();
                        }
                    }
                });
    }

    @Override
    public void loadGroupSearchTasks(int[] groupIds, double lat, double lon, boolean showRefreshLoadingUI,
                                     int pageCount, int pageSize, String applyId, String mobile, String plateNo) {
        if (showRefreshLoadingUI) {
            if (mAdminView.isActive())
                mAdminView.showAdminTasksIndicator(showRefreshLoadingUI);
        } else {

        }
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("groupIDList", groupIds);
        objectMap.put("LAT", lat);
        objectMap.put("LON", lon);
        objectMap.put("pageIndex", pageCount);
        objectMap.put("pageSize", pageSize);
        objectMap.put("applycd",applyId);
        objectMap.put("mobile",mobile);
        objectMap.put("licensePlateNo",plateNo);
        subscription7 = mService.getGroupTaskQuery(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<GroupTaskQuery>() {
                    @Override
                    protected void _onNext(GroupTaskQuery o) {
                        if (mAdminView.isActive()) {
                            mAdminView.showAdminTasksIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    mAdminView.showAdminTasksError();
                                } else {
                                    if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0)
                                        mAdminView.showAdminGroupTasks(o.getData().getTaskList());
                                    else {
                                        mAdminView.showNoAdminTasks();
                                    }
                                }
                            } else
                                mAdminView.showAdminTasksError();
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()) {
                            mAdminView.showAdminTasksIndicator(false);
                            mAdminView.showAdminTasksError();
                        }
                    }
                });
    }

    @Override
    public void groupTaskDraw(final boolean showRefreshLoadingUI, List<GroupTaskQuery.DataBean.TaskListBean> taskListBeanList) {
        if (showRefreshLoadingUI) {
            if (mAdminView.isActive())
                mAdminView.showLoadingView(showRefreshLoadingUI);
        }
//        mService.groupTaskDraw(MtApplication.mSPUtils.getInt(Api.USERID), taskListBeanList)
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("taskList", taskListBeanList);
        subscription4 = mService.groupTaskDraw(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mAdminView.isActive()) {
                            mAdminView.showLoadingView(false);
                            int code = o.getCode();
                            boolean isSuccess = code == 0;
                            mAdminView.showGroupTaskDraw(isSuccess, o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()) {
                            mAdminView.showLoadingView(false);
                            mAdminView.showGroupTaskDraw(false,null);
                        }
                    }
                });
    }

    @Override
    public void giveUpTasks(TaskAbandonRequest[] requests) {
        if (mAdminView.isActive()) {
            mAdminView.showLoadingView(true);
        }
//        mService.TaskAbandon(MtApplication.mSPUtils.getInt(Api.USERID), requests)
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("taskList", requests);
        subscription5 = mService.TaskAbandon(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mAdminView.isActive()) {
                            mAdminView.showLoadingView(false);
                            int code = o.getCode();
                            if (code == 0 || code == -1) {
                                boolean isSuccess = code == 0;
                                mAdminView.showGiveUpTasksAndNotifyUi(isSuccess, o.getMsg());
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()) {
                            mAdminView.showLoadingView(false);
                            mAdminView.showGiveUpTasksAndNotifyUi(false,null);
                        }
                    }
                });
    }

    @Override
    public void openDetailTask(TypeTaskData.DataBean.TaskListBean taskListBean) {
        mAdminView.showAdminTaskDetailUi(taskListBean);
    }

    @Override
    public void taskManagerRead(final int position, int agencyID, int caseID, int applyID, String applyCD,int datasource) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
        objectMap.put("agencyID",agencyID);
        objectMap.put("caseID",caseID);
        objectMap.put("applyID",applyID);
        objectMap.put("applyCD",applyCD);
        objectMap.put("datasource",datasource);
        subscription6 = mService.taskManagerRead(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TaskAbandon>() {
                    @Override
                    protected void _onNext(TaskAbandon o) {
                        if (mAdminView.isActive()) {
                            int code = o.getCode();
                            if (code == 0 || code == -1) {
                                boolean isSuccess = code == 0;
                                mAdminView.updateRead(isSuccess, position);
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mAdminView.isActive()) {
                            mAdminView.updateRead(false, position);
                        }
                    }
                });
    }
}
