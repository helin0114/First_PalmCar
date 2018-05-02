package com.cango.adpickcar.fc.billdetail;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/04/13
 *     desc   :
 * </pre>
 */
public interface BillDetailContract {
    interface BillDetailView extends BaseView<BillDetailPresenter> {
        void showAVLoading(boolean isShow);
        void updateUi();
        void showNoData();
        void showSorry();
    }

    interface BillDetailPresenter extends BasePresenter {
        void getData(boolean isShow, String userId, String pageIndex, String pageSize);
    }
}
