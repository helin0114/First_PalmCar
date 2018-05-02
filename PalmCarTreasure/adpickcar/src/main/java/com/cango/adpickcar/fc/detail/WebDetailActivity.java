package com.cango.adpickcar.fc.detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.fc.location.LocationActivity;
import com.cango.adpickcar.model.EventModel.RefreshFCMainEvent;
import com.cango.adpickcar.util.AppUtils;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.EncryptUtils;
import com.cango.adpickcar.util.FileUtils;
import com.cango.adpickcar.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class WebDetailActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, WebDetailContract.View {

    private static final int REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP = 10087;
    private static final int REQUEST_STORAGE_GROUP = 10088;
    private static final int REQUEST_GALLERY = 10088;
    private static final int REQUEST_CAMERA = 10089;
    private WebDetailContract.Presenter mPresenter;
    private WebView mWebView;
    private ValueCallback<Uri[]> mValueCallback;
    private String imageExtension, galleryExtension;
    private Uri photoURI = null;
    /**
     * 判断此页面是否被回收
     */
    private boolean isRecycler;
    /**
     * 用来给H5调用的
     */
    private boolean isH5Recycler;
    /**
     * 用来给H5调用的，在页面被回收的时候要保存
     */
    private String H5FileList;
    /**
     * mType : 打开相机或者是打开图库 mResultCode : 返回的结果码
     */
    private int mRequestCode = -1001, mResultCode = -1000;
    /**
     * 保存onactivityresult中的图库的intent
     */
    private Intent mGalleryIntent;
    private ProgressDialog loadDia,downLoaDia;
    private String mUrl, mTaskListBean;
//    private FcVisitTaskList.TaskListBean data;
//    private String mPdfType = "家访任务书";//pdf文件类型：催收函/家访任务书
    /**
     * 当前传入拍照应用的图片地址
     */
    private String mCurrentPhotoPath;
    //定位相关
    private AMapLocationClient mLocationClient;
    private double mCurrentLat, mCurrentLon;
    private AMapLocationListener mLoactionListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (CommUtil.checkIsNull(aMapLocation)) {
                mCurrentLat = 0;
                mCurrentLon = 0;
            } else {
                if (aMapLocation.getErrorCode() == 0) {
                    if (!CommUtil.checkIsNull(aMapLocation.getLatitude())) {
                        mCurrentLat = aMapLocation.getLatitude();
                    }
                    if (!CommUtil.checkIsNull(aMapLocation.getLongitude())) {
                        mCurrentLon = aMapLocation.getLongitude();
                    }
                } else {
                    mCurrentLat = 0;
                    mCurrentLon = 0;
                    int errorCode = aMapLocation.getErrorCode();
                    Logger.d("errorCode = " + errorCode + " errorInfo = " + aMapLocation.getErrorInfo());
                }
//                Logger.d(mPhoneLat);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isRecycler = true;
            isH5Recycler = true;
            mCurrentPhotoPath = savedInstanceState.getString("photo_path");
            photoURI = savedInstanceState.getParcelable("uri");
            H5FileList = savedInstanceState.getString("h5_file_list");
        }
        Logger.d("onCreate");
        setContentView(R.layout.activity_web_detail);
        mWebView = (WebView) findViewById(R.id.web_detail);
        mTaskListBean = getIntent().getStringExtra("TaskListBean");
        mPresenter = new WebDetailPresenter(this);
        initLocation();
        openPermissions();
        initWebView();
        imageExtension = "image/*";
        galleryExtension = "gallery/*";

//        data = getIntent().getParcelableExtra("data");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("photo_path", mCurrentPhotoPath);
        outState.putParcelable("uri", photoURI);
        outState.putString("h5_file_list",H5FileList);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mUrl = Api.DETAIL_URL;
        WebSettings webSettings = mWebView.getSettings();

        //TODO 开启webview的调试功能，使用chrome可以调试手机同步演示，在生产中关闭
//        WebView.setWebContentsDebuggingEnabled(true);

        webSettings.setJavaScriptEnabled(true);
        //不支持缩放
        webSettings.setSupportZoom(false);
        //设置不可缩放
        webSettings.setBuiltInZoomControls(false);
        //隐藏原生的缩放按钮
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);//允许访问文件数据
//        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        //不使用缓存:
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        loadDia = new ProgressDialog(this);
        loadDia.setCancelable(true);
        loadDia.setMessage("正在加载...");
        loadDia.setIcon(R.mipmap.ic_launcher);
        loadDia.onStart();
        loadDia.show();
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) { // 重写此方法可以让webview处理https请求
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loadDia.dismiss();
                if (isRecycler) {
                    openStorage();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {

//            @Override
//            public void onProgressChanged(WebView webView, int progress) {
//                // 增加Javascript异常监控
//                /**
//                 * 设置Javascript的异常监控
//                 *
//                 * @param webView 指定被监控的webView
//                 * @param autoInject 是否自动注入Bugly.js文件
//                 * @return true 设置成功；false 设置失败
//                 */
//                CrashReport.setJavascriptMonitor(webView, true);
//                super.onProgressChanged(webView, progress);
//            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                //当正常的拍照流程没有问题的话，那么如果拍完照片的话并且给H5，那么的话手机端不知道什么时候要删除图片
                //为了防止图片过多，那么在点击input标签的时候就删除上一个图片地址
                deleteImageFile();
                String[] chooserParams = fileChooserParams.getAcceptTypes();
                Logger.d(Arrays.toString(chooserParams));
                List<String> list = Arrays.asList(chooserParams);
//                if (isRecycler) {
//                    isRecycler = false;
//                    //如果是已经被回收了，然后H5判断是否被回收，然后模拟input
//                    if (list.contains(imageExtension)) {
//                        //打开拍照后并没有选择或者直接返回的话，需要把当前传入给相机应用的图片文件删除
//                        if (mResultCode == Activity.RESULT_CANCELED) {
//                            deleteImageFile();
//                            if (!CommUtil.checkIsNull(filePathCallback)) {
//                                //如果相机没有选择或者直接返回需要给callback设置，不设置的话onShowFileChooser方法不会调用
//                                filePathCallback.onReceiveValue(null);
//                            }
//                        } else if (mResultCode == Activity.RESULT_OK) {
//                            if (filePathCallback != null) {
//                                Uri[] results;
//                                results = new Uri[]{photoURI};
//                                filePathCallback.onReceiveValue(results);
//                            }
//                        }
//                    } else if (list.contains(galleryExtension)) {
//                        if (mResultCode == Activity.RESULT_CANCELED) {
//                            //如果相册没有选择或者直接返回需要给callback设置，不设置的话onShowFileChooser方法不会调用
//                            if (filePathCallback != null) {
//                                filePathCallback.onReceiveValue(null);
//                            }
//                        }
//                        if (mResultCode == Activity.RESULT_OK) {
//                            if (!CommUtil.checkIsNull(mGalleryIntent)) {
//                                if (!CommUtil.checkIsNull(filePathCallback)) {
//                                    Uri[] results = null;
//                                    if (mGalleryIntent.getData() != null) {
//                                        results = new Uri[]{mGalleryIntent.getData()};
//                                    } else {
//                                        String dataString = mGalleryIntent.getDataString();
//                                        ClipData clipData = mGalleryIntent.getClipData();
//                                        if (clipData != null) {
//                                            results = new Uri[clipData.getItemCount()];
//                                            for (int i = 0; i < clipData.getItemCount(); i++) {
//                                                ClipData.Item item = clipData.getItemAt(i);
//                                                results[i] = item.getUri();
//                                            }
//                                        }
//                                        if (dataString != null)
//                                            results = new Uri[]{Uri.parse(dataString)};
//                                    }
//                                    filePathCallback.onReceiveValue(results);
//                                }
//                            } else {
//                                //防止图库放回的data是null，这个时候要把mvaluecallback重置
//                                if (!CommUtil.checkIsNull(filePathCallback)) {
//                                    filePathCallback.onReceiveValue(null);
//                                }
//                            }
//                        }
//                    } else {
//                        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
//                    }
//                    return true;
//                }
//                else {
                if (list.contains(imageExtension)) {
                    openCamera();
                } else if (list.contains(galleryExtension)) {
//                    openGallery();
                    //采用图库并不使用系统自带的
                    PhotoPicker.builder()
                            .setPhotoCount(1)
                            .setShowCamera(false)
                            .setShowGif(false)
                            .setPreviewEnabled(false)
                            .start(WebDetailActivity.this, PhotoPicker.REQUEST_CODE);
                } else {
                    return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
                }
                mValueCallback = filePathCallback;
                return true;
//                }
            }
        });
        mWebView.addJavascriptInterface(new AndroidToJs(), "android");
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void setPresenter(WebDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showIndicator(boolean active) {

    }

    @Override
    public void showDocDownloadSuccess() {

    }

    @Override
    public void showDocDownloadError() {

    }

    @Override
    public void showNoDatas() {

    }

    @Override
    public void showDataDetailUi(int id) {

    }

    @Override
    public void downFileSuccess() {
        downLoaDia.dismiss();
        ToastUtils.showShort("下载完成");
    }

    @Override
    public void downFileFailed() {
        downLoaDia.dismiss();
        ToastUtils.showShort("下载失败！");
    }
    @Override
    public void downFileNow() {
        if(downLoaDia == null){
            downLoaDia = new ProgressDialog(this);
            downLoaDia.setCancelable(true);
            downLoaDia.setMessage("正在下载...");
            downLoaDia.setIcon(R.mipmap.ic_launcher);
        }
        downLoaDia.onStart();
        downLoaDia.show();
    }

    // 继承自Object类
    class AndroidToJs {
        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public String encrypt(String source) {
            String encryptString = null;
            try {
                encryptString = EncryptUtils.encrypt(Api.KEY, source);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return encryptString;
        }

        @JavascriptInterface
        public String getAppVersion() {
            String appVersion = AppUtils.getVersionName(ADApplication.getmContext());
            return appVersion + "@" + Api.DEVICE_TYPE;
        }

        @JavascriptInterface
        public String getAuthorization(String serverTime) {
            String token = ADApplication.mSPUtils.getString(Api.TOKEN);
            String encryptString = null;
            try {
                encryptString = EncryptUtils.encrypt(Api.KEY, token + serverTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return encryptString;
        }

        @JavascriptInterface
        public String getWebHeadInfo() {
            return mTaskListBean;
        }

        @JavascriptInterface
        public String getUserId() {
            return ADApplication.mSPUtils.getString(Api.USERID);
        }

        @JavascriptInterface
        public void back() {
            finish();
        }

        /**
         * H5点击IMEI调用此方法
         *
         * @param IMEI 传入的IEMI字段
         */
        @JavascriptInterface
        public void startLocationUI(String IMEI) {
            if (!TextUtils.isEmpty(IMEI)) {
                Intent intent = new Intent(WebDetailActivity.this, LocationActivity.class);
                intent.putExtra("IMEI", IMEI);
                startActivity(intent);
            } else {
                ToastUtils.showShort(R.string.data_error);
            }
        }

        /**
         * 下载家纺催收函、下载家纺任务书
         * @param VisitID      家访ID
         * @param datasource   数据来源
         * @param ActionID     接口类型  1：催收函；2：任务书
         * @param CustomerType 客户类型  1：申请人；2：共同申请人；3：担保人（催收函必传）
         */
        @JavascriptInterface
        public void downloadPDF(String VisitID, String datasource, String ActionID, String CustomerType) {
//            if("1".equals(ActionID)){
//                mPdfType = "催收函";
//            }else if("2".equals(ActionID)){
//                mPdfType = "家访任务书";
//            }
            downHomePdfFile(VisitID, datasource, ActionID, CustomerType);
        }

        /**
         * 给H5的方法用来判断是否此页面被回收
         *
         * @return
         */
        @JavascriptInterface
        public boolean isRecycler() {
            return isRecycler;
        }

        /**
         * 给H5的方法用来得到回收的类型，如果10088 是图库 10089是拍照
         *
         * @return
         */
        @JavascriptInterface
        public String getRecyclerType() {
            return mRequestCode + "";
        }

        /**
         * H5得到是否回收的状态
         * @return
         */
        @JavascriptInterface
        public boolean getH5Recycler(){
            return isH5Recycler;
        }

        /**
         * H5设置是否回收状态
         * @param status
         */
        @JavascriptInterface
        public void setH5Recycler(boolean status){
            isH5Recycler = status;
        }

        /**
         * H5调用，得到H5保存的url组字符串
         * @return
         */
        @JavascriptInterface
        public String getH5FileList(){
            return H5FileList;
        }

        /**
         * H5调用，设置H5得到的url组字符串，H5在上传完图片之后得到返回的数组需要调用此方法设置
         * @param list
         */
        @JavascriptInterface
        public void setH5FileList(String list){
            H5FileList = list;
        }

        /**
         * H5完成家访任务后调用此方法刷新列表
         */
        @JavascriptInterface
        public void onRefreshFCMain(){
            onRefreshMain();
        }

        /**
         * 把得到的位置的经纬度信息传给H5
         * @return
         */
        @JavascriptInterface
        public String getNativeLocation(){
            return mCurrentLat+","+mCurrentLon;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLoactionListener);
            mLocationClient.onDestroy();
        }
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
    }

    private void onRefreshMain(){
        EventBus.getDefault().post(new RefreshFCMainEvent("refresh_fc_main"));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        openGallery();
//        这里不用询问权限是因为这个页面是请求过文件权限的
//        openCamera();
//        startActivity(new Intent(this,LocationActivity.class));

//        if(data != null) {
//            downHomePdfFile(data.getVisitID(), data.getDatasource(), "2", "1");
//        }
    }

    /**
     * pdf文件下载
     * @param VisitID      家访ID
     * @param datasource   数据来源
     * @param ActionID     接口类型  1：催收函；2：任务书
     * @param CustomerType 客户类型  1：申请人；2：共同申请人；3：担保人（催收函必传）
     */
    private void downHomePdfFile(String VisitID, String datasource, String ActionID, String CustomerType) {
//        boolean checkDownOver = checkDownOver(data.getApplyCD(), mPdfType);
//        if (checkDownOver) {
//            ToastUtils.showShort("你已经下载过此文件了");
//        } else {
            mPresenter.docDownload(ADApplication.mSPUtils.getString(Api.USERID), VisitID, datasource, ActionID, CustomerType,
                    Environment.getExternalStoragePublicDirectory(Api.FCPDFPATH).getAbsolutePath());
//        }
    }

    /**
     * 判断pdf文件是否已经下载过
     * @param applyCD
     * @param type
     * @return
     */
    private boolean checkDownOver(String applyCD, String type) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Api.FCPDFPATH);
        String suffix = ".pdf";
        if (storageDir == null || !FileUtils.isDir(storageDir)) return false;
        File[] files = storageDir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    String fileName = file.getName();
                    if (fileName.contains(applyCD)) {
                        if (fileName.contains(type)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * 打开系统图片选择器
     */
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
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
        if (EasyPermissions.hasPermissions(this, perms)) {
            mLocationClient.startLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage),
                    REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP, perms);
        }
    }

    @AfterPermissionGranted(REQUEST_STORAGE_GROUP)
    private void openStorage() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (isRecycler) {
                isRecycler = false;
                recycler();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage),
                    REQUEST_STORAGE_GROUP, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            mLocationClient.startLocation();
        } else if (requestCode == REQUEST_STORAGE_GROUP) {
            if (isRecycler) {
                isRecycler = false;
                recycler();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_group_and_storage)
                    .build().show();
        } else if (requestCode == REQUEST_STORAGE_GROUP) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_STORAGE_GROUP)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_group_and_storage)
                    .build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult");
        //保存一些值用来回收判断
        mRequestCode = requestCode;
        mResultCode = resultCode;
        mGalleryIntent = data;
        if (requestCode == REQUEST_LOCATION_GROUP_AND_STORAGE_GROUP) {
            openPermissions();
        }
        if (requestCode == REQUEST_STORAGE_GROUP) {
            openStorage();
        }
        if (resultCode == Activity.RESULT_CANCELED && requestCode == REQUEST_GALLERY && mValueCallback != null) {
            //如果相册没有选择或者直接返回需要给callback设置，不设置的话onShowFileChooser方法不会调用
            mValueCallback.onReceiveValue(null);
            mValueCallback = null;
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY) {
            if (!CommUtil.checkIsNull(data)) {
                if (!CommUtil.checkIsNull(mValueCallback)) {
                    Uri[] results = null;
                    if (data.getData() != null) {
                        results = new Uri[]{data.getData()};
                    } else {
                        String dataString = data.getDataString();
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            results = new Uri[clipData.getItemCount()];
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                results[i] = item.getUri();
                            }
                        }
                        if (dataString != null)
                            results = new Uri[]{Uri.parse(dataString)};
                    }
                    mValueCallback.onReceiveValue(results);
                    mValueCallback = null;
                }
            } else {
                //防止图库放回的data是null，这个时候要把mvaluecallback重置
                if (!CommUtil.checkIsNull(mValueCallback)) {
                    mValueCallback.onReceiveValue(null);
                    mValueCallback = null;
                }
            }
        }
        if (requestCode == REQUEST_CAMERA) {
            //打开拍照后并没有选择或者直接返回的话，需要把当前传入给相机应用的图片文件删除
            if (resultCode == Activity.RESULT_CANCELED) {
                deleteImageFile();
                if (!CommUtil.checkIsNull(mValueCallback)) {
                    //如果相机没有选择或者直接返回需要给callback设置，不设置的话onShowFileChooser方法不会调用
                    mValueCallback.onReceiveValue(null);
                    mValueCallback = null;
                }
            } else if (resultCode == Activity.RESULT_OK) {
                //TODO 下一步应该压缩图片
                if (mValueCallback != null) {
                    Uri[] results = null;
//                    if (data == null) {
                    //如果data是null，当然也有些手机getdata就是null
                    results = new Uri[]{photoURI};
//                    }
//                    else {
//                        String dataString = data.getDataString();
//                        ClipData clipData = data.getClipData();
//                        if (clipData != null) {
//                            results = new Uri[clipData.getItemCount()];
//                            for (int i = 0; i < clipData.getItemCount(); i++) {
//                                ClipData.Item item = clipData.getItemAt(i);
//                                results[i] = item.getUri();
//                            }
//                        }
//                        if (dataString != null)
//                            results = new Uri[]{Uri.parse(dataString)};
//                    }
                    mValueCallback.onReceiveValue(results);
                    mValueCallback = null;
                }
            }
        }
        if (requestCode == PhotoPicker.REQUEST_CODE) {
            if (resultCode == RESULT_OK){
                if (data != null) {
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    Logger.d(photos.get(0));
                    File file = new File(photos.get(0));
                    Observable.just(file)
                            .map(new Func1<File, Uri>() {
                                @Override
                                public Uri call(File file) {
                                    return getImageContentUri(WebDetailActivity.this,file);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Uri>() {
                                @Override
                                public void call(Uri uri) {
                                    if (!CommUtil.checkIsNull(uri)){
                                        Uri[] results = new Uri[]{uri};
                                        mValueCallback.onReceiveValue(results);
                                        mValueCallback = null;
                                    }else {
                                        mValueCallback.onReceiveValue(null);
                                        mValueCallback = null;
                                    }
                                }
                            });
                }else {
                    ToastUtils.showShort(R.string.data_unusual);
                    mValueCallback.onReceiveValue(null);
                    mValueCallback = null;
                }
            }else {
                mValueCallback.onReceiveValue(null);
                mValueCallback = null;
            }
        }

    }

    /**
     * 绝对路径转uri
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 假如被回收要做的事情
     */
    private void recycler() {
        isRecycler = false;
        switch (mRequestCode) {
            case REQUEST_CAMERA:
                if (mResultCode == Activity.RESULT_OK) {
                    if (mWebView != null) {
                        Logger.d(mCurrentPhotoPath);
                        Observable
                                .just(mCurrentPhotoPath)
                                .map(new Func1<String, String>() {
                                    @Override
                                    public String call(String s) {
                                        return bitmapToString(s);
//                                        return encode(s);
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        String call = "javascript:recyclerPhoto(\"" + s + "\")";
                                        mWebView.loadUrl(call);
                                    }
                                });
                    }
                } else if (mResultCode == Activity.RESULT_CANCELED) {
                    deleteImageFile();
                } else {

                }
                break;
            case REQUEST_GALLERY:
                if (mResultCode == Activity.RESULT_OK) {
                    if (mGalleryIntent != null) {
                        Uri galleryUri = mGalleryIntent.getData();
                        if (galleryUri != null) {
                            String galleryPath = getPathByUri(this, galleryUri);
                            if (mWebView != null) {
                                Logger.d(galleryPath);
                                Observable
                                        .just(galleryPath)
                                        .map(new Func1<String, String>() {
                                            @Override
                                            public String call(String s) {
                                                return bitmapToString(s);
                                            }
                                        })
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                String call = "javascript:recyclerPhoto(\"" + s + "\")";
                                                mWebView.loadUrl(call);
                                            }
                                        });
                            }
                        }
                    }
                } else if (mResultCode == Activity.RESULT_CANCELED) {

                } else {

                }
                break;
        }
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(getApplicationContext());
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

    /**
     * 根据uri得到图片路径
     *
     * @param context
     * @param uri
     * @return
     */
    public String getPathByUri(final Context context, final Uri uri) {
        try {
            return getRealFilePath(context, uri);
        } catch (Exception e) {
//            return uri.getPath();
            ToastUtils.showShort(R.string.get_gallery_failure);
            return null;
        }
    }

    /**
     * get url by uri
     */
    private String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 先判断是否有相机模块
     */
    private void openCamera() {
        boolean hasSystemFeature = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (hasSystemFeature) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        photoURI = FileProvider.getUriForFile(this, "com.cango.adpickcar.fileprovider", photoFile);

                    } else {
                        photoURI = Uri.fromFile(photoFile);
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                }
            }
        }
    }

    /**
     * 创建一个图片文件
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * 删除当前的图片文件
     *
     * @return
     */
    private boolean deleteImageFile() {
        if (mCurrentPhotoPath != null) {
            File emptyFile = new File(mCurrentPhotoPath);
            if (emptyFile.exists())
                return emptyFile.delete();
        }
        return false;
    }
}
