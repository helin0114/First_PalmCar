package com.cango.adpickcar.fc.resetps;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;

/**
 * Created by cango on 2017/9/21.
 */

public interface ResetPSContract {
    interface View extends BaseView<Presenter> {
        void showResetIndicator(boolean active);

        void showResetError();

        void showResetSuccess(boolean isSuccess, String message);

        void openOtherUi();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        /**
         * UserID											用户姓名									string
         * Password											用户密码									string
         * NewPassword											用户新密码									string
         * ConfirmPassword											用户确认密码									string
         */
        void resetPS(boolean showRefreshLoadingUI, String userId, String password, String newPassword, String confirmPassword);
    }
}
