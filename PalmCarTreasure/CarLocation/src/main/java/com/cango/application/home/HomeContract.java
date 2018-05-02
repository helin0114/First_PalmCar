package com.cango.application.home;

import com.cango.application.base.BasePresenter;
import com.cango.application.base.BaseView;
import com.cango.application.model.ImeiQuery;

/**
 * Created by cango on 2017/6/6.
 */

public interface HomeContract {
    interface View extends BaseView<HomeContract.Presenter> {
        void showInfoIndicator(boolean active);

        void showInfoError();

        void showInfoSuccess(boolean isSuccess, ImeiQuery imeiQuery);

        void showInfoNoData(String message);

        void openOtherUi();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void getImeiQuery(boolean showRefreshLoadingUI,int userId, String IMEI);
    }
}
