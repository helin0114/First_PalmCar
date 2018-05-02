package com.cango.adpickcar.fc.parkspace;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.util.AppUtils;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ParkFragment extends BaseFragment implements ParkContract.IParkView, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 1100;

    @BindView(R.id.toolbar_park)
    Toolbar mToolbar;
    @BindView(R.id.fl_map)
    FrameLayout flMap;
    @BindView(R.id.rv_park)
    RecyclerView mRecyclerView;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView mLoading;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.rl_shadow)
    RelativeLayout rlShodow;

    @OnClick({R.id.iv_map_nav})
    public void onClick(View view) {
        switch (view.getId()) {
            //导航
            case R.id.iv_map_nav:
                boolean hasBaiduMap = AppUtils.isHasBaiduMap(mActivity);
                boolean hasGaodeMap = AppUtils.isHasGaodeMap(mActivity);
                if (!hasBaiduMap && !hasGaodeMap) {
                    ToastUtils.showShort("请安装百度地图或高德地图");
                } else {
                    if (mPartGPSLatLng != null) {
                        rlShodow.setVisibility(View.VISIBLE);
                        selectMapPW.showAtLocation(rlShodow, Gravity.BOTTOM, 0, 0);
                    } else {
                        ToastUtils.showShort("暂未获取车辆定位信息");
                    }
                }
                break;
        }
    }

    private ParkContract.IParkPresenter mPresenter;
    private ParkActivity mActivity;
    private ParkAdapter mAdapter;
    private int mCurrentPosition = -1;
    private PopupWindow selectMapPW;
    //地图相关
    private AMap mMap;
    //定位相关
    private AMapLocationClient mLocationClient;
    private double mLat, mLon;
    private LatLng mPartGPSLatLng;
    private boolean isShouldFirstAddData = true;
    private AMapLocationListener mLoactionListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (CommUtil.checkIsNull(aMapLocation)) {
                mLat = 0;
                mLon = 0;
            } else {
                if (aMapLocation.getErrorCode() == 0) {
                    mLat = aMapLocation.getLatitude();
                    mLon = aMapLocation.getLongitude();
//                    Logger.d("Lat = " + aMapLocation.getLatitude() + "   Lon = " + aMapLocation.getLongitude() + "   address = " + aMapLocation.getAddress());
                } else {
                    mLat = 0;
                    mLon = 0;
                    int errorCode = aMapLocation.getErrorCode();
                    Logger.d("errorCode = " + errorCode + " errorInfo = " + aMapLocation.getErrorInfo());
                }
            }
            if (mLat > 0 && mLon > 0) {
                if (isShouldFirstAddData) {
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition(new LatLng(mLat, mLon), 15, 0, 0)));
                    isShouldFirstAddData = false;
                    onLoadData();
                }
            }
        }
    };

    public static ParkFragment newInstance() {
        return new ParkFragment();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_park;
    }

    @Override
    protected void initView() {
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_title_back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        selectMapPW = getPopupWindow(mActivity, R.layout.map_nav_bottom);
        openPermissions();
        setUpMapIfNeeded();
        initRecyclerView();
    }

    private void initRecyclerView() {
        final int color = Color.parseColor("#999999");
        final ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(31.222342, 121.543781));
        latLngs.add(new LatLng(31.163526, 121.559878));
        latLngs.add(new LatLng(31.334928, 121.400627));
        latLngs.add(new LatLng(31.200599, 121.334511));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new ParkAdapter(mActivity, latLngs, false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<LatLng>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, LatLng data, int position) {
                if (mCurrentPosition >= 0) {
                    mRecyclerView.getChildAt(mCurrentPosition).setBackgroundColor(Color.WHITE);
                }
                viewHolder.getConvertView().setBackgroundColor(color);
                for (Marker marker : mMap.getMapScreenMarkers()) {
                    LatLng latLng = marker.getOptions().getPosition();
                    if (!CommUtil.checkIsNull(mPartGPSLatLng) && !CommUtil.checkIsNull(latLng)) {
                        if (latLng.latitude == mPartGPSLatLng.latitude && latLng.longitude == mPartGPSLatLng.longitude) {
                            marker.destroy();
                        }
                    }
                }
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition(data, mMap.getCameraPosition().zoom, 0, 0)));
                MarkerOptions parkMarker = new MarkerOptions()
                        .position(data)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_map));
                mMap.addMarker(parkMarker);
                mCurrentPosition = viewHolder.getAdapterPosition();
                mPartGPSLatLng = data;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mActivity = (ParkActivity) getActivity();
        initLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLoactionListener);
            mLocationClient.onDestroy();
        }
        if (!CommUtil.checkIsNull(mPresenter)) {
            mPresenter.onDetach();
        }
    }

    @Override
    public void setPresenter(ParkContract.IParkPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAVLoading(boolean isShow) {
        if (isShow)
            mLoading.smoothToShow();
        else
            mLoading.smoothToHide();
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void showNoData() {
        flMap.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSorry() {
        flMap.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
    }

    private void onLoadData() {
        showAVLoading(false);
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
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
            setLocationBluePoint();
        }
    }

    private void setLocationBluePoint() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle
                .myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER)
                ////连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
                .interval(5000);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
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

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        TextView tvBaiduMap = (TextView) popupView.findViewById(R.id.tv_baidu_map);
        ImageView ivBaidu = (ImageView) popupView.findViewById(R.id.ivbaidu);
        TextView tvGaodeMap = (TextView) popupView.findViewById(R.id.tv_gaode_map);
        ImageView ivGaode = (ImageView) popupView.findViewById(R.id.ivgaode);
        if (!AppUtils.isHasBaiduMap(context)) {
            tvBaiduMap.setVisibility(View.GONE);
            ivBaidu.setVisibility(View.GONE);
        }
        if (!AppUtils.isHasGaodeMap(context)) {
            tvGaodeMap.setVisibility(View.GONE);
            ivGaode.setVisibility(View.GONE);
        }
        tvBaiduMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "停车位位置";
                startBaiduMap(mPartGPSLatLng.latitude, mPartGPSLatLng.longitude, name);
            }
        });
        tvGaodeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "停车位位置";
                startGaodeMap(mPartGPSLatLng.latitude, mPartGPSLatLng.longitude, name);
            }
        });
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#36000000")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlShodow.setVisibility(View.GONE);
            }
        });

        popupWindow.update();
        return popupWindow;
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

    private void startBaiduMap(double lat, double lon, String name) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //将功能Scheme以URI的方式传入data
        Uri uri = Uri.parse("baidumap://map/direction?origin=&destination=latlng:" + lat + "," + lon + "|name:" + name + "&mode=driving&sy=&index=&target=");
        intent.setData(uri);
        //启动该页面即可
        startActivity(intent);
    }

    private void startGaodeMap(double lat, double lon, String name) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //将功能Scheme以URI的方式传入data
        Uri uri = Uri.parse("androidamap://route?sourceApplication=cango&slat=&slon=&sname=&dlat=+" + lat + "&dlon=" + lon + "&dname=" + name + "&dev=0&t=1");
        intent.setData(uri);
        //启动该页面即可
        startActivity(intent);
    }

    class ParkAdapter extends com.cango.adpickcar.baseAdapter.BaseAdapter<LatLng> {

        public ParkAdapter(Context context, List<LatLng> datas, boolean isOpenLoadMore) {
            super(context, datas, isOpenLoadMore);
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.park_item;
        }

        @Override
        protected void convert(BaseHolder holder, LatLng data) {
            View line = holder.getView(R.id.view_line);
            if (mDatas.indexOf(data) == mDatas.size() - 1) {
                line.setVisibility(View.GONE);
            } else {
                line.setVisibility(View.VISIBLE);
            }
            TextView tvAddress = holder.getView(R.id.tv_address);
            tvAddress.setText(data.latitude + ":" + data.longitude);
        }
    }
}
