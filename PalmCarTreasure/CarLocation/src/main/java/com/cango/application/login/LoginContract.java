package com.cango.application.login;

import com.cango.application.base.BasePresenter;
import com.cango.application.base.BaseView;

/**
 * Created by cango on 2017/3/27.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showLoginIndicator(boolean active);

        void showLoginError();

        void showLoginSuccess(boolean isSuccess, String message);

        void openOtherUi();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void login(boolean showRefreshLoadingUI,String userName, String password, String imei, double lat, double lon, String deviceToken,String deviceType);
    }
}
