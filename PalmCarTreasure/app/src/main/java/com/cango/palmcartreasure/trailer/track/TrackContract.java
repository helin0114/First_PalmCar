package com.cango.palmcartreasure.trailer.track;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.CarTrackQuery;

/**
 * Created by cango on 2017/6/8.
 */

public interface TrackContract {
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

