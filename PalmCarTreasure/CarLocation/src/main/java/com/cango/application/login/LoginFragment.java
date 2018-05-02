package com.cango.application.login;


import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.application.R;
import com.cango.application.api.Api;
import com.cango.application.base.BaseFragment;
import com.cango.application.home.HomeActivity;
import com.cango.application.net.MultiClickSubscribe;
import com.cango.application.util.CommUtil;
import com.cango.application.util.PhoneUtils;
import com.cango.application.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements LoginContract.View, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_READ_PHONE_STATE_AND_LOCATION = 100;
    @BindView(R.id.et_login_name)
    EditText etUserName;
    @BindView(R.id.et_login_password)
    EditText etPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

//    @OnClick({R.id.tv_login})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_login:
////                isDoLogin = true;
////                doLogin();
//                break;
//        }
//    }

    private LoginContract.Presenter mPresenter;
    private LoginActivity mActivity;
    private boolean isDoLogin;
    private AMapLocationClient mLocationClient;
    private double mLat, mLon;
    private boolean isFromLogout;
    private AMapLocationListener mLoactionListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (CommUtil.checkIsNull(aMapLocation)) {
                mLat = 0;
                mLon = 0;
            } else {
                if (aMapLocation.getErrorCode() == 0) {
                    if (!CommUtil.checkIsNull(aMapLocation.getLatitude())) {
                        mLat = aMapLocation.getLatitude();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLongitude())) {
                        mLon = aMapLocation.getLongitude();
                    }
//                    Logger.d("Lat = " + aMapLocation.getLatitude() + "   Lon = " + aMapLocation.getLongitude() + "   address = " + aMapLocation.getAddress());
                } else {
                    mLat = 0;
                    mLon = 0;
                    int errorCode = aMapLocation.getErrorCode();
                    if (errorCode == 12 || errorCode == 13) {
                        ToastUtils.showShort(R.string.put_sim_and_permissions);
                    }
                    Logger.d("errorCode = " + aMapLocation.getErrorCode() + " errorInfo = " + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLoactionListener);
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        showLoginIndicator(false);
        openPermissions();
        Observable.create(new MultiClickSubscribe(tvLogin))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer s) {
                        isDoLogin=true;
//                        openPermissions();
//                        doLogin();
                        openPermissions();
                    }
                });
    }

    @Override
    protected void initData() {
        mActivity = (LoginActivity) getActivity();
        if (!CommUtil.checkIsNull(getArguments())) {
            isFromLogout = getArguments().getBoolean("isFromLogout", false);
            Logger.d(isFromLogout);
        }
        initLocation();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoginIndicator(boolean active) {
        if (active) {
            mLoadView.smoothToShow();
        } else {
            mLoadView.smoothToHide();
        }
    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void showLoginSuccess(boolean isSuccess, String message) {
        showLoginIndicator(false);
        if (!isSuccess) {
            if (!TextUtils.isEmpty(message))
                ToastUtils.showShort(message);
        }else {
            openOtherUi();
        }
    }

    @Override
    public void openOtherUi() {
        mActivity.mSwipeBackHelper.forwardAndFinish(HomeActivity.class);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void doLogin() {
//        if (mLat > 0 && mLon > 0) {
            String imei = PhoneUtils.getIMEI(getActivity());
            mPresenter.login(true, etUserName.getText().toString(), etPassword.getText().toString(),
                    imei, mLat, mLon, null, Api.DEVICE_TYPE);
//        } else {
//            ToastUtils.showShort(R.string.no_get_location);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_READ_PHONE_STATE_AND_LOCATION)
    private void openPermissions() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            mLocationClient.startLocation();
            if (isDoLogin) {
                isDoLogin = false;
                doLogin();
            } else {

            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_phone_state), REQUEST_READ_PHONE_STATE_AND_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.d("onPermissionsGranted");
        mLocationClient.startLocation();
        if (isDoLogin) {
            isDoLogin = false;
            doLogin();
        } else {

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Logger.d("onPermissionsDenied");
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        if (requestCode == REQUEST_READ_PHONE_STATE_AND_LOCATION) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_READ_PHONE_STATE_AND_LOCATION)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_read_phone_state)
                    .build().show();
        }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult");
        if (requestCode == REQUEST_READ_PHONE_STATE_AND_LOCATION) {
            // Do something after user returned from app settings screen, like showing a Toast.
            openPermissions();
        }
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(mActivity.getApplicationContext());
        //设置定位参数
        mLocationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        mLocationClient.setLocationListener(mLoactionListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
}
