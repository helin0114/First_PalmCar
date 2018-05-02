package com.cango.application.login;

import android.text.TextUtils;

import com.cango.application.MTApplication;
import com.cango.application.api.Api;
import com.cango.application.api.LoginService;
import com.cango.application.model.LoginData;
import com.cango.application.net.NetManager;
import com.cango.application.net.RxSubscriber;
import com.cango.application.util.EncryptUtils;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/6/5.
 */

public class LoginPresenter implements LoginContract.Presenter{
    private LoginContract.View mView;
    private LoginService mService;
    public LoginPresenter(LoginContract.View view) {
        mView=view;
        mView.setPresenter(this);
        mService= NetManager.getInstance().create(LoginService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void login(boolean showRefreshLoadingUI,String userName, String password, String imei, double lat, double lon,String deviceToken, String deviceType) {
        if (mView.isActive()){
            mView.showLoginIndicator(showRefreshLoadingUI);
        }
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("username", userName);
        objectMap.put("password", EncryptUtils.encryptMD5ToString(password));
        objectMap.put("imei", imei);
        objectMap.put("LAT", lat);
        objectMap.put("LON", lon);
        objectMap.put("deviceToken", deviceToken);
        objectMap.put("deviceType", deviceType);
        mService.login(objectMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<LoginData>() {
                    @Override
                    protected void _onNext(LoginData o) {
                        if (mView.isActive()) {
                            mView.showLoginIndicator(false);
                            int code = o.getCode();
                            String msg = o.getMsg();
                            boolean isSuccess;
                            if (code == 0) {
                                MTApplication.mSPUtils.put(Api.TOKEN, o.getData().getToken());
                                if (!TextUtils.isEmpty(o.getData().getUserid())&&!TextUtils.isEmpty(o.getData().getRole())){
                                    MTApplication.mSPUtils.put(Api.USERID, Integer.parseInt(o.getData().getUserid()));
                                    MTApplication.mSPUtils.put(Api.ROLE, o.getData().getRole());
                                    isSuccess=true;
                                }else {
                                    isSuccess=false;
                                }
                            }else {
                                isSuccess=false;
                            }
                            mView.showLoginSuccess(isSuccess,msg);
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) mView.showLoginIndicator(false);
                    }
                });
    }
}
