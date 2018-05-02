package com.cango.adpickcar.fc.everyday;

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
public interface EverydayFCSContract {
    interface IEverydayFCSView extends BaseView<IEverydayFCSPresenter> {
        void toOtherUi(Class tClass, String key, boolean value);
    }

    interface IEverydayFCSPresenter extends BasePresenter {

    }
}
