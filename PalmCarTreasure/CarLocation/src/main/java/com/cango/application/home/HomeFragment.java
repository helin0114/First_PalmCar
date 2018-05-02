package com.cango.application.home;


import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.cango.application.MTApplication;
import com.cango.application.R;
import com.cango.application.api.Api;
import com.cango.application.base.BaseFragment;
import com.cango.application.location.LocationActivity;
import com.cango.application.model.ImeiQuery;
import com.cango.application.util.CommUtil;
import com.cango.application.util.KeyboardUtils;
import com.cango.application.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 主页界面的view
 */
public class HomeFragment extends BaseFragment implements HomeContract.View, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 101;

    @BindView(R.id.et_home_imei)
    EditText etImei;
    @BindView(R.id.ll_query)
    LinearLayout llQuery;
    @BindView(R.id.iv_shadow)
    ImageView ivShadow;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.tv_imei)
    TextView tvImei;
    @BindView(R.id.tv_customname)
    TextView tvCustomName;
    @BindView(R.id.tv_vin)
    TextView tvVin;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_car_voltage)
    TextView tvCarVoltage;
    @BindView(R.id.tv_device_voltage)
    TextView tvDeviceVoltage;
    @BindView(R.id.tv_gps)
    TextView tvGPS;
    @BindView(R.id.tv_gsm)
    TextView tvGSM;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    @OnClick({R.id.ll_query, R.id.tv_look_location, R.id.ll_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_query:
                if (checkIMEI()) {
                    llInfo.setVisibility(View.GONE);
                    ivShadow.setVisibility(View.GONE);
                    KeyboardUtils.hideSoftInput(mActivity);
                    mPresenter.getImeiQuery(true, MTApplication.mSPUtils.getInt(Api.USERID), etImei.getText().toString().trim());
                } else {
                    ToastUtils.showShort(R.string.please_input_IMEI);
                }
                break;
            case R.id.tv_look_location:
                isGetLocation=true;
                mPresenter.getImeiQuery(true,MTApplication.mSPUtils.getInt(Api.USERID),mImeiQuery.getData().getIMEI());
                break;
            case R.id.ll_info:
                llInfo.setVisibility(View.GONE);
                ivShadow.setVisibility(View.GONE);
                break;
        }
    }

    private HomeContract.Presenter mPresenter;
    private HomeActivity mActivity;
    private ImeiQuery mImeiQuery;
    /**
     * 得到最新的GPS最后定位信息,再次访问一次IMEI info接口
     */
    private boolean isGetLocation;
    //地图相关
    private AMap mMap;
    //定位相关
    private AMapLocationClient mLocationClient;
    private double mLat, mLon;
    private String mProvince;
    private boolean isShouldFirstAddData = true;
    private AMapLocationListener mLoactionListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (CommUtil.checkIsNull(aMapLocation)) {
                mLat = 0;
                mLon = 0;
            } else {
                if (aMapLocation.getErrorCode() == 0) {
                    if (!CommUtil.checkIsNull(aMapLocation.getProvince())) {
                        mProvince = aMapLocation.getProvince();
                    }
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
                    Logger.d("errorCode = " + errorCode + " errorInfo = " + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        llInfo.setVisibility(View.GONE);
        ivShadow.setVisibility(View.GONE);
//        mImeiQuery=new ImeiQuery();
//        ImeiQuery.DataBean dataBean=new ImeiQuery.DataBean();
//        dataBean.setResultLAT(31.25753);
//        dataBean.setResultLON(121.568433);
        showInfoIndicator(false);
        openPermissions();
        setUpMapIfNeeded();
//        etImei.setText("1696000141");
        etImei.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH){
                    if (checkIMEI()) {
                        llInfo.setVisibility(View.GONE);
                        ivShadow.setVisibility(View.GONE);
                        mPresenter.getImeiQuery(true, MTApplication.mSPUtils.getInt(Api.USERID), etImei.getText().toString().trim());
                    } else {
                        ToastUtils.showShort(R.string.please_input_IMEI);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        mActivity = (HomeActivity) getActivity();
        initLocation();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showInfoIndicator(boolean active) {
        if (active) {
            mLoadView.smoothToShow();
        } else {
            mLoadView.smoothToHide();
        }
    }

    @Override
    public void showInfoError() {
        if (isGetLocation)
            isGetLocation=false;
    }

    @Override
    public void showInfoSuccess(boolean isSuccess, ImeiQuery imeiQuery) {
        if (isSuccess){
            if (isGetLocation){
                isGetLocation=false;
                mImeiQuery=imeiQuery;
                Intent intent=new Intent(mActivity,LocationActivity.class);
                intent.putExtra("DataBean",mImeiQuery.getData());
                mActivity.mSwipeBackHelper.forward(intent);
            }else {
                llInfo.setVisibility(View.VISIBLE);
                ivShadow.setVisibility(View.VISIBLE);
                mImeiQuery=imeiQuery;
                updateUi();
            }
        }
    }

    /**
     * 更新界面UI
     */
    private void updateUi() {
        if (mImeiQuery!=null&&mImeiQuery.getData()!=null){
            ImeiQuery.DataBean data = mImeiQuery.getData();
            tvImei.setText(data.getIMEI());
            tvCustomName.setText(data.getCustomerName());
            tvVin.setText(data.getVin());
            tvStatus.setText(data.getGpsstatus());
            tvCarVoltage.setText(data.getApplyCD());
            tvDeviceVoltage.setText(data.getDeviceVoltage());
            tvGPS.setText(data.getGpssignal());
            tvGSM.setText(data.getGsmsignal());
            tvTime.setText(data.getLatesttime());
            tvAddress.setText(data.getAddress());
        }
    }

    @Override
    public void showInfoNoData(String message) {
        if (!CommUtil.checkIsNull(message))
            ToastUtils.showShort(message);
        if (isGetLocation)
            isGetLocation=false;
    }

    @Override
    public void openOtherUi() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * 检查IMEI合法性
     * @return true：不为空
     */
    private boolean checkIMEI() {
        boolean isOk = true;
        String IMEI = etImei.getText().toString().trim();
        if (TextUtils.isEmpty(IMEI))
            isOk = false;
        return isOk;
    }

    /**
     * 获取Amap 对象
     */
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fg_map);
            mMap = supportMapFragment.getMap();
            UiSettings uiSettings = mMap.getUiSettings();
            //设置不显示+-符号
//            uiSettings.setZoomControlsEnabled(false);
            uiSettings.setScaleControlsEnabled(true);
            uiSettings.setCompassEnabled(true);
            uiSettings
                    .setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
            //TODO
            setLocationBluePoint();
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13f));
//            setCarGPSLocationPoint();
        }
    }

//    private void setCarGPSLocationPoint() {
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(carGPSLatLng, 15, 0, 0)));
//        mMap.clear();
//        MarkerOptions carMarker = new MarkerOptions()
//                .position(carGPSLatLng)
////                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark));
//        mMap.addMarker(carMarker);
//    }

    private void setLocationBluePoint() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
                .interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
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
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            mLocationClient.startLocation();
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
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_group_and_storage)
                    .build().show();
        }
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
