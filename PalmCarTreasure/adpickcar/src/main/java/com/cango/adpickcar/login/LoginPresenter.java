package com.cango.adpickcar.login;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.api.LoginService;
import com.cango.adpickcar.model.LoginData;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.net.RxSubscriber;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.EncryptUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/9/21.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private LoginService mService;
    private Subscription subscription;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(LoginService.class);
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
    public void login(boolean showRefreshLoadingUI, String loginID, final String Password, String IMEI, String deviceToken, String deviceType) {
        if (mView.isActive()) {
            mView.showLoginIndicator(showRefreshLoadingUI);
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("LoginID", loginID);
        paramsMap.put("Password", EncryptUtils.encryptMD5ToString(Password));
        paramsMap.put("IMEI", IMEI);
        paramsMap.put("DeviceToken", deviceToken);
        paramsMap.put("DeviceType", deviceType);
        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
        paramsMap = new HashMap<>();
        paramsMap.put("RequestContent", encrypt);
        subscription = mService.login(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<LoginData>() {
                    @Override
                    protected void _onNext(LoginData o) {
                        if (mView.isActive()) {
                            mView.showLoginIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (isSuccess) {
                                ADApplication.mSPUtils.put(Api.TOKEN, o.getData().getToken());
                                ADApplication.mSPUtils.put(Api.USERID, o.getData().getUserID());
                                ADApplication.mSPUtils.put(Api.PASSWORD, Password);
                                ADApplication.mSPUtils.put(Api.MOBILE, o.getData().getMobile());
                                ADApplication.mSPUtils.put(Api.NICKNAME, o.getData().getNickName());
                                ADApplication.mSPUtils.put(Api.ROLE, o.getData().getRole());
                                mView.showLoginSuccess(isSuccess, o.getMsg(),o.getData().getRole());
                            } else {
                                mView.showLoginSuccess(isSuccess, o.getMsg(),null);
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showLoginIndicator(false);
                            mView.showLoginError();
                        }
                    }
                });
    }

}
