package com.cango.palmcartreasure.trailer.map;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.ShowQRActivity;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.customview.TrackDialogFragment;
import com.cango.palmcartreasure.customview.UploadDialogFragment;
import com.cango.palmcartreasure.model.NavigationCar;
import com.cango.palmcartreasure.model.QRCodeBean;
import com.cango.palmcartreasure.model.SelectPhoto;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.model.WareHouse;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.trailer.main.TrailerActivity;
import com.cango.palmcartreasure.trailer.track.TrackActivity;
import com.cango.palmcartreasure.trailer.track.TrackFragment;
import com.cango.palmcartreasure.util.AppUtils;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.FileUtils;
import com.cango.palmcartreasure.util.ScreenUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 拖车导航和送车入库
 */
public class TrailerMapFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    public static final String TYPE = "type";
    public static final String TRAILER_NAV = "trailer_navigation";
    public static final String SEND_CAR_LIBRARY = "send_car_library";
    public static final String HISTORY = "history";
    public static final String TASKLISTBEAN = "taskListBean";
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 101;

    @BindView(R.id.toolbar_trailer_map)
    Toolbar mToolbar;
    @BindView(R.id.ll_get_qrcode)
    LinearLayout llQrCode;
    @BindView(R.id.ll_toolbar_right)
    LinearLayout llRight;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;
//    @BindView(R.id.iv_task_down)
//    ImageView ivDown;
    @BindView(R.id.tv_map_title)
    TextView mTitle;
    @BindView(R.id.rl_fragment)
    RelativeLayout rlFragment;
    @BindView(R.id.rl_map_top)
    RelativeLayout rlMapTop;
    @BindView(R.id.tv_people1)
    TextView tvPeople1;
    @BindView(R.id.tv_people2)
    TextView tvPeople2;
    @BindView(R.id.tv_people3)
    TextView tvPeople3;
    @BindView(R.id.tv_num1)
    TextView tvNum1;
    @BindView(R.id.tv_num2)
    TextView tvNum2;
    @BindView(R.id.tv_num3)
    TextView tvNum3;
    @BindView(R.id.rl_shadow)
    RelativeLayout rlShadow;
    @BindView(R.id.iv_map_location)
    ImageView ivLocation;
    @BindView(R.id.rl_map_bottom)
    RelativeLayout rlMapBottom;
    @BindView(R.id.tv_car_number)
    TextView tvCarNum;
    @BindView(R.id.tv_time_status)
    TextView tvTimeStatus;
    @BindView(R.id.tv_last_address)
    TextView tvLastAddress;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.tv_one_speed)
    TextView tvOne;
    @BindView(R.id.tv_one_point_five_speed)
    TextView tvOneFive;
    @BindView(R.id.tv_two_speed)
    TextView tvTwo;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    private LatLng carGPSLatLng,resultLatLng;
    private String mfirstServerTime;
    private double mLat, mLon;

    @OnClick({R.id.ll_select, R.id.iv_map_nav, R.id.iv_map_location, R.id.tv_one_speed, R.id.tv_one_point_five_speed, R.id.tv_two_speed,
            R.id.btn_map_send,R.id.ll_toolbar_right,R.id.ll_get_qrcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_select:
//                if (ivDown.getVisibility() == View.VISIBLE) {
//                    //说明是送车入库并且已经成功加载数据
//                    ivDown.setImageResource(R.drawable.upward);
//                    rlShadow.setVisibility(View.VISIBLE);
//                    mSelectPW.update();
//                    mSelectPW.showAsDropDown(mToolbar);
//                }
                break;
            //拖车导航
            case R.id.iv_map_nav:
                rlShadow.setVisibility(View.VISIBLE);
                selectMapPW.showAtLocation(rlShadow, Gravity.BOTTOM, 0, 0);
                break;
            //定位
            case R.id.iv_map_location:
                if (mMap != null) {
                    setLocationBluePoint();
                }
                break;
            case R.id.tv_one_speed:
                tvOne.setBackgroundResource(R.drawable.trailer_map_run_speed_bg);
                tvOneFive.setBackgroundColor(Color.WHITE);
                tvTwo.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_one_point_five_speed:
                tvOne.setBackgroundColor(Color.WHITE);
                tvOneFive.setBackgroundResource(R.drawable.trailer_map_run_speed_bg);
                tvTwo.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_two_speed:
                tvOne.setBackgroundColor(Color.WHITE);
                tvOneFive.setBackgroundColor(Color.WHITE);
                tvTwo.setBackgroundResource(R.drawable.trailer_map_run_speed_bg);
                break;
            //送车入库
            case R.id.btn_map_send:
//                if (isSendCarOk) {
////                    ToastUtils.showLong("已经送车入库了！");
//                } else {
//                    showUpLoadDialog();
//                }
                break;
            //轨迹查询（老板和员工都可以看到轨迹查询）
            case R.id.ll_toolbar_right:
                showTrackDialog();
                break;
            //开启一个页面展示二维码
            case R.id.ll_get_qrcode:
                if (CommUtil.checkIsNull(mTaskListBean))
                    return;
                Intent intent = new Intent(mActivity, ShowQRActivity.class);
                QRCodeBean qrCodeBean = new QRCodeBean();
                qrCodeBean.setTCUserID(MtApplication.mSPUtils.getInt(Api.USERID)+"");
                qrCodeBean.setAgencyID(mTaskListBean.getAgencyID()+"");
                qrCodeBean.setApplyCD(mTaskListBean.getApplyCD());
                qrCodeBean.setLAT(mPhoneLat+"");
                qrCodeBean.setLON(mPhoneLon+"");
                qrCodeBean.setDatasource(mTaskListBean.getDatasource()+"");
                intent.putExtra("QRCodeBean",qrCodeBean);
                mActivity.mSwipeBackHelper.forward(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 拖车导航还是拖车入库
     */
    private String mType;
    private UploadDialogFragment upLoadDialog;
    private boolean isSendCarOk;
    private String mTrackDeviceId;
    //压缩后的图片file集合
    private List<File> fileList;
    private TrailerTaskService mService;
    private Subscription subscription1,subscription2,subscription3;
    private Observable<Long> interval;
    //固定时间查询动态轨迹
    private Subscription subscribeInterval;
    private TypeTaskData.DataBean.TaskListBean mTaskListBean;
    //地图相关
    private AMap mMap;
    private TrailerMapActivity mActivity;
    private WareHouse mWareHouse;
    private WareHouse.DataBean currentLibrary;
    private PopupWindow selectMapPW, mSelectPW;
    //定位相关
    private AMapLocationClient mLocationClient;
    private double mPhoneLat, mPhoneLon;
    private String mProvince;
    private boolean isShouldFirstAddData = true;
    private AMapLocationListener mLoactionListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (CommUtil.checkIsNull(aMapLocation)) {
                mPhoneLat = 0;
                mPhoneLon = 0;
            } else {
                if (aMapLocation.getErrorCode() == 0) {
                    if (!CommUtil.checkIsNull(aMapLocation.getProvince())){
                        mProvince=aMapLocation.getProvince();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLatitude())) {
//                        BigDecimal latBD = new BigDecimal(String.valueOf(aMapLocation.getLatitude()));
//                        mPhoneLat = latBD.floatValue();
                        mPhoneLat=aMapLocation.getLatitude();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLongitude())) {
//                        BigDecimal lonBD = new BigDecimal(String.valueOf(aMapLocation.getLongitude()));
//                        mPhoneLon = lonBD.floatValue();
                        mPhoneLon=aMapLocation.getLongitude();
                    }
                    if (mPhoneLat > 0 && mPhoneLon > 0) {
                        if (isShouldFirstAddData) {
                            isShouldFirstAddData = false;
                            onLoadData();
                        }
                    }
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    String dateString = df.format(date);
//                    Logger.d(dateString + ": Lat = " + aMapLocation.getLatitude() + "   Lon = " + aMapLocation.getLongitude() + "   address = " + aMapLocation.getAddress());
                } else {
                    mPhoneLat = 0;
                    mPhoneLon = 0;
                    int errorCode = aMapLocation.getErrorCode();
                    if (errorCode == 12 || errorCode == 13) {
                        ToastUtils.showShort(R.string.put_sim_and_permissions);
                    }
                    Logger.d("errorCode = " + errorCode + " errorInfo = " + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    public static TrailerMapFragment newInstance(String type, TypeTaskData.DataBean.TaskListBean taskListBean) {
        TrailerMapFragment fragment = new TrailerMapFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putParcelable(TASKLISTBEAN, taskListBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLoactionListener);
            mLocationClient.onDestroy();
        }
        if (!CommUtil.checkIsNull(subscription1))
            subscription1.unsubscribe();
        if (!CommUtil.checkIsNull(subscription2))
            subscription2.unsubscribe();
        if (!CommUtil.checkIsNull(subscription3))
            subscription3.unsubscribe();
        if (interval != null && subscribeInterval != null) {
            subscribeInterval.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLocationClient!=null){
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLocationClient!=null){
            mLocationClient.stopLocation();
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_trailer_map;
    }

    @Override
    protected void initView() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (TRAILER_NAV.equals(mType)) {
            mTitle.setText(R.string.traialer_navigation);
            llRight.setVisibility(View.GONE);
            llQrCode.setVisibility(View.GONE);
            rlMapTop.setVisibility(View.GONE);
            llDate.setVisibility(View.GONE);
            rlMapBottom.setVisibility(View.GONE);
            ivLocation.setVisibility(View.GONE);
            rlFragment.setVisibility(View.GONE);
//            ivDown.setVisibility(View.GONE);
        } else if (SEND_CAR_LIBRARY.equals(mType)) {
            mTitle.setText(R.string.send_car_library);
            llRight.setVisibility(View.GONE);
            llQrCode.setVisibility(View.GONE);
            rlMapTop.setVisibility(View.GONE);
            rlMapBottom.setVisibility(View.GONE);
            ivLocation.setVisibility(View.GONE);
            rlFragment.setVisibility(View.GONE);
//            ivDown.setVisibility(View.GONE);
        } else {
            mTitle.setText(R.string.vehicle_localization);
            llRight.setVisibility(View.GONE);
            rlMapTop.setVisibility(View.GONE);
            rlMapBottom.setVisibility(View.GONE);
            ivLocation.setVisibility(View.GONE);
            llDate.setVisibility(View.VISIBLE);
            rlFragment.setVisibility(View.GONE);
//            ivDown.setVisibility(View.GONE);
        }
        selectMapPW = getPopupWindow(mActivity, R.layout.map_nav_bottom);
        openPermissions();
    }

    /**
     * 加载数据
     */
    private void onLoadData() {
        if (TRAILER_NAV.equals(mType)) {
            if (isAdded()) {
                mLoadView.smoothToShow();
            }
            subscription1 = mService.navigationCar(MtApplication.mSPUtils.getInt(Api.USERID), mTaskListBean.getAgencyID(), mTaskListBean.getCaseID(),null,mTaskListBean.getDatasource())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<NavigationCar>() {
                        @Override
                        protected void _onNext(NavigationCar o) {
                            if (isAdded()) {
                                mLoadView.smoothToHide();
                                int code = o.getCode();
                                if (code == 0) {
                                    if (CommUtil.checkIsNull(o.getData())) {
//                                        llSorry.setVisibility(View.VISIBLE);
                                        llNoData.setVisibility(View.VISIBLE);
                                    } else {
                                        double resultLAT = o.getData().getResultLAT();
                                        double resultLON = o.getData().getResultLON();
                                        if (resultLAT == 0 && resultLON == 0) {
//                                            llSorry.setVisibility(View.VISIBLE);
                                            llNoData.setVisibility(View.VISIBLE);
                                            //测试，应该用gone的，但是测试
                                            llRight.setVisibility(View.GONE);
                                        } else {
                                            mTrackDeviceId= o.getData().getResultDeviceId();
                                            if (!TextUtils.isEmpty(mTrackDeviceId)){
                                                llRight.setVisibility(View.VISIBLE);
                                            }
                                            tvCarNum.setText("车牌号码："+mTaskListBean.getCarPlateNO());
                                            tvTimeStatus.setText("最后定位时间："+o.getData().getCACHETIME() + "(" + o.getData().getConnectflag() + ")");
                                            tvLastAddress.setText("最后定位位置："+o.getData().getResultAddress());
                                            rlMapBottom.setVisibility(View.VISIBLE);
                                            ivLocation.setVisibility(View.VISIBLE);
                                            rlFragment.setVisibility(View.VISIBLE);
                                            mLat = resultLAT;
                                            mLon = resultLON;
                                            BigDecimal latBD = new BigDecimal(String.valueOf(mLat));
                                            BigDecimal lonBD = new BigDecimal(String.valueOf(mLon));
                                            carGPSLatLng = new LatLng(latBD.doubleValue(), lonBD.doubleValue());
                                            resultLatLng = new LatLng(latBD.doubleValue(), lonBD.doubleValue());
                                            setUpMapIfNeeded();
                                            if (!TextUtils.isEmpty(o.getData().getServerTime())){
                                                mfirstServerTime = o.getData().getServerTime();
                                                doCarDynamicInfoSearch(mfirstServerTime);
                                            }
                                        }
                                    }
                                } else {
//                                    llSorry.setVisibility(View.VISIBLE);
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (isAdded()) {
                                mLoadView.smoothToHide();
                                llSorry.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } else if (SEND_CAR_LIBRARY.equals(mType)) {
            if (isAdded()) {
                mLoadView.smoothToShow();
            }
            if (mPhoneLat > 0 && mPhoneLon > 0) {
                subscription2 = mService.wareHouse(MtApplication.mSPUtils.getInt(Api.USERID), mTaskListBean.getAgencyID(), mTaskListBean.getCaseID(),
                        mPhoneLat, mPhoneLon,mProvince,mTaskListBean.getDatasource())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<WareHouse>() {
                            @Override
                            protected void _onNext(WareHouse o) {
                                if (isAdded()) {
                                    mLoadView.smoothToHide();
                                    int code = o.getCode();
                                    if (code == 0) {
                                        if (CommUtil.checkIsNull(o.getData())) {
//                                            llSorry.setVisibility(View.VISIBLE);
                                            llNoData.setVisibility(View.VISIBLE);
                                        } else {
                                            if (o.getData().size()==0){
                                                llNoData.setVisibility(View.VISIBLE);
                                            }else {
                                                llQrCode.setVisibility(View.VISIBLE);
                                                mWareHouse = o;
                                                currentLibrary = mWareHouse.getData().get(0);
                                                mSelectPW = getPopupWindow(mActivity, R.layout.libary_point);
//                                                ivDown.setVisibility(View.VISIBLE);
                                                rlMapTop.setVisibility(View.VISIBLE);
                                                rlMapBottom.setVisibility(View.VISIBLE);
                                                ivLocation.setVisibility(View.VISIBLE);
                                                rlFragment.setVisibility(View.VISIBLE);
                                                updateCurrentLibrary();
                                            }
                                        }
                                    } else {
//                                        llSorry.setVisibility(View.VISIBLE);
                                        llNoData.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (isAdded()) {
                                    mLoadView.smoothToHide();
                                    llSorry.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            } else {
                ToastUtils.showShort(R.string.no_get_location);
            }
        }

    }

    private void doCarDynamicInfoSearch(final String firstServerTime){
        if (TextUtils.isEmpty(firstServerTime))
            return;
        //10s一次轮询
        interval = Observable.interval(10, TimeUnit.SECONDS);
        subscribeInterval = interval.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                    mService.navigationCar(MtApplication.mSPUtils.getInt(Api.USERID), mTaskListBean.getAgencyID(), mTaskListBean.getCaseID(),firstServerTime,mTaskListBean.getDatasource())
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    // 需要在主线程执行
                                    if (isAdded()){
                                        mLoadView.smoothToShow();
                                    }
                                }
                            })
                            .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new RxSubscriber<NavigationCar>() {
                                @Override
                                protected void _onNext(NavigationCar o) {
                                    if (isAdded()) {
                                        mLoadView.smoothToHide();
                                        int code = o.getCode();
                                        if (code == 0) {
                                            if (CommUtil.checkIsNull(o.getData())) {
//                                                llNoData.setVisibility(View.VISIBLE);
                                            } else {
                                                double resultLAT = o.getData().getResultLAT();
                                                double resultLON = o.getData().getResultLON();
                                                if (resultLAT == 0 && resultLON == 0) {
//                                                    llNoData.setVisibility(View.VISIBLE);
                                                    llRight.setVisibility(View.GONE);
                                                } else {
                                                    mTrackDeviceId= o.getData().getResultDeviceId();
                                                    if (!TextUtils.isEmpty(mTrackDeviceId)){
                                                        llRight.setVisibility(View.VISIBLE);
                                                    }
                                                    tvCarNum.setText("车牌号码："+mTaskListBean.getCarPlateNO());
                                                    tvTimeStatus.setText("最后定位时间："+o.getData().getCACHETIME() + "(" + o.getData().getConnectflag() + ")");
                                                    tvLastAddress.setText("最后定位位置："+o.getData().getResultAddress());
                                                    rlMapBottom.setVisibility(View.VISIBLE);
                                                    ivLocation.setVisibility(View.VISIBLE);
                                                    rlFragment.setVisibility(View.VISIBLE);
                                                    mLat = resultLAT;
                                                    mLon = resultLON;

                                                    //画轨迹路线了
                                                    doDrawTrack(o.getData());
                                                }
                                            }
                                        } else {
//                                            llNoData.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }

                                @Override
                                protected void _onError() {
                                    if (isAdded()) {
                                        mLoadView.smoothToHide();
//                                        llSorry.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
            }
        });
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
    private SmoothMoveMarker smoothMarker;

    private void doDrawTrack(NavigationCar.DataBean data) {
        List<NavigationCar.DataBean.TrackListBean> trackList = data.getTrackList();
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
    }

    @Override
    protected void initData() {
        mActivity = (TrailerMapActivity) getActivity();
        mType = getArguments().getString(TYPE);
        mTaskListBean = getArguments().getParcelable(TASKLISTBEAN);
        mService = NetManager.getInstance().create(TrailerTaskService.class);
        initLocation();
    }

    /**
     * 展示上传图片dialog
     */
    private void showUpLoadDialog() {
        if (CommUtil.checkIsNull(upLoadDialog)) {
            upLoadDialog = new UploadDialogFragment();
            upLoadDialog.setmListener(new UploadDialogFragment.UploadListener() {
                @Override
                public void upLoadClick(List<SelectPhoto> selectPhotoList,List<String> strings) {
                    upLoadImages(selectPhotoList,strings);
                }
            });
        }
        if (upLoadDialog.isVisible()) {

        } else {
            if (!upLoadDialog.isAdded()){
                upLoadDialog.show(getFragmentManager(), "DotDialog");
            }
        }
    }

    private void upLoadImages(final List<SelectPhoto> selectPhotoList, final List<String> strings) {
        if (isAdded()) {
            mLoadView.smoothToShow();
        }
        for (int i = 0; i < selectPhotoList.size() - 1; i++) {
            File file = new File(selectPhotoList.get(i).getPhotoUrl());
            Luban.get(mActivity)
                    .load(file)                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调

                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        }

                        @Override
                        public void onSuccess(File file) {
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                            if (fileList == null) {
                                fileList = new ArrayList<>();
                            }
                            fileList.add(file);
                            Logger.d(file.getAbsolutePath());
                            if (fileList.size() == (selectPhotoList.size() - 1)) {
                                upLoadImages(strings);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过去出现问题时调用
                            if (isAdded()) {
                                mLoadView.smoothToShow();
                            }
                        }
                    }).launch();    //启动压缩
        }
    }

    private void closeUpLoadDialog() {
        if (CommUtil.checkIsNull(upLoadDialog)) {

        } else {
            if (upLoadDialog.isVisible()) {
                upLoadDialog.dismiss();
            }
        }
    }

    /**
     * 上传图片集合
     * @param strings
     */
    private void upLoadImages(List<String> strings) {
        if (mPhoneLat > 0 && mPhoneLon > 0) {
            isSendCarOk=true;
            RequestBody userId = RequestBody.create(null, MtApplication.mSPUtils.getInt(Api.USERID) + "");
            RequestBody LAT = RequestBody.create(null, mPhoneLat + "");
            RequestBody LON = RequestBody.create(null, mPhoneLon + "");
            RequestBody agencyID = RequestBody.create(null, mTaskListBean.getAgencyID() + "");
            RequestBody caseID = RequestBody.create(null, mTaskListBean.getCaseID() + "");
//            RequestBody realSPID =RequestBody.create(null,currentLibrary.getSpid()+"");
//            JsonArray jsonArray=new JsonArray();
//            for (String str:strings) {
//                jsonArray.add(str);
//            }
//            RequestBody answerList=RequestBody.create(null,jsonArray.toString());
            Map<String, RequestBody> photos = new HashMap<>();
            for (int i = 0; i < fileList.size(); i++) {
                File file = fileList.get(i);
                RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
                if (i == 0) {
                    photos.put("file\"; filename=\"" + file.getName(), photoBody);
                } else {
                    photos.put("file" + i + "\"; filename=\"" + file.getName(), photoBody);
                }
            }

            RequestBody datasource = RequestBody.create(null, mTaskListBean.getDatasource() + "");

            subscription3 = mService.godownSubmit(userId, LAT, LON, agencyID, caseID, photos,datasource)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TaskAbandon>() {
                        @Override
                        protected void _onNext(TaskAbandon o) {
                            if (isAdded()) {
                                //清空pictures
                                deleteImagePictures();
                                mLoadView.smoothToHide();
                                int code = o.getCode();
                                if (!CommUtil.checkIsNull(o.getMsg())) {
                                    ToastUtils.showShort(o.getMsg());
                                }
                                if (code == 0) {
                                    isSendCarOk = false;
                                    //将文件夹清空
                                    deleteImageFileList(fileList);
                                    //通过eventbus来设置删除acivitylist
                                    Intent intent = new Intent(mActivity, TrailerActivity.class);
                                    intent.putExtra("isFromSMS", true);
                                    mActivity.mSwipeBackHelper.forwardAndFinish(intent);
                                } else {
                                    isSendCarOk = false;
                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (isAdded()) {
                                //清空pictures
                                deleteImagePictures();
                                mLoadView.smoothToHide();
                                isSendCarOk = false;
                            }
                        }
                    });
        } else {
            if (isAdded()) {
                mLoadView.smoothToHide();
            }
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    /**
     * 删除所有图片文件
     * @param fileList
     */
    private void deleteImageFileList(List<File> fileList) {
        if (!CommUtil.checkIsNull(fileList)) {
            for (int i = 0; i < fileList.size(); i++) {
                fileList.get(i).delete();
            }
            fileList.clear();
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
            uiSettings.setScaleControlsEnabled(true);
            uiSettings.setCompassEnabled(true);
            uiSettings
                    .setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
//            setLocationBluePoint();
            setCarGPSLocationPoint();
        }
    }

    /**
     * 设置车辆定位点
     */
    private void setCarGPSLocationPoint() {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(carGPSLatLng, 15, 0, 0)));
        mMap.clear();
        MarkerOptions carMarker = new MarkerOptions()
                .position(carGPSLatLng)
//                .title("库点位置")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark));
        if (carGPSLatLng.latitude==0&&carGPSLatLng.longitude==0){
            ToastUtils.showShort("未获取最后一次定位信息");
        }else {
            mMap.addMarker(carMarker);
        }
    }

    /**
     * 设置定位蓝点
     */
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
                    .setRationale(R.string.setting_group_and_storage1)
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

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        if (layoutId == R.layout.map_nav_bottom) {
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
                    String name = "";
                    if (TRAILER_NAV.equals(mType)) {
                        name = "车辆位置";
                    } else if (SEND_CAR_LIBRARY.equals(mType)) {
                        name = "库点位置";
                    } else {

                    }
                    startBaiduMap(mLat, mLon, name);
                }
            });
            tvGaodeMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = "";
                    if (TRAILER_NAV.equals(mType)) {
                        name = "车辆位置";
                    } else if (SEND_CAR_LIBRARY.equals(mType)) {
                        name = "库点位置";
                    } else {

                    }
                    startGaodeMap(mLat, mLon, name);
                }
            });
        } else {
            if (mWareHouse != null) {
                TextView tvFirst = (TextView) popupView.findViewById(R.id.tv_first_library);
                TextView tvSecond = (TextView) popupView.findViewById(R.id.tv_second_library);
                TextView tvThird = (TextView) popupView.findViewById(R.id.tv_thrid_library);

                WareHouse.DataBean dataBean1 = mWareHouse.getData().get(0);
                tvFirst.setText(dataBean1.getWarehouseName() + "（" + dataBean1.getDistance() + "）");
                tvFirst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentLibrary = mWareHouse.getData().get(0);
                        updateCurrentLibrary();
                        popupWindow.dismiss();
                    }
                });
                if (mWareHouse.getData().size()>1){
                    WareHouse.DataBean dataBean2 = mWareHouse.getData().get(1);
                    tvSecond.setText(dataBean2.getWarehouseName() + "（" + dataBean2.getDistance() + "）");
                }else {
                    tvSecond.setVisibility(View.GONE);
                }
                tvSecond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentLibrary = mWareHouse.getData().get(1);
                        updateCurrentLibrary();
                        popupWindow.dismiss();
                    }
                });
                if (mWareHouse.getData().size()>2){
                    WareHouse.DataBean dataBean3 = mWareHouse.getData().get(2);
                    tvThird.setText(dataBean3.getWarehouseName() + "（" + dataBean3.getDistance() + "）");
                }else {
                    tvThird.setVisibility(View.GONE);
                }
                tvThird.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentLibrary = mWareHouse.getData().get(2);
                        updateCurrentLibrary();
                        popupWindow.dismiss();
                    }
                });

            }
        }
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#36000000")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlShadow.setVisibility(View.GONE);
                if (layoutId == R.layout.libary_point) {
//                    ivDown.setImageResource(R.drawable.down);
                }
            }
        });

        popupWindow.update();
        return popupWindow;
    }

    /**
     * 更新界面
     */
    private void updateCurrentLibrary() {
//        mTitle.setText(currentLibrary.getWarehouseName() + "（" + currentLibrary.getDistance() + "）");
        tvCarNum.setText(currentLibrary.getWarehouseName());
        tvTimeStatus.setText(currentLibrary.getWarehouseAdd());
//        tvPeople1.setText(currentLibrary.getContactPerson());
//        tvPeople2.setText(currentLibrary.getContactPerson());
//        tvPeople3.setText(currentLibrary.getContactPerson());
        //Andrioid电催信息、家访信息、库管员信息中我司人员名字隐去，只留姓+XX
        String name = getFirstNameXX(currentLibrary.getContactPerson());
        tvPeople1.setText(getFirstNameXX(name));
        tvPeople2.setText(getFirstNameXX(name));
        tvPeople3.setText(getFirstNameXX(name));
        tvNum1.setText(currentLibrary.getMobilePhone1());
        tvNum2.setText(currentLibrary.getMobilePhone2());
        tvNum3.setText(currentLibrary.getMobilePhone3());

        mLat = currentLibrary.getResultLAT();
        mLon = currentLibrary.getResultLON();
        carGPSLatLng = new LatLng(mLat, mLon);
        if (mMap == null) {
            setUpMapIfNeeded();
        } else {
            setCarGPSLocationPoint();
        }
    }

    /**
     * 展示轨迹日历控件
     */
    private TrackDialogFragment mTrackDialog;
    private void showTrackDialog() {
        if (CommUtil.checkIsNull(mTrackDialog)) {
            //TODO
            int type;
//            if (MTApplication.mSPUtils.getString(Api.ROLE).equals(Api.ADMIN)){
//                type=1;
//            }else {
//                type=0;
//            }
            //TODO test
            type = 0;
            mTrackDialog = TrackDialogFragment.newInstance(type);
            mTrackDialog.setCalendarDilaogListener(new TrackDialogFragment.CalendarDilaogListener() {
                @Override
                public void onCalendarClick(Date date, Date date1) {
                    Logger.d(date.toString());
                    Intent intent = new Intent(mActivity, TrackActivity.class);
//                    mTrackDeviceId = "2170006363";
                    intent.putExtra(TrackFragment.IMEI, mTrackDeviceId);
                    if (!CommUtil.checkIsNull(date) && !CommUtil.checkIsNull(date1)) {
                        intent.putExtra(TrackFragment.START_TIME, date);
                        intent.putExtra(TrackFragment.END_TIME, date1);
                    } else {
                        intent.putExtra(TrackFragment.START_TIME, date);
                        intent.putExtra(TrackFragment.END_TIME, date);
                    }
                    mActivity.mSwipeBackHelper.forward(intent);
                }
            });
        }
        if (mTrackDialog.isVisible()) {

        } else {
            mTrackDialog.show(getFragmentManager(), "CalendarDialog");
        }
    }

    private void closeTrackDialog() {
        if (CommUtil.checkIsNull(mTrackDialog)) {

        } else {
            if (mTrackDialog.isVisible()) {
                mTrackDialog.dismiss();
            }
        }
    }

    private String getFirstNameXX(String name){
        String xx="";
        if (TextUtils.isEmpty(name)){
        }else {
            if (name.length()>0){
                xx=name.substring(0,1)+"XX";
            }else {
            }
        }
        return xx;
    }

    /**
     * 读取坐标点
     *
     * @return
     */
    private List<LatLng> readLatLngs(List<NavigationCar.DataBean.TrackListBean> listBean) {
        List<LatLng> points = new ArrayList<LatLng>();
        for (NavigationCar.DataBean.TrackListBean bean : listBean) {
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

        Polyline mPolyline = mMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
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

    /**
     * 打开百度地图
     * @param lat
     * @param lon
     * @param name
     */
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

    /**
     * 打开高德地图
     * @param lat
     * @param lon
     * @param name
     */
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

    /**
     * 删除图片
     */
    private void deleteImagePictures() {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        boolean deleteDir = FileUtils.deleteDir(storageDir);
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
