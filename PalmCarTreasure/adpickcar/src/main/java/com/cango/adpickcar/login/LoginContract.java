package com.cango.adpickcar.login;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.LoginData;

/**
 * Created by cango on 2017/9/21.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showLoginIndicator(boolean active);

        void showLoginError();

        void showLoginSuccess(boolean isSuccess, String message, String role);

        void openOtherUi();

        boolean isActive();
    }

    /**
     * LoginID											用户姓名									string
     * Password											用户密码									string
     * IMEI											手机IMEI									string
     * DeviceToken											设备推送号码									string
     * DeviceType											设备类型									string
     */
    interface Presenter extends BasePresenter {
        void login(boolean showRefreshLoadingUI, String loginID, String Password, String IMEI, String deviceToken, String deviceType);
    }
}
