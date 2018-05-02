package com.cango.application.location;

import com.cango.application.base.BasePresenter;
import com.cango.application.base.BaseView;
import com.cango.application.model.ImeiQuery;
import com.cango.application.model.LocationQuery;

/**
 * Created by cango on 2017/6/6.
 */

public interface LocationContract {
    interface View extends BaseView<LocationContract.Presenter> {
        void showInfoIndicator(boolean active);

        void showInfoError();

        void showInfoSuccess(boolean isSuccess, LocationQuery locationQuery);

        void showInfoNoData(String message);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void locationQuery(boolean showRefreshLoadingUI,int userId, String IMEI,String startTime);
    }
}
