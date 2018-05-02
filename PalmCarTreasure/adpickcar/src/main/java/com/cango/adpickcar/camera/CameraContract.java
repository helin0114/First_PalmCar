package com.cango.adpickcar.camera;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.PhotoResult;

import java.io.File;

/**
 * Created by dell on 2017/12/5.
 */

public class CameraContract {
    interface View extends BaseView<Presenter> {
        void showIndicator(boolean active);
        void openOtherUi();
        void savePhotoResult(boolean isSuccess, PhotoResult mPhotoResult);
    }

    interface Presenter extends BasePresenter {
        
        void saveDisCarInfo(boolean showRefreshLoadingUI, String UserID, String DisCarID, String PicGroup,
                            String SubCategory, String SubID, String PicFileID, File file);
    }
}
