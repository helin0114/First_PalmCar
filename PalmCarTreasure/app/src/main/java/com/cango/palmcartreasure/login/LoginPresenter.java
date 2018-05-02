package com.cango.palmcartreasure.login;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.LoginService;
import com.cango.palmcartreasure.model.LoginData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.EncryptUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/3/27.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mLoginView;
    private LoginService mLoginService;
    private Subscription subscription;

    public LoginPresenter(LoginContract.View loginView) {
        this.mLoginView = loginView;
        mLoginView.setPresenter(this);
        mLoginService = NetManager.getInstance().create(LoginService.class);
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
    public void login(String userName, String password, String imei, final double lat, final double lon, String deviceToken, String deviceType) {
        mLoginView.showLoginIndicator(true);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("username", userName);
        objectMap.put("password", EncryptUtils.encryptMD5ToString(password));
        objectMap.put("imei", imei);
        objectMap.put("LAT", lat);
        objectMap.put("LON", lon);
        objectMap.put("deviceToken", deviceToken);
        objectMap.put("deviceType", deviceType);
        subscription = mLoginService.getLoginData(objectMap)
//        mLoginService.getLoginData(userName, EncryptUtils.encryptMD5ToString(password), imei, lat, lon, deviceToken, deviceType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new RxSubscriber<LoginData>() {
                    @Override
                    protected void _onNext(LoginData o) {
                        if (mLoginView.isActive()) {
                            mLoginView.showLoginIndicator(false);
                            int code = o.getCode();
                            String msg = o.getMsg();
                            boolean isSuccess;
                            if (code == 0) {
                                MtApplication.mSPUtils.put(Api.TOKEN, o.getData().getToken());
                                MtApplication.mSPUtils.put(Api.USERID, Integer.parseInt(o.getData().getUserid()));
                                String role = o.getData().getRole();
                                if ("Emp".equals(role)) {
                                    MtApplication.mSPUtils.put(Api.USERROLEID, 1032);
                                } else if ("Admin".equals(role)) {
                                    MtApplication.mSPUtils.put(Api.USERROLEID, 1031);
                                } else {
                                    MtApplication.mSPUtils.put(Api.USERROLEID, -1);
                                }
//                                MtApplication.mSPUtils.put(Api.LOGIN_LAST_LAT, lat);
//                                MtApplication.mSPUtils.put(Api.LOGIN_LAST_LON, lon);
//                                mLoginView.openOtherUi();
                                isSuccess = true;
                            } else {
                                isSuccess = false;
                            }
                            mLoginView.showLoginSuccess(isSuccess, msg);
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mLoginView.isActive()) mLoginView.showLoginIndicator(false);
                    }
                });
    }
}
