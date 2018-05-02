package com.cango.adpickcar.login;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.fc.main.fcmain.FcMainActivity;
import com.cango.adpickcar.main.MainActivity;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.PhoneUtils;
import com.cango.adpickcar.util.SnackbarUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements LoginContract.View, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_READ_PHONE_STATE_CAMERA_STORAGE = 100;

    public static LoginFragment getInstance() {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @BindView(R.id.et_login_username)
    EditText etUserName;
    @BindView(R.id.et_login_password)
    EditText etPassword;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    @OnClick({R.id.btn_login_signin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_signin:
                isDoLogin = true;
                openPermissions();
                break;
        }
    }

    private LoginActivity mActivity;
    private LoginContract.Presenter mPresenter;
    private boolean isDoLogin;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!CommUtil.checkIsNull(mPresenter)) {
            mPresenter.onDetach();
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        showLoginIndicator(false);
//        etUserName.setText("13601952324");
//        etPassword.setText("Cango123456");
        openPermissions();
    }

    @Override
    protected void initData() {
        mActivity = (LoginActivity) getActivity();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoginIndicator(boolean active) {
        if (active)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showLoginError() {
        isDoLogin = true;
    }

    @Override
    public void showLoginSuccess(boolean isSuccess, String message, String role) {
        isDoLogin = true;
        if (isSuccess) {
            Intent intent = new Intent();
            if (Api.AD.equals(role)) {
                intent.setClass(mActivity,MainActivity.class);
            } else if (Api.FCEmp.equals(role) || Api.FCS.equals(role)) {
//                intent.setClass(mActivity, com.cango.adpickcar.fc.main.MainActivity.class);
                intent.setClass(mActivity, FcMainActivity.class);
            } else {
                SnackbarUtils.showLongDisSnackBar(mActivity.findViewById(R.id.layout_login),
                        R.string.data_unusual);
                return;
            }
            startActivity(intent);
            mActivity.finish();
        } else {
            if (!TextUtils.isEmpty(message))
//                ToastUtils.showShort(message);
                SnackbarUtils.showLongDisSnackBar(mActivity.findViewById(R.id.layout_login), message);
        }
    }

    @Override
    public void openOtherUi() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void login() {
        String name = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String imei = PhoneUtils.getIMEI(mActivity);
        if (checkInputParams(name, password)) {
            mPresenter.login(true, name, password, imei, null, Api.DEVICE_TYPE);
        }
    }

    private boolean checkInputParams(String name, String password) {
        boolean isOk = false;
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            isOk = true;
        }
        return isOk;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_READ_PHONE_STATE_CAMERA_STORAGE)
    private void openPermissions() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            if (isDoLogin) {
                isDoLogin = false;
                login();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_phone_state), REQUEST_READ_PHONE_STATE_CAMERA_STORAGE, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (isDoLogin) {
            isDoLogin = false;
            login();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Logger.d("onPermissionsDenied");
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        if (requestCode == REQUEST_READ_PHONE_STATE_CAMERA_STORAGE) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_READ_PHONE_STATE_CAMERA_STORAGE)
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
        if (requestCode == REQUEST_READ_PHONE_STATE_CAMERA_STORAGE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            openPermissions();
        }
    }
}
