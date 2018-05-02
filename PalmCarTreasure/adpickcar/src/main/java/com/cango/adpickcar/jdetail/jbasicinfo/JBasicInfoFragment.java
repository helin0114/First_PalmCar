package com.cango.adpickcar.jdetail.jbasicinfo;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.jdetail.JDetailActivity;
import com.cango.adpickcar.jdetail.JDetailFragment;
import com.cango.adpickcar.jdetail.JDetailPresenter;
import com.cango.adpickcar.model.DeliveryTaskList;
import com.cango.adpickcar.model.DeliveryTaskList.DataBean.CarDeliveryTaskListBean;
import com.cango.adpickcar.model.EventModel.RefreshJMainEvent;
import com.cango.adpickcar.model.JCarBaseInfo;
import com.cango.adpickcar.model.JCarBaseInfo.DataBean.DLVListBean;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.SnackbarUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class JBasicInfoFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 10086;

    public static JBasicInfoFragment getInstance(CarDeliveryTaskListBean carDeliveryTaskListBean) {
        JBasicInfoFragment jBasicInfoFragment = new JBasicInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", carDeliveryTaskListBean);
        jBasicInfoFragment.setArguments(bundle);
        return jBasicInfoFragment;
    }

    @BindView(R.id.nsv_jbasic)
    NestedScrollView nsvBasic;
//    @BindView(R.id.sp_jway)
//    Spinner spWay;
    @BindView(R.id.tv_jway1)
    TextView tvWay;
//    @BindView(R.id.et_jtime)
//    EditText etTime;
    @BindView(R.id.tv_jtime)
    TextView tvTime;
//    @BindView(R.id.et_jaddress)
//    EditText etAddress;
    @BindView(R.id.tv_jaddress)
    TextView tvAddress;
//    @BindView(R.id.sp_jkyc)
//    Spinner spKyc;
    @BindView(R.id.tv_jkyc)
    TextView tvKyc;
//    @BindView(R.id.et_jname)
//    EditText etName;
    @BindView(R.id.tv_jname)
    TextView tvName;
//    @BindView(R.id.et_jidcard)
//    EditText etIDCard;
    @BindView(R.id.tv_jidcard)
    TextView tvIDCard;
//    @BindView(R.id.et_jphone)
//    EditText etPhone;
    @BindView(R.id.tv_jphone)
    TextView tvPhone;
//    @BindView(R.id.et_jrequest)
//    EditText etRequest;
    @BindView(R.id.tv_jrequest)
    TextView tvRequest;
//    @BindView(R.id.et_jpayee)
//    EditText etPayEE;
    @BindView(R.id.tv_jpayee)
    TextView tvPayEE;
//    @BindView(R.id.et_jpaytime)
//    EditText etPayTime;
    @BindView(R.id.tv_jpaytime)
    TextView tvPayTime;
    @BindView(R.id.et_mileage)
    EditText etMileage;
    @BindView(R.id.et_jremark)
    EditText etRemark;
    @BindView(R.id.btn_jok)
    Button btnOK;
    @BindView(R.id.btn_jfailure)
    Button btnFailure;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;

    @OnClick({R.id.btn_jok, R.id.btn_jfailure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_jok:
                if (isOK) {
                    isOK = false;
                    if (presenter != null && mCarDeliveryTaskListBean != null) {
                        if (mPhoneLat <= 0 || mPhoneLon <= 0) {
//                            ToastUtils.showShort("暂未能获取位置信息");
                            Snackbar.make(nsvBasic, "暂未能获取位置信息", Snackbar.LENGTH_LONG)
                                    .setAction("消失", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .show();
                            isOK = true;
                            return;
                        }
                        if (TextUtils.isEmpty(etMileage.getText().toString()) || Integer.parseInt(etMileage.getText().toString()) <= 0) {
                            Snackbar.make(nsvBasic, "请填写正确交车里程数", Snackbar.LENGTH_LONG)
                                    .setAction("消失", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .show();
                            isOK = true;
                            return;
                        }
                        presenter.SaveCarDeliveryTask(true, mCarDeliveryTaskListBean.getCDLVTasKID() + "",
                                "30", mPhoneLat + "", mPhoneLon + "", etRemark.getText().toString().trim(), 0, etMileage.getText().toString().trim());
                    }
                }
                break;
            case R.id.btn_jfailure:
                if (isFailure) {
                    isFailure = false;
                    if (presenter != null && mCarDeliveryTaskListBean != null) {
                        if (TextUtils.isEmpty(etRemark.getText().toString())) {
//                            ToastUtils.showShort("请填写交车情况备注");
                            Snackbar.make(nsvBasic, "请填写交车情况备注", Snackbar.LENGTH_LONG)
                                    .setAction("消失", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .show();
                            isFailure = true;
                            return;
                        }
                        presenter.SaveCarDeliveryTask(true, mCarDeliveryTaskListBean.getCDLVTasKID() + "",
                                "40", "", "", etRemark.getText().toString().trim(), 1, etMileage.getText().toString().trim());
                    }
                }
                break;
        }
    }

    private JDetailActivity mActivity;
    private JDetailFragment detailFragment;
    private JDetailPresenter presenter;
    private DeliveryTaskList.DataBean.CarDeliveryTaskListBean mCarDeliveryTaskListBean;
    private boolean isEdit;
    private JCarBaseInfo mBaseInfo;
    //用来做防止提交过程中再次提交
    private boolean isOK = true, isFailure = true;
    //定位相关
    private AMapLocationClient mLocationClient;
    private double mPhoneLat, mPhoneLon;
    private AMapLocationListener mLoactionListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (CommUtil.checkIsNull(aMapLocation)) {
                mPhoneLat = 0;
                mPhoneLon = 0;
            } else {
                if (aMapLocation.getErrorCode() == 0) {
                    if (!CommUtil.checkIsNull(aMapLocation.getLatitude())) {
                        mPhoneLat = aMapLocation.getLatitude();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLongitude())) {
                        mPhoneLon = aMapLocation.getLongitude();
                    }
                } else {
                    mPhoneLat = 0;
                    mPhoneLon = 0;
                    int errorCode = aMapLocation.getErrorCode();
                    Logger.d("errorCode = " + errorCode + " errorInfo = " + aMapLocation.getErrorInfo());
                }
//                Logger.d(mPhoneLat);
            }
        }
    };

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_jbasic_info;
    }

    @Override
    protected void initView() {
        if (isEdit) {
            openPermissions();
        }
        getData();
    }

    @Override
    protected void initData() {
        mActivity = (JDetailActivity) getActivity();
        detailFragment = (JDetailFragment) getParentFragment();
        presenter = (JDetailPresenter) ((JDetailFragment) getParentFragment()).mPresenter;
        mCarDeliveryTaskListBean = getArguments().getParcelable("bean");
        isEdit = ((JDetailFragment) getParentFragment()).isEdit;
        if (isEdit) {
            initLocation();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("BaseInfo",mBaseInfo);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null){
            mBaseInfo = savedInstanceState.getParcelable("BaseInfo");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    public void getData() {
        if (mCarDeliveryTaskListBean != null && presenter != null) {
            presenter.GetBaseInfo(true, mCarDeliveryTaskListBean.getCDLVTasKID() + "");
        }
    }

    public void updateUI(JCarBaseInfo baseInfo) {
        mBaseInfo = baseInfo;
        if (isEdit) {
//            etTime.setFocusable(false);
//            etTime.setFocusableInTouchMode(false);
//            etAddress.setFocusable(false);
//            etAddress.setFocusableInTouchMode(false);
//            etName.setFocusableInTouchMode(false);
//            etName.setFocusable(false);
//            etIDCard.setFocusable(false);
//            etIDCard.setFocusableInTouchMode(false);
//            etPhone.setFocusable(false);
//            etPhone.setFocusableInTouchMode(false);
//            etRequest.setFocusable(false);
//            etRequest.setFocusableInTouchMode(false);
//            etPayEE.setFocusableInTouchMode(false);
//            etPayEE.setFocusable(false);
//            etPayTime.setFocusable(false);
//            etPayTime.setFocusableInTouchMode(false);

//            spWay.setClickable(false);
//            spWay.setFocusable(false);
//            spWay.setFocusableInTouchMode(false);
//            spWay.setEnabled(false);
//            spKyc.setClickable(false);
//            spKyc.setFocusable(false);
//            spKyc.setFocusableInTouchMode(false);
//            spKyc.setEnabled(false);

            etRemark.setFocusable(true);
            etRemark.setFocusableInTouchMode(true);
            etMileage.setFocusable(true);
            etMileage.setFocusableInTouchMode(true);
            btnOK.setVisibility(View.VISIBLE);
            btnFailure.setVisibility(View.VISIBLE);
        } else {
//            spWay.setClickable(false);
//            spWay.setFocusable(false);
//            spWay.setFocusableInTouchMode(false);
//            spWay.setEnabled(false);
//            spKyc.setClickable(false);
//            spKyc.setFocusable(false);
//            spKyc.setFocusableInTouchMode(false);
//            spKyc.setEnabled(false);

//            etTime.setFocusable(false);
//            etTime.setFocusableInTouchMode(false);
//            etAddress.setFocusable(false);
//            etAddress.setFocusableInTouchMode(false);
//            etName.setFocusableInTouchMode(false);
//            etName.setFocusable(false);
//            etIDCard.setFocusable(false);
//            etIDCard.setFocusableInTouchMode(false);
//            etPhone.setFocusable(false);
//            etPhone.setFocusableInTouchMode(false);
//            etRequest.setFocusable(false);
//            etRequest.setFocusableInTouchMode(false);
//            etPayEE.setFocusableInTouchMode(false);
//            etPayEE.setFocusable(false);
//            etPayTime.setFocusable(false);
//            etPayTime.setFocusableInTouchMode(false);
            etRemark.setFocusable(false);
            etRemark.setFocusableInTouchMode(false);
            etMileage.setFocusable(false);
            etMileage.setFocusableInTouchMode(false);
            btnOK.setVisibility(View.GONE);
            btnFailure.setVisibility(View.GONE);
        }
        nsvBasic.setVisibility(View.VISIBLE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        JCarBaseInfo.DataBean dataBean = mBaseInfo.getData();
        //交车方式
        ArrayList<DLVListBean> wayBean = (ArrayList<DLVListBean>) dataBean.getDLVList();
//        ArrayAdapter<DLVListBean> wayAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, wayBean);
//        wayAdapter.setDropDownViewResource(R.layout.my_drop_down_item);
//        spWay.setAdapter(wayAdapter);
//        for (DLVListBean bean : wayBean) {
//            if (bean.getId().equals(dataBean.getDeliveryWay())) {
//                spWay.setSelection(wayBean.indexOf(bean));
//            }
//        }
        for (DLVListBean bean : wayBean) {
            if (bean.getId().equals(dataBean.getDeliveryWay())) {
                tvWay.setText(bean.getValue());
            }
        }
        //计划交车时间
//        etTime.setText(dataBean.getPlanDLVTime());
        tvTime.setText(dataBean.getPlanDLVTime());
        //计划交车地点
//        etAddress.setText(dataBean.getPlanDLVPlace());
        tvAddress.setText(dataBean.getPlanDLVPlace());
        //取车身份验证
        ArrayList<DLVListBean> kycBean = (ArrayList<DLVListBean>) dataBean.getCTPList();
//        ArrayAdapter<DLVListBean> kycAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, kycBean);
//        kycAdapter.setDropDownViewResource(R.layout.my_drop_down_item);
//        spKyc.setAdapter(kycAdapter);
//        for (DLVListBean bean : kycBean) {
//            if (bean.getId().equals(dataBean.getCTPIDCHKFlag())) {
//                spKyc.setSelection(wayBean.indexOf(bean));
//            }
//        }
        for (DLVListBean bean : kycBean) {
            if (bean.getId().equals(dataBean.getCTPIDCHKFlag())) {
                tvKyc.setText(bean.getValue());
            }
        }
        //取车人姓名
//        etName.setText(dataBean.getCTPName());
        tvName.setText(dataBean.getCTPName());
        //取车人身份编号
//        etIDCard.setText(dataBean.getCTPIDNO());
        tvIDCard.setText(dataBean.getCTPIDNO());
        //取车人手机号
//        etPhone.setText(dataBean.getCTPCallNO());
        tvPhone.setText(dataBean.getCTPCallNO());
        //交车要求
//        etRequest.setText(dataBean.getDLVRequire());
        tvRequest.setText(dataBean.getDLVRequire());
        //收款人
//        etPayEE.setText(dataBean.getCFMUser());
        tvPayEE.setText(dataBean.getCFMUser());
        //交车时里程数（公里）
        if(!TextUtils.isEmpty(dataBean.getDLVMile()) && Double.parseDouble(dataBean.getDLVMile()) > 0){
            etMileage.setText(dataBean.getDLVMile());
        }
        //收款时间
//        etPayTime.setText(dataBean.getCFMTime());
        tvPayTime.setText(dataBean.getCFMTime());
        //交车情况备注
        etRemark.setText(dataBean.getDLVMemo());
    }

    public void showError() {
        nsvBasic.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    public void showNoData() {
        nsvBasic.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    public void showOKResult(boolean isSuccess, String message) {
        isOK = true;
        if (isSuccess) {
            EventBus.getDefault().post(new RefreshJMainEvent("showOKResult"));
            mActivity.finish();
        } else {
            if (!CommUtil.checkIsNull(message)) {
//                ToastUtils.showShort(message);
//                Snackbar.make(nsvBasic,message,Snackbar.LENGTH_LONG)
//                        .setAction("消失", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        })
//                        .show();
                SnackbarUtils.showLongDisSnackBar(nsvBasic, message);
            }
        }
    }

    public void showFailureResult(boolean isSuccess, String message) {
        isFailure = true;
        if (isSuccess) {
            EventBus.getDefault().post(new RefreshJMainEvent("showFailureResult"));
            mActivity.finish();
        } else {
            if (!CommUtil.checkIsNull(message)) {
//                ToastUtils.showShort(message);
                SnackbarUtils.showLongDisSnackBar(nsvBasic, message);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
    private void openPermissions() {
        String[] perms = {
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            mLocationClient.startLocation();
//            onLoadData();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mLocationClient.startLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_group_and_storage)
                    .build().show();
        }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
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
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
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
