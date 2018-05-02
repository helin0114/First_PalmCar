package com.cango.palmcartreasure.trailer.checkorder;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.CheckOrderData;

/**
 * Created by cango on 2017/9/6.
 */

public interface CheckOrderContract {
    interface View extends BaseView<CheckOrderContract.Presenter> {
        void showIndicator(boolean active);

        void showError(String msg);

        void toOtherUi();

        void showDataSuccess(CheckOrderData data);

        void showDataError(String msg);

        void showNoData();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void getCheckOrderData(boolean showIndicatorUI, int userId, int agencyID, String applyCD, int caseID,int datasource);
    }
}
