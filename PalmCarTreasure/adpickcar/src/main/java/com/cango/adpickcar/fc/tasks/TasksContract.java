package com.cango.adpickcar.fc.tasks;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.FcVisitTaskList;

import java.util.List;

/**
 * Created by dell on 2017/12/11.
 */

public interface TasksContract {

    interface View extends BaseView<TasksContract.Presenter> {
        boolean isActive();

        /**
         * 验证是否登录
         */
        void openOtherUi();

        /**
         * 今日任务数据显示失败
         */
        void showTodayError();

        /**
         * 今日任务无数据
         */
        void showTodayNoData();

        /**
         * 初次家访今日数据显示成功
         */
        void showTodayTasksSuccess(List<FcVisitTaskList.TaskListBean> data);

        /**
         * 本周任务数据显示失败
         */
        void showWeekError();

        /**
         * 本周任务无数据
         */
        void showWeekNoData();

        /**
         * 初次家访本周任务数据显示成功
         */
        void showWeekTasksSuccess(List<FcVisitTaskList.TaskListBean> data);

        /**
         * 全部任务数据显示失败
         */
        void showAllError();

        /**
         * 全部任务无数据
         */
        void showAllNoData();

        /**
         * 初次家访全部任务数据显示成功
         */
        void showAllTasksSuccess(List<FcVisitTaskList.TaskListBean> data);
    }

    interface Presenter extends BasePresenter {
        /**
         * 得到今日任务数据
         * @param UserID
         */
        void getVisitTaskListToday(String UserID);

        /**
         * 得到本周任务数据
         * @param UserID
         */
        void getVisitTaskListWeek(String UserID);

        /**
         * 得到历史任务数据
         * @param UserID
         */
        void getVisitTaskListAll(String UserID);
    }
}
