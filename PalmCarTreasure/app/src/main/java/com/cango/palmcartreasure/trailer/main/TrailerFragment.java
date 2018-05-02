package com.cango.palmcartreasure.trailer.main;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.LoginService;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.camera.CameraActivity;
import com.cango.palmcartreasure.customview.CalendarDialogFragment;
import com.cango.palmcartreasure.customview.DotTrailerDialogFragment;
import com.cango.palmcartreasure.customview.MessageItemDialogFragment;
import com.cango.palmcartreasure.customview.UpdateFragment;
import com.cango.palmcartreasure.model.CheckPointOkToHomeEvent;
import com.cango.palmcartreasure.model.MessageList;
import com.cango.palmcartreasure.model.PersonalInfo;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.TrailerEvent;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.trailer.checkorder.CheckOrderActivity;
import com.cango.palmcartreasure.trailer.complete.TrailerCompleteActivity;
import com.cango.palmcartreasure.trailer.complete.TrailerCompleteFragment;
import com.cango.palmcartreasure.trailer.map.TrailerMapActivity;
import com.cango.palmcartreasure.trailer.map.TrailerMapFragment;
import com.cango.palmcartreasure.trailer.mine.MineActivity;
import com.cango.palmcartreasure.trailer.task.TrailerTasksActivity;
import com.cango.palmcartreasure.trailer.task.TrailerTasksFragment;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailActivity;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailFragment;
import com.cango.palmcartreasure.update.ProgressListener;
import com.cango.palmcartreasure.update.UpdatePresenter;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.rd.Orientation;
import com.rd.PageIndicatorView;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 首页view
 */
public class TrailerFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 140;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @BindView(R.id.toolbar_trailer)
    Toolbar mToolbarTrailer;
    @BindView(R.id.vp_trailer)
    ViewPager mViewPager;
    @BindView(R.id.iv_vp)
    ImageView ivVp;
    @BindView(R.id.ll_for_task)
    LinearLayout llForTask;
    @BindView(R.id.ll_new_task)
    LinearLayout llNewTask;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView mPageIndicatorView;
    @BindView(R.id.ll_trailer_center)
    LinearLayout llCenter;
    @BindView(R.id.ll_start_task)
    LinearLayout llStartTask;
    @BindView(R.id.ll_trailer_navigation)
    LinearLayout llTrailerNav;
    @BindView(R.id.ll_dot_trailer)
    LinearLayout llDotTrailer;
    @BindView(R.id.ll_send_car_library)
    LinearLayout llSendCarLibrary;
    @BindView(R.id.rl_top_no_data)
    RelativeLayout rlNoData;
    @BindView(R.id.iv_task)
    ImageView ivTask;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    private Badge badge1, badge2;

    @OnClick({R.id.iv_start_task, R.id.ll_tasks, R.id.ll_mine, R.id.ll_trailer_navigation, R.id.ll_dot_trailer, R.id.ll_send_car_library,
            R.id.ll_for_task, R.id.ll_query, R.id.ll_new_task, R.id.ll_complete_task})
    public void onClick(View view) {
        switch (view.getId()) {
            //开始任务
            case R.id.iv_start_task:
                if (!CommUtil.checkIsNull(currentBean)) {
                    if (currentBean.getIsStart().equals("T"))
                        showCalendarDialog();
                    else {
                        ToastUtils.showShort("不能开始任务");
                    }
                }
                break;
            case R.id.ll_mine:
                mActivity.mSwipeBackHelper.forward(MineActivity.class);
                break;
            case R.id.ll_trailer_navigation:
                Intent trailNavIntent = new Intent(mActivity, TrailerMapActivity.class);
                trailNavIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.TRAILER_NAV);
                trailNavIntent.putExtra(TrailerMapFragment.TASKLISTBEAN, currentBean);
                mActivity.mSwipeBackHelper.forward(trailNavIntent);
                break;
            //拖车打点如果可以大点才可以,因为进行开始任务、拖车大点、拖车入库、都会重新请求数据，并且把当前页数保存
            case R.id.ll_dot_trailer:
//                openCamera();
                if (!CommUtil.checkIsNull(currentBean)) {
                    if (currentBean.getIsCheckPoint().equals("T")){
                        pointLat = mLat;
                        pointLon = mLon;
                        pointProvince = mProvince;
//                        mCurrentPhotoPath = data.getStringExtra("path");
                        Intent completeIntent = new Intent(mActivity, CheckOrderActivity.class);
                        completeIntent.putExtra(TrailerCompleteFragment.TYPE, "0");
                        completeIntent.putExtra(TrailerCompleteFragment.IMG_PATH, mCurrentPhotoPath);
                        completeIntent.putExtra(TrailerCompleteFragment.LAT, pointLat);
                        completeIntent.putExtra(TrailerCompleteFragment.LON, pointLon);
                        completeIntent.putExtra(TrailerCompleteFragment.PROVINCE, pointProvince);
                        completeIntent.putExtra(TrailerCompleteFragment.TASKLISTBEAN, currentBean);
                        mActivity.mSwipeBackHelper.forward(completeIntent);
//                        openCamera();
//                        startActivity(new Intent(mActivity,CameraActivity.class));
                    } else {
                        ToastUtils.showShort("请先开始任务");
                    }
                }
                break;
            case R.id.ll_send_car_library:
                if (!CommUtil.checkIsNull(currentBean)) {
                    if (currentBean.getIsDone().equals("T")) {
                        Intent sendCarLibraryIntent = new Intent(mActivity, TrailerMapActivity.class);
                        sendCarLibraryIntent.putExtra(TrailerMapFragment.TYPE, TrailerMapFragment.SEND_CAR_LIBRARY);
                        sendCarLibraryIntent.putExtra(TrailerMapFragment.TASKLISTBEAN, currentBean);
                        mActivity.mSwipeBackHelper.forward(sendCarLibraryIntent);
                    } else {
                        ToastUtils.showShort("请先拖车成功");
                    }
                }
                break;
            case R.id.ll_for_task:
                Intent tasksIntent = new Intent(mActivity, TrailerTasksActivity.class);
                tasksIntent.putExtra("type", TrailerTasksFragment.INPROGRESS_TASK);
                mActivity.mSwipeBackHelper.forward(tasksIntent);
                break;
            case R.id.ll_new_task:
                Intent taskNewIntent = new Intent(mActivity, TrailerTasksActivity.class);
                taskNewIntent.putExtra("type", TrailerTasksFragment.NEW_TASK);
                mActivity.mSwipeBackHelper.forward(taskNewIntent);
                break;
            case R.id.ll_complete_task:
                Intent taskCompleteIntent = new Intent(mActivity, TrailerTasksActivity.class);
                taskCompleteIntent.putExtra("type", TrailerTasksFragment.DONE_TASK);
                mActivity.mSwipeBackHelper.forward(taskCompleteIntent);
                break;
            case R.id.ll_query:
                Intent queryTasksIntent = new Intent(mActivity, TrailerTasksActivity.class);
                queryTasksIntent.putExtra("type", TrailerTasksFragment.SEARCH);
                mActivity.mSwipeBackHelper.forward(queryTasksIntent);
                break;
            default:
                break;
        }
    }

    private boolean isFromSMS;
    private String mCurrentPhotoPath;

    /**
     * 针对于是进行中的任务和新分配任务
     */
    private int vpType = -1;//0-进行中 1-新分配 2-没有数据
    private int isHavePointDot;
    private TrailerActivity mActivity;
    private TrailerTaskService mService;
    private Subscription subscription1,subscription2;
    private List<View> viewList = new ArrayList<>();
    private MainPageOtherAdapter mPageAdapter;
    private List<TypeTaskData.DataBean.TaskListBean> taskListBeanList = new ArrayList<>();
    private TypeTaskData.DataBean.TaskListBean currentBean;
    private int currentPosition = 0;
    private boolean isShowPosition;
    private CalendarDialogFragment mCalendarDialog;
    private DotTrailerDialogFragment mDotDialog;
    //定位相关
    private AMapLocationClient mLocationClient;
    private boolean isShouldFirstAddData = true;
    private boolean isShouldSizeUpdate = true;
    private double mLat, mLon;
    private String mProvince;
    private double pointLat, pointLon;
    private String pointProvince;
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
//                        BigDecimal latBD = new BigDecimal(String.valueOf(aMapLocation.getLatitude()));
//                        mLat = latBD.floatValue();
                        mLat = aMapLocation.getLatitude();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLongitude())) {
//                        BigDecimal lonBD = new BigDecimal(String.valueOf(aMapLocation.getLongitude()));
//                        mLon = lonBD.floatValue();
                        mLon = aMapLocation.getLongitude();
                    }
                    if (mLat > 0 && mLon > 0) {
                        if (isShouldFirstAddData) {
                            isShouldFirstAddData = false;
                            addData();
                            if (isShouldSizeUpdate){
                                isShouldSizeUpdate = false;
                                //获取是否更新apk仅仅是第一次开启页面才显示
                                NetManager.getInstance().create(LoginService.class).getPersonalInfo(MtApplication.mSPUtils.getInt(Api.USERID))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new RxSubscriber<PersonalInfo>() {
                                            @Override
                                            protected void _onNext(PersonalInfo o) {
                                                if (o!=null){
                                                    int code = o.getCode();
                                                    if (code == Api.APP_UPDATE){
                                                        updateAPK();
                                                    }
                                                }
                                            }

                                            @Override
                                            protected void _onError() {

                                            }
                                        });
                            }
                        }
                    }
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    String dateString = df.format(date);
//                    Logger.d(dateString + ": Lat = " + aMapLocation.getLatitude() + "   Lon = " + aMapLocation.getLongitude() + "   address = " + aMapLocation.getAddress());
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

    public static TrailerFragment newInstance() {
        TrailerFragment fragment = new TrailerFragment();
        return fragment;
    }

    public static TrailerFragment newInstance(boolean isFromSMS) {
        TrailerFragment fragment = new TrailerFragment();
        Bundle args = new Bundle();
        args.putBoolean("isFromSMS", isFromSMS);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_trailer;
    }

    @Override
    protected void initView() {
        ivTask.setImageResource(R.drawable.task_on);
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbarTrailer.setLayoutParams(layoutParams);
            mToolbarTrailer.setPadding(0, statusBarHeight, 0, 0);
        }
        mPageAdapter = new MainPageOtherAdapter();
        mPageAdapter.setViewList(viewList);
        mViewPager.setAdapter(mPageAdapter);
        mPageIndicatorView.setViewPager(mViewPager);
        mPageIndicatorView.setOrientation(Orientation.HORIZONTAL);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //得到当前的viewpager position，等首页刷新将其设置到当前的curren position
                currentPosition = position;
                currentBean = taskListBeanList.get(position);
                if (vpType == 0) {
                    llStartTask.setVisibility(View.GONE);
                } else if (vpType == 1) {
                    llStartTask.setVisibility(View.VISIBLE);
                } else {
                }

                //首页只有进行中的任务，所以如果不能大点了，那么一定是大点完成了
                if ("F".equals(currentBean.getIsCheckPoint())) {
                    llDotTrailer.setVisibility(View.GONE);
                } else {
                    llDotTrailer.setVisibility(View.VISIBLE);
                }

                ObjectAnimator.ofFloat(llCenter, "rotationX", 0.0F, 360F)
                        .setDuration(500)
                        .start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mService = NetManager.getInstance().create(TrailerTaskService.class);
        mLoadView.smoothToShow();
        llCenter.setVisibility(View.INVISIBLE);
        ivVp.setVisibility(View.INVISIBLE);
        mViewPager.setVisibility(View.INVISIBLE);

//        openPermissions();
//        showUpdateDialog();
    }

//    private UpdateFragment mUpdateDialog;

    /**
     * 显示更新dialog
     */
    private void showUpdateDialog() {
//        if (CommUtil.checkIsNull(mUpdateDialog)) {
//            mUpdateDialog = new UpdateFragment();
//        }
//        if (mUpdateDialog.isVisible()) {
//
//        } else {
//            mUpdateDialog.show(getFragmentManager(), "UpdateDialog");
//        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
    private void openPermissions() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            mLocationClient.startLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.d("onPermissionsGranted");
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
//    }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            pointLat = mLat;
            pointLon = mLon;
            pointProvince = mProvince;
            mCurrentPhotoPath = data.getStringExtra("path");
            Intent completeIntent = new Intent(mActivity, TrailerCompleteActivity.class);
            completeIntent.putExtra(TrailerCompleteFragment.TYPE, "0");
            completeIntent.putExtra(TrailerCompleteFragment.IMG_PATH, mCurrentPhotoPath);
            completeIntent.putExtra(TrailerCompleteFragment.LAT, pointLat);
            completeIntent.putExtra(TrailerCompleteFragment.LON, pointLon);
            completeIntent.putExtra(TrailerCompleteFragment.PROVINCE, pointProvince);
            completeIntent.putExtra(TrailerCompleteFragment.TASKLISTBEAN, currentBean);
            mActivity.mSwipeBackHelper.forward(completeIntent);
//            showDotDialog();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            deleteImageFile();
        }
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            openPermissions();
        }
    }

    private void addData() {
        if (mLat > 0 && mLon > 0) {
            subscription1 = mService.getTaskInprogressList(MtApplication.mSPUtils.getInt(Api.USERID), mLat,
                    mLon, 1, 5)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TypeTaskData>() {
                        @Override
                        protected void _onNext(TypeTaskData o) {
                            if (isAdded()) {
                                int code = o.getCode();
                                if (code == 0) {
                                    if (CommUtil.checkIsNull(o.getData())) {
//                                        doNewTask();
                                        mLoadView.smoothToHide();
                                        llCenter.setVisibility(View.INVISIBLE);
                                        ivVp.setVisibility(View.VISIBLE);
                                        mViewPager.setVisibility(View.INVISIBLE);
                                        rlNoData.setVisibility(View.VISIBLE);
                                    } else {

                                        badge1 = null;
                                        badge2 = null;
                                        if (o.getData().getTotalCount()>0){
                                            //设置首页小红点
                                            badge1 = new QBadgeView(getActivity())
                                                    .setBadgeBackgroundColor(getResources().getColor(R.color.mtffe0b9))
                                                    .setShowShadow(false)
                                                    .setBadgeTextColor(getResources().getColor(R.color.mt757575))
                                                    .bindTarget(llForTask)
                                                    .setBadgeNumber(o.getData().getTotalCount())
                                                    .setBadgeGravity(Gravity.TOP | Gravity.END)
                                                    .setGravityOffset(SizeUtil.px2dp(getContext(), 28), true);
                                        }
                                        if (o.getData().getNewTaskCount()>0){
                                            badge2 = new QBadgeView(getActivity())
                                                    .setBadgeBackgroundColor(Color.WHITE)
                                                    .setShowShadow(false)
                                                    .setBadgeTextColor(getResources().getColor(R.color.mt757575))
                                                    .bindTarget(llNewTask)
                                                    .setBadgeNumber(o.getData().getNewTaskCount())
                                                    .setBadgeGravity(Gravity.TOP | Gravity.END)
                                                    .setGravityOffset(SizeUtil.px2dp(getContext(), 28), true);
                                        }

                                        if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0) {
                                            mLoadView.smoothToHide();
                                            updateView(0, o);

                                        } else {
//                                            doNewTask();
                                            mLoadView.smoothToHide();
                                            llCenter.setVisibility(View.INVISIBLE);
                                            ivVp.setVisibility(View.VISIBLE);
                                            mViewPager.setVisibility(View.INVISIBLE);
                                            rlNoData.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
//                                    if (code == Api.APP_UPDATE){
//                                        updateAPK();
//                                    }
                                    mLoadView.smoothToHide();
                                    llCenter.setVisibility(View.INVISIBLE);
                                    ivVp.setVisibility(View.VISIBLE);
                                    mViewPager.setVisibility(View.INVISIBLE);
                                    rlNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (isAdded()) {
                                mLoadView.smoothToHide();
                                llCenter.setVisibility(View.INVISIBLE);
                                ivVp.setVisibility(View.VISIBLE);
                                mViewPager.setVisibility(View.INVISIBLE);
                                rlNoData.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    @Override
    protected void initData() {
        mActivity = (TrailerActivity) getActivity();
        if (!CommUtil.checkIsNull(getArguments())) {
            isFromSMS = getArguments().getBoolean("isFromSMS");
        }
        initLocation();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFromSMS) {
            MtApplication.clearExceptLastActivitys();
            isFromSMS = false;

            mLoadView.smoothToShow();

            viewList.clear();
            taskListBeanList.clear();
            currentBean = null;
            vpType = -1;
            if (!CommUtil.checkIsNull(badge1)) {
                badge1.hide(true);
            }
            if (!CommUtil.checkIsNull(badge2)) {
                badge2.hide(true);
            }
            mPageAdapter.notifyDataSetChanged();

            isShowPosition = true;
            openPermissions();
        }

//        if (!isShouldFirstAddData)
        //不开心的加入了刷新
        isShouldFirstAddData = true;
        mLoadView.smoothToShow();
        isShowPosition = true;

        viewList.clear();
        taskListBeanList.clear();
        currentBean = null;
        vpType = -1;
        if (!CommUtil.checkIsNull(badge1)) {
            badge1.hide(true);
        }
        if (!CommUtil.checkIsNull(badge2)) {
            badge2.hide(true);
        }
        mPageAdapter.notifyDataSetChanged();
        openPermissions();
    }

    //接受推送后的首页刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPushTrailerEvent(TrailerEvent trailerEvent) {
        mLoadView.smoothToShow();
        isShowPosition = true;

        viewList.clear();
        taskListBeanList.clear();
        currentBean = null;
        vpType = -1;
        if (!CommUtil.checkIsNull(badge1)) {
            badge1.hide(true);
        }
        if (!CommUtil.checkIsNull(badge2)) {
            badge2.hide(true);
        }
        mPageAdapter.notifyDataSetChanged();
        addData();
    }

    //拖车大点成功,刷新数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckPointOkToHomeEvent(CheckPointOkToHomeEvent event) {
        mLoadView.smoothToShow();
        isShowPosition = true;

        viewList.clear();
        taskListBeanList.clear();
        currentBean = null;
        vpType = -1;
        if (!CommUtil.checkIsNull(badge1)) {
            badge1.hide(true);
        }
        if (!CommUtil.checkIsNull(badge2)) {
            badge2.hide(true);
        }
        mPageAdapter.notifyDataSetChanged();
        addData();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLoactionListener);
            mLocationClient.onDestroy();
        }
        if (!CommUtil.checkIsNull(subscription1))
            subscription1.unsubscribe();
        if (!CommUtil.checkIsNull(subscription2))
            subscription2.unsubscribe();
    }

    private void doNewTask() {
        mService.getNewTaskList(MtApplication.mSPUtils.getInt(Api.USERID), mLat,
                mLon, 1, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TypeTaskData>() {
                    @Override
                    protected void _onNext(TypeTaskData o) {
                        if (isAdded()) {
                            mLoadView.smoothToHide();
                            int code = o.getCode();
                            if (code == 0) {
                                if (CommUtil.checkIsNull(o.getData())) {
                                    llCenter.setVisibility(View.INVISIBLE);
                                    ivVp.setVisibility(View.VISIBLE);
                                    mViewPager.setVisibility(View.INVISIBLE);
                                    rlNoData.setVisibility(View.VISIBLE);
                                } else {
                                    if (!CommUtil.checkIsNull(o.getData().getTaskList()) && o.getData().getTaskList().size() > 0) {
                                        mLoadView.smoothToHide();
                                        updateView(1, o);
                                    } else {
                                        llCenter.setVisibility(View.INVISIBLE);
                                        ivVp.setVisibility(View.VISIBLE);
                                        mViewPager.setVisibility(View.INVISIBLE);
                                        rlNoData.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                llCenter.setVisibility(View.INVISIBLE);
                                ivVp.setVisibility(View.VISIBLE);
                                mViewPager.setVisibility(View.INVISIBLE);
                                rlNoData.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (isAdded()) {
                            mLoadView.smoothToHide();
                            llCenter.setVisibility(View.INVISIBLE);
                            ivVp.setVisibility(View.VISIBLE);
                            mViewPager.setVisibility(View.INVISIBLE);
                            rlNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * 更新首页的view
     * @param type
     * @param o
     */
    private void updateView(int type, TypeTaskData o) {
        vpType = type;
        llCenter.setVisibility(View.VISIBLE);
        ivVp.setVisibility(View.INVISIBLE);
        rlNoData.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);

        for (final TypeTaskData.DataBean.TaskListBean bean : o.getData().getTaskList()) {
            taskListBeanList.add(bean);
            View inflate = mActivity.getLayoutInflater().inflate(R.layout.trailer_vp_item, null);
            AutoRelativeLayout arlContain = (AutoRelativeLayout) inflate.findViewById(R.id.arl_contain);
            TextView tvPlate = (TextView) inflate.findViewById(R.id.tv_vp_item_plate);
            TextView tvName = (TextView) inflate.findViewById(R.id.tv_vp_item_name);
            TextView tvMents = (TextView) inflate.findViewById(R.id.tv_vp_item_monthpayments);
            TextView tvDistance = (TextView) inflate.findViewById(R.id.tv_distance);
            TextView tvTag = (TextView) inflate.findViewById(R.id.tv_vp_item_tag);
            TextView tvStatus = (TextView) inflate.findViewById(R.id.tv_status);
            ImageView ivCarLogo = (ImageView) inflate.findViewById(R.id.iv_car_logo);
            TextView tvCarName = (TextView) inflate.findViewById(R.id.tv_car_name);
            tvPlate.setText(bean.getCustomerName());
            tvName.setText(bean.getCarPlateNO());
            tvMents.setText(bean.getAddress());
            tvDistance.setText(bean.getDistance());
            tvTag.setText(bean.getShortName());
            tvStatus.setText(bean.getFlowStauts());
            arlContain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra(TaskDetailFragment.TASKLISTBEAN, bean);
                    mActivity.mSwipeBackHelper.forward(intent);
                }
            });
            String url = bean.getUrl();
            if (!TextUtils.isEmpty(url)) {
                Glide.with(this).load(url).into(ivCarLogo);
            }
            tvCarName.setText(bean.getCarBrand());
            viewList.add(inflate);
        }
        currentBean = taskListBeanList.get(0);
        mPageAdapter.notifyDataSetChanged();
        //首页只有进行中的任务，所以如果不能大点了，那么一定是大点完成了
        if ("F".equals(currentBean.getIsCheckPoint())) {
            llDotTrailer.setVisibility(View.GONE);
        } else {
            llDotTrailer.setVisibility(View.VISIBLE);
        }
        if (isShowPosition) {
            mViewPager.setCurrentItem(currentPosition);
            if (currentPosition < taskListBeanList.size()) {
                currentBean = taskListBeanList.get(currentPosition);
                if ("F".equals(taskListBeanList.get(currentPosition).getIsCheckPoint())) {
                    llDotTrailer.setVisibility(View.GONE);
                } else {
                    llDotTrailer.setVisibility(View.VISIBLE);
                }
            }
            isShowPosition = false;
        }
        if (vpType == 0) {
            llStartTask.setVisibility(View.GONE);
        } else if (vpType == 1) {
            llStartTask.setVisibility(View.VISIBLE);
        } else {
        }

    }

    private void showDotDialog() {
        if (CommUtil.checkIsNull(mDotDialog)) {
            mDotDialog = new DotTrailerDialogFragment();
            mDotDialog.setmListener(new DotTrailerDialogFragment.OnStatusListener() {
                @Override
                public void onStatusClick(final int type) {
                    File file = new File(mCurrentPhotoPath);
                    Luban.get(mActivity)
                            .load(file)                     //传人要压缩的图片
                            .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                            .setCompressListener(new OnCompressListener() { //设置回调

                                @Override
                                public void onStart() {
                                    // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                    if (isAdded()) {
                                        mLoadView.smoothToShow();
                                    }
                                }

                                @Override
                                public void onSuccess(File file) {
                                    // TODO 压缩成功后调用，返回压缩后的图片文件
                                    upLoadImage(type, file);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过去出现问题时调用
                                    if (isAdded()) {
                                        mLoadView.smoothToHide();
                                    }
                                }
                            }).launch();    //启动压缩
                }
            });
        }
        if (mDotDialog.isVisible()) {

        } else {
            mDotDialog.show(getFragmentManager(), "DotDialog");
        }
    }

    private void upLoadImage(int type, File file) {
        if (mLat > 0 && mLon > 0 && pointLat > 0 && pointLon > 0) {
            String notifyCust = type == 0 ? "1" : "0";
            RequestBody userId = RequestBody.create(null, MtApplication.mSPUtils.getInt(Api.USERID) + "");
            RequestBody LAT = RequestBody.create(null, pointLat + "");
            RequestBody LON = RequestBody.create(null, pointLon + "");
            RequestBody agencyID = RequestBody.create(null, currentBean.getAgencyID() + "");
            RequestBody caseID = RequestBody.create(null, currentBean.getCaseID() + "");
            RequestBody notifyCustImm = RequestBody.create(null, notifyCust);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), photoBody);
//            mService.checkPiontSubmit(userId, LAT, LON, agencyID, caseID, notifyCustImm, part)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new RxSubscriber<TaskAbandon>() {
//                        @Override
//                        protected void _onNext(TaskAbandon o) {
//                            if (isAdded()) {
//                                deleteImageFile();
//                                mLoadView.smoothToHide();
//                            }
//                            //拖车大点是否成功，如果成功的话，从新刷新vp
//                            int code = o.getCode();
//                            if (!CommUtil.checkIsNull(o.getMsg())) {
//                                ToastUtils.showShort(o.getMsg());
//                            }
//                            if (code == 0) {
//
//                                isShowPosition = true;
//
//                                viewList.clear();
//                                taskListBeanList.clear();
//                                currentBean = null;
//                                vpType = -1;
//                                if (!CommUtil.checkIsNull(badge1)) {
//                                    badge1.hide(true);
//                                }
//                                if (!CommUtil.checkIsNull(badge2)) {
//                                    badge2.hide(true);
//                                }
//                                mPageAdapter.notifyDataSetChanged();
//                                addData();
//                            } else {
//
//                            }
//                        }
//
//                        @Override
//                        protected void _onError() {
//                            if (isAdded()) {
//                                deleteImageFile();
//                                mLoadView.smoothToHide();
//                            }
//                        }
//                    });
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    private void closeDotDialog() {
        if (CommUtil.checkIsNull(mDotDialog)) {

        } else {
            if (mDotDialog.isVisible()) {
                mDotDialog.dismiss();
            }
        }
    }

    /**
     * 显示日历空间dialog
     */
    private void showCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarDialog)) {
            mCalendarDialog = new CalendarDialogFragment();
            mCalendarDialog.setCalendarDilaogListener(new CalendarDialogFragment.CalendarDilaogListener() {
                @Override
                public void onCalendarClick(Date date) {

                    if (mLat > 0 && mLon > 0) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateString = formatter.format(date);
                        if (isAdded()) {
                            mLoadView.smoothToShow();
                        }
                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
                        objectMap.put("agencyID", currentBean.getAgencyID());
                        objectMap.put("caseID", currentBean.getCaseID());
                        objectMap.put("planDonetime", dateString);
                        objectMap.put("LAT", mLat);
                        objectMap.put("LON", mLon);
                        objectMap.put("datasource",currentBean.getDatasource());
                        subscription2 = mService.startTaskSubmit(objectMap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new RxSubscriber<TaskAbandon>() {
                                    @Override
                                    protected void _onNext(TaskAbandon o) {
                                        if (isAdded()) {
                                            mLoadView.smoothToHide();
                                            int code = o.getCode();
                                            if (code == 0) {
                                                ToastUtils.showShort("操作成功");
                                                //刷新viewpager
//                                                mFragments.clear();
                                                viewList.clear();
                                                taskListBeanList.clear();
                                                currentBean = null;
                                                vpType = -1;
                                                if (!CommUtil.checkIsNull(badge1)) {
                                                    badge1.hide(true);
                                                }
                                                if (!CommUtil.checkIsNull(badge2)) {
                                                    badge2.hide(true);
                                                }
                                                mPageAdapter.notifyDataSetChanged();
                                                addData();
                                            } else {
                                                ToastUtils.showShort("操作失败");
                                            }
                                        }
                                    }

                                    @Override
                                    protected void _onError() {
                                        if (isAdded()) {
                                            mLoadView.smoothToHide();
                                            ToastUtils.showShort("操作失败");
                                        }
                                    }
                                });
                    } else {
                        ToastUtils.showShort(R.string.no_get_location);
                    }
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
     * 打开自定义相机
     */
    private void openCamera() {
        boolean hasSystemFeature = mActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (hasSystemFeature) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (photoFile != null) {
//                    Uri photoURI;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        photoURI = FileProvider.getUriForFile(mActivity, "com.cango.palmcartreasure.fileprovider", photoFile);
//
//                    } else {
//                        photoURI = Uri.fromFile(photoFile);
//                    }
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
                Intent cameraIntent = new Intent(mActivity, CameraActivity.class);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean deleteImageFile() {
        if (mCurrentPhotoPath != null) {
            File emptyFile = new File(mCurrentPhotoPath);
            if (emptyFile.exists())
                return emptyFile.delete();
        }
        return false;
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

    private NumberFormat nt;
    private Subscription subscriptionUP;
    private boolean isDownloaded;
    //跟新apk
    private void updateAPK() {
        nt = NumberFormat.getPercentInstance();
        String parentDir = MtApplication.getmContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        final String apkPath = parentDir + File.separator + "kingkong.apk";
        final UpdatePresenter presenter = new UpdatePresenter();
        presenter.start();
        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
        ///dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        dialog.setProgress(R.mipmap.ic_launcher);
//        dialog.setSecondaryProgress(R.mipmap.image002);//设置二级进度条的背景
        progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setIcon(R.mipmap.ic_launcher);//
        // 设置提示的title的图标，默认是没有的，需注意的是如果没有设置title的话只设置Icon是不会显示图标的
        progressDialog.setTitle("更新");
        progressDialog.setMax(100);
        // dismiss监听
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        // 监听Key事件被传递给dialog
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return false;
            }
        });
        // 监听cancel事件
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
//        设置可点击的按钮，最多有三个(默认情况下)
//        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if ("点击安装".equals(progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).getText())) {
//                            installApk(apkPath);
//                        }
//                    }
//                });
        progressDialog.setMessage("正在下载......");
        progressDialog.show();
//        progressDialog.incrementProgressBy(1);
//        progressDialog.incrementSecondaryProgressBy(15);//二级进度条更新方式

        presenter.downLoadAPK(apkPath, new ProgressListener() {
            @Override
            public void update(final long bytesRead, final long contentLength, boolean done) {
                if(bytesRead < contentLength){//done为true的进度会进来两次，这里判断了只执行第一次done
                    isDownloaded = false;
                }
                if (done && !isDownloaded) {
                    isDownloaded = true;
                    //这里下载完成以后延迟1秒再去安装下载好的apk
                    //防止安装apk的时候下载的包还没有准备好会出现解析包错误
                    Observable.timer(1, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    installApk(apkPath);
                                }
                            });
//                    progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("点击安装");
                } else {
                    Observable<Long> updateObservable = Observable.create(new Observable.OnSubscribe<Long>() {
                        @Override
                        public void call(Subscriber<? super Long> subscriber) {
                            subscriber.onNext(Long.valueOf(bytesRead));
                            subscriber.onCompleted();
                        }
                    });
                    subscriptionUP = updateObservable
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    //设置百分数精确度2即保留两位小数
                                    nt.setMinimumFractionDigits(2);
                                    float baifen = (float) aLong / (float) contentLength * 100;
//                                    tvProgress.setText((int) baifen + " %");
//                                    progressBar.setProgress((int) baifen);
                                    progressDialog.setProgress((int) baifen);
                                }
                            });
                }
            }
        });
    }

    public void installApk(String apkPath) {
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(getActivity(), "com.cango.palmcartreasure.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
