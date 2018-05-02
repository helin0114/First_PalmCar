package com.cango.adpickcar.fc.message;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.baseAdapter.OnLoadMoreListener;
import com.cango.adpickcar.model.MessageList;
import com.cango.adpickcar.util.CommUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 消息界面view
 */
public class MessageFragment extends BaseFragment implements MessageContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar_message)
    Toolbar mToolbar;
    @BindView(R.id.swipeRefreshLayout_message)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView_message)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    private MessageActivity mActivity;
    private MessageContract.Presenter mPresenter;
    private MessageAdapter mAdapter;
    private int mPageCount = 1, mTempPageCount = 2;
    static int PAGE_SIZE = 10;
    private boolean isLoadMore;
    private List<MessageList.DataBean.MessageListBean> dataBeanList = new ArrayList<>();
    private MessageItemDialogFragment mDialog;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!CommUtil.checkIsNull(mPresenter))
            mPresenter.onDetach();
    }

    @Override
    protected void initView() {
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_title_back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new MessageAdapter(mActivity, dataBeanList, true);
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                mPresenter.loadMessages(false, -1, mPageCount, PAGE_SIZE);
            }
        });
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<MessageList.DataBean.MessageListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, MessageList.DataBean.MessageListBean data, int position) {
                showMessageDialog(data);
                //1-未读 / 2-已读
                String status = data.getStatus();
                if ("1".equals(status)) {
                    data.setStatus("2");
                    mAdapter.notifyDataSetChanged();
                    //提交已读信息
                    mPresenter.postMessageRead(data.getMessageID());
                } else {

                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.start();
        mPresenter.loadMessages(true, -1, mPageCount, PAGE_SIZE);
    }

    @Override
    protected void initData() {
        mActivity = (MessageActivity) getActivity();
    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMessagesIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMessagesError() {
        if (isLoadMore) {
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessages(List<MessageList.DataBean.MessageListBean> dataBeanList) {
        if (isLoadMore) {
            mTempPageCount++;
            mAdapter.setLoadMoreData(dataBeanList);
        } else {
//            mAdapter.setNewData(dataBeanList);
            mAdapter.setNewDataNoError(dataBeanList);
        }
        if (dataBeanList.size() < PAGE_SIZE) {
            mAdapter.setLoadEndView(R.layout.load_end_layout);
        }

    }

    @Override
    public void showMessageDetailUi(int id) {

    }

    @Override
    public void showMessageSuccess(boolean isSuccess, String message) {
        Logger.d("showMessageSuccess");
        if (isSuccess){
            EventBus.getDefault().post(new MessageEvent(message));
        }else {

        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        dataBeanList.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        mPresenter.loadMessages(true, -1, mPageCount, PAGE_SIZE);
    }

    /**
     * 展示消息详情
     * @param messageListBean
     */
    private void showMessageDialog(MessageList.DataBean.MessageListBean messageListBean) {
//        if (CommUtil.checkIsNull(mDialog)) {
            mDialog = MessageItemDialogFragment.getInstance(messageListBean);
//        }
        if (mDialog.isVisible()) {

        } else {
            mDialog.show(getFragmentManager(), "DotDialog");
        }
    }

    private void closeMessageDialog() {
        if (CommUtil.checkIsNull(mDialog)) {

        } else {
            if (mDialog.isVisible()) {
                mDialog.dismiss();
            }
        }
    }
}
