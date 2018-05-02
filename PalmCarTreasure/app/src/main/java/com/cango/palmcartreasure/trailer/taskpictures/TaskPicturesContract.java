package com.cango.palmcartreasure.trailer.taskpictures;

import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.base.BaseView;
import com.cango.palmcartreasure.model.TackPictureInfo;

/**
 * Created by dell on 2018/3/14.
 */

public interface TaskPicturesContract {
    interface View extends BaseView<Presenter> {
        void showLoadingView(boolean isShow);

        void showNoData();

        void showError();

        boolean isActive();

        void success(TackPictureInfo pictureInfo);

    }

    interface Presenter extends BasePresenter {
        void getErpimageinfo(int datasource, String applyCd);
    }
}
