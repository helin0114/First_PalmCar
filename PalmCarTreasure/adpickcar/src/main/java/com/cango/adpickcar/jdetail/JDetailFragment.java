package com.cango.adpickcar.jdetail;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.jdetail.jbasicinfo.JBasicInfoFragment;
import com.cango.adpickcar.jdetail.jcarimageinfo.JCarImageFragment;
import com.cango.adpickcar.jdetail.jcarinfo.JCarInfoFragment;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.main.MainFragment;
import com.cango.adpickcar.model.DeliveryTaskList;
import com.cango.adpickcar.model.EventModel.RefreshMainEvent;
import com.cango.adpickcar.model.JCarBaseInfo;
import com.cango.adpickcar.model.JCarFiles;
import com.cango.adpickcar.model.JCarInfo;
import com.cango.adpickcar.model.PhotoResult;
import com.cango.adpickcar.util.BarUtil;
import com.cango.adpickcar.util.SnackbarUtils;
import com.cango.adpickcar.util.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class JDetailFragment extends BaseFragment implements JDetailContract.View {
    public static JDetailFragment getInstance(DeliveryTaskList.DataBean.CarDeliveryTaskListBean carDeliveryTaskListBean, int type) {
        JDetailFragment jDetailFragment = new JDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("CarDeliveryTaskListBean", carDeliveryTaskListBean);
        bundle.putInt("Type", type);
        jDetailFragment.setArguments(bundle);
        return jDetailFragment;
    }

    @BindView(R.id.toolbar_jdetail)
    Toolbar mToolbar;
    @BindView(R.id.tv_jback)
    TextView tvBack;
    @BindView(R.id.iv_jdetail_basic_info)
    ImageView ivBasic;
    @BindView(R.id.tv_jdetail_basic_info)
    TextView tvBasic;
    @BindView(R.id.iv_jdetail_car_info)
    ImageView ivCar;
    @BindView(R.id.tv_jdetail_car_info)
    TextView tvCar;
    @BindView(R.id.iv_jdetail_image_info)
    ImageView ivImage;
    @BindView(R.id.tv_jdetail_image_info)
    TextView tvImage;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.layout_main_jcar_detail)
    View jCarDetailLayout;

    @OnClick({R.id.ll_jdetail_basic_info, R.id.ll_jdetail_car_info, R.id.ll_jdetail_image_info, R.id.tv_jback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_jdetail_basic_info:
                if (currentPosition == 0) {
                    return;
                }
                currentPosition = 0;
                SelectChildTitle(currentPosition);
                hideFragments();
                showFragment(currentPosition);
                if (mBaseInfo!=null){

                }else {
                    jBasicInfoFragment.getData();
                }
                break;
            case R.id.ll_jdetail_car_info:
                if (currentPosition == 1) {
                    return;
                }
                currentPosition = 1;
                SelectChildTitle(currentPosition);
                hideFragments();
                showFragment(currentPosition);
                if (mJCarDataBean!=null){

                }else {
                    jCarInfoFragment.getData();
                }
//                jCarInfoFragment.getData();
                break;
            case R.id.ll_jdetail_image_info:
                if (currentPosition == 2) {
                    return;
                }
                currentPosition = 2;
                SelectChildTitle(currentPosition);
                hideFragments();
                showFragment(currentPosition);
                if (mCarFilesInfo!=null){

                }else {
                    jCarImageFragment.getData();
                }
//                jCarImageFragment.getData();
                break;
            case R.id.tv_jback:
                tvBack.setEnabled(false);
                mPresenter.RestoreCarDeliveryTask(true,mCarDeliveryTaskListBean.getCDLVTasKID()+"");
                break;
        }
    }

    /**
     * 当前类型，有些可以编辑有些不可以编辑
     */
    public int mCurrentType;
    /**
     * 详情是否可以编辑 false 不可编辑 true 可编辑
     */
    public boolean isEdit;
    private JDetailActivity mActivity;
    public JDetailContract.Presenter mPresenter;
    FragmentManager fm;
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private JBasicInfoFragment jBasicInfoFragment;
    private JCarInfoFragment jCarInfoFragment;
    private JCarImageFragment jCarImageFragment;
    //保存基本信息和物品信息
    private JCarBaseInfo mBaseInfo;
    //保存车辆信息
    private JCarInfo.DataBean mJCarDataBean;
    //保存影像信息
    private JCarFiles mCarFilesInfo;
    public DeliveryTaskList.DataBean.CarDeliveryTaskListBean mCarDeliveryTaskListBean;
    /**
     * 4个fragment的当前选中
     */
    private int currentPosition;
    private int selectColor, noSelectColor;
    private boolean isDoSaveCarBasicItemInfo = true;
    private boolean isDoSaveCarInfo = true;
    private boolean isDoSubmitCarTakeStore = true;
    private int mOnSaveInstanceStatePosition = -1;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_jdetail;
    }

    @Override
    protected void initView() {
        showIndicator(false);
        initToolbar();
        addChildFragment();
        currentPosition = 0;
        SelectChildTitle(currentPosition);
        hideFragments();
        showFragment(0);

        if (mCurrentType != -1) {
            switch (mCurrentType) {
                case MainFragment.DAIJIAOCHE:
                    isEdit = true;
                    tvBack.setVisibility(View.GONE);
                    break;
                case MainFragment.YIJIAOCHE:
                    if(mCarDeliveryTaskListBean != null && mCarDeliveryTaskListBean.getCanReturn()){
                        tvBack.setVisibility(View.VISIBLE);
                    }else{
                        tvBack.setVisibility(View.GONE);
                    }
                    isEdit = false;
                    break;
                case MainFragment.JIAOCHESHIBAI:
                    tvBack.setVisibility(View.GONE);
                    isEdit = false;
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
    }

    @Override
    protected void initData() {
        mActivity = (JDetailActivity) getActivity();
        jCarDetailLayout = mActivity.findViewById(R.id.layout_main_jcar_detail);
        mCarDeliveryTaskListBean = getArguments().getParcelable("CarDeliveryTaskListBean");
        mCurrentType = getArguments().getInt("Type");
        selectColor = getResources().getColor(R.color.colorPrimary);
        noSelectColor = getResources().getColor(R.color.ad888888);
    }

    private void initToolbar() {
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("show", true);
        outState.putInt("currentPosition", currentPosition);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mOnSaveInstanceStatePosition = savedInstanceState.getInt("currentPosition",-1);
//            boolean isShow = savedInstanceState.getBoolean("show", false);
//            if (isShow)
//                ToastUtils.showLong("运行内存不足，请重新打开app");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mOnSaveInstanceStatePosition > 0){
            currentPosition = mOnSaveInstanceStatePosition;
            SelectChildTitle(currentPosition);
            hideFragments();
            showFragment(currentPosition);
            switch (mOnSaveInstanceStatePosition) {
                case 1:
                    jCarInfoFragment.getData();
                    break;
                case 2:
                    jCarImageFragment.getData();
                    break;
            }
            mOnSaveInstanceStatePosition = -1;
        }
    }

    private void addChildFragment() {
        //就是为了防止页面被回收后重新走initview方法，先删除一些
        mFragments.clear();
        fm = getChildFragmentManager();
        Fragment baseRemove = fm.findFragmentByTag(JBasicInfoFragment.class.getSimpleName());
        Fragment CarInfoRemove = fm.findFragmentByTag(JCarInfoFragment.class.getSimpleName());
        Fragment CarImageRemove = fm.findFragmentByTag(JCarImageFragment.class.getSimpleName());
        FragmentTransaction ftRemove = fm.beginTransaction();
        if (baseRemove!=null)
            ftRemove.remove(baseRemove);
        if (CarInfoRemove!=null)
            ftRemove.remove(CarInfoRemove);
        if (CarImageRemove!=null)
            ftRemove.remove(CarImageRemove);
        ftRemove.commit();

        jBasicInfoFragment = JBasicInfoFragment.getInstance(mCarDeliveryTaskListBean);
        jCarInfoFragment = JCarInfoFragment.getInstance(mCarDeliveryTaskListBean);
        jCarImageFragment = JCarImageFragment.getInstance(mCarDeliveryTaskListBean);
        mFragments.add(jBasicInfoFragment);
        mFragments.add(jCarInfoFragment);
        mFragments.add(jCarImageFragment);
        fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment base : mFragments) {
            ft.add(R.id.fl_jdetail_child_container, base, base.getClass().getSimpleName());
        }
        ft.commit();
    }

    public void showFragment(int position) {
        hideFragments();
        Fragment fragment = mFragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : mFragments) {
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    private void SelectChildTitle(int position) {
        switch (position) {
            case 0:
                ivBasic.setImageResource(R.drawable.jibenxinxi_on);
                tvBasic.setTextColor(selectColor);
                ivCar.setImageResource(R.drawable.cheliangxinxi_off);
                tvCar.setTextColor(noSelectColor);
                ivImage.setImageResource(R.drawable.yingxiangxinxi_off);
                tvImage.setTextColor(noSelectColor);
                break;
            case 1:
                ivBasic.setImageResource(R.drawable.jibenxinxi_off);
                tvBasic.setTextColor(noSelectColor);
                ivCar.setImageResource(R.drawable.cheliangxinxi_on);
                tvCar.setTextColor(selectColor);
                ivImage.setImageResource(R.drawable.yingxiangxinxi_off);
                tvImage.setTextColor(noSelectColor);
                break;
            case 2:
                ivBasic.setImageResource(R.drawable.jibenxinxi_off);
                tvBasic.setTextColor(noSelectColor);
                ivCar.setImageResource(R.drawable.cheliangxinxi_off);
                tvCar.setTextColor(noSelectColor);
                ivImage.setImageResource(R.drawable.yingxiangxinxi_on);
                tvImage.setTextColor(selectColor);
                break;
        }
    }

    @Override
    public void setPresenter(JDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showIndicator(boolean active) {
        if (active) {
            mLoadView.smoothToShow();
        } else {
            mLoadView.smoothToHide();
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showSuccess(boolean isSuccess, String message) {

    }

    @Override
    public void openOtherUi() {
//        ToastUtils.showShort("认证失败，请重新登录");
        SnackbarUtils.showLongDisSnackBar(jCarDetailLayout, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showBaseInfo(JCarBaseInfo baseInfo) {
        mBaseInfo = baseInfo;
        jBasicInfoFragment.updateUI(baseInfo);
    }

    @Override
    public void showBaseInfoError() {
        jBasicInfoFragment.showError();
    }

    @Override
    public void showBaseInfoNoData() {
        jBasicInfoFragment.showNoData();
    }

    @Override
    public void showCarInfo(JCarInfo.DataBean carDataBean) {
        mJCarDataBean = carDataBean;
        jCarInfoFragment.updateUI(carDataBean);
    }

    @Override
    public void showCarInfoError() {
        jCarInfoFragment.showError();
    }

    @Override
    public void showCarInfoNoData() {
        jCarInfoFragment.showNoData();
    }

    @Override
    public void showCarFilesInfo(JCarFiles carFilesInfo) {
        mCarFilesInfo = carFilesInfo;
        jCarImageFragment.updateUI(carFilesInfo);
    }

    @Override
    public void showCarFilesInfoError() {
        jCarImageFragment.showError();
    }

    @Override
    public void showCarFilesInfoNoData() {
        jCarImageFragment.showNoData();
    }

    @Override
    public void showSaveDisCarInfo(boolean isSuccess, PhotoResult photoResult) {
        isDoSaveDisCarFile = true;
        jCarImageFragment.updateAddPhoto(isSuccess, photoResult);
    }

    @Override
    public void showDeleteDisCarInfo(boolean isSuccess, String message) {
        isDoDeleteDisCarFile = true;
        jCarImageFragment.updateDeletePhoto(isSuccess, message);
    }

    @Override
    public void showJiaoCheTaskStatus(boolean isSuccess, String message, int type) {
        switch (type) {
            case 0:
                jBasicInfoFragment.showOKResult(isSuccess, message);
                break;
            case 1:
                jBasicInfoFragment.showFailureResult(isSuccess, message);
                break;
        }
    }

    @Override
    public void showRestoreTaskStatus(boolean isSuccess, String message) {
        tvBack.setEnabled(true);
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(jCarDetailLayout, message);
        if (isSuccess) {
            EventBus.getDefault().post(new RefreshMainEvent("showRestoreTaskStatus"));
            mActivity.finish();
        }
    }

    private boolean isDoSaveDisCarFile = true;

    public void zipPicture(String mImgPath, final String UserID, final String DisCarID, final String PicGroup, final String SubCategory,
                           final String SubID, final String PicFileID) {
        if (!isDoSaveDisCarFile) {
            return;
        }
        isDoSaveDisCarFile = false;
        final File tempFile = new File(mImgPath);
        Luban.with(mActivity)
                .load(tempFile)                     //传人要压缩的图片
                .ignoreBy(100)
//                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        if (isAdded()) {
                            showIndicator(true);
                        }
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        mPresenter.saveDisCarInfo(true, UserID, DisCarID, PicGroup, SubCategory, SubID, PicFileID, file);
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        if (isAdded()) {
                            showIndicator(false);
                            isDoSaveDisCarFile = true;
                        }
                    }
                }).launch();    //启动压缩
    }

    private boolean isDoDeleteDisCarFile = true;

    public void DeletePhoto(int type, int subType, boolean showRefreshLoadingUI, String UserID, String PicFileID) {
        if (!isDoDeleteDisCarFile) {
            return;
        }
        isDoDeleteDisCarFile = false;
        mPresenter.deleteDisCarFile(showRefreshLoadingUI, UserID, PicFileID);
    }
}
