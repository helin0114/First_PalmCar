package com.cango.adpickcar.fc.parkspace;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/04/24
 *     desc   :
 * </pre>
 */
public class ParkPresenter implements ParkContract.IParkPresenter {
    private ParkContract.IParkView mView;
    public ParkPresenter(ParkContract.IParkView view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {

    }
}
