package com.cango.adpickcar.fc.main.fcsearch;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.FcVisitTaskList;

import java.util.List;

/**
 * Created by dell on 2018/4/24.
 */

public class FcSearchContract {
    interface View extends BaseView<Presenter> {
        void showLoadView(boolean isShow);

        boolean isActive();

        void openOtherUi();

        /**
         * 下拉旋转动画
         * @param active
         */
        void showMainIndicator(boolean active);

        /**
         * 搜索结果任务列表数据显示失败
         */
        void showError();

        /**
         * 搜索结果任务列表无数据
         */
        void showNoData();

        /**
         * 搜索结果任务列表数据显示成功
         */
        void showTasksSuccess(List<FcVisitTaskList.TaskListBean> data);
    }
    interface Presenter extends BasePresenter {
        /**
         * 得到搜索结果
         * @param UserID
         */
        void getSearchResultTaskList(String UserID, String PageIndex, String PageSize);
    }
}
