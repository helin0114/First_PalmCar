package com.cango.adpickcar.detail;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.detail.basicinfo.BasicInfoFragment;
import com.cango.adpickcar.detail.carinfo.CarInfoFragment;
import com.cango.adpickcar.detail.imageinfo.ImageInfoFragment;
import com.cango.adpickcar.detail.iteminfo.ItemInfoFragment;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.main.MainFragment;
import com.cango.adpickcar.model.BaseInfo;
import com.cango.adpickcar.model.CarFilesInfo;
import com.cango.adpickcar.model.CarInfo;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.model.EventModel.RefreshMainEvent;
import com.cango.adpickcar.model.PhotoResult;
import com.cango.adpickcar.util.BarUtil;
import com.cango.adpickcar.util.SnackbarUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class DetailFragment extends BaseFragment implements DetailContract.View {

    public static DetailFragment getInstance(CarTakeTaskList.DataBean.CarTakeTaskListBean carTakeTaskListBean, int type) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("CarTakeTaskListBean", carTakeTaskListBean);
        bundle.putInt("Type", type);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.d("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @BindView(R.id.toolbar_detail)
    Toolbar mToolbar;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.iv_detail_basic_info)
    ImageView ivBasic;
    @BindView(R.id.tv_detail_basic_info)
    TextView tvBasic;
    @BindView(R.id.iv_detail_item_info)
    ImageView ivItem;
    @BindView(R.id.tv_detail_item_info)
    TextView tvItem;
    @BindView(R.id.iv_detail_car_info)
    ImageView ivCar;
    @BindView(R.id.tv_detail_car_info)
    TextView tvCar;
    @BindView(R.id.iv_detail_image_info)
    ImageView ivImage;
    @BindView(R.id.tv_detail_image_info)
    TextView tvImage;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.layout_main_detail)
    View mainView;

    @OnClick({R.id.ll_detail_basic_info, R.id.ll_detail_item_info, R.id.ll_detail_car_info, R.id.ll_detail_image_info,
            R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_detail_basic_info:
                if (currentPosition == 0) {
                    return;
                }
                currentPosition = 0;
                SelectChildTitle(currentPosition);
                hideFragments();
                showFragment(currentPosition);
                if (mBaseInfo!=null){

                }else {
                    basicInfoFragment.getData();

                }
                if (isEdit) {
                    tvSave.setVisibility(View.VISIBLE);
                    tvSave.setText("保存");
                } else {
                    tvSave.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.ll_detail_item_info:
                if (currentPosition == 1) {
                    return;
                }
                currentPosition = 1;
                SelectChildTitle(currentPosition);
                hideFragments();
                showFragment(currentPosition);
                if (mBaseInfo!=null){

                }else {
                    itemInfoFragment.getData();
                }
                if (isEdit) {
                    tvSave.setVisibility(View.VISIBLE);
                    tvSave.setText("保存");
                } else {
                    tvSave.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.ll_detail_car_info:
                if (currentPosition == 2) {
                    return;
                }
                currentPosition = 2;
                SelectChildTitle(currentPosition);
                hideFragments();
                showFragment(currentPosition);
                if (mCarInfo!=null){

                }else {
                    carInfoFragment.getData();
                }
//                carInfoFragment.getData();
                //杨丽杨说这里不要保存
//                tvSave.setText("保存");
                tvSave.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_detail_image_info:
                if (currentPosition == 3) {
                    return;
                }
                currentPosition = 3;
                SelectChildTitle(currentPosition);
                hideFragments();
                showFragment(currentPosition);
                if (mCarFilesInfo!=null){

                }else {
                    imageInfoFragment.getData();
                }
//                imageInfoFragment.getData();
                if (isEdit) {
                    tvSave.setVisibility(View.VISIBLE);
                    tvSave.setText("提交");
                } else {
                    tvSave.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.tv_save:
                switch (currentPosition) {
                    //上传基本信息或者物品信息的对象
                    case 0:
                    case 1:
                        if (checkBasicAndItemParamsMust()) {
                            if (isDoSaveCarBasicItemInfo) {
                                isDoSaveCarBasicItemInfo = false;
                                mBaseInfo.getData().setUserID(ADApplication.mSPUtils.getString(Api.USERID));
                                mPresenter.saveCarBasicItemInfo(true, mBaseInfo.getData());
                            }
                        }
                        break;
                    //保存车辆信息
                    case 2:
//                        saveCarInfo();
                        break;
                    //提交
                    case 3:
                        if (checkBasicAndItemParamsMust()) {
                            if (isDoSubmitCarTakeStore) {
                                isDoSubmitCarTakeStore = false;
                                mBaseInfo.getData().setUserID(ADApplication.mSPUtils.getString(Api.USERID));
                                mPresenter.submitCarTakeStore(true, mBaseInfo.getData());
                            }
                        }
                        break;
                }
                break;
        }
    }

    public void saveCarInfo(String IsErpMapping, String CarModelID,String CarModelName) {
        if (isDoSaveCarInfo) {
            isDoSaveCarInfo = false;
            mPresenter.saveCarInfo(true, ADApplication.mSPUtils.getString(Api.USERID),
                    carInfoFragment.getLicenseplateNO(), IsErpMapping,
                    mCarTakeTaskListBean.getDisCarID() + "", CarModelID, CarModelName);
        }
    }


    /**
     * 当前类型，为接车等,有些可以编辑有些不可以编辑
     */
    public int mCurrentType;
    /**
     * 详情是否可以编辑 false 不可编辑 true 可编辑
     */
    public boolean isEdit;
    private DetailActivity mActivity;
    public DetailContract.Presenter mPresenter;
    FragmentManager fm;
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private BasicInfoFragment basicInfoFragment;
    private ItemInfoFragment itemInfoFragment;
    private CarInfoFragment carInfoFragment;
    private ImageInfoFragment imageInfoFragment;
    //保存基本信息和物品信息
    private BaseInfo mBaseInfo;
    //保存车辆信息
    private CarInfo mCarInfo;
    //保存影像信息
    private CarFilesInfo mCarFilesInfo;
    public CarTakeTaskList.DataBean.CarTakeTaskListBean mCarTakeTaskListBean;
    /**
     * 4个fragment的当前选中
     */
    private int currentPosition;
    private int selectColor, noSelectColor;
    private boolean isDoSaveCarBasicItemInfo = true;
    private boolean isDoSaveCarInfo = true;
    private boolean isDoSubmitCarTakeStore = true;

    private int mOnSaveInstanceStatePosition = -1;
    private int subPosition = -1;

    /**
     * 上传图片类型，0：物品信息里面的图片，1：车辆拍照的图片
     */
    private int mFromType = -1;
    /**
     * 删除图片的类型，0：物品信息里面的图片,1:车辆拍照的图片
     */
    private int mDeleteType = -1;

    /**
     * 车辆拍照内的子布局的图片，0:外观照，1：详细照，2：补充照
     */
    private int mSubType = -1;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_detail;
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
        if (isEdit) {
            tvSave.setVisibility(View.VISIBLE);
        } else {
            tvSave.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean("show", true);
        outState.putInt("currentPosition", currentPosition);
        outState.putInt("subPosition", imageInfoFragment.mViewPager.getCurrentItem());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mOnSaveInstanceStatePosition = savedInstanceState.getInt("currentPosition",-1);
            subPosition = savedInstanceState.getInt("subPosition", -1);
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
            if (isEdit) {
                tvSave.setVisibility(View.VISIBLE);
                tvSave.setText("保存");
            } else {
                tvSave.setVisibility(View.INVISIBLE);
            }
            switch (currentPosition){
                case 1:
                    itemInfoFragment.getData();
                    break;
                case 2:
                    carInfoFragment.getData();
                    tvSave.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    imageInfoFragment.getData();
                    if(subPosition > 0){
                        imageInfoFragment.mViewPager.setCurrentItem(subPosition);
                    }
                    tvSave.setText("提交");
                    break;
            }
            mOnSaveInstanceStatePosition = -1;
        }
    }

    private void addChildFragment() {
        //就是为了防止页面被回收后重新走initview方法，先删除一些
        mFragments.clear();
        fm = getChildFragmentManager();
        Fragment baseRemove = fm.findFragmentByTag(BasicInfoFragment.class.getSimpleName());
        Fragment ItemRemove = fm.findFragmentByTag(ItemInfoFragment.class.getSimpleName());
        Fragment CarInfoRemove = fm.findFragmentByTag(CarInfoFragment.class.getSimpleName());
        Fragment ImageInfoRemove = fm.findFragmentByTag(ImageInfoFragment.class.getSimpleName());
        FragmentTransaction ftRemove = fm.beginTransaction();
        if (baseRemove != null)
            ftRemove.remove(baseRemove);
        if (ItemRemove != null)
            ftRemove.remove(ItemRemove);
        if (CarInfoRemove != null)
            ftRemove.remove(CarInfoRemove);
        if (ImageInfoRemove != null)
            ftRemove.remove(ImageInfoRemove);
        ftRemove.commit();

        basicInfoFragment = BasicInfoFragment.getInstance(mCarTakeTaskListBean);
        itemInfoFragment = ItemInfoFragment.getInstance(mCarTakeTaskListBean);
        carInfoFragment = CarInfoFragment.getInstance(mCarTakeTaskListBean);
        imageInfoFragment = ImageInfoFragment.getInstance(mCarTakeTaskListBean);
        mFragments.add(basicInfoFragment);
        mFragments.add(itemInfoFragment);
        mFragments.add(carInfoFragment);
        mFragments.add(imageInfoFragment);
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment base : mFragments) {
            ft.add(R.id.fl_detail_child_container, base, base.getClass().getSimpleName());
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

    @Override
    protected void initData() {
        mActivity = (DetailActivity) getActivity();
        mCarTakeTaskListBean = getArguments().getParcelable("CarTakeTaskListBean");
        mCurrentType = getArguments().getInt("Type");
        selectColor = getResources().getColor(R.color.colorPrimary);
        noSelectColor = getResources().getColor(R.color.ad888888);
        if (mCurrentType != -1) {
            switch (mCurrentType) {
                case MainFragment.WEIJIECHE:
                case MainFragment.WEITIJIAO:
                case MainFragment.SHENHETUIHUI:
                    isEdit = true;
                    break;
                case MainFragment.SHENHEZHON:
                case MainFragment.SHENHETONGUO:
                    isEdit = false;
                    break;
            }
        }
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

    private void SelectChildTitle(int position) {
        switch (position) {
            case 0:
                ivBasic.setImageResource(R.drawable.jibenxinxi_on);
                tvBasic.setTextColor(selectColor);
                ivItem.setImageResource(R.drawable.wupinxinxi_off);
                tvItem.setTextColor(noSelectColor);
                ivCar.setImageResource(R.drawable.cheliangxinxi_off);
                tvCar.setTextColor(noSelectColor);
                ivImage.setImageResource(R.drawable.yingxiangxinxi_off);
                tvImage.setTextColor(noSelectColor);
                break;
            case 1:
                ivBasic.setImageResource(R.drawable.jibenxinxi_off);
                tvBasic.setTextColor(noSelectColor);
                ivItem.setImageResource(R.drawable.wupinxinxi_on);
                tvItem.setTextColor(selectColor);
                ivCar.setImageResource(R.drawable.cheliangxinxi_off);
                tvCar.setTextColor(noSelectColor);
                ivImage.setImageResource(R.drawable.yingxiangxinxi_off);
                tvImage.setTextColor(noSelectColor);
                break;
            case 2:
                ivBasic.setImageResource(R.drawable.jibenxinxi_off);
                tvBasic.setTextColor(noSelectColor);
                ivItem.setImageResource(R.drawable.wupinxinxi_off);
                tvItem.setTextColor(noSelectColor);
                ivCar.setImageResource(R.drawable.cheliangxinxi_on);
                tvCar.setTextColor(selectColor);
                ivImage.setImageResource(R.drawable.yingxiangxinxi_off);
                tvImage.setTextColor(noSelectColor);
                break;
            case 3:
                ivBasic.setImageResource(R.drawable.jibenxinxi_off);
                tvBasic.setTextColor(noSelectColor);
                ivItem.setImageResource(R.drawable.wupinxinxi_off);
                tvItem.setTextColor(noSelectColor);
                ivCar.setImageResource(R.drawable.cheliangxinxi_off);
                tvCar.setTextColor(noSelectColor);
                ivImage.setImageResource(R.drawable.yingxiangxinxi_on);
                tvImage.setTextColor(selectColor);
                break;
        }
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
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
        SnackbarUtils.showLongDisSnackBar(mainView, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showCarTakeStoreBaseInfo(BaseInfo baseInfo) {
        mBaseInfo = baseInfo;
        basicInfoFragment.updateUI(baseInfo);
    }

    @Override
    public void showBaseInfoError() {
        basicInfoFragment.showError();
    }

    @Override
    public void showBaseInfoNoData() {
        basicInfoFragment.showNoData();
    }

    @Override
    public void showItemInfo(BaseInfo baseInfo) {
        mBaseInfo = baseInfo;
        itemInfoFragment.updateUI(baseInfo);
    }

    @Override
    public void showItemInfoError() {
        itemInfoFragment.showError();
    }

    @Override
    public void showItemInfoNoData() {
        itemInfoFragment.showNoData();
    }

    @Override
    public void showCarInfo(CarInfo carInfo) {
        mCarInfo = carInfo;
        carInfoFragment.updateUI(carInfo);
    }

    @Override
    public void showCarInfoNoData() {
        carInfoFragment.showNoData();
    }

    @Override
    public void showCarInfoError() {
        carInfoFragment.showError();
    }

    @Override
    public void showSaveBasicItem(boolean isSuccess, String message) {
        isDoSaveCarBasicItemInfo = true;
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(mainView, message);
        if (isSuccess) {
            //刷新基本信息和物品信息
            basicInfoFragment.getData();
        } else {
            basicInfoFragment.updateUI(mBaseInfo);
            itemInfoFragment.updateUI(mBaseInfo);
        }
    }

    @Override
    public void showSaveCarInfo(boolean isSuccess, String message) {
        isDoSaveCarInfo = true;
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(mainView, message);
        if (isSuccess) {
            //刷新基本信息和物品信息
            carInfoFragment.getData();
        } else {
            carInfoFragment.updateUI(mCarInfo);
        }
    }

    @Override
    public void showCarFilesInfo(CarFilesInfo carFilesInfo) {
        mCarFilesInfo = carFilesInfo;
        imageInfoFragment.updateUI(carFilesInfo);
    }

    @Override
    public void showCarFilesInfoError() {
        imageInfoFragment.showError();
    }

    @Override
    public void showCarFilesInfoNoData() {
        imageInfoFragment.showNoData();
    }

    @Override
    public void showSaveDisCarInfo(boolean isSuccess, PhotoResult photoResult) {
        isDoSaveDisCarFile = true;
        if (isSuccess) {
            if (mFromType == 0) {
                itemInfoFragment.updateAddPhoto(photoResult);
            } else if (mFromType == 1) {
                if (mSubType == 0) {
                    imageInfoFragment.updateAddFacadePhoto(photoResult);
                } else if (mSubType == 1) {
                    imageInfoFragment.updateAddPartPhoto(photoResult);
                } else if (mSubType == 2) {
                    imageInfoFragment.updateAddSupplePhoto(photoResult);
                } else {

                }
            }
        } else {
            if (!TextUtils.isEmpty(photoResult.getMsg())) {
//                ToastUtils.showShort(photoResult.getMsg());
                SnackbarUtils.showLongDisSnackBar(mainView, photoResult.getMsg());
            }
        }
    }

    @Override
    public void showDeleteDisCarInfo(boolean isSuccess, String message) {
        isDoDeleteDisCarFile = true;
        if (isSuccess) {
            if (mDeleteType == 0) {
                itemInfoFragment.updateDeletePhoto();
            } else if (mDeleteType == 1) {
                if (mSubType == 0) {
                    imageInfoFragment.updateDelteFacadePhoto();
                } else if (mSubType == 1) {
                    imageInfoFragment.updateDeltePartPhoto();
                } else if (mSubType == 2) {
                    imageInfoFragment.updateDelteSupplePhoto();
                } else {

                }
            }
        } else {
            if (!TextUtils.isEmpty(message)) {
//                ToastUtils.showShort(message);
                SnackbarUtils.showLongDisSnackBar(mainView, message);
            }
        }
    }

    @Override
    public void showSubmitCarTakeStore(boolean isSuccess, String message) {
        isDoSubmitCarTakeStore = true;
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(mainView, message);
        if (isSuccess) {
            EventBus.getDefault().post(new RefreshMainEvent("showSubmitCarTakeStore"));
            mActivity.finish();
        } else {
            basicInfoFragment.updateUI(mBaseInfo);
            itemInfoFragment.updateUI(mBaseInfo);
        }
    }

    private boolean checkBasicAndItemParamsMust() {
        boolean check = false;
        if (!TextUtils.isEmpty(basicInfoFragment.getMileAgeReg()) && !TextUtils.isEmpty(basicInfoFragment.getCarInfoDesc()) &&
                !TextUtils.isEmpty(basicInfoFragment.getSPNum()) && !TextUtils.isEmpty(basicInfoFragment.getHasCard()) &&
                !TextUtils.isEmpty(basicInfoFragment.getGPSScreen()) && !TextUtils.isEmpty(basicInfoFragment.getGPSInstall()) &&
                !TextUtils.isEmpty(basicInfoFragment.getBatteryPowerSupply()) && !TextUtils.isEmpty(basicInfoFragment.getLocks()) &&
                !TextUtils.isEmpty(basicInfoFragment.getCarPager())) {
            check = true;
        } else {
//            ToastUtils.showShort("输入信息不完整");
            SnackbarUtils.showLongDisSnackBar(mainView, "接车信息或物品信息的必填信息输入不完整");
        }
        basicInfoFragment.getAntitowing();
        basicInfoFragment.getRegMemo();
        basicInfoFragment.getWhPosition();
        basicInfoFragment.getCarStatus();
        basicInfoFragment.getStatus();
        basicInfoFragment.getApproveStatus();

//        itemInfoFragment.getCarDlvNO();
        itemInfoFragment.getInCarList();
        itemInfoFragment.getInCarNmb();
//        itemInfoFragment.getInCarDlvComp();
        return check;
    }

    private boolean isDoSaveDisCarFile = true;

    public void zipPicture(int fromType, int subType, String mImgPath, final String UserID, final String DisCarID, final String PicGroup, final String SubCategory,
                           final String SubID, final String PicFileID) {
        if (!isDoSaveDisCarFile) {
            return;
        }
        isDoSaveDisCarFile = false;
        mFromType = fromType;
        mSubType = subType;
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
        mDeleteType = type;
        mSubType = subType;
        mPresenter.deleteDisCarFile(showRefreshLoadingUI, UserID, PicFileID);
    }
}
