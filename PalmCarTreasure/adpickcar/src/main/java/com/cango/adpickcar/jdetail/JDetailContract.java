package com.cango.adpickcar.jdetail;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.CarFilesInfo;
import com.cango.adpickcar.model.JCarBaseInfo;
import com.cango.adpickcar.model.JCarFiles;
import com.cango.adpickcar.model.JCarInfo;
import com.cango.adpickcar.model.PhotoResult;

import java.io.File;

/**
 * Created by cango on 2017/11/24.
 */

public interface JDetailContract {
    interface View extends BaseView<JDetailContract.Presenter> {
        void showIndicator(boolean active);

        void showError();

        void showNoData();

        void showSuccess(boolean isSuccess, String message);

        void openOtherUi();

        boolean isActive();

        void showBaseInfo(JCarBaseInfo baseInfo);

        void showBaseInfoError();

        void showBaseInfoNoData();

        void showCarInfo(JCarInfo.DataBean carDataBean);

        void showCarInfoError();

        void showCarInfoNoData();

        void showCarFilesInfo(JCarFiles carFilesInfo);

        void showCarFilesInfoError();

        void showCarFilesInfoNoData();

        void showSaveDisCarInfo(boolean isSuccess, PhotoResult photoResult);

        void showDeleteDisCarInfo(boolean isSuccess, String message);

        /**
         * 展示交车成功或者失败(type: 0是交车成功1：交车失败)
         * @param isSuccess
         * @param message
         */
        void showJiaoCheTaskStatus(boolean isSuccess,String message,int type);

        /**展示交车退回
         * @param isSuccess
         * @param message
         */
        void showRestoreTaskStatus(boolean isSuccess,String message);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取交车基本信息
         * @param showRefreshLoadingUI
         * @param CDLVTaskID
         */
        void GetBaseInfo(boolean showRefreshLoadingUI, String CDLVTaskID);

        /**
         * 获取交车车辆信息
         * @param showRefreshLoadingUI
         * @param DisCarID
         */
        void GetCarInfo(boolean showRefreshLoadingUI, String DisCarID);

        /**
         * 获取交车影像信息
         * @param showRefreshLoadingUI
         * @param DisCarID
         */
        void GetCarFilesInfo(boolean showRefreshLoadingUI, String DisCarID);

        /**
         * 交车成功和交车失败
         * CDLVTaskID											交车任务ID									string				○
         * CDLVTaskFlag											状态									string				○			30/交车完成、40/交车失败
         * LAT											交车经度									string				○			CDLVTASKFLAG=30时必填
         * LNG											交车纬度									string				○			CDLVTASKFLAG=30时必填
         * DLVMemo											交车情况备注									string				○			CDLVTASKFLAG=40时必填
         * @param showRefreshLoadingUI
         * @param CDLVTaskID
         * @param CDLVTaskFlag
         * @param LAT
         * @param LNG
         * @param DLVMemo
         * @param type 0：是交车成功1：交车失败
         */
        void SaveCarDeliveryTask(boolean showRefreshLoadingUI, String CDLVTaskID, String CDLVTaskFlag,
                                 String LAT, String LNG, String DLVMemo,int type,String DLVMile);

        /**
         * 交车退回
         * @param showRefreshLoadingUI
         * @param CDLVTaskID
         */
        void RestoreCarDeliveryTask(boolean showRefreshLoadingUI, String CDLVTaskID);

        void saveDisCarInfo(boolean showRefreshLoadingUI, String UserID, String DisCarID, String PicGroup,
                            String SubCategory, String SubID, String PicFileID, File file);

        void deleteDisCarFile(boolean showRefreshLoadingUI, String UserID, String PicFileID);
    }
}
