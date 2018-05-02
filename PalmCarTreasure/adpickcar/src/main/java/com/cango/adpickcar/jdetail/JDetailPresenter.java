package com.cango.adpickcar.jdetail;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.api.JDetailService;
import com.cango.adpickcar.model.BaseData;
import com.cango.adpickcar.model.JCarBaseInfo;
import com.cango.adpickcar.model.JCarFiles;
import com.cango.adpickcar.model.JCarInfo;
import com.cango.adpickcar.model.PhotoResult;
import com.cango.adpickcar.model.ServerTime;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.net.RxSubscriber;
import com.cango.adpickcar.util.CommUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/11/24.
 */

public class JDetailPresenter implements JDetailContract.Presenter {
    private JDetailContract.View mView;
    private JDetailService mService;
    private Subscription baseInfoSubscription, carInfoSubscription, fileSubscription, saveOKSubscription,
            saveFileSub, deleteFileSub,jcarRestoreSubscription;
    public JDetailPresenter(JDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(JDetailService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(carInfoSubscription))
            carInfoSubscription.unsubscribe();
        if (!CommUtil.checkIsNull(baseInfoSubscription))
            baseInfoSubscription.unsubscribe();
        if (!CommUtil.checkIsNull(saveOKSubscription))
            saveOKSubscription.unsubscribe();
        if (!CommUtil.checkIsNull(fileSubscription))
            fileSubscription.unsubscribe();
        if (!CommUtil.checkIsNull(saveFileSub))
            saveFileSub.unsubscribe();
        if (!CommUtil.checkIsNull(deleteFileSub))
            deleteFileSub.unsubscribe();
        if (!CommUtil.checkIsNull(jcarRestoreSubscription))
            jcarRestoreSubscription.unsubscribe();
    }

    @Override
    public void GetBaseInfo(boolean showRefreshLoadingUI, final String CDLVTaskID) {
        if (mView.isActive()) {
            mView.showIndicator(showRefreshLoadingUI);
        }
        baseInfoSubscription = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<JCarBaseInfo>>() {
                    @Override
                    public Observable<JCarBaseInfo> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        return mService.GetCarDeliveryTaskInfo(CDLVTaskID);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JCarBaseInfo>() {
                    @Override
                    protected void _onNext(JCarBaseInfo o) {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            if (isSuccess) {
                                if (o.getData() != null) {
                                    mView.showBaseInfo(o);
                                } else {
                                    mView.showBaseInfoNoData();
                                }
                            } else {
                                mView.showBaseInfoNoData();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            mView.showBaseInfoError();
                        }
                    }
                });
    }

    @Override
    public void GetCarInfo(boolean showRefreshLoadingUI, final String DisCarID) {
        if (mView.isActive()) {
            mView.showIndicator(showRefreshLoadingUI);
        }
        carInfoSubscription = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<JCarInfo>>() {
                    @Override
                    public Observable<JCarInfo> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        return mService.GetCarInfo(DisCarID);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JCarInfo>() {
                    @Override
                    protected void _onNext(JCarInfo o) {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            if (isSuccess) {
                                if (o.getData() != null) {
                                    mView.showCarInfo(o.getData());
                                } else {
                                    mView.showCarInfoNoData();
                                }
                            } else {
                                mView.showCarInfoNoData();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            mView.showCarInfoError();
                        }
                    }
                });
    }

    @Override
    public void GetCarFilesInfo(boolean showRefreshLoadingUI, final String DisCarID) {
        if (mView.isActive()) {
            mView.showIndicator(showRefreshLoadingUI);
        }
        fileSubscription = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<JCarFiles>>() {
                    @Override
                    public Observable<JCarFiles> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        return mService.GetCarDeliveryTaskFiles(DisCarID);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<JCarFiles>() {
                    @Override
                    protected void _onNext(JCarFiles o) {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            if (isSuccess) {
                                if (o.getData() != null) {
                                    mView.showCarFilesInfo(o);
                                } else {
                                    mView.showCarFilesInfoNoData();
                                }
                            } else {
                                mView.showCarFilesInfoNoData();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            mView.showCarFilesInfoError();
                        }
                    }
                });
    }

    @Override
    public void SaveCarDeliveryTask(boolean showRefreshLoadingUI, final String CDLVTaskID, final String CDLVTaskFlag,
                                    final String LAT, final String LNG, final String DLVMemo, final int type, final String DLVMile) {
        if (mView.isActive()) {
            mView.showIndicator(showRefreshLoadingUI);
        }
        saveOKSubscription = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<BaseData>>() {
                    @Override
                    public Observable<BaseData> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("CDLVTaskID", CDLVTaskID);
                        paramsMap.put("CDLVTaskFlag", CDLVTaskFlag);
                        paramsMap.put("LAT", LAT);
                        paramsMap.put("LNG", LNG);
                        paramsMap.put("DLVMemo", DLVMemo);
                        paramsMap.put("DLVMile", DLVMile);
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        paramsMap = new HashMap<>();
                        paramsMap.put("RequestContent", encrypt);
                        return mService.SaveCarDeliveryTask(paramsMap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseData>() {
                    @Override
                    protected void _onNext(BaseData o) {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            mView.showJiaoCheTaskStatus(isSuccess, o.getMsg(), type);
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            //如果网络异常呢，那么将重置
                            mView.showJiaoCheTaskStatus(false, null, type);
                        }
                    }
                });
    }

    @Override
    public void RestoreCarDeliveryTask(boolean showRefreshLoadingUI, final String CDLVTaskID) {
        if (mView.isActive()) {
            mView.showIndicator(showRefreshLoadingUI);
        }
        jcarRestoreSubscription = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<BaseData>>() {
                    @Override
                    public Observable<BaseData> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("CDLVTaskID", CDLVTaskID);
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        paramsMap = new HashMap<>();
                        paramsMap.put("RequestContent", encrypt);
                        return mService.RestoreCarDeliveryTask(paramsMap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseData>() {
                    @Override
                    protected void _onNext(BaseData o) {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            if(isSuccess){
                                mView.showRestoreTaskStatus(isSuccess, o.getMsg());
                            }
                        }
                    }
                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            mView.showRestoreTaskStatus(false, null);
                        }
                    }
                });
    }

    @Override
    public void saveDisCarInfo(boolean showRefreshLoadingUI, final String UserID, final String DisCarID,
                               final String PicGroup, final String SubCategory, final String SubID, final String PicFileID, final File file) {
        saveFileSub = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<PhotoResult>>() {
                    @Override
                    public Observable<PhotoResult> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("UserID", UserID);
                        paramsMap.put("DisCarID", DisCarID);
                        paramsMap.put("PicGroup", PicGroup);
                        paramsMap.put("SubCategory", SubCategory);
                        paramsMap.put("SubID", SubID);
                        paramsMap.put("PicFileID", PicFileID);
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        RequestBody RequestContent = RequestBody.create(null, encrypt);
                        RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), photoBody);
                        return mService.saveDisCarFile(RequestContent, part);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<PhotoResult>() {
                    @Override
                    protected void _onNext(PhotoResult o) {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            deleteImageFile(file);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            mView.showSaveDisCarInfo(isSuccess, o);
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            mView.showSaveDisCarInfo(false, null);
                            deleteImageFile(file);
                        }
                    }
                });
    }

    @Override
    public void deleteDisCarFile(boolean showRefreshLoadingUI, final String UserID, final String PicFileID) {
        if (mView.isActive()) {
            mView.showIndicator(showRefreshLoadingUI);
        }
        deleteFileSub = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<BaseData>>() {
                    @Override
                    public Observable<BaseData> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("UserID", UserID);
                        paramsMap.put("PicFileID", PicFileID);
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        paramsMap = new HashMap<>();
                        paramsMap.put("RequestContent", encrypt);
                        return mService.deleteDisCarFile(paramsMap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseData>() {
                    @Override
                    protected void _onNext(BaseData o) {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            mView.showDeleteDisCarInfo(isSuccess, o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showIndicator(false);
                            mView.showDeleteDisCarInfo(false, null);
                        }
                    }
                });
    }

    private boolean deleteImageFile(File file) {
        if (file.exists())
            return file.delete();
        return false;
    }
}
