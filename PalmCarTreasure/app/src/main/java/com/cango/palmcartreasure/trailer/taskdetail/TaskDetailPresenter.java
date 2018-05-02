package com.cango.palmcartreasure.trailer.taskdetail;

import android.net.Uri;
import android.text.TextUtils;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.model.CallRecord;
import com.cango.palmcartreasure.model.CaseInfo;
import com.cango.palmcartreasure.model.CustomerInfo;
import com.cango.palmcartreasure.model.HomeVisitRecord;
import com.cango.palmcartreasure.model.TaskDetailData;
import com.cango.palmcartreasure.model.TrailerInfo;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/4/17.
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {
    private TaskDetailContract.View mView;
    TrailerTaskService mService;
    private Subscription subscription1,subscription2,subscription3,subscription4,subscription5;

    public TaskDetailPresenter(TaskDetailContract.View detailView) {
        mView = detailView;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(TrailerTaskService.class);
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
    }

    @Override
    public void loadTaskDetail(int type, boolean showLoadingUI, int agencyID, int caseID,int datasource) {
        if (mView.isActive()) {
            mView.showTaskDetailIndicator(showLoadingUI);
        }
        switch (type) {
            case 0:
                subscription1 = mService.callRecord(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID,datasource)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CallRecord>() {
                            @Override
                            protected void _onNext(CallRecord o) {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (o.getData() == null) {
                                            mView.showTasksDetailError();
                                        } else {
                                            if (CommUtil.checkIsNull(o.getData().getCallRecordData()) && CommUtil.checkIsNull(o.getData().getOverdueData())) {
                                                mView.showNoTaskDetail();
                                            } else {
                                                TaskDetailData data = setCallInfo(o);
                                                mView.showTaskDetail(data);
                                            }
                                        }
                                    } else {
                                        mView.showTasksDetailError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    mView.showTasksDetailError();
                                }
                            }
                        });
                break;
            case 1:
                subscription2 = mService.homeVisitRecord(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID,datasource)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<HomeVisitRecord>() {
                            @Override
                            protected void _onNext(HomeVisitRecord o) {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (o.getData() == null) {
                                            mView.showTasksDetailError();
                                        } else {
                                            if (o.getData().size() > 0) {
                                                TaskDetailData data = setHomeInfo(o);
                                                mView.showTaskDetail(data);
                                            } else {
                                                mView.showNoTaskDetail();
                                            }
                                        }
                                    } else {
                                        mView.showTasksDetailError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    mView.showTasksDetailError();
                                }
                            }
                        });
                break;
            case 2:
                subscription3 = mService.caseInfo(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID,datasource)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CaseInfo>() {
                            @Override
                            protected void _onNext(CaseInfo o) {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (o.getData() == null) {
                                            mView.showTasksDetailError();
                                        } else {
                                            TaskDetailData data = setCaseInfo(o);
                                            mView.showTaskDetail(data);
                                        }
                                    } else {
                                        mView.showTasksDetailError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    mView.showTasksDetailError();
                                }
                            }
                        });
                break;
            case 3:
                subscription4 = mService.customerinfo(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID,datasource)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CustomerInfo>() {
                            @Override
                            protected void _onNext(CustomerInfo o) {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (CommUtil.checkIsNull(o.getData())) {
                                            mView.showTasksDetailError();
                                        } else {
                                            if (CommUtil.checkIsNull(o.getData().getCustInfo()) && CommUtil.checkIsNull(o.getData().getMateInfo())) {
                                                mView.showTasksDetailError();
                                            } else {
                                                TaskDetailData data = setCustomInfo(o);
                                                mView.showTaskDetail(data);
                                            }
                                        }
                                    } else {
                                        mView.showTasksDetailError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                    mView.showTasksDetailError();
                                }
                            }
                        });
                break;
        }

    }

    @Override
    public void loadTrailerInfo(boolean showLoadingUI, int applyID, String applyCD, int caseID,int datasource) {
        subscription5 = mService.trailerInfo(MtApplication.mSPUtils.getInt(Api.USERID), applyID, applyCD, caseID,datasource)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TrailerInfo>() {
                    @Override
                    protected void _onNext(TrailerInfo o) {
                        if (mView.isActive()) {
                            mView.showTaskDetailIndicator(false);
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    mView.showTasksDetailError();
                                } else {
                                    if (CommUtil.checkIsNull(o.getData().getTrailerInfo()) && CommUtil.checkIsNull(o.getData().getTrailerRemarks())) {
                                        mView.showTasksDetailError();
                                    } else {
                                        TaskDetailData data = setTrailerInfo(o);
                                        mView.showTaskDetail(data);
                                    }
                                }
                            } else {
                                mView.showTasksDetailError();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showTaskDetailIndicator(false);
                            mView.showTasksDetailError();
                        }
                    }
                });
    }

    private TaskDetailData setCallInfo(CallRecord o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;
        if (o.getData().getCallRecordData() != null) {
            section = new TaskDetailData.TaskSection();
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<CallRecord.DataBean.CallRecordDataBean> callRecordDataBeanList = o.getData().getCallRecordData();

            //手动增加催记员 发生时间 备注
            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("催记员");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setCenter("发生日期");
            taskInfo.setCenterColor(R.color.mt9c9c9c);
            taskInfo.setRight("备注");
            taskInfo.setRightColor(R.color.mt9c9c9c);
            taskInfoList.add(taskInfo);

            for (CallRecord.DataBean.CallRecordDataBean bean : callRecordDataBeanList) {
                taskInfo = new TaskDetailData.TaskSection.TaskInfo();
//                taskInfo.setLeft(bean.getCallUser());
                //Andrioid电催信息、家访信息、库管员信息中我司人员名字隐去，只留姓+XX
                taskInfo.setLeft(getFirstNameXX(bean.getCallUser()));
                taskInfo.setLeftColor(R.color.colorPrimary);
                taskInfo.setCenter(bean.getCallTime());
                taskInfo.setCenterColor(R.color.colorPrimary);
                taskInfo.setRight(bean.getComment());
                taskInfo.setRightColor(R.color.colorPrimary);
                taskInfoList.add(taskInfo);
            }
            section.setIvId(R.drawable.cuijixinxi);
            section.setTitle("催记信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        if (o.getData().getOverdueData() != null) {
            section = new TaskDetailData.TaskSection();
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            CallRecord.DataBean.OverdueDataBean overdueDataBean = o.getData().getOverdueData();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("逾期原因");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(overdueDataBean.getOverdueReason());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("逾期原因备注");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(overdueDataBean.getOverdueComment());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.overdue);
            section.setTitle("逾期原因");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }


        data.setTaskSectionList(taskSectionList);
        return data;
    }

    private TaskDetailData setHomeInfo(HomeVisitRecord o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;

        List<List<HomeVisitRecord.DataBean>> dataBeanList = o.getData();
//        for (HomeVisitRecord.DataBean bean : dataBeanList) {
        for (int i = 0; i < dataBeanList.size(); i++) {
            List<HomeVisitRecord.DataBean> dataBean = dataBeanList.get(i);

            section = new TaskDetailData.TaskSection();
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();

            //手动
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("家访员");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setCenter("家访日期");
            taskInfo.setCenterColor(R.color.mt9c9c9c);
            taskInfo.setRight("家访备注");
            taskInfo.setRightColor(R.color.mt9c9c9c);
            taskInfoList.add(taskInfo);

            for (int j = 0; j < dataBean.size(); j++) {
                HomeVisitRecord.DataBean bean = dataBean.get(j);
                taskInfo = new TaskDetailData.TaskSection.TaskInfo();
//                taskInfo.setLeft(bean.getVisitUser());
//                Andrioid电催信息、家访信息、库管员信息中我司人员名字隐去，只留姓+XX
                taskInfo.setLeft(getFirstNameXX(bean.getVisitUser()));
                taskInfo.setLeftColor(R.color.colorPrimary);
                taskInfo.setCenter(bean.getVisitTime());
                taskInfo.setCenterColor(R.color.colorPrimary);
                taskInfo.setRight(bean.getRedueTrueReason());
                taskInfo.setRightColor(R.color.colorPrimary);
                taskInfoList.add(taskInfo);
            }

            section.setIvId(R.drawable.home_record);
            section.setTitle("家访记录" + i + 1);
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        data.setTaskSectionList(taskSectionList);
        return data;
    }

    private TaskDetailData setCaseInfo(CaseInfo o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;
        if (o.getData() != null) {
            section = new TaskDetailData.TaskSection();
            CaseInfo.DataBean dataBean = o.getData();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("申请编号");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getApplycd());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("创建日期");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getApplytime());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("品牌");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getCarbrandcg());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("车型");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getCarmodelidcg());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("抵押信息");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getHasCarletterPass());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("GPS安装情况");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getHasgpsflg());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("GPS是否在线");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(dataBean.getIsgpsonline());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

//            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
//            taskInfo.setLeft("IMEI1");
//            taskInfo.setRight(dataBean.getIMEI1());
//            taskInfoList.add(taskInfo);
//
//            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
//            taskInfo.setLeft("IMEI2");
//            taskInfo.setRight(dataBean.getIMEI2());
//            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.case_information);
            section.setTitle("案件信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        data.setTaskSectionList(taskSectionList);
        return data;
    }

    private TaskDetailData setCustomInfo(CustomerInfo o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;
        if (o.getData().getCustInfo() != null) {
            section = new TaskDetailData.TaskSection();
            CustomerInfo.DataBean.CustInfoBean custInfo = o.getData().getCustInfo();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("姓名");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getCustomername());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("性别");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getSex());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("证件号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getPaperno());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("手机");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getMobile());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("婚姻状况");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getMaritalstatus());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住省");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getProvince());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住市");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getCity());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住地地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getAddress());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住地电话区号");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getTelarea());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现居住地电话号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getTel() + "");
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址省");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidenceprovince());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址市");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidencecity());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidenceaddress());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址电话区号");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidencetelarea());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("户籍地址电话号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidencetel());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位省");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorkprovince());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位市");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorkcity());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorkaddress());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位名称");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorkunitname());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位电话区号");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getResidencetelarea());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位电话号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorktel());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("现工作单位电话分机");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getWorktelext());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section =new TaskDetailData.TaskSection();
            section.setIvId(R.drawable.customer_information_small);
            section.setTitle("客户信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);

            //紧急联系人信息
            taskInfoList = new ArrayList<>();
            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人1姓名");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgencyName());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人1地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgencyAddress());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人1手机");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgencyTel());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人1关系");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgencyRelation());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人2姓名");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgency2Name());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人2地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgency2Address());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人2手机");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgency2Tel());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人2关系");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgency2Relation());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人3姓名");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgency3Name());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人3地址");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgency3Address());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人3手机");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgency3Tel());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("紧急联系人3关系");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(custInfo.getUrgency3Relation());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section = new TaskDetailData.TaskSection();
            section.setIvId(R.drawable.customer_information_small);
            section.setTitle("紧急联系人信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        if (o.getData().getMateInfo() != null) {
            section = new TaskDetailData.TaskSection();
            CustomerInfo.DataBean.MateInfoBean mateInfo = o.getData().getMateInfo();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("配偶姓名");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(mateInfo.getPartnername());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("配偶证件号码");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(mateInfo.getPartnerpaperno());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.spouse_information);
            section.setTitle("配偶信息");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }
        data.setTaskSectionList(taskSectionList);
        return data;
    }

    private TaskDetailData setTrailerInfo(TrailerInfo o) {
        TaskDetailData data = new TaskDetailData();
        List<TaskDetailData.TaskSection> taskSectionList = new ArrayList<>();
        TaskDetailData.TaskSection section;
        if (o.getData().getTrailerRemarks() != null) {
            section = new TaskDetailData.TaskSection();
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            List<TrailerInfo.DataBean.TrailerRemarksBean> callRecordDataBeanList = o.getData().getTrailerRemarks();

            //手动
            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("注记员");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setCenter("注记信息");
            taskInfo.setCenterColor(R.color.mt9c9c9c);
            taskInfo.setRight("注记内容");
            taskInfo.setRightColor(R.color.mt9c9c9c);
            taskInfoList.add(taskInfo);

            for (TrailerInfo.DataBean.TrailerRemarksBean bean : callRecordDataBeanList) {
                taskInfo = new TaskDetailData.TaskSection.TaskInfo();
                taskInfo.setLeft(bean.getRemarkUser());
                taskInfo.setLeftColor(R.color.colorPrimary);
                taskInfo.setCenter(bean.getRemarkTime());
                taskInfo.setCenterColor(R.color.colorPrimary);
                taskInfo.setRight(bean.getRemarkContent());
                taskInfo.setRightColor(R.color.colorPrimary);
                taskInfoList.add(taskInfo);
            }
            section.setIvId(R.drawable.cuijixinxi);
            section.setTitle("沟通历史");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        if (o.getData().getTrailerInfo() != null) {
            section = new TaskDetailData.TaskSection();
            List<TaskDetailData.TaskSection.TaskInfo> taskInfoList = new ArrayList<>();
            TaskDetailData.TaskSection.TaskInfo taskInfo;
            TrailerInfo.DataBean.TrailerInfoBean trailerInfoBean = o.getData().getTrailerInfo();

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("是否首次派发");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(trailerInfoBean.getIsFirstAssign());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("拖车派发日期");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight(trailerInfoBean.getEntrustDate());
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

//            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
//            taskInfo.setLeft("供应商反馈标示");
//            taskInfo.setLeftColor(R.color.mt9c9c9c);
//            taskInfo.setRight("");
//            taskInfo.setRightColor(R.color.colorPrimary);
//            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("反馈收回时间");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight("");
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

//            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
//            taskInfo.setLeft("反馈库点城市");
//            taskInfo.setLeftColor(R.color.mt9c9c9c);
//            taskInfo.setRight("");
//            taskInfo.setRightColor(R.color.colorPrimary);
//            taskInfoList.add(taskInfo);
//
//            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
//            taskInfo.setLeft("拖回城市");
//            taskInfo.setLeftColor(R.color.mt9c9c9c);
//            taskInfo.setRight("");
//            taskInfo.setRightColor(R.color.colorPrimary);
//            taskInfoList.add(taskInfo);

//            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
//            taskInfo.setLeft("供应商反馈备注");
//            taskInfo.setLeftColor(R.color.mt9c9c9c);
//            taskInfo.setRight("");
//            taskInfo.setRightColor(R.color.colorPrimary);
//            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("外包拖车审批备注");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight("");
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("收回理由");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight("");
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            taskInfo = new TaskDetailData.TaskSection.TaskInfo();
            taskInfo.setLeft("派发备注");
            taskInfo.setLeftColor(R.color.mt9c9c9c);
            taskInfo.setRight("");
            taskInfo.setRightColor(R.color.colorPrimary);
            taskInfoList.add(taskInfo);

            section.setIvId(R.drawable.overdue);
            section.setTitle("拖车记录");
            section.setTaskInfoList(taskInfoList);
            taskSectionList.add(section);
        }

        data.setTaskSectionList(taskSectionList);
        return data;
    }

    @Override
    public void downLoadFile(int type, boolean showLoadingUI, int userId, int agencyID, int caseID,
                             String docType, final String parentDir, final TaskDetailFragment.OnDownloadListener listener,int datasource) {
        if (mView.isActive()) {
            mView.showTaskDetailIndicator(showLoadingUI);
        }
        mService.docDownLoad(userId, agencyID, caseID, docType,datasource)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len;
                        FileOutputStream fos = null;
//                        // 储存下载文件的目录
//                        String savePath = isExistDir(saveDir);
                        try {
                            if (response.isSuccessful()) {
                                Headers headers = response.headers();
                                String[] strings = headers.get("Content-Disposition").split(";");
                                String fileName = strings[1].replaceAll("filename=", "");
                                String decode = Uri.decode(fileName);
                                File file = new File(parentDir, decode.trim());
                                is = response.body().byteStream();
                                long total = response.body().contentLength();
                                fos = new FileOutputStream(file);
                                long sum = 0;
                                while ((len = is.read(buf)) != -1) {
                                    fos.write(buf, 0, len);
                                    sum += len;
                                    int progress = (int) (sum * 1.0f / total * 100);
                                    // 下载中
                                    listener.onDownloading(progress);
                                }
                                fos.flush();
                                // 下载完成
                                listener.onDownloadSuccess(file);
                                if (mView.isActive()) {
                                    mView.showTaskDetailIndicator(false);
                                }
                            } else {
                                listener.onDownloadFailed("下载失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onDownloadFailed(e.getMessage());
                            if (mView.isActive()) {
                                mView.showTaskDetailIndicator(false);
                            }
                        } finally {
                            if (mView.isActive()) {
                                mView.showTaskDetailIndicator(false);
                            }
                            try {
                                if (is != null)
                                    is.close();
                            } catch (IOException e) {
                            }
                            try {
                                if (fos != null)
                                    fos.close();
                            } catch (IOException e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Logger.d(t.getMessage());
                        if (mView.isActive()) {
                            mView.showTaskDetailIndicator(false);
                        }
                    }
                });
    }

    public String getFirstNameXX(String name){
        String xx="";
        if (TextUtils.isEmpty(name)){
        }else {
            if (name.length()>0){
                xx=name.substring(0,1)+"XX";
            }else {
            }
        }
        return xx;
    }
}
