package com.cango.palmcartreasure.trailer.complete;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.WareHouse;

import java.io.File;
import java.util.List;

/**
 * Created by cango on 2017/6/26.
 */

public interface TrailerCompleteContract {
    interface View extends BaseView<TrailerCompleteContract.Presenter> {
        void showIndicator(boolean active);

        void showError(String msg);

        void toOtherUi();

        void showComfirmSuccess(String msg);

        void showWareHouseSuccess(WareHouse wareHouse);

        void showWareHouseError(String msg);

        void showWareHouseNoData();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        /**
         * 送车入库导航，得到库点和原因
         */
        void wareHouse(boolean showIndicatorUI, int agencyID, int caseID, double lat, double lon, String province,int datasource);

        void comfirmTrailerComplete(int userId, double LAT, double LON,int agencyID, int caseID, String isNotifyCustImm,
                                    List<String> answerList, int realSPID, String tmpReason, File file,int datasource);
        void comfirmTrailerCompleteNoFile(int userId, double LAT, double LON,int agencyID, int caseID, String isNotifyCustImm,
                                          List<String> answerList, int realSPID, String tmpReason,int datasource);
    }
}
