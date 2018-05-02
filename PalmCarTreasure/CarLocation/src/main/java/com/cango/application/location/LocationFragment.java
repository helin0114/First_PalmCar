package com.cango.application.location;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.cango.application.MTApplication;
import com.cango.application.R;
import com.cango.application.api.Api;
import com.cango.application.base.BaseFragment;
import com.cango.application.customview.CalendarDialogFragment;
import com.cango.application.model.ImeiQuery;
import com.cango.application.model.LocationQuery;
import com.cango.application.trajectory.TrajectoryActivity;
import com.cango.application.trajectory.TrajectoryFragment;
import com.cango.application.util.AppUtils;
import com.cango.application.util.CommUtil;
import com.cango.application.util.ScreenUtil;
import com.cango.application.util.SizeUtil;
import com.cango.application.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 定位界面的view
 */
public class LocationFragment extends BaseFragment implements LocationContract.View, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 102;

    @BindView(R.id.toolbar_trailer_map)
    Toolbar mToolbar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_shadow)
    ImageView ivShodow;
    @BindView(R.id.ll_toolbar_right)
    LinearLayout llRight;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    private Polyline mPolyline;
    private SmoothMoveMarker smoothMarker;
    private MarkerOptions carMarker;

    @OnClick({R.id.iv_map_nav, R.id.ll_toolbar_right})
    public void onClick(View view) {
        switch (view.getId()) {
            //导航
            case R.id.iv_map_nav:
                boolean hasBaiduMap = AppUtils.isHasBaiduMap(mActivity);
                boolean hasGaodeMap = AppUtils.isHasGaodeMap(mActivity);
                if (!hasBaiduMap && !hasGaodeMap) {
                    ToastUtils.showShort("请安装百度地图或高德地图");
                } else {
                    ivShodow.setVisibility(View.VISIBLE);
                    selectMapPW.showAtLocation(ivShodow, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.ll_toolbar_right:
                showCalendarDialog();
                break;
        }
    }

    /**
     * 得到轨迹的开始时间，考虑销毁重建的问题
     */
    private String mStartTime;
    private List<LatLng> mPoints;
    /**
     * 当前的点
     */
    private List<LatLng> mCurrentPoints;
    /**
     * 最后一个点
     */
    private List<LatLng> mLastPoints;
    private LocationActivity mActivity;
    private LocationContract.Presenter mPresenter;
    private ImeiQuery.DataBean mDataBean;
    private LocationQuery.DataBean mLocationBean;
    private PopupWindow selectMapPW;
    private CalendarDialogFragment mCalendarDialog;
    //地图相关
    private AMap mMap;
    //定位相关
    private AMapLocationClient mLocationClient;
    private double mLat, mLon;
    private LatLng carGPSLatLng,resultLatLng;
    private String mProvince;
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

    public static LocationFragment newInstance(ImeiQuery.DataBean dataBean) {
        LocationFragment locationFragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putParcelable("DataBean", dataBean);
        locationFragment.setArguments(args);
        return locationFragment;
    }

    Observable<Long> interval;
    Subscription subscribe;

    @Override
    public void onResume() {
        super.onResume();
        //10s一次轮询
        interval = Observable.interval(10, TimeUnit.SECONDS);
        subscribe = interval.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                if (!TextUtils.isEmpty(mStartTime)) {
                    mPresenter.locationQuery(true, MTApplication.mSPUtils.getInt(Api.USERID), mDataBean.getIMEI(), mStartTime);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (interval != null && subscribe != null) {
            subscribe.unsubscribe();
        }
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
        return R.layout.fragment_location;
    }

    @Override
    protected void initView() {
        mLoadView.smoothToHide();
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        selectMapPW = getPopupWindow(mActivity, R.layout.map_nav_bottom);
        if (!CommUtil.checkIsNull(mDataBean)) {
            tvName.setText(mDataBean.getCustomerName());
            tvAddress.setText(mDataBean.getAddress());
        }
        if (Api.ADMIN.equals(MTApplication.mSPUtils.getString(Api.ROLE))) {
            llRight.setVisibility(View.VISIBLE);
        } else {
            llRight.setVisibility(View.INVISIBLE);
        }
        openPermissions();
        setUpMapIfNeeded();
    }

    /**
     * 更新界面UI
     */
    private void updateUi() {
        if (!CommUtil.checkIsNull(mLocationBean)) {
            tvName.setText(mLocationBean.getCustomerName());
            tvAddress.setText(mLocationBean.getAddress());
        }
    }

    @Override
    protected void initData() {
        mActivity = (LocationActivity) getActivity();
        mDataBean = getArguments().getParcelable("DataBean");
        mStartTime = mDataBean.getServerTime();
        carGPSLatLng = new LatLng(mDataBean.getResultLAT(), mDataBean.getResultLON());
        resultLatLng=new LatLng(mDataBean.getResultLAT(),mDataBean.getResultLON());
        initLocation();
    }

    @Override
    public void setPresenter(LocationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showInfoIndicator(boolean active) {
        if (active){
            mLoadView.smoothToShow();
        }else {
            mLoadView.smoothToHide();
        }
    }

    @Override
    public void showInfoError() {

    }

    @Override
    public void showInfoSuccess(boolean isSuccess, LocationQuery locationQuery) {
        if (locationQuery.getData() != null) {
            LocationQuery.DataBean data = locationQuery.getData();
            mLocationBean = data;
            carGPSLatLng = new LatLng(mLocationBean.getResultLAT(), mLocationBean.getResultLON());
            updateUi();
            List<LocationQuery.DataBean.TrackListBean> trackList = data.getTrackList();
            if (trackList != null && trackList.size() > 0) {
//                carMarker.visible(false);
                mPoints = readLatLngs(trackList);
                if (mLastPoints == null) {
                    if (mPoints.size() > 0) {
                        mMap.clear();
                        if (smoothMarker != null) {
                            smoothMarker.destroy();
                        }
                        mLastPoints = mPoints;

                        List<LatLng> points = new ArrayList<>();
                        for (int i = 0; i < mPoints.size(); i++) {
                            points.add(new LatLng(mPoints.get(i).latitude, mPoints.get(i).longitude));
                        }
                        points.add(0,resultLatLng);

                        addPolylineInPlayGround(points);
                        startMove(points);
                    }
                } else {
                    int allSize = mPoints.size();
                    int lastSize = mLastPoints.size();
                    if (allSize > lastSize) {
//                        mCurrentPoints = mPoints.subList(allSize - lastSize - 1, allSize - 1);
                        mCurrentPoints = new ArrayList<>();
                        for (int i = lastSize; i < mPoints.size(); i++) {
                            mCurrentPoints.add(new LatLng(mPoints.get(i).latitude, mPoints.get(i).longitude));
                        }

                        List<LatLng> points = new ArrayList<>();
                        for (int i = 0; i < mPoints.size(); i++) {
                            points.add(new LatLng(mPoints.get(i).latitude, mPoints.get(i).longitude));
                        }
                        points.add(0,resultLatLng);

                        mLastPoints = mPoints;
                        if (mPoints.size() > 0 && mCurrentPoints.size() > 0) {
                            mMap.clear();
                            if (smoothMarker != null) {
                                smoothMarker.destroy();
                            }
                            addPolylineInPlayGround(points);
                            startMove(mCurrentPoints);
                        }
                    }
                }
            }
//            else {
//                mMap.clear();
////                carMarker.visible(false);
////                mPoints = readLatLngs(trackList);
//                size += 0.01;
//                mPoints = getLatLng(size);
//                if (mLastPoints == null) {
//                    mLastPoints = mPoints;
//                    if (smoothMarker != null) {
//                        smoothMarker.destroy();
//                    }
//                    addPolylineInPlayGround();
//                    startMove(mPoints);
//                } else {
//                    if (smoothMarker != null) {
//                        smoothMarker.destroy();
//                    }
//                    mCurrentPoints = new ArrayList<>();
//                    for (int i = 0; i < mPoints.size(); i++) {
//                        mCurrentPoints.add(new LatLng(mPoints.get(i).latitude, mPoints.get(i).longitude));
//                    }
//                    mPoints.addAll(0, mLastPoints);
//                    addPolylineInPlayGround();
//                    startMove(mCurrentPoints);
//                }
//            }
        }
    }

    double size;

    @Override
    public void showInfoNoData(String message) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * 展示日历控件
     */
    private void showCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {
            //TODO
            int type;
//            if (MTApplication.mSPUtils.getString(Api.ROLE).equals(Api.ADMIN)){
//                type=1;
//            }else {
//                type=0;
//            }
            //TODO 当时是因为性能问题只查一天
            type = 0;
            mCalendarDialog = CalendarDialogFragment.newInstance(type);
            mCalendarDialog.setCalendarDilaogListener(new CalendarDialogFragment.CalendarDilaogListener() {
                @Override
                public void onCalendarClick(Date date, Date date1) {
                    Intent intent = new Intent(mActivity, TrajectoryActivity.class);
                    intent.putExtra(TrajectoryFragment.IMEI, mDataBean.getIMEI());
                    if (!CommUtil.checkIsNull(date) && !CommUtil.checkIsNull(date1)) {
                        intent.putExtra(TrajectoryFragment.START_TIME, date);
                        intent.putExtra(TrajectoryFragment.END_TIME, date1);
                    } else {
                        intent.putExtra(TrajectoryFragment.START_TIME, date);
                        intent.putExtra(TrajectoryFragment.END_TIME, date);
                    }
                    mActivity.mSwipeBackHelper.forward(intent);
                }
            });
        }
        if (mCalendarDialog.isVisible()) {

        } else {
            mCalendarDialog.show(getFragmentManager(), "CalendarDialog");
        }
    }

    private void closeCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {

        } else {
            if (mCalendarDialog.isVisible()) {
                mCalendarDialog.dismiss();
            }
        }
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
//            setLocationBluePoint();
            setCarGPSLocationPoint();
        }
    }

    private void setCarGPSLocationPoint() {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(carGPSLatLng, 15, 0, 0)));
        mMap.clear();
        carMarker = new MarkerOptions()
                .position(carGPSLatLng)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
        if (carGPSLatLng.latitude==0&&carGPSLatLng.longitude==0){
            ToastUtils.showShort(R.string.no_get_last_gps);
        }else {
            mMap.addMarker(carMarker);
        }
    }

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
                String name = "车辆位置";
                startBaiduMap(carGPSLatLng.latitude, carGPSLatLng.longitude, name);
            }
        });
        tvGaodeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "车辆位置";
                startGaodeMap(carGPSLatLng.latitude, carGPSLatLng.longitude, name);
            }
        });
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#36000000")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivShodow.setVisibility(View.GONE);
            }
        });

        popupWindow.update();
        return popupWindow;
    }

    /**
     * 读取坐标点
     *
     * @return
     */
    private List<LatLng> readLatLngs(List<LocationQuery.DataBean.TrackListBean> listBean) {
        List<LatLng> points = new ArrayList<LatLng>();
        for (LocationQuery.DataBean.TrackListBean bean : listBean) {
            points.add(new LatLng(bean.getResultLAT(), bean.getResultLON()));
        }
        return points;
    }

    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround(List<LatLng> points) {
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < points.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = mMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(points)
                .useGradient(true)
                .width(18));

//        LatLngBounds bounds = new LatLngBounds(mPoints.get(0), mPoints.get(mPoints.size() - 2));
////        LatLngBounds bounds = new LatLngBounds(mPoints.get(0), mPoints.get(mPoints.size()-1));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    /**
     * 开始移动
     */
    public void startMove(List<LatLng> points) {
        // 构建 轨迹的显示区域
//        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
////        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size()-1));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // 实例 SmoothMoveMarker 对象
        smoothMarker = new SmoothMoveMarker(mMap);
        // 设置 平滑移动的 图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.car));

        // 取轨迹点的第一个点 作为 平滑移动的启动
        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());


        // 设置轨迹点
        smoothMarker.setPoints(subList);
        // 设置平滑移动的总时间  单位  秒
        smoothMarker.setTotalDuration(3);
        smoothMarker.startSmoothMove();
        float zoom = mMap.getCameraPosition().zoom;
        LatLng target = mMap.getCameraPosition().target;
        LatLng latLng = new LatLng(points.get(0).latitude, points.get(0).longitude);
//        LatLng latLng = new LatLng(target.latitude, target.longitude);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, zoom, 0, 0)));

        final int screenWidth = ScreenUtil.getScreenWidth(getContext());
        final int screenHeight = ScreenUtil.getScreenHeight(getContext());
        final int navigationBarHeight = ScreenUtil.getNavigationBarHeight(mActivity);
        final int bottomSize = SizeUtil.dp2px(mActivity, 85);
        final int borderX = screenWidth/6;
        final int borderY = screenHeight/8;
        smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
            @Override
            public void move(final double distance) {

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        float zoom = mMap.getCameraPosition().zoom;
                        Point mPoint = mMap.getProjection().toScreenLocation(smoothMarker.getPosition());
                        if (mPoint != null) {
                            int x = mPoint.x;
                            int y = mPoint.y;
                            if (x<borderX||screenWidth-x<borderX||y<borderY||screenHeight-y<borderY+navigationBarHeight+bottomSize){
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(smoothMarker.getPosition(), zoom, 0, 0)));
                            }
                        }
                    }
                });

            }
        });
    }

    private List<LatLng> getLatLng(double num) {
        LatLng latLng = new LatLng(31.215963, 121.557094);
        if (mPoints != null) {
            latLng = new LatLng(mPoints.get(mPoints.size() - 1).latitude, mPoints.get(mPoints.size() - 1).longitude);
        }
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
//            LatLng latLng = new LatLng(31.215963 + num + (i * 0.01), 121.557094);
            LatLng latLng1 = new LatLng(latLng.latitude + num + (i + 1) * 0.01, latLng.longitude);
            list.add(latLng1);
        }
        return list;
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
}
