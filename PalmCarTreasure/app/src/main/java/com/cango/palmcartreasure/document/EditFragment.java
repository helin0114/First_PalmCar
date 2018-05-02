package com.cango.palmcartreasure.document;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.TrailerTaskService;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.model.EditEvent;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditFragment extends BaseFragment {

    public static final String BEAN = "bean";
    @BindView(R.id.toolbar_edit)
    Toolbar mToolbar;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    @OnClick({R.id.tv_toolbar_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_right:
                checkComment();
                break;
        }
    }

    private EditActivity mActivity;
    private TrailerTaskService mService;
    private TypeTaskData.DataBean.TaskListBean mTaskListBean;
    private Subscription subscription;

    public static EditFragment newInstance(TypeTaskData.DataBean.TaskListBean bean) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putParcelable(BEAN, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!CommUtil.checkIsNull(subscription))
            subscription.unsubscribe();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_edit;
    }

    @Override
    protected void initView() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity = (EditActivity) getActivity();
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mLoadView.smoothToHide();
    }

    @Override
    protected void initData() {
        mActivity = (EditActivity) getActivity();
        mService = NetManager.getInstance().create(TrailerTaskService.class);
        mTaskListBean = getArguments().getParcelable(BEAN);
    }

    private void checkComment() {
        String comment = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            ToastUtils.showShort(R.string.please_input_note);
        } else {
            if (isAdded()) {
                mLoadView.smoothToShow();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("userid", MtApplication.mSPUtils.getInt(Api.USERID));
                objectMap.put("agencyID", mTaskListBean.getAgencyID());
                objectMap.put("applyID", mTaskListBean.getApplyID());
                objectMap.put("applyCD", mTaskListBean.getApplyCD());
                objectMap.put("caseID", mTaskListBean.getCaseID());
                objectMap.put("datasource",mTaskListBean.getDatasource());
                objectMap.put("comment", comment);
                subscription = mService.agencySave(objectMap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<TaskAbandon>() {
                            @Override
                            protected void _onNext(TaskAbandon o) {
                                if (isAdded()) {
                                    mLoadView.smoothToHide();
                                    boolean isSuccess = o.getCode() == 0;
                                    if (isSuccess) {
                                        //填写标注成功，返回，如果前一页面处于拖车信息的话，刷新内容
                                        EventBus.getDefault().post(new EditEvent(""));
                                        mActivity.mSwipeBackHelper.swipeBackward();
                                        MtApplication.clearLastActivity();
                                    } else {
                                        if (!CommUtil.checkIsNull(o.getMsg())) {
                                            ToastUtils.showShort(o.getMsg());
                                        }
                                    }
                                }
                            }

                            @Override
                            protected void _onError() {
                                if (isAdded()) {
                                    mLoadView.smoothToHide();
                                }
                            }
                        });
            }
        }
    }
}
