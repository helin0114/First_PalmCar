package com.cango.adpickcar.fc.main.fcsweeklyscheduling;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.FcVisitTaskList;

import java.util.List;

/**
 * Created by dell on 2017/12/11.
 */

public interface FcsWeeklySchedulingContract {

    interface View extends BaseView<FcsWeeklySchedulingContract.Presenter> {
        void showLoadView(boolean isShow);
        boolean isActive();
        /**
         * 验证是否登录
         */
        void openOtherUi();

        /**
         * 新任务数据显示失败
         */
        void showTodayError();

        /**
         * 新任务无数据
         */
        void showTodayNoData();

        void showTodayTasksSuccess(List<FcVisitTaskList.TaskListBean> data);
    }

    interface Presenter extends BasePresenter {
        void getVisitTaskListToday(String UserID);
    }
}
