package com.cango.palmcartreasure.trailer.complete;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.model.CheckPointOkToDetailEvent;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.model.WareHouse;
import com.cango.palmcartreasure.util.BarUtil;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.SizeUtil;
import com.cango.palmcartreasure.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class TrailerCompleteFragment extends BaseFragment implements TrailerCompleteContract.View {

    /**
     * 判断是从首页还是详情页面进入的 type
     */
    public static final String TYPE = "type";
    public static final String IMG_PATH = "img_path";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String PROVINCE = "province";
    public static final String TASKLISTBEAN = "tasklistbean";
    @BindView(R.id.toolbar_complete)
    Toolbar mToolbar;
    @BindView(R.id.rg_first)
    RadioGroup rgFirst;
    @BindView(R.id.rg_second)
    RadioGroup rgSecond;
    @BindView(R.id.rg_third)
    RadioGroup rgThird;
    @BindView(R.id.cb_fourth_one)
    CheckBox cbFourthOne;
    @BindView(R.id.cb_fourth_two)
    CheckBox cbFourthTwo;
    @BindView(R.id.cb_fourth_three)
    CheckBox cbFourthThree;
    @BindView(R.id.cb_fifth_one)
    CheckBox cbFifthOne;
    @BindView(R.id.cb_fifth_two)
    CheckBox cbFifthTwo;
    @BindView(R.id.cb_fifth_three)
    CheckBox cbFifthThree;
    @BindView(R.id.cb_fifth_four)
    CheckBox cbFifthFour;
    @BindView(R.id.ll_sixth)
    LinearLayout llSixth;
    @BindView(R.id.rg_sixth)
    RadioGroup rgSixth;
    @BindView(R.id.rb_sixth_one)
    RadioButton rbSixthOne;
    @BindView(R.id.rb_sixth_two)
    RadioButton rbSixthTwo;
    @BindView(R.id.rb_sixth_three)
    RadioButton rbSixthThree;
    @BindView(R.id.rg_seventh)
    RadioGroup rgSeventh;
    @BindView(R.id.tv_complete_confirm)
    TextView tvConfirm;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    public boolean isCanDoConfirm = true;

    @OnClick({R.id.tv_complete_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_complete_confirm:
                if (isCanDoConfirm) {
                    Logger.d("isCanDoConfirm");
                    if (checkQuestonIsOver()) {
                        isCanDoConfirm = false;
//                        zipPicture();
                        if (isAdded()) {
                            mLoadView.smoothToShow();
                        }
                        upLoadImageNoFile();
                    } else {
                        ToastUtils.showShort("请填写全部问卷！");
                    }
                }
                break;
        }
    }

    //0:首页进入；1：详情页面进入
    private String mType, mImgPath, mProvince;
    private String[] sevenResults = new String[]{"车辆故障", "库管暂时无法接车", "集中派送", "其他"};
    private double mLat, mLon;
    private TrailerCompleteActivity mActivity;
    private TrailerCompleteContract.Presenter mPresenter;
    private TypeTaskData.DataBean.TaskListBean mTaskListBean;
    private List<WareHouse.DataBean> mWareHouseList;
    private List<String> mAnswerList = new ArrayList<>();
    private String isNotifyCustImm, secondValue, thirdValue, mTmpReason;
    private int mRealSPID = -1;

    public static TrailerCompleteFragment getInstance(String type, String imgPath, double lat, double lon, String province, TypeTaskData.DataBean.TaskListBean taskListBean) {
        TrailerCompleteFragment trailerCompleteFragment = new TrailerCompleteFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        bundle.putString(IMG_PATH, imgPath);
        bundle.putDouble(LAT, lat);
        bundle.putDouble(LON, lon);
        bundle.putString(PROVINCE, province);
        bundle.putParcelable(TASKLISTBEAN, taskListBean);
        trailerCompleteFragment.setArguments(bundle);
        return trailerCompleteFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_trailer_complete;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!CommUtil.checkIsNull(mPresenter))
            mPresenter.onDetach();
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
        tvConfirm.setVisibility(View.GONE);
        llSixth.setVisibility(View.GONE);
        mPresenter.wareHouse(true, mTaskListBean.getAgencyID(), mTaskListBean.getCaseID(), mLat, mLon, mProvince,mTaskListBean.getDatasource());

        //动态的生成radiobutton
        final List<RadioButton> radioButtons = new ArrayList<>();
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < 4; i++) {
            final RadioButton radioButton = new RadioButton(mActivity);
            radioButton.setButtonDrawable(getResources().getDrawable(R.drawable.radio_button_selector));
            radioButton.setPadding(0, SizeUtil.dp2px(mActivity, 5), 0, SizeUtil.dp2px(mActivity, 5));
            radioButton.setTextColor(getResources().getColor(R.color.mt9c9c9c));
            radioButton.setTextSize(13);
            radioButton.setText(sevenResults[i]);
            radioButton.setLayoutParams(layoutParams);
            final int finalI = i;
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
//                        mTmpReason = radioButton.getText().toString().trim();
                        mTmpReason = "QN01,5;" + (finalI + 1);
                    }
                }
            });
            radioButtons.add(radioButton);
        }

        rgFirst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_first_one) {
                    isNotifyCustImm = "0";
                } else if (checkedId == R.id.rb_first_two) {
                    isNotifyCustImm = "1";
                } else {
                }
            }
        });
        rgSecond.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_second_one) {
                    secondValue = "1";
                } else if (checkedId == R.id.rb_second_two) {
                    secondValue = "2";
                } else if (checkedId == R.id.rb_second_three) {
                    secondValue = "3";
                } else if (checkedId == R.id.rb_second_four) {
                    secondValue = "4";
                } else {
                }
            }
        });
        rgThird.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_third_one:
                        thirdValue = "1";
                        break;
                    case R.id.rb_third_two:
                        thirdValue = "2";
                        break;
                    case R.id.rb_third_three:
                        thirdValue = "3";
                        break;
                    case R.id.rb_third_four:
                        thirdValue = "4";
                        break;
                }
            }
        });
        rgSixth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (mWareHouseList.size() == 1) {
                    if (checkedId == R.id.rb_sixth_one) {
                        mRealSPID = mWareHouseList.get(0).getSpid();
                        if (mWareHouseList.get(0).isTemp()) {
                            for (RadioButton bean : radioButtons) {
                                rgSeventh.addView(bean);
                            }
                        } else {
                            rgSeventh.removeAllViews();
                        }
                    }
                } else if (mWareHouseList.size() == 2) {
                    if (checkedId == R.id.rb_sixth_one) {
                        mRealSPID = mWareHouseList.get(0).getSpid();
                        if (mWareHouseList.get(0).isTemp()) {
                            for (RadioButton bean : radioButtons) {
                                rgSeventh.addView(bean);
                            }
                        } else {
                            rgSeventh.removeAllViews();
                        }
                    } else if (checkedId == R.id.rb_sixth_two) {
                        mRealSPID = mWareHouseList.get(1).getSpid();
                        if (mWareHouseList.get(1).isTemp()) {
                            for (RadioButton bean : radioButtons) {
                                rgSeventh.addView(bean);
                            }
                        } else {
                            rgSeventh.removeAllViews();
                        }
                    } else {

                    }
                } else if (mWareHouseList.size() == 3) {
                    if (checkedId == R.id.rb_sixth_one) {
                        mRealSPID = mWareHouseList.get(0).getSpid();
                        if (mWareHouseList.get(0).isTemp()) {
                            for (RadioButton bean : radioButtons) {
                                rgSeventh.addView(bean);
                            }
                        } else {
                            rgSeventh.removeAllViews();
                        }
                    } else if (checkedId == R.id.rb_sixth_two) {
                        mRealSPID = mWareHouseList.get(1).getSpid();
                        if (mWareHouseList.get(1).isTemp()) {
                            for (RadioButton bean : radioButtons) {
                                rgSeventh.addView(bean);
                            }
                        } else {
                            rgSeventh.removeAllViews();
                        }
                    } else if (checkedId == R.id.rb_sixth_three) {
                        mRealSPID = mWareHouseList.get(2).getSpid();
                        if (mWareHouseList.get(2).isTemp()) {
                            for (RadioButton bean : radioButtons) {
                                rgSeventh.addView(bean);
                            }
                        } else {
                            rgSeventh.removeAllViews();
                        }
                    } else {

                    }
                } else {

                }
            }
        });

        //防抖动
//        Observable.create(new MultiClickSubscribe(tvConfirm))
//                .throttleFirst(2, TimeUnit.SECONDS)
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer s) {
//                        if (checkQuestonIsOver()) {
//                            zipPicture();
//                        } else {
//                            ToastUtils.showShort("请填写全部问卷！");
//                        }
//                    }
//                });
    }

    @Override
    protected void initData() {
        mType = getArguments().getString(TYPE);
        mImgPath = getArguments().getString(IMG_PATH);
        mLat = getArguments().getDouble(LAT);
        mLon = getArguments().getDouble(LON);
        mProvince = getArguments().getString(PROVINCE);
        mTaskListBean = getArguments().getParcelable(TASKLISTBEAN);
        mActivity = (TrailerCompleteActivity) getActivity();
    }

    @Override
    public void setPresenter(TrailerCompleteContract.Presenter presenter) {
        mPresenter = presenter;
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
        if (!CommUtil.checkIsNull(msg)) {
            ToastUtils.showShort(msg);
        }
        isCanDoConfirm = true;
    }

    @Override
    public void toOtherUi() {

    }

    @Override
    public void showComfirmSuccess(String msg) {
        isCanDoConfirm = true;
        if (!CommUtil.checkIsNull(msg)) {
            ToastUtils.showShort(msg);
        }
        if ("0".equals(mType)) {
            //因为首页做了可见刷新所以暂时不加
//            EventBus.getDefault().post(new CheckPointOkToHomeEvent(""));
        } else if ("1".equals(mType)) {
            EventBus.getDefault().post(new CheckPointOkToDetailEvent(""));
        } else {

        }
        //在这里先finishcheckorderactivity然后finish当前的activity并且把acitivylist减少了
        MtApplication.clearSecondLastActivity();
//        mActivity.mSwipeBackHelper.swipeBackward();
        MtApplication.clearLastActivity();
        mActivity.finish();
    }

    @Override
    public void showWareHouseSuccess(WareHouse wareHouse) {
        tvConfirm.setVisibility(View.VISIBLE);
        mWareHouseList = wareHouse.getData();
        if (mWareHouseList.size() > 3) {
        } else {
            llSixth.setVisibility(View.VISIBLE);
            if (mWareHouseList.size() == 1) {
                rbSixthOne.setText(mWareHouseList.get(0).getWarehouseName() + "(" + mWareHouseList.get(0).getDistance() + ")");
                rgSixth.setVisibility(View.VISIBLE);
                rbSixthOne.setVisibility(View.VISIBLE);
                rbSixthTwo.setVisibility(View.GONE);
                rbSixthThree.setVisibility(View.GONE);
            } else if (mWareHouseList.size() == 2) {
                rbSixthOne.setText(mWareHouseList.get(0).getWarehouseName() + "(" + mWareHouseList.get(0).getDistance() + ")");
                rbSixthTwo.setText(mWareHouseList.get(1).getWarehouseName() + "(" + mWareHouseList.get(1).getDistance() + ")");
                rgSixth.setVisibility(View.VISIBLE);
                rbSixthOne.setVisibility(View.VISIBLE);
                rbSixthTwo.setVisibility(View.VISIBLE);
                rbSixthThree.setVisibility(View.GONE);
            } else {
                rbSixthOne.setText(mWareHouseList.get(0).getWarehouseName() + "(" + mWareHouseList.get(0).getDistance() + ")");
                rbSixthTwo.setText(mWareHouseList.get(1).getWarehouseName() + "(" + mWareHouseList.get(1).getDistance() + ")");
                rbSixthThree.setText(mWareHouseList.get(2).getWarehouseName() + "(" + mWareHouseList.get(2).getDistance() + ")");
                rgSixth.setVisibility(View.VISIBLE);
                rbSixthOne.setVisibility(View.VISIBLE);
                rbSixthTwo.setVisibility(View.VISIBLE);
                rbSixthThree.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showWareHouseError(String msg) {
        llSixth.setVisibility(View.GONE);
        rgSixth.setVisibility(View.GONE);
        tvConfirm.setVisibility(View.GONE);
    }

    @Override
    public void showWareHouseNoData() {
        llSixth.setVisibility(View.GONE);
        rgSixth.setVisibility(View.GONE);
        tvConfirm.setVisibility(View.GONE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private boolean checkQuestonIsOver() {

        boolean isCheckFirst = rgFirst.getCheckedRadioButtonId() > 0;

        boolean fourthOne = cbFourthOne.isChecked();
        boolean fourthTwo = cbFourthTwo.isChecked();
        boolean FourthThree = cbFourthThree.isChecked();

        boolean fifthOne = cbFifthOne.isChecked();
        boolean fifthTwo = cbFifthTwo.isChecked();
        boolean fifthThree = cbFifthThree.isChecked();
        boolean fifthFour = cbFifthFour.isChecked();
        if (isCheckFirst && !CommUtil.checkIsNull(secondValue) &&
                !CommUtil.checkIsNull(thirdValue) && (fourthOne || fourthTwo || FourthThree) && (fifthOne || fifthTwo || fifthThree || fifthFour)
                && mRealSPID != -1) {
            for (int i = 0; i < mWareHouseList.size(); i++) {
                if (mWareHouseList.get(i).getSpid() == mRealSPID) {
                    if (mWareHouseList.get(i).isTemp()) {
                        if (CommUtil.checkIsNull(mTmpReason)) {
                            return false;
                        }
                    }
                }
            }
            mAnswerList.clear();
            mAnswerList.add("QN01,1;" + secondValue);
            mAnswerList.add("QN01,2;" + thirdValue);
            String strFourth = "";
            if (fourthOne)
                strFourth += "1,";
            if (fourthTwo)
                strFourth += "2,";
            if (FourthThree)
                strFourth += "3,";
            String strFifth = "";
            if (fifthOne)
                strFifth += "1,";
            if (fifthTwo)
                strFifth += "2,";
            if (fifthThree)
                strFifth += "3,";
            if (fifthFour)
                strFifth += "4,";
            mAnswerList.add("QN01,3;" + strFourth);
            mAnswerList.add("QN01,4;" + strFifth);
            return true;
        }
        return false;
    }

    private void zipPicture() {
        File file = new File(mImgPath);
        Luban.get(mActivity)
                .load(file)                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        if (isAdded()) {
                            mLoadView.smoothToShow();
                        }
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        upLoadImage(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        if (isAdded()) {
                            mLoadView.smoothToHide();
                        }
                        isCanDoConfirm = true;
                    }
                }).launch();    //启动压缩
    }

    private void upLoadImage(File file) {
        mPresenter.comfirmTrailerComplete(MtApplication.mSPUtils.getInt(Api.USERID), mLat, mLon, mTaskListBean.getAgencyID(),
                mTaskListBean.getCaseID(), isNotifyCustImm, mAnswerList, mRealSPID, mTmpReason, file,mTaskListBean.getDatasource());
    }
    private void upLoadImageNoFile(){
        mPresenter.comfirmTrailerCompleteNoFile(MtApplication.mSPUtils.getInt(Api.USERID), mLat, mLon, mTaskListBean.getAgencyID(),
                mTaskListBean.getCaseID(), isNotifyCustImm, mAnswerList, mRealSPID, mTmpReason,mTaskListBean.getDatasource());
    }
}
