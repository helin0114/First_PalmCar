package com.cango.palmcartreasure.trailer.checkorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.model.CheckOrderData;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.trailer.complete.TrailerCompleteActivity;
import com.cango.palmcartreasure.trailer.complete.TrailerCompleteFragment;
import com.cango.palmcartreasure.util.BarUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by cango on 2017/9/6.
 */

public class CheckOrderFragment extends BaseFragment implements CheckOrderContract.View {
    @BindView(R.id.toolbar_order)
    Toolbar mToolbar;
    @BindView(R.id.rg_first)
    RadioGroup rgFirst;
    @BindView(R.id.rb_first_one)
    RadioButton rbFirstOne;
    @BindView(R.id.rb_first_two)
    RadioButton rbFirstTwo;
    @BindView(R.id.rb_first_three)
    RadioButton rbFirstThree;
    @BindView(R.id.rg_second)
    RadioGroup rgSecond;
    @BindView(R.id.rb_second_one)
    RadioButton rbSecondOne;
    @BindView(R.id.rb_second_two)
    RadioButton rbSecondTwo;
    @BindView(R.id.rb_second_three)
    RadioButton rbSecondThree;
    @BindView(R.id.tv_order_submit)
    TextView tvSubmit;
    @BindView(R.id.ll_order_content)
    LinearLayout llContent;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    @OnClick({R.id.tv_order_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_order_submit:
                if (firstSelect == answerOne && seconSelect == answerTwo) {
                    //选择正确跳转到填写页面
                    Intent completeIntent = new Intent(mActivity, TrailerCompleteActivity.class);
                    completeIntent.putExtra(TrailerCompleteFragment.TYPE, mType);
                    completeIntent.putExtra(TrailerCompleteFragment.IMG_PATH, mImgPath);
                    completeIntent.putExtra(TrailerCompleteFragment.LAT, mLat);
                    completeIntent.putExtra(TrailerCompleteFragment.LON, mLon);
                    completeIntent.putExtra(TrailerCompleteFragment.PROVINCE, mProvince);
                    completeIntent.putExtra(TrailerCompleteFragment.TASKLISTBEAN, mTaskListBean);
                    mActivity.mSwipeBackHelper.forward(completeIntent);
                } else if (firstSelect == answerOne && seconSelect != answerTwo) {
//                    ToastUtils.showShort(R.string.please_check_number);
                    new AlertDialog.Builder(mActivity)
                            .setMessage(R.string.please_check_number)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create().show();
                } else if (firstSelect != answerOne && seconSelect == answerTwo) {
//                    ToastUtils.showShort(R.string.please_check_name);
                    new AlertDialog.Builder(mActivity)
                            .setMessage(R.string.please_check_name)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create().show();
                } else {
//                    ToastUtils.showShort(R.string.please_check_message);
                    new AlertDialog.Builder(mActivity)
                            .setMessage(R.string.please_check_message)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create().show();
                }
                break;
        }
    }

    private CheckOrderContract.Presenter mPresenter;
    private CheckOrderActivity mActivity;
    //0:首页进入；1：详情页面进入
    private String mType, mImgPath, mProvince;
    private double mLat, mLon;
    private TypeTaskData.DataBean.TaskListBean mTaskListBean;
    private int answerOne = -1, answerTwo = -1;
    private int firstSelect = -1, seconSelect = -1;

    public static CheckOrderFragment getInstance(String type, String imgPath, double lat, double lon,
                                                 String province, TypeTaskData.DataBean.TaskListBean taskListBean) {
        CheckOrderFragment checkOrderFragment = new CheckOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TrailerCompleteFragment.TYPE, type);
        bundle.putString(TrailerCompleteFragment.IMG_PATH, imgPath);
        bundle.putDouble(TrailerCompleteFragment.LAT, lat);
        bundle.putDouble(TrailerCompleteFragment.LON, lon);
        bundle.putString(TrailerCompleteFragment.PROVINCE, province);
        bundle.putParcelable(TrailerCompleteFragment.TASKLISTBEAN, taskListBean);
        checkOrderFragment.setArguments(bundle);
        return checkOrderFragment;
    }

    @Override
    public void setPresenter(CheckOrderContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_check_order;
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
        showIndicator(false);
        llContent.setVisibility(View.GONE);
        rgFirst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_first_one) {
                    firstSelect = 0;
                } else if (checkedId == R.id.rb_first_two) {
                    firstSelect = 1;
                } else if (checkedId == R.id.rb_first_three) {
                    firstSelect = 2;
                } else {
                    firstSelect = -1;
                }
            }
        });
        rgSecond.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_second_one) {
                    seconSelect = 0;
                } else if (checkedId == R.id.rb_second_two) {
                    seconSelect = 1;
                } else if (checkedId == R.id.rb_second_three) {
                    seconSelect = 2;
                } else {
                    seconSelect = -1;
                }
            }
        });
        mPresenter.getCheckOrderData(true, MtApplication.mSPUtils.getInt(Api.USERID), mTaskListBean.getAgencyID(),
                mTaskListBean.getApplyCD(), mTaskListBean.getCaseID(),mTaskListBean.getDatasource());
    }

    @Override
    protected void initData() {
        mActivity = (CheckOrderActivity) getActivity();
        mType = getArguments().getString(TrailerCompleteFragment.TYPE);
        mImgPath = getArguments().getString(TrailerCompleteFragment.IMG_PATH);
        mLat = getArguments().getDouble(TrailerCompleteFragment.LAT);
        mLon = getArguments().getDouble(TrailerCompleteFragment.LON);
        mProvince = getArguments().getString(TrailerCompleteFragment.PROVINCE);
        mTaskListBean = getArguments().getParcelable(TrailerCompleteFragment.TASKLISTBEAN);
    }

    @Override
    public void showIndicator(boolean active) {
        if (active) {
            mLoadView.smoothToShow();
        } else
            mLoadView.smoothToHide();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void toOtherUi() {

    }

    @Override
    public void showDataSuccess(CheckOrderData data) {
        answerOne = data.getData().getQuestionList().get(0).getValue();
        answerTwo = data.getData().getQuestionList().get(1).getValue();
        llContent.setVisibility(View.VISIBLE);
        updateUI(data.getData());
    }

    private void updateUI(CheckOrderData.DataBean dataBean) {
        List<String> firstList = dataBean.getQuestionList().get(0).getAnswerList();
        rbFirstOne.setText(" " + firstList.get(0));
        rbFirstTwo.setText(" " + firstList.get(1));
        rbFirstThree.setText(" " + firstList.get(2));
        List<String> secondList = dataBean.getQuestionList().get(1).getAnswerList();
        rbSecondOne.setText(" " + secondList.get(0));
        rbSecondTwo.setText(" " + secondList.get(1));
        rbSecondThree.setText(" " + secondList.get(2));
    }

    @Override
    public void showDataError(String msg) {
        llContent.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoData() {
        llContent.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
        super.onDestroy();
    }
}
