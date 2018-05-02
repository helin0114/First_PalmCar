package com.cango.adpickcar.camera;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.api.DetailService;
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
 * Created by dell on 2017/12/5.
 */

public class CameraPresenter implements CameraContract.Presenter{
    private CameraContract.View mView;
    private DetailService mService;
    private Subscription subscription;
    public CameraPresenter(CameraContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(DetailService.class);
    }
    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscription))
            subscription.unsubscribe();
    }

    @Override
    public void saveDisCarInfo(boolean showRefreshLoadingUI, final String UserID, final String DisCarID, final String PicGroup,
                               final String SubCategory, final String SubID, final String PicFileID, final File file) {
        subscription = mService.getServerTime()
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
                        mView.showIndicator(false);
                        deleteImageFile(file);
                        boolean isSuccess = o.getCode().equals("200");
                        if (CommUtil.handingCodeLogin(o.getCode())) {
                            mView.openOtherUi();
                            return;
                        }
//                        mView.showSaveDisCarInfo(isSuccess, o);
                        mView.savePhotoResult(isSuccess,o);
                    }

                    @Override
                    protected void _onError() {
                        mView.showIndicator(false);
//                        mView.showSaveDisCarInfo(false, null);
                        deleteImageFile(file);
                        mView.savePhotoResult(false,null);
                    }
                });
    }
    private boolean deleteImageFile(File file) {
        if (file.exists())
            return file.delete();
        return false;
    }
}
