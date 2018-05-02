package com.cango.application.trajectory;

import com.cango.application.base.BasePresenter;
import com.cango.application.base.BaseView;
import com.cango.application.model.CarTrackQuery;

import java.util.Date;

/**
 * Created by cango on 2017/6/8.
 */

public interface TrajectoryContract {
    interface View extends BaseView<TrajectoryContract.Presenter> {
        void showInfoIndicator(boolean active);

        void showInfoError();

        void showInfoDataError();

        void showInfoSuccess(boolean isSuccess, CarTrackQuery.DataBean dataBean);

        void openOtherUi();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void carTrackQuery(boolean showRefreshLoadingUI, int userId, String IMEI, String startTime,String endTime,
                           int dependMinute);
    }
}

