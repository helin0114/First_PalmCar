package com.cango.adpickcar.fc.main.weeklyscheduling;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.FcVisitTaskList;

import java.util.List;

/**
 * Created by dell on 2017/12/11.
 */

public interface WeeklySchedulingContract {

    interface View extends BaseView<WeeklySchedulingContract.Presenter> {
        boolean isActive();

//        /**
//         * 是否显示新任务的loadview
//         * @param isShow
//         */
//        void showLoadViewNew(boolean isShow);
//
//        /**
//         * 是否显示待审批的loadview
//         * @param isShow
//         */
//        void showLoadViewApproval(boolean isShow);
//
//        /**
//         * 是否显示待反馈的loadview
//         * @param isShow
//         */
//        void showLoadViewFeedBack(boolean isShow);

        /**
         * 验证是否登录
         */
        void openOtherUi();

        /**
         * 新任务数据显示失败
         */
        void showNewError();

        /**
         * 新任务无数据
         */
        void showNewNoData();

        /**
         * 新任务数据显示成功
         */
        void showNewTasksSuccess(List<FcVisitTaskList.TaskListBean> data);

        /**
         * 待审批数据显示失败
         */
        void showApprovalError();

        /**
         * 待审批无数据
         */
        void showApprovalNoData();

        /**
         * 待审批数据显示成功
         */
        void showApprovalTasksSuccess(List<FcVisitTaskList.TaskListBean> data);

        /**
         * 待反馈数据显示失败
         */
        void showFeedBackError();

        /**
         * 待反馈无数据
         */
        void showFeedBackNoData();

        /**
         * 待反馈数据显示成功
         */
        void showFeedBackTasksSuccess(List<FcVisitTaskList.TaskListBean> data);
    }

    interface Presenter extends BasePresenter {
        /**
         * 得到新任务数据
         * @param UserID
         */
        void getVisitTaskListNew(String UserID);

        /**
         * 得到待审批数据
         * @param UserID
         */
        void getVisitTaskListApproval(String UserID);

        /**
         * 得到待反馈数据
         * @param UserID
         */
        void getVisitTaskListFeedBack(String UserID);
    }
}
