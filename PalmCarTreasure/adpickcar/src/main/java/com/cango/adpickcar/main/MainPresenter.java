package com.cango.adpickcar.main;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.api.MainService;
import com.cango.adpickcar.model.BaseData;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.model.DeliveryTaskList;
import com.cango.adpickcar.model.GetQRCodeData;
import com.cango.adpickcar.model.ServerTime;
import com.cango.adpickcar.net.NetManager;
import com.cango.adpickcar.net.RxSubscriber;
import com.cango.adpickcar.util.CommUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/9/26.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private MainService mService;
    private Subscription subscription1, subscription2, subscription3, subscription4, subscription5,
            subscription6, subscription7,subscription8,subscription9,subscription10,subscription11;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(MainService.class);
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
        if (!CommUtil.checkIsNull(subscription3))
            subscription3.unsubscribe();
        if (!CommUtil.checkIsNull(subscription4))
            subscription4.unsubscribe();
        if (!CommUtil.checkIsNull(subscription5))
            subscription5.unsubscribe();
        if (!CommUtil.checkIsNull(subscription6))
            subscription6.unsubscribe();
        if (!CommUtil.checkIsNull(subscription7))
            subscription7.unsubscribe();
        if (!CommUtil.checkIsNull(subscription8))
            subscription8.unsubscribe();
        if (!CommUtil.checkIsNull(subscription9))
            subscription9.unsubscribe();
        if (!CommUtil.checkIsNull(subscription10))
            subscription10.unsubscribe();
        if (!CommUtil.checkIsNull(subscription11))
            subscription11.unsubscribe();
    }

    @Override
    public void loadListByStatus(boolean showRefreshLoadingUI, final String UserID, final String CustName, final String LicensePlateNO,
                                 final String CarBrandName, final String QueryType, final String PageIndex, final String PageSize) {
        if (mView.isActive()) {
            mView.showMainIndicator(showRefreshLoadingUI);
        }
        switch (Integer.parseInt(QueryType)) {
            case MainFragment.WEIJIECHE:
                subscription1 = mService.getServerTime()
                        .flatMap(new Func1<ServerTime, Observable<CarTakeTaskList>>() {
                            @Override
                            public Observable<CarTakeTaskList> call(ServerTime serverTime) {
                                boolean isSuccess = serverTime.getCode().equals("200");
                                if (isSuccess) {
                                    ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                                }
                                return mService.getDatasByStatus(UserID, CustName, LicensePlateNO, CarBrandName, QueryType,
                                        PageIndex, PageSize);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CarTakeTaskList>() {
                            @Override
                            protected void _onNext(CarTakeTaskList o) {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    boolean isSuccess = o.getCode().equals("200");
                                    if (CommUtil.handingCodeLogin(o.getCode())) {
                                        mView.openOtherUi();
                                        return;
                                    }
//                                    o.setCode("211");
                                    if ("211".equals(o.getCode())) {
                                        mView.updateApk();
                                        return;
                                    }
                                    if (isSuccess) {
                                        CarTakeTaskList.DataBean dataBean = o.getData();
                                        if (!CommUtil.checkIsNull(dataBean)) {
                                            mView.showMainTitle(dataBean);
                                            ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean> list =
                                                    (ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean>) dataBean.getCarTakeTaskList();
                                            if (!CommUtil.checkIsNull(list) && list.size() > 0) {
                                                mView.showMainSuccess(isSuccess, list);
                                            } else {
                                                mView.showNoData();
                                            }
                                        } else {
                                            mView.showMainTitleError();
                                            mView.showNoData();
                                        }
                                    } else {
                                        mView.showMainTitleError();
                                        mView.showMainError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    mView.showMainTitleError();
                                    mView.showMainError();
                                }
                            }
                        });
                break;
            case MainFragment.WEITIJIAO:
                subscription2 = mService.getServerTime()
                        .flatMap(new Func1<ServerTime, Observable<CarTakeTaskList>>() {
                            @Override
                            public Observable<CarTakeTaskList> call(ServerTime serverTime) {
                                boolean isSuccess = serverTime.getCode().equals("200");
                                if (isSuccess) {
                                    ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                                }
                                return mService.getDatasByStatus(UserID, CustName, LicensePlateNO, CarBrandName, QueryType,
                                        PageIndex, PageSize);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CarTakeTaskList>() {
                            @Override
                            protected void _onNext(CarTakeTaskList o) {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    boolean isSuccess = o.getCode().equals("200");
                                    if (CommUtil.handingCodeLogin(o.getCode())) {
                                        mView.openOtherUi();
                                        return;
                                    }
                                    if (isSuccess) {
                                        CarTakeTaskList.DataBean dataBean = o.getData();
                                        if (!CommUtil.checkIsNull(dataBean)) {
                                            mView.showMainTitle(dataBean);
                                            ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean> list =
                                                    (ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean>) dataBean.getCarTakeTaskList();
                                            if (!CommUtil.checkIsNull(list) && list.size() > 0) {
                                                mView.showMainSuccess(isSuccess, list);
                                            } else {
                                                mView.showNoData();
                                            }
                                        } else {
                                            mView.showMainTitleError();
                                            mView.showNoData();
                                        }
                                    } else {
                                        mView.showMainTitleError();
                                        mView.showMainError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    mView.showMainTitleError();
                                    mView.showMainError();
                                }
                            }
                        });
                break;
            case MainFragment.SHENHEZHON:
                subscription3 = mService.getServerTime()
                        .flatMap(new Func1<ServerTime, Observable<CarTakeTaskList>>() {
                            @Override
                            public Observable<CarTakeTaskList> call(ServerTime serverTime) {
                                boolean isSuccess = serverTime.getCode().equals("200");
                                if (isSuccess) {
                                    ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                                }
                                return mService.getDatasByStatus(UserID, CustName, LicensePlateNO, CarBrandName, QueryType,
                                        PageIndex, PageSize);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CarTakeTaskList>() {
                            @Override
                            protected void _onNext(CarTakeTaskList o) {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    boolean isSuccess = o.getCode().equals("200");
                                    if (CommUtil.handingCodeLogin(o.getCode())) {
                                        mView.openOtherUi();
                                        return;
                                    }
                                    if (isSuccess) {
                                        CarTakeTaskList.DataBean dataBean = o.getData();
                                        if (!CommUtil.checkIsNull(dataBean)) {
                                            mView.showMainTitle(dataBean);
                                            ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean> list =
                                                    (ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean>) dataBean.getCarTakeTaskList();
                                            if (!CommUtil.checkIsNull(list) && list.size() > 0) {
                                                mView.showMainSuccess(isSuccess, list);
                                            } else {
                                                mView.showNoData();
                                            }
                                        } else {
                                            mView.showMainTitleError();
                                            mView.showNoData();
                                        }
                                    } else {
                                        mView.showMainTitleError();
                                        mView.showMainError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    mView.showMainTitleError();
                                    mView.showMainError();
                                }
                            }
                        });
                break;
            case MainFragment.SHENHETONGUO:
                subscription4 = mService.getServerTime()
                        .flatMap(new Func1<ServerTime, Observable<CarTakeTaskList>>() {
                            @Override
                            public Observable<CarTakeTaskList> call(ServerTime serverTime) {
                                boolean isSuccess = serverTime.getCode().equals("200");
                                if (isSuccess) {
                                    ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                                }
                                return mService.getDatasByStatus(UserID, CustName, LicensePlateNO, CarBrandName, QueryType,
                                        PageIndex, PageSize);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CarTakeTaskList>() {
                            @Override
                            protected void _onNext(CarTakeTaskList o) {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    boolean isSuccess = o.getCode().equals("200");
                                    if (CommUtil.handingCodeLogin(o.getCode())) {
                                        mView.openOtherUi();
                                        return;
                                    }
                                    if (isSuccess) {
                                        CarTakeTaskList.DataBean dataBean = o.getData();
                                        if (!CommUtil.checkIsNull(dataBean)) {
                                            mView.showMainTitle(dataBean);
                                            ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean> list =
                                                    (ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean>) dataBean.getCarTakeTaskList();
                                            if (!CommUtil.checkIsNull(list) && list.size() > 0) {
                                                mView.showMainSuccess(isSuccess, list);
                                            } else {
                                                mView.showNoData();
                                            }
                                        } else {
                                            mView.showMainTitleError();
                                            mView.showNoData();
                                        }
                                    } else {
                                        mView.showMainTitleError();
                                        mView.showMainError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    mView.showMainTitleError();
                                    mView.showMainError();
                                }
                            }
                        });
                break;
            case MainFragment.SHENHETUIHUI:
                subscription5 = mService.getServerTime()
                        .flatMap(new Func1<ServerTime, Observable<CarTakeTaskList>>() {
                            @Override
                            public Observable<CarTakeTaskList> call(ServerTime serverTime) {
                                boolean isSuccess = serverTime.getCode().equals("200");
                                if (isSuccess) {
                                    ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                                }
                                return mService.getDatasByStatus(UserID, CustName, LicensePlateNO, CarBrandName, QueryType,
                                        PageIndex, PageSize);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<CarTakeTaskList>() {
                            @Override
                            protected void _onNext(CarTakeTaskList o) {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    boolean isSuccess = o.getCode().equals("200");
                                    if (CommUtil.handingCodeLogin(o.getCode())) {
                                        mView.openOtherUi();
                                        return;
                                    }
                                    if (isSuccess) {
                                        CarTakeTaskList.DataBean dataBean = o.getData();
                                        if (!CommUtil.checkIsNull(dataBean)) {
                                            mView.showMainTitle(dataBean);
                                            ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean> list =
                                                    (ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean>) dataBean.getCarTakeTaskList();
                                            if (!CommUtil.checkIsNull(list) && list.size() > 0) {
                                                mView.showMainSuccess(isSuccess, list);
                                            } else {
                                                mView.showNoData();
                                            }
                                        } else {
                                            mView.showMainTitleError();
                                            mView.showNoData();
                                        }
                                    } else {
                                        mView.showMainTitleError();
                                        mView.showMainError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showMainIndicator(false);
                                    mView.showMainTitleError();
                                    mView.showMainError();
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void loadJListByStatus(boolean showRefreshLoadingUI, final String UserID, final String CTPName, final String CTPMobile,
                                  final String LicensePlateNO, String QueryType, final String PageIndex, final String PageSize) {
        if (mView.isActive()) {
            mView.showJMainIndicator(showRefreshLoadingUI);
        }
        switch (Integer.parseInt(QueryType)) {
            case MainFragment.DAIJIAOCHE:
                subscription9 = mService.getServerTime()
                        .flatMap(new Func1<ServerTime, Observable<DeliveryTaskList>>() {
                            @Override
                            public Observable<DeliveryTaskList> call(ServerTime serverTime) {
                                boolean isSuccess = serverTime.getCode().equals("200");
                                if (isSuccess) {
                                    ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                                }
                                return mService.GetCarDeliveryTaskList(UserID, CTPName, CTPMobile, LicensePlateNO, 1+"",
                                        PageIndex, PageSize);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<DeliveryTaskList>() {
                            @Override
                            protected void _onNext(DeliveryTaskList o) {
                                if (mView.isActive()) {
                                    mView.showJMainIndicator(false);
                                    boolean isSuccess = o.getCode().equals("200");
                                    if (CommUtil.handingCodeLogin(o.getCode())) {
                                        mView.openOtherUi();
                                        return;
                                    }
//                                    o.setCode("211");
                                    if ("211".equals(o.getCode())) {
                                        mView.updateApk();
                                        return;
                                    }
                                    if (isSuccess) {
                                        DeliveryTaskList.DataBean dataBean = o.getData();
                                        if (!CommUtil.checkIsNull(dataBean)) {
                                            mView.showJMainTitle(dataBean);
                                            ArrayList<DeliveryTaskList.DataBean.CarDeliveryTaskListBean> list =
                                                    (ArrayList<DeliveryTaskList.DataBean.CarDeliveryTaskListBean>) dataBean.getCarDeliveryTaskList();
                                            if (!CommUtil.checkIsNull(list) && list.size() > 0) {
                                                mView.showJMainSuccess(isSuccess, list);
                                            } else {
                                                mView.showJNoData();
                                            }
                                        } else {
                                            mView.showJMainTitleError();
                                            mView.showJNoData();
                                        }
                                    } else {
                                        mView.showJMainTitleError();
                                        mView.showJMainError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showJMainIndicator(false);
                                    mView.showJMainTitleError();
                                    mView.showJMainError();
                                }
                            }
                        });
                break;
            case MainFragment.YIJIAOCHE:
                subscription10 = mService.getServerTime()
                        .flatMap(new Func1<ServerTime, Observable<DeliveryTaskList>>() {
                            @Override
                            public Observable<DeliveryTaskList> call(ServerTime serverTime) {
                                boolean isSuccess = serverTime.getCode().equals("200");
                                if (isSuccess) {
                                    ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                                }
                                return mService.GetCarDeliveryTaskList(UserID, "", "", "", 2+"",
                                        PageIndex, PageSize);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<DeliveryTaskList>() {
                            @Override
                            protected void _onNext(DeliveryTaskList o) {
                                if (mView.isActive()) {
                                    mView.showJMainIndicator(false);
                                    boolean isSuccess = o.getCode().equals("200");
                                    if (CommUtil.handingCodeLogin(o.getCode())) {
                                        mView.openOtherUi();
                                        return;
                                    }
                                    if (isSuccess) {
                                        DeliveryTaskList.DataBean dataBean = o.getData();
                                        if (!CommUtil.checkIsNull(dataBean)) {
                                            mView.showJMainTitle(dataBean);
                                            ArrayList<DeliveryTaskList.DataBean.CarDeliveryTaskListBean> list =
                                                    (ArrayList<DeliveryTaskList.DataBean.CarDeliveryTaskListBean>) dataBean.getCarDeliveryTaskList();
                                            if (!CommUtil.checkIsNull(list) && list.size() > 0) {
                                                mView.showJMainSuccess(isSuccess, list);
                                            } else {
                                                mView.showJNoData();
                                            }
                                        } else {
                                            mView.showJMainTitleError();
                                            mView.showJNoData();
                                        }
                                    } else {
                                        mView.showJMainTitleError();
                                        mView.showJMainError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showJMainIndicator(false);
                                    mView.showJMainTitleError();
                                    mView.showJMainError();
                                }
                            }
                        });
                break;
            case MainFragment.JIAOCHESHIBAI:
                subscription11 = mService.getServerTime()
                        .flatMap(new Func1<ServerTime, Observable<DeliveryTaskList>>() {
                            @Override
                            public Observable<DeliveryTaskList> call(ServerTime serverTime) {
                                boolean isSuccess = serverTime.getCode().equals("200");
                                if (isSuccess) {
                                    ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                                }
                                return mService.GetCarDeliveryTaskList(UserID, "", "", "", 3+"",
                                        PageIndex, PageSize);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<DeliveryTaskList>() {
                            @Override
                            protected void _onNext(DeliveryTaskList o) {
                                if (mView.isActive()) {
                                    mView.showJMainIndicator(false);
                                    boolean isSuccess = o.getCode().equals("200");
                                    if (CommUtil.handingCodeLogin(o.getCode())) {
                                        mView.openOtherUi();
                                        return;
                                    }
                                    if (isSuccess) {
                                        DeliveryTaskList.DataBean dataBean = o.getData();
                                        if (!CommUtil.checkIsNull(dataBean)) {
                                            mView.showJMainTitle(dataBean);
                                            ArrayList<DeliveryTaskList.DataBean.CarDeliveryTaskListBean> list =
                                                    (ArrayList<DeliveryTaskList.DataBean.CarDeliveryTaskListBean>) dataBean.getCarDeliveryTaskList();
                                            if (!CommUtil.checkIsNull(list) && list.size() > 0) {
                                                mView.showJMainSuccess(isSuccess, list);
                                            } else {
                                                mView.showJNoData();
                                            }
                                        } else {
                                            mView.showJMainTitleError();
                                            mView.showJNoData();
                                        }
                                    } else {
                                        mView.showJMainTitleError();
                                        mView.showJMainError();
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (mView.isActive()) {
                                    mView.showJMainIndicator(false);
                                    mView.showJMainTitleError();
                                    mView.showJMainError();
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void logout(boolean showRefreshLoadingUI, final String UserID) {
        if (mView.isActive())
            mView.showLoadView(showRefreshLoadingUI);
        subscription6 = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<BaseData>>() {
                    @Override
                    public Observable<BaseData> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("UserID", UserID);
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        paramsMap = new HashMap<>();
                        paramsMap.put("RequestContent", encrypt);
                        return mService.logout(paramsMap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseData>() {
                    @Override
                    protected void _onNext(BaseData o) {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            mView.showLogout(isSuccess, o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            mView.showLogout(false, null);
                        }
                    }
                });
    }

    @Override
    public void GetCarTakeTaskList(boolean showRefreshLoadingUI, final String UserID, final String CTTaskID,
                                   final String CTWhno, final String Vin, final String CarID) {
        if (mView.isActive())
            mView.showLoadView(showRefreshLoadingUI);
        subscription7 = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<BaseData>>() {
                    @Override
                    public Observable<BaseData> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("UserID", UserID);
                        paramsMap.put("CTTaskID", CTTaskID);
                        paramsMap.put("CTWhno", CTWhno);
                        paramsMap.put("Vin", Vin);
                        paramsMap.put("CarID", CarID);
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        paramsMap = new HashMap<>();
                        paramsMap.put("RequestContent", encrypt);
                        return mService.carTakeStoreConfirm(paramsMap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseData>() {
                    @Override
                    protected void _onNext(BaseData o) {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            mView.showGetCarTake(isSuccess, o.getMsg());
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            mView.showGetCarTake(false, null);
                        }
                    }
                });
    }

    @Override
    public void carTakeStoreConfirmByQRCode(boolean showRefreshLoadingUI, final String UserID, final String TCUserID,
                                            final String AgencyID, final String ApplyCD, final String Lat, final String Lon, final String WHNO, final String datasource) {
        if (mView.isActive())
            mView.showLoadView(showRefreshLoadingUI);
        subscription8 = mService.getServerTime()
                .flatMap(new Func1<ServerTime, Observable<GetQRCodeData>>() {
                    @Override
                    public Observable<GetQRCodeData> call(ServerTime serverTime) {
                        boolean isSuccess = serverTime.getCode().equals("200");
                        if (isSuccess) {
                            ADApplication.mSPUtils.put(Api.SERVERTIME, serverTime.getData().getServerTime());
                        }
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("UserID", UserID);
                        paramsMap.put("TCUserID", TCUserID);
                        paramsMap.put("AgencyID", AgencyID);
                        paramsMap.put("ApplyCD", ApplyCD);
                        paramsMap.put("Lat", Lat);
                        paramsMap.put("Lon", Lon);
                        paramsMap.put("WHNO", WHNO);
                        paramsMap.put("datasource",datasource);
                        String encrypt = CommUtil.getParmasMapToJsonByEncrypt(paramsMap);
                        paramsMap = new HashMap<>();
                        paramsMap.put("RequestContent", encrypt);
                        return mService.cartakestoreconfirmbyqrcode(paramsMap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<GetQRCodeData>() {
                    @Override
                    protected void _onNext(GetQRCodeData o) {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            boolean isSuccess = o.getCode().equals("200");
                            if (CommUtil.handingCodeLogin(o.getCode())) {
                                mView.openOtherUi();
                                return;
                            }
                            mView.showQRCodeStatus(isSuccess, o);
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showLoadView(false);
                            mView.showQRCodeStatus(false, null);
                        }
                    }
                });
    }
}
