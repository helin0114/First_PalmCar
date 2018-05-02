package com.cango.adpickcar.fc.main;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.FcVisitTaskList;

/**
 * Created by dell on 2017/12/11.
 */

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void showLoadView(boolean isShow);

        void showLogout(boolean isSuccess, String message);

        boolean isActive();

        void openOtherUi();

        /**
         * 下拉旋转动画
         * @param active
         */
        void showMainIndicator(boolean active);

        /**
         * 首页任务列表数据显示失败
         */
        void showError();

        /**
         * 首页任务列表无数据
         */
        void showNoData();

        /**
         * 首页任务列表数据显示成功
         */
        void showTasksSuccess(FcVisitTaskList.FcVisitTaskBean mFcVisitTaskBean);

        /**
         * 升级apk
         */
        void updateApk();
    }
    interface Presenter extends BasePresenter {
        void logout(boolean showRefreshLoadingUI, String UserID);

        /**
         * 得到首页任务列表
         * @param UserID
         */
        void getVisitTaskList(String UserID, String PageIndex, String PageSize, String LAT, String LON);
    }
}
