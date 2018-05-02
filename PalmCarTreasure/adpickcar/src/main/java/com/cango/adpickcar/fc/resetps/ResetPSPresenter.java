package com.cango.adpickcar.fc.resetps;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.api.ResetPSService;
import com.cango.adpickcar.model.BaseData;
import com.cango.adpickcar.model.ServerTime;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.net.RxSubscriber;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.EncryptUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/9/21.
 */

public class ResetPSPresenter implements ResetPSContract.Presenter {
    private ResetPSContract.View mView;
    private ResetPSService mService;
    private Subscription subscription;

    public ResetPSPresenter(ResetPSContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(ResetPSService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscription))
            subscription.unsubscribe();
    }

    @Override
    public void resetPS(boolean showRefreshLoadingUI, final String userId, final String password, final String newPassword, final String confirmPassword) {
        if (mView.isActive()) {
            mView.showResetIndicator(showRefreshLoadingUI);
        }
        subscription = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<BaseData>>() {
                    @Override
                    public Observable<BaseData> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("UserID", userId);
                        paramsMap.put("Password", EncryptUtils.encryptMD5ToString(password));
                        paramsMap.put("NewPassword", EncryptUtils.encryptMD5ToString(newPassword));
                        paramsMap.put("ConfirmPassword", EncryptUtils.encryptMD5ToString(confirmPassword));
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        paramsMap = new HashMap<>();
                        paramsMap.put("RequestContent", encrypt);
                        return mService.changePassword(paramsMap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseData>() {
                    @Override
                    protected void _onNext(BaseData o) {
                        if (mView.isActive()) {
                            mView.showResetIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())){
                                mView.openOtherUi();
                                return;
                            }
                            if (isSuccess) {
//                                ADApplication.mSPUtils.put(Api.PASSWORD, newPassword);
                                ADApplication.mSPUtils.clear();
                            }
                            mView.showResetSuccess(isSuccess, o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showResetIndicator(false);
                            mView.showResetError();
                        }
                    }
                });
    }
}
