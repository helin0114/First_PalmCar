package com.cango.palmcartreasure.trailer.complete;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.WareHouse;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.google.gson.JsonArray;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/6/26.
 */

public class TrailerCompletePresenter implements TrailerCompleteContract.Presenter {
    private TrailerCompleteContract.View mView;
    private TrailerTaskService mService;
    private Subscription subscription1,subscription2;
    public TrailerCompletePresenter(TrailerCompleteContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(TrailerTaskService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
        if (!CommUtil.checkIsNull(subscription1))
            subscription1.unsubscribe();
        if (!CommUtil.checkIsNull(subscription2))
            subscription2.unsubscribe();
    }

    @Override
    public void wareHouse(boolean showIndicatorUI, int agencyID, int caseID, double lat, double lon, String province,int datasource) {
        if (mView.isActive()) {
            mView.showIndicator(showIndicatorUI);
        }
        if (lat > 0 && lon > 0 && province != null) {
            subscription1 = mService.wareHouse(MtApplication.mSPUtils.getInt(Api.USERID), agencyID, caseID, lat, lon, province,datasource)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<WareHouse>() {
                        @Override
                        protected void _onNext(WareHouse o) {
                            if (mView.isActive()) {
                                mView.showIndicator(false);
                                int code = o.getCode();
                                if (code == 0) {
                                    if (CommUtil.checkIsNull(o.getData())) {
                                        mView.showWareHouseNoData();
                                    } else {
                                        if (o.getData().size() == 0) {
                                            mView.showWareHouseNoData();
                                        } else {
                                            mView.showWareHouseSuccess(o);
                                        }
                                    }
                                } else {
                                    mView.showWareHouseNoData();
                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mView.isActive()) {
                                mView.showIndicator(false);
                                mView.showWareHouseError("");
                            }
                        }
                    });
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }

    }

    @Override
    public void comfirmTrailerComplete(int userId, double LAT, double LON, int agencyID, int caseID,
                                       String isNotifyCustImm, List<String> answerList, int realSPID, String tmpReason, final File file,int datasource) {
        if (LAT > 0 && LON > 0) {
            RequestBody mUserId = RequestBody.create(null, userId + "");
            RequestBody mLat = RequestBody.create(null, LAT + "");
            RequestBody mLon = RequestBody.create(null, LON + "");
            RequestBody mAgencyID = RequestBody.create(null, agencyID + "");
            RequestBody mCaseID = RequestBody.create(null, caseID + "");
            RequestBody notifyCustImm = RequestBody.create(null, isNotifyCustImm);
            JsonArray jsonArray = new JsonArray();
            for (String str : answerList) {
                jsonArray.add(str);
            }
            RequestBody mAnswerList = RequestBody.create(null, jsonArray.toString());
            RequestBody mRealSPID = RequestBody.create(null, realSPID + "");
            if (CommUtil.checkIsNull(tmpReason)){
                tmpReason="";
            }else {
            }
            RequestBody mTmpReason = RequestBody.create(null, tmpReason);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), photoBody);

            RequestBody datasourceBody = RequestBody.create(null,datasource+"");

            subscription2 = mService.checkPiontSubmit(mUserId, mLat, mLon, mAgencyID, mCaseID, notifyCustImm, mAnswerList,
                    mRealSPID, mTmpReason, part,datasourceBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TaskAbandon>() {
                        @Override
                        protected void _onNext(TaskAbandon o) {
                            if (mView.isActive()) {
                                mView.showIndicator(false);
                                deleteImageFile(file);
                                boolean isSuccess = o.getCode() == 0;
                                if (isSuccess) {
                                    mView.showComfirmSuccess(o.getMsg());
                                } else {
                                    mView.showError(o.getMsg());
                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mView.isActive()) {
                                mView.showIndicator(false);
                                mView.showError(null);
                                deleteImageFile(file);
                            }
                        }
                    });
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    @Override
    public void comfirmTrailerCompleteNoFile(int userId, double LAT, double LON, int agencyID, int caseID,
                                             String isNotifyCustImm, List<String> answerList, int realSPID, String tmpReason,int datasource) {
        if (LAT > 0 && LON > 0) {
            RequestBody mUserId = RequestBody.create(null, userId + "");
            RequestBody mLat = RequestBody.create(null, LAT + "");
            RequestBody mLon = RequestBody.create(null, LON + "");
            RequestBody mAgencyID = RequestBody.create(null, agencyID + "");
            RequestBody mCaseID = RequestBody.create(null, caseID + "");
            RequestBody notifyCustImm = RequestBody.create(null, isNotifyCustImm);
            JsonArray jsonArray = new JsonArray();
            for (String str : answerList) {
                jsonArray.add(str);
            }
            RequestBody mAnswerList = RequestBody.create(null, jsonArray.toString());
            RequestBody mRealSPID = RequestBody.create(null, realSPID + "");
            if (CommUtil.checkIsNull(tmpReason)){
                tmpReason="";
            }else {
            }
            RequestBody mTmpReason = RequestBody.create(null, tmpReason);

            RequestBody datasourceBody = RequestBody.create(null,datasource+"");

            subscription2 = mService.checkPiontSubmitNoFile(mUserId, mLat, mLon, mAgencyID, mCaseID, notifyCustImm, mAnswerList,
                    mRealSPID, mTmpReason,datasourceBody).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<TaskAbandon>() {
                        @Override
                        protected void _onNext(TaskAbandon o) {
                            if (mView.isActive()) {
                                mView.showIndicator(false);
                                boolean isSuccess = o.getCode() == 0;
                                if (isSuccess) {
                                    mView.showComfirmSuccess(o.getMsg());
                                } else {
                                    mView.showError(o.getMsg());
                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            if (mView.isActive()) {
                                mView.showIndicator(false);
                                mView.showError(null);
                            }
                        }
                    });
        } else {
            ToastUtils.showShort(R.string.no_get_location);
        }
    }

    private boolean deleteImageFile(File file) {
        if (file.exists())
            return file.delete();
        return false;
    }
}
