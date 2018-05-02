package com.cango.palmcartreasure.trailer.taskpictures;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TaskPicturesService;
import com.cango.palmcartreasure.model.TackPictureInfo;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.CommUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dell on 2018/3/14.
 */

public class TaskPicturesPresenter implements TaskPicturesContract.Presenter {

    private TaskPicturesContract.View mView;
    TaskPicturesService mService;

    public TaskPicturesPresenter(TaskPicturesContract.View detailView) {
        mView = detailView;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(TaskPicturesService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {
    }

    @Override
    public void getErpimageinfo(int datasource, String applyCd) {
        if (mView.isActive()) {
            mView.showLoadingView(true);
        }
        mService.getErpimageinfo(MtApplication.mSPUtils.getInt(Api.USERID), datasource, applyCd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<TackPictureInfo>() {
                    @Override
                    protected void _onNext(TackPictureInfo o) {
                        if (mView.isActive()) {
                            if (CommUtil.checkIsNull(o) || CommUtil.checkIsNull(o.getData()) ||
                                    CommUtil.checkIsNull(o.getData().getFileList()) ||
                                    o.getData().getFileList().size() <= 0) {
                                mView.showLoadingView(false);
                                mView.showNoData();
                            } else {
                                //成功后不要把加载动画消失，因为成功后还要继续进行请求
                                mView.showLoadingView(false);
                                mView.success(o);
                            }
                        }
                    }

                    @Override
                    protected void _onError() {
                        if (mView.isActive()) {
                            mView.showLoadingView(false);
                            mView.showError();
                        }
                    }
                });
    }
}
