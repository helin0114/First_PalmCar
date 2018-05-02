package com.cango.adpickcar.fc.download;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;

import java.util.List;

/**
 * Created by cango on 2017/5/8.
 */

public interface DownloadContract {
    interface View extends BaseView<Presenter> {
        void showDatasIndicator(boolean active);

        void showDatasError();

        void showDatas(List<String> datas);

        void showNoDatas();

        void showDataDetailUi(int id);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadDtas(boolean showRefreshLoadingUI, int pageCount, int pageSize);
    }
}
