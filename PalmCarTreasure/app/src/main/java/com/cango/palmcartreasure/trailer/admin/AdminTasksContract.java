package com.cango.palmcartreasure.trailer.admin;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;
import com.cango.palmcartreasure.model.TaskAbandonRequest;
import com.cango.palmcartreasure.model.TaskManageList;
import com.cango.palmcartreasure.model.TypeTaskData;

import java.util.List;

/**
 * Created by cango on 2017/4/27.
 */

public interface AdminTasksContract {
    interface View extends BaseView<AdminTasksContract.Presenter> {
        void showAdminTasksIndicator(boolean active);

        void showAdminTasksError();

        /**
         * 展示管理员所有的分组列表
         *
         * @param
         */
        void showAdminTasks(List<GroupTaskCount.DataBean.TaskCountListBean> tasks);

        /**
         * 展示具体的group的所有任务
         */
        void showAdminGroupTasks(List<GroupTaskQuery.DataBean.TaskListBean> tasks);

        /**
         * 展示抽回结果
         *
         * @param isSuccess
         * @param message
         */
        void showGroupTaskDraw(boolean isSuccess, String message);

        /**
         * 展示管理员所有未分配的任务
         */
        void showAdminUnabsorbedTasks(List<TaskManageList.DataBean.TaskListBean> tasks);

        /**
         * 已经读取后刷新内容
         */
        void updateRead(boolean success,int position);

        void showGiveUpTasksAndNotifyUi(boolean isSuccess, String message);

        void showNoAdminTasks();

        void showAdminTaskDetailUi(TypeTaskData.DataBean.TaskListBean taskListBean);

        void showLoadingView(boolean isShow);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadAdminTasks(String type, double lat, double lon, boolean showRefreshLoadingUI, int pageCount, int pageSize,String applyId,
                            String mobile,String plateNo, String gpsStatus);

        void loadGroupTasks(int[] groupIds, double lat, double lon, boolean showRefreshLoadingUI, int pageCount, int pageSize);

        void loadGroupSearchTasks(int[] groupIds, double lat, double lon, boolean showRefreshLoadingUI, int pageCount, int pageSize,String applyId,
                                  String mobile,String plateNo);

        //抽回任务
        void groupTaskDraw(boolean showRefreshLoadingUI, List<GroupTaskQuery.DataBean.TaskListBean> taskListBeanList);

        //放弃任务
        void giveUpTasks(TaskAbandonRequest[] requests);

        void openDetailTask(TypeTaskData.DataBean.TaskListBean taskListBean);

        /**
         "userid": 0,
         "agencyID": 0,
         "caseID": 0,
         "applyID": 0,
         "applyCD": "string"
         */
        void taskManagerRead(int position,int agencyID,int caseID,int applyID,String applyCD,int datasource);
    }
}
