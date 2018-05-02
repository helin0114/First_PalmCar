package com.cango.adpickcar.fc.parkspace;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/04/24
 *     desc   :
 * </pre>
 */
public interface ParkContract {
    interface IParkView extends BaseView<IParkPresenter> {
        void showAVLoading(boolean isShow);

        void updateUi();

        void showNoData();

        void showSorry();
    }

    interface IParkPresenter extends BasePresenter {

    }
}
