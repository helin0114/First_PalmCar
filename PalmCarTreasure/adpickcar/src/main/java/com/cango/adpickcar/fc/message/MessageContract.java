package com.cango.adpickcar.fc.message;

import com.cango.adpickcar.base.BasePresenter;
import com.cango.adpickcar.base.BaseView;
import com.cango.adpickcar.model.MessageList;

import java.util.List;

/**
 * Created by cango on 2017/5/8.
 */

public interface MessageContract {
    interface View extends BaseView<Presenter> {
        void showMessagesIndicator(boolean active);

        void showMessagesError();

        void showMessages(List<MessageList.DataBean.MessageListBean> dataBeanList);

        void showMessageDetailUi(int id);

        void showMessageSuccess(boolean isSuccess, String message);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void openDetailTask(int id);

        void loadMessages(boolean showRefreshLoadingUI, int status, int pageCount, int pageSize);

        void postMessageRead(int messageID);
    }
}
