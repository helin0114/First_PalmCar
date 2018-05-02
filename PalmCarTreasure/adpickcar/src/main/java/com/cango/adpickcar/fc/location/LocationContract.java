package com.cango.adpickcar.fc.location;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.LocationQuery;

/**
 * Created by cango on 2017/6/6.
 */

public interface LocationContract {
    interface View extends BaseView<Presenter> {
        void showInfoIndicator(boolean active);

        void showInfoError();

        void showInfoSuccess(boolean isSuccess, LocationQuery locationQuery);

        void showInfoNoData(String message);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void locationQuery(boolean showRefreshLoadingUI, String userId, String IMEI, String startTime);
    }
}
