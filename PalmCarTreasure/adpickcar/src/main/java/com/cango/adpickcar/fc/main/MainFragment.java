package com.cango.adpickcar.fc.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseAdapter;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.baseAdapter.OnLoadMoreListener;
import com.cango.adpickcar.customview.CalendarDialogFragment;
import com.cango.adpickcar.fc.detail.WebDetailActivity;
import com.cango.adpickcar.fc.download.DownloadActivity;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.model.EventModel.RefreshFCMainEvent;
import com.cango.adpickcar.model.EventModel.RefreshMainEvent;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.update.ProgressListener;
import com.cango.adpickcar.update.UpdatePresenter;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.SnackbarUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dell on 2017/12/11.
 */

public class MainFragment extends BaseFragment implements MainContract.View, SwipeRefreshLayout.OnRefreshListener, EasyPermissions.PermissionCallbacks{
    private static final int PAGE_SIZE = 10;
    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 10086;//判断获取位置信息权限
    private static final int REQUEST_GROUP_AND_STORAGE_GROUP = 10087;//判断更新apk权限
    private MainActivity mActivity;

    @BindView(R.id.tv_mine_nickName)
    TextView tvUserMobile;
    @BindView(R.id.layout_fc_main)
    RelativeLayout mMainLayout;
    @BindView(R.id.fc_toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.tv_button_home)
    TextView tvHome;
    @BindView(R.id.tv_button_mine)
    TextView tvMine;
    @BindView(R.id.layout_main)
    RelativeLayout layoutMain;
    @BindView(R.id.layout_mine)
    LinearLayout layoutMine;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.fc_srl_tasks)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fc_recyclerview_tasks)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;


    @OnClick({R.id.layout_about, R.id.layout_password, R.id.layout_download, R.id.layout_signout, R.id.tv_button_home, R.id.tv_button_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_about:
                startActivity(new Intent(mActivity, com.cango.adpickcar.fc.about.DocActivity.class));
                break;
            case R.id.layout_password:
                startActivity(new Intent(mActivity, com.cango.adpickcar.fc.resetps.ResetPSActivity.class));
                break;
            case R.id.layout_download:
                startActivity(new Intent(mActivity, DownloadActivity.class));
                break;
            case R.id.layout_signout:
                if (isDoLogout) {
                    isDoLogout = false;
                    mPresenter.logout(true, ADApplication.mSPUtils.getString(Api.USERID));
                }
                break;
//            case R.id.layout_tasks_search:
//                mActivity.startActivity(new Intent(mActivity,SchedulingActivity.class));
//                break;
//            case R.id.layout_home_tasks:
//                mActivity.startActivity(new Intent(mActivity,TasksActivity.class));
//                break;
            case R.id.tv_button_home://首页按钮点击切换到首页页面
                layoutMain.setVisibility(View.VISIBLE);
                layoutMine.setVisibility(View.GONE);
                tvHome.setTextColor(mActivity.getResources().getColor(R.color.fc_text_main_blue));
                tvHome.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_main_on),null,null);
                tvMine.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_my_off),null,null);
                tvMine.setTextColor(mActivity.getResources().getColor(R.color.ad888888));
                tvTitle.setText(mActivity.getResources().getString(R.string.fc_main_title));
                break;
            case R.id.tv_button_mine://我的按钮点击切换到我的页面
                layoutMain.setVisibility(View.GONE);
                layoutMine.setVisibility(View.VISIBLE);
                tvHome.setTextColor(mActivity.getResources().getColor(R.color.ad888888));
                tvHome.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_main_off),null,null);
                tvMine.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(R.drawable.icon_my_on),null,null);
                tvMine.setTextColor(mActivity.getResources().getColor(R.color.fc_text_main_blue));
                tvTitle.setText(mActivity.getResources().getString(R.string.fc_mine_title));
                break;
            default:
                break;
        }
    }

    private MainContract.Presenter mPresenter;
    private boolean isDoLogout = true;
    private MainTasksAdapter mMainTasksAdapter;
    private List<FcVisitTaskList.TaskListBean> mDatas;
    private boolean isLoadMore;
    private int mPageCount = 1, mTempPageCount = 2;
    //定位相关
    private AMapLocationClient mLocationClient;
    private double mLat, mLon;
    private boolean isFristLocation = true;//第一次获取定位结果标志
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
                } else {
                    mLat = 0;
                    mLon = 0;
                    int errorCode = aMapLocation.getErrorCode();
                    Logger.d("errorCode = " + errorCode + " errorInfo = " + aMapLocation.getErrorInfo());
                }
            }
            if(isFristLocation){
                onRefresh();
                isFristLocation = false;
            }
        }
    };

    public static MainFragment getInstance(){
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_fc_main;
    }

    @Override
    protected void initView() {
        openLocationPermissions();
        tvUserMobile.setText(ADApplication.mSPUtils.getString(Api.NICKNAME));
        initRecyclerView();
    }

    @Override
    protected void initData() {
        mActivity = (MainActivity) getActivity();
        initLocation();
    }

    /**
     * 初始化定位
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
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(10000);//可选，设置网络请求超时时间。为10秒。在仅设备模式下无效
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

    /**
     * 初始化任务列表
     */
    private void initRecyclerView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mDatas = new ArrayList<>();
        mMainTasksAdapter = new MainTasksAdapter(mActivity, mDatas, true);
        mMainTasksAdapter.setLoadingView(R.layout.load_loading_layout);
        mMainTasksAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                getData();
            }
        });
        mMainTasksAdapter.setOnItemClickListener(new OnBaseItemClickListener<FcVisitTaskList.TaskListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, final FcVisitTaskList.TaskListBean data, final int position) {
                try {
                    String taskListBean = new Gson().toJson(data);
                    Intent intent = new Intent(mActivity, WebDetailActivity.class);
                    intent.putExtra("TaskListBean",taskListBean);
//                    intent.putExtra("data",data);
                    startActivity(intent);
                } catch (Exception e) {
                    SnackbarUtils.showLongDisSnackBar(mMainLayout, R.string.data_error);
                    e.printStackTrace();
                }

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mMainTasksAdapter);
    }

    /**
     * 获取列表数据
     */
    private void getData(){
        mPresenter.getVisitTaskList(ADApplication.mSPUtils.getString(Api.USERID),mPageCount+"",
                PAGE_SIZE+"", mLat+"", mLon+"");
    }

    @Override
    public void showLoadView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showMainIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void openOtherUi() {
//        ToastUtils.showShort("认证失败，请重新登录");
        SnackbarUtils.showLongDisSnackBar(mMainLayout, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public void showTasksSuccess(FcVisitTaskList.FcVisitTaskBean mFcVisitTaskBean) {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (mFcVisitTaskBean.getTaskList()!=null&&mFcVisitTaskBean.getTaskList().size() == 0){
            if (isLoadMore){
                mMainTasksAdapter.setLoadEndView(R.layout.load_end_layout);
            }else {
                showNoData();
            }
            return;
        }
        if (isLoadMore) {
            mTempPageCount++;
            mMainTasksAdapter.setLoadMoreData(mFcVisitTaskBean.getTaskList());
        } else {
            mMainTasksAdapter.setNewDataNoError(mFcVisitTaskBean.getTaskList());
        }
        if (mFcVisitTaskBean.getTaskList().size() < PAGE_SIZE) {
            mMainTasksAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    @Override
    public void updateApk() {
        openInstallPermissions();
    }

    @Override
    public void showLogout(boolean isSuccess, String message) {
        isDoLogout = true;
        if (isSuccess) {
            ADApplication.mSPUtils.clear();
            startActivity(new Intent(mActivity, LoginActivity.class));
            mActivity.finish();
        }
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(mMainLayout, message);
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        //判断三点动画是否显示，显示的话就不显示圆圈动画
        if(!mLoadView.isShown()){
            showMainIndicator(true);
        }
        mDatas.clear();
        mMainTasksAdapter.setLoadingView(R.layout.load_loading_layout);
        mMainTasksAdapter.notifyDataSetChanged();
        getData();
    }
    public void showError() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    public void showNoData() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 判断是否获取需要的权限
     * 判断权限后获取位置信息
     */
    @AfterPermissionGranted(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
    private void openLocationPermissions() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            showLoadView(true);
            mLocationClient.startLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP, perms);
        }
    }

    /**
     * 判断是否获取需要的权限
     * 判断权限后更新apk
     */
    @AfterPermissionGranted(REQUEST_GROUP_AND_STORAGE_GROUP)
    private void openInstallPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            updateAPK();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_GROUP_AND_STORAGE_GROUP, perms);
        }
    }


    /**
     * 获取权限成功
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP){
            showLoadView(true);
            mLocationClient.startLocation();
        }else if(requestCode == REQUEST_GROUP_AND_STORAGE_GROUP){
            updateAPK();
        }
    }

    /**
     * 获取权限失败
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP ||
                requestCode == REQUEST_GROUP_AND_STORAGE_GROUP) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(requestCode)
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
            openLocationPermissions();
        }else if (requestCode == REQUEST_GROUP_AND_STORAGE_GROUP) {
            openInstallPermissions();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (subscription != null)
            subscription.unsubscribe();
    }

    //接受详情提交后返回成功，然后首页刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshFCMainEvent(RefreshFCMainEvent event) {
        onRefresh();
    }

    private NumberFormat nt;
    private Subscription subscription;
    private boolean isDownloaded;
    /**
     * 更新apk
     */
    private void updateAPK() {
        nt = NumberFormat.getPercentInstance();
        String parentDir = ADApplication.getmContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        final String apkPath = parentDir + File.separator + "kingkong_ad.apk";
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
        progressDialog.setMessage("正在下载......");
        progressDialog.show();
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
                } else {
                    Observable<Long> updateObservable = Observable.create(new Observable.OnSubscribe<Long>() {
                        @Override
                        public void call(Subscriber<? super Long> subscriber) {
                            subscriber.onNext(Long.valueOf(bytesRead));
                            subscriber.onCompleted();
                        }
                    });
                    subscription = updateObservable
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    //设置百分数精确度2即保留两位小数
                                    nt.setMinimumFractionDigits(2);
                                    float baifen = (float) aLong / (float) contentLength * 100;
                                    progressDialog.setProgress((int) baifen);
                                }
                            });
                }
            }
        });
    }

    /**
     * 安装下载好的apk
     * @param apkPath
     */
    public void installApk(String apkPath) {
        File file = new File(apkPath);
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(getActivity(), "com.cango.adpickcar.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private class MainTasksAdapter extends BaseAdapter<FcVisitTaskList.TaskListBean> {

        public MainTasksAdapter(Context context, List<FcVisitTaskList.TaskListBean> datas, boolean isOpenLoadMore) {
            super(context, datas, isOpenLoadMore);
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.fc_tasks_item;
        }

        @Override
        protected void convert(BaseHolder holder, final FcVisitTaskList.TaskListBean data) {
            TextView tvId = holder.getView(R.id.fc_item_id);
            TextView tvOverdueTerm = holder.getView(R.id.tv_overdue_term);
            TextView tvOverdueDay = holder.getView(R.id.tv_overdue_day);
            TextView tvOverdueMoney = holder.getView(R.id.tv_overdue_money);
            TextView tvName = holder.getView(R.id.tv_fc_tasks_item_name);
            TextView tvPlate = holder.getView(R.id.tv_fc_tasks_item_plate);
            TextView tvAllotment = holder.getView(R.id.tv_tasks_item_allotment);
            TextView tvDistance = holder.getView(R.id.tv_fc_tasks_item_distance);
            TextView tvDate = holder.getView(R.id.tv_fc_tasks_item_date);
            TextView tvLabelOne = holder.getView(R.id.tv_fc_item_label_one);
            TextView tvLabelTwo = holder.getView(R.id.tv_fc_item_label_two);
//            TextView tvLabelThree = holder.getView(R.id.tv_fc_item_label_three);

            tvId.setText(data.getApplyCD());
            tvOverdueTerm.setText(data.getDueTerms());
            tvOverdueDay.setText(data.getOverDueDays());
            tvOverdueMoney.setText(data.getDueAmount());
            tvName.setText(data.getCustomerName());
            tvPlate.setText(data.getLicensePlateNO());
            tvDistance.setText(data.getDistance());
            tvDistance.setVisibility(View.VISIBLE);
            tvDate.setVisibility(View.GONE);
            tvAllotment.setVisibility(View.GONE);
            String msg = "";
            if("1".equals(data.getCallFlg())){
                msg = msg + data.getCallFlgName();
            }
            if("1".equals(data.getVisitFlg())){
                if("".equals(msg)){
                    msg = msg + data.getVisitFlgName();
                }else{
                    msg = msg + "+" + data.getVisitFlgName();
                }
            }
            if("1".equals(data.getAgencyFlg())){
                if("".equals(msg)){
                    msg = msg + data.getAgencyFlgName();
                }else{
                    msg = msg + "+" + data.getAgencyFlgName();
                }
            }
            if("1".equals(data.getLegalFlg())){
                if("".equals(msg)){
                    msg = msg + data.getLegalFlgName();
                }else{
                    msg = msg + "+" + data.getLegalFlgName();
                }
            }
            tvLabelOne.setText(msg);
            tvLabelTwo.setText(data.getFininstName());
//            tvAllotment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showCalendarDialog();
//                }
//            });
        }

        private CalendarDialogFragment mCalendarDialog;
        /**
         * 显示日历空间dialog
         */
        private void showCalendarDialog() {
            if (CommUtil.checkIsNull(mCalendarDialog)) {
                mCalendarDialog = new CalendarDialogFragment();
                mCalendarDialog.setCalendarDilaogListener(new CalendarDialogFragment.CalendarDilaogListener() {
                    @Override
                    public void onCalendarClick(Date date) {
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
    }

}
