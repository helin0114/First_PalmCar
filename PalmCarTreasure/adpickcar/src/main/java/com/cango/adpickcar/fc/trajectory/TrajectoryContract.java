package com.cango.adpickcar.fc.trajectory;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.CarTrackQuery;

/**
 * Created by cango on 2017/6/8.
 */

public interface TrajectoryContract {
    interface View extends BaseView<Presenter> {
        void showInfoIndicator(boolean active);

        void showInfoError();

        void showInfoDataError();

        void showInfoSuccess(boolean isSuccess, CarTrackQuery.DataBean dataBean);

        void openOtherUi();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void carTrackQuery(boolean showRefreshLoadingUI, int userId, String IMEI, String startTime, String endTime,
                           int dependMinute);
    }
}

