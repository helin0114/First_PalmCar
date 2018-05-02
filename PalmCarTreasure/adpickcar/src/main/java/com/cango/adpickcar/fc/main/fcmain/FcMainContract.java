package com.cango.adpickcar.fc.main.fcmain;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;

/**
 * Created by dell on 2018/4/8.
 */

public class FcMainContract {
    interface View extends BaseView<Presenter> {
        void showLoadView(boolean isShow);

        void showLogout(boolean isSuccess, String message);

        boolean isActive();

        void openOtherUi();
        /**
         * 得到家访数据成功
         */
        void getHomeVisitDataSuccess();

        /**
         * 得到家访数据失败
         */
        void showHomeVisitError();

        /**
         * 没有家访数据
         */
        void showHomeVisitNoData();
    }
    interface Presenter extends BasePresenter {
        void logout(boolean showRefreshLoadingUI, String UserID);

        void getHomeVisitData();
    }
}
