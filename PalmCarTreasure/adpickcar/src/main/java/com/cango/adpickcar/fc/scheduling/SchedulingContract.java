package com.cango.adpickcar.fc.scheduling;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.FcSchedulingInfo;

import java.util.List;

/**
 * Created by dell on 2017/12/11.
 */

public interface SchedulingContract {
    interface View extends BaseView<SchedulingContract.Presenter> {
        boolean isActive();

        void openOtherUi();

        void showLoadView(boolean isShow);

        void showError();

        void showNoData();

        void showMainIndicator(boolean active);

        void showSchedulingSuccess(List<FcSchedulingInfo> list);
    }

    interface Presenter extends BasePresenter {
        void GetSchedulingData();
    }
}
