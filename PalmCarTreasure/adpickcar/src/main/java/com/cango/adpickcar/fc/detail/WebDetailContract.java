package com.cango.adpickcar.fc.detail;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/01/17
 *     desc   :
 * </pre>
 */
public interface WebDetailContract {
    interface View extends BaseView<WebDetailContract.Presenter> {
        void showIndicator(boolean active);

        void showDocDownloadSuccess();

        void showDocDownloadError();

        void showNoDatas();

        void showDataDetailUi(int id);

        /**
         * 下载pdf文件成功
         */
        void downFileSuccess();

        /**
         * 下载pdf文件失败
         */
        void downFileFailed();

        /**
         * 正在下载pdf文件
         */
        void downFileNow();
    }

    interface Presenter extends BasePresenter {
        /**
         * 下载pdf文件
         * @param userId
         * @param VisitID
         * @param datasource
         * @param ActionID
         * @param CustomerType
         * @param parentDir
         */
        void docDownload(String userId, String VisitID, String datasource, String ActionID, String CustomerType, String parentDir);
    }
}
