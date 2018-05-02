package com.cango.adpickcar.fc.accountmanager;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/04/11
 *     desc   :
 * </pre>
 */
public class AccManagerPresenter implements IAccManagerContract.IAccManagerPresenter {
    private IAccManagerContract.IAccManagerView mView;

    public AccManagerPresenter(IAccManagerContract.IAccManagerView view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void confirmMoney() {

    }
}
