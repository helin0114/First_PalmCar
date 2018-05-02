package com.cango.adpickcar.fc.accountmanager;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/04/11
 *     desc   :
 * </pre>
 */
public interface IAccManagerContract {
    interface IAccManagerView extends BaseView<IAccManagerPresenter> {
        void openLoading();
        void closeLoading();
        void toAccountBook();
        void showConfirmResult(boolean isOK,Object object,String msg);
        void toOtherUi(Class tClass);
    }

    interface IAccManagerPresenter extends BasePresenter {
        void confirmMoney();
    }
}
