package com.cango.adpickcar.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.CustomQRCodeActivity;
import com.cango.adpickcar.DocActivity;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.baseAdapter.OnLoadMoreListener;
import com.cango.adpickcar.detail.DetailActivity;
import com.cango.adpickcar.jdetail.JDetailActivity;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.model.DeliveryTaskList;
import com.cango.adpickcar.model.EventModel.RefreshJMainEvent;
import com.cango.adpickcar.model.EventModel.RefreshMainEvent;
import com.cango.adpickcar.model.GetQRCodeData;
import com.cango.adpickcar.model.QRCodeBean;
import com.cango.adpickcar.resetps.ResetPSActivity;
import com.cango.adpickcar.update.ProgressListener;
import com.cango.adpickcar.update.UpdatePresenter;
import com.cango.adpickcar.util.BarUtil;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.SnackbarUtils;
import com.cango.adpickcar.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, MainContract.View, EasyPermissions.PermissionCallbacks {
    public int BIGTYPE = -1;
    public static final int JIECHE = 10;
    public static final int JIAOCHE = 11;

    public int JIAOCHETYPE = -1;
    public static final int DAIJIAOCHE = 12;
    public static final int YIJIAOCHE = 13;
    public static final int JIAOCHESHIBAI = 14;
    /**
     * 查询类型（1：未接车 2.未提交 3：审核中 4：审批退回 5.审批通过）
     */
    public static final int WEIJIECHE = 1;
    public static final int WEITIJIAO = 2;
    public static final int SHENHEZHON = 3;
    public static final int SHENHETUIHUI = 4;
    public static final int SHENHETONGUO = 5;
    public int CURRENT_TYPE = -1;
    private QRCodeBean currentQRCodeBean;


    public static MainFragment getInstance() {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @BindView(R.id.drawer_main)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.rl_drawer)
    RelativeLayout rlDrawer;
    @BindView(R.id.tv_user_mobile)
    TextView tvUserMobile;
    @BindView(R.id.rl_main_head)
    RelativeLayout rlHead;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_main_scan)
    LinearLayout llMainScan;
    @BindView(R.id.ll_main_search)
    LinearLayout llMainSearch;
    @BindView(R.id.iv_main_popup_parent)
    ImageView ivPopupParent;
    @BindView(R.id.cardview_main)
    CardView mCardView;
    @BindView(R.id.cv_jiaoche_main)
    CardView mJiaoCheCV;
    @BindView(R.id.tv_main_first_num)
    TextView tvFirstNum;
    @BindView(R.id.iv_main_first)
    ImageView ivFirst;
    @BindView(R.id.tv_main_first)
    TextView tvFirst;
    @BindView(R.id.tv_main_second_num)
    TextView tvSecondNum;
    @BindView(R.id.iv_main_second)
    ImageView ivSecond;
    @BindView(R.id.tv_main_second)
    TextView tvSecond;
    @BindView(R.id.tv_main_third_num)
    TextView tvThirdNum;
    @BindView(R.id.iv_main_third)
    ImageView ivThird;
    @BindView(R.id.tv_main_third)
    TextView tvThird;
    @BindView(R.id.tv_main_fourth_num)
    TextView tvFourthNum;
    @BindView(R.id.iv_main_fourth)
    ImageView ivFourth;
    @BindView(R.id.tv_main_fourth)
    TextView tvFourth;
    @BindView(R.id.tv_main_fifth_num)
    TextView tvFifthNum;
    @BindView(R.id.iv_main_fifth)
    ImageView ivFifth;
    @BindView(R.id.tv_main_fifth)
    TextView tvFifth;
    @BindView(R.id.tv_jiaoche_main_first_num)
    TextView tvJFirstNum;
    @BindView(R.id.iv_jiaoche_main_first)
    ImageView ivJFirst;
    @BindView(R.id.tv_jiaoche_main_first)
    TextView tvJFirst;
    @BindView(R.id.tv_jiaoche_main_third_num)
    TextView tvJThirdNum;
    @BindView(R.id.iv_jiaoche_main_third)
    ImageView ivJThird;
    @BindView(R.id.tv_jiaoche_main_third)
    TextView tvJThird;
    @BindView(R.id.tv_jiaoche_main_fifth_num)
    TextView tvJFifthNum;
    @BindView(R.id.iv_jiaoche_main_fifth)
    ImageView ivJFifth;
    @BindView(R.id.tv_jiaoche_main_fifth)
    TextView tvJFifth;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.srl_jiaoche_main)
    SwipeRefreshLayout mJSwipeRefreshLayout;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.recyclerview_main)
    RecyclerView mRecyclerView;
    @BindView(R.id.recyclerview_jiaoche_main)
    RecyclerView mJRecyclerView;
    @BindView(R.id.iv_bottom_jieche)
    ImageView ivBottomJieChe;
    @BindView(R.id.tv_bottom_jieche)
    TextView tvBottomJieChe;
    @BindView(R.id.iv_bottom_jiaoche)
    ImageView ivBottomJiaoChe;
    @BindView(R.id.tv_bottom_jiaoche)
    TextView tvBottomJiaoChe;
    @BindView(R.id.fl_shadow)
    FrameLayout flShadow;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;

    @OnClick({R.id.ll_modify_ps, R.id.ll_main_scan, R.id.ll_main_search, R.id.rl_main_first, R.id.rl_main_second, R.id.rl_main_third,
            R.id.rl_main_fourth, R.id.rl_main_fifth, R.id.ll_sign_off, R.id.rl_drawer, R.id.ll_about_us,
            R.id.rl_jiaoche_main_first, R.id.rl_jiaoche_main_third, R.id.rl_jiaoche_main_fifth,
            R.id.ll_jieche_bottom, R.id.ll_jiaoche_bottom, R.id.ll_xunjian_bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_modify_ps:
                startActivity(new Intent(mActivity, ResetPSActivity.class));
                break;
            case R.id.ll_main_scan:
                //使用第三方sdk的activity
//                IntentIntegrator.forSupportFragment(this).initiateScan();
                //使用自己定义的界面
                IntentIntegrator.forSupportFragment(this).setCaptureActivity(CustomQRCodeActivity.class).initiateScan();
//                new IntentIntegrator.forSupportFragment(this).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
                break;
            case R.id.ll_main_search:
                switch (BIGTYPE) {
                    case JIECHE:
                        showPopSearch();
                        break;
                    case JIAOCHE:
                        showJPopSearch();
                        break;
                }
                break;
            case R.id.rl_main_first:
                llMainScan.setVisibility(View.VISIBLE);
                llMainSearch.setVisibility(View.VISIBLE);
                selectTitleStatus(0);
                if (CURRENT_TYPE != WEIJIECHE) {
                    CURRENT_TYPE = WEIJIECHE;
                    mLicensePlateNO = "";
                    mCustName = "";
                    mCarBrandName = "";
                    onRefresh();
                }
                break;
            case R.id.rl_main_second:
                llMainScan.setVisibility(View.INVISIBLE);
                llMainSearch.setVisibility(View.INVISIBLE);
                selectTitleStatus(1);
                if (CURRENT_TYPE != WEITIJIAO) {
                    CURRENT_TYPE = WEITIJIAO;
                    mLicensePlateNO = "";
                    mCustName = "";
                    mCarBrandName = "";
                    onRefresh();
                }
                break;
            case R.id.rl_main_third:
                llMainScan.setVisibility(View.INVISIBLE);
                llMainSearch.setVisibility(View.INVISIBLE);
                selectTitleStatus(2);
                if (CURRENT_TYPE != SHENHEZHON) {
                    CURRENT_TYPE = SHENHEZHON;
                    mLicensePlateNO = "";
                    mCustName = "";
                    mCarBrandName = "";
                    onRefresh();
                }
                break;
            case R.id.rl_main_fourth:
                llMainScan.setVisibility(View.INVISIBLE);
                llMainSearch.setVisibility(View.INVISIBLE);
                selectTitleStatus(3);
                if (CURRENT_TYPE != SHENHETUIHUI) {
                    CURRENT_TYPE = SHENHETUIHUI;
                    mLicensePlateNO = "";
                    mCustName = "";
                    mCarBrandName = "";
                    onRefresh();
                }
                break;
            case R.id.rl_main_fifth:
                llMainScan.setVisibility(View.INVISIBLE);
                llMainSearch.setVisibility(View.INVISIBLE);
                selectTitleStatus(4);
                if (CURRENT_TYPE != SHENHETONGUO) {
                    CURRENT_TYPE = SHENHETONGUO;
                    mLicensePlateNO = "";
                    mCustName = "";
                    mCarBrandName = "";
                    onRefresh();
                }
                break;
            case R.id.rl_jiaoche_main_first:
                llMainSearch.setVisibility(View.VISIBLE);
                selectJTitleStatus(0);
                if (JIAOCHETYPE != DAIJIAOCHE) {
                    JIAOCHETYPE = DAIJIAOCHE;
                    mJCarCust = "";
                    mJCarNo = "";
                    mJName = "";
                    llNoData.setVisibility(View.GONE);
                    llSorry.setVisibility(View.GONE);
                    onRefresh();
                }
                break;
            case R.id.rl_jiaoche_main_third:
                llMainSearch.setVisibility(View.INVISIBLE);
                selectJTitleStatus(1);
                if (JIAOCHETYPE != YIJIAOCHE) {
                    JIAOCHETYPE = YIJIAOCHE;
                    mJCarCust = "";
                    mJCarNo = "";
                    mJName = "";
                    llNoData.setVisibility(View.GONE);
                    llSorry.setVisibility(View.GONE);
                    onRefresh();
                }
                break;
            case R.id.rl_jiaoche_main_fifth:
                llMainSearch.setVisibility(View.INVISIBLE);
                selectJTitleStatus(2);
                if (JIAOCHETYPE != JIAOCHESHIBAI) {
                    JIAOCHETYPE = JIAOCHESHIBAI;
                    mJCarCust = "";
                    mJCarNo = "";
                    mJName = "";
                    llNoData.setVisibility(View.GONE);
                    llSorry.setVisibility(View.GONE);
                    onRefresh();
                }
                break;
            case R.id.ll_sign_off:
                if (isDoLogout) {
                    isDoLogout = false;
                    mPresenter.logout(true, ADApplication.mSPUtils.getString(Api.USERID));
                }
                break;
            case R.id.rl_drawer:
                break;
            case R.id.ll_about_us:
                startActivity(new Intent(mActivity, DocActivity.class));
                break;
            case R.id.ll_jieche_bottom:
                if (BIGTYPE == JIECHE) {
                    return;
                }
                BIGTYPE = JIECHE;
                mLicensePlateNO = "";
                mCustName = "";
                mCarBrandName = "";
                tvTitle.setText(getString(R.string.main_title));
                selectBottomBarStatus(0);
                mCardView.setVisibility(View.VISIBLE);
                mJiaoCheCV.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mJSwipeRefreshLayout.setVisibility(View.GONE);
                switch (CURRENT_TYPE) {
                    case WEIJIECHE:
                        llMainScan.setVisibility(View.VISIBLE);
                        llMainSearch.setVisibility(View.VISIBLE);
                        break;
                    case WEITIJIAO:
                        llMainScan.setVisibility(View.INVISIBLE);
                        llMainSearch.setVisibility(View.INVISIBLE);
                        break;
                    case SHENHEZHON:
                        llMainScan.setVisibility(View.INVISIBLE);
                        llMainSearch.setVisibility(View.INVISIBLE);
                        break;
                    case SHENHETUIHUI:
                        llMainScan.setVisibility(View.INVISIBLE);
                        llMainSearch.setVisibility(View.INVISIBLE);
                        break;
                    case SHENHETONGUO:
                        llMainScan.setVisibility(View.INVISIBLE);
                        llMainSearch.setVisibility(View.INVISIBLE);
                        break;
                }
                onRefresh();
                break;
            case R.id.ll_jiaoche_bottom:
                if (BIGTYPE == JIAOCHE) {
                    return;
                }
                BIGTYPE = JIAOCHE;
                mJCarCust = "";
                mJCarNo = "";
                mJName = "";
                tvTitle.setText(getString(R.string.main_jiaoche));
                selectBottomBarStatus(1);
                mCardView.setVisibility(View.GONE);
                mJiaoCheCV.setVisibility(View.VISIBLE);
                llMainScan.setVisibility(View.INVISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                mJSwipeRefreshLayout.setVisibility(View.VISIBLE);
                switch (JIAOCHETYPE) {
                    case DAIJIAOCHE:
                        llMainSearch.setVisibility(View.VISIBLE);
                        break;
                    case YIJIAOCHE:
                        llMainSearch.setVisibility(View.INVISIBLE);
                        break;
                    case JIAOCHESHIBAI:
                        llMainSearch.setVisibility(View.INVISIBLE);
                        break;
                }
                onRefresh();
                break;
            case R.id.ll_xunjian_bottom:
                break;
        }
    }

    private MainActivity mActivity;
    private MainContract.Presenter mPresenter;
    private MainAdapter mAdapter;
    private JMainAdapter mJAdapter;
    private ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean> datas;
    private ArrayList<DeliveryTaskList.DataBean.CarDeliveryTaskListBean> JDatas;
    private int itemOnClickPosition;
    private Badge firstQV, secondQV, thirdQV, fourthQV, fifthQV, oneQV, twoQV, threeQV;
    private int selectColor, noSelectColor;
    private int mPageCount = 1, mTempPageCount = 2;
    static int PAGE_SIZE = 10;
    private boolean isLoadMore;
    //客户名称 客户车牌号
    private String mCustName, mLicensePlateNO, mCarBrandName;
    private String mJCarNo, mJName, mJCarCust;
    private boolean isDoLogout = true, isDoCarTakeStoreConfirm = true;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!CommUtil.checkIsNull(mPresenter))
            mPresenter.onDetach();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_main;
    }

    /**
     *
     */
    @Override
    protected void initView() {
        showLoadView(false);
        int statusBarHeight = BarUtil.getStatusBarHeight(getActivity());
        int actionBarHeight = BarUtil.getActionBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + actionBarHeight);
            mToolbar.setLayoutParams(layoutParams);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mToggle.syncState();
        mToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });

        mDrawerLayout.addDrawerListener(mToggle);

        BIGTYPE = JIECHE;
        CURRENT_TYPE = WEIJIECHE;
        JIAOCHETYPE = DAIJIAOCHE;
        tvTitle.setText(getString(R.string.main_title));
        selectBottomBarStatus(0);
        initNum(0, 0, 0, 0, 0);
        initJiaoChe(0, 0, 0);
        selectTitleStatus(0);
        selectJTitleStatus(0);
        llMainScan.setVisibility(View.VISIBLE);
        tvUserMobile.setText(ADApplication.mSPUtils.getString(Api.NICKNAME));
        initRecyclerView();
    }

    private void initNum(int first, int second, int third, int fourth, int fifth) {
        firstQV = new QBadgeView(mActivity).bindTarget(tvFirstNum).setBadgeNumber(first).setShowShadow(false).setBadgeBackgroundColor(Color.TRANSPARENT);
        secondQV = new QBadgeView(mActivity).bindTarget(tvSecondNum).setBadgeNumber(second).setShowShadow(false).setBadgeBackgroundColor(Color.TRANSPARENT);
        thirdQV = new QBadgeView(mActivity).bindTarget(tvThirdNum).setBadgeNumber(third).setShowShadow(false).setBadgeBackgroundColor(Color.TRANSPARENT);
        fourthQV = new QBadgeView(mActivity).bindTarget(tvFourthNum).setBadgeNumber(fourth).setBadgeTextColor(Color.WHITE).setShowShadow(false);
        fifthQV = new QBadgeView(mActivity).bindTarget(tvFifthNum).setBadgeNumber(fifth).setShowShadow(false).setBadgeBackgroundColor(Color.TRANSPARENT);
    }

    private void initJiaoChe(int first, int second, int third) {
        oneQV = new QBadgeView(mActivity).bindTarget(tvJFirstNum).setBadgeNumber(first).setShowShadow(false).setBadgeBackgroundColor(Color.TRANSPARENT);
        twoQV = new QBadgeView(mActivity).bindTarget(tvJThirdNum).setBadgeNumber(second).setShowShadow(false).setBadgeBackgroundColor(Color.TRANSPARENT);
        threeQV = new QBadgeView(mActivity).bindTarget(tvJFifthNum).setBadgeNumber(third).setBadgeTextColor(Color.WHITE).setShowShadow(false);
    }

    private void selectTitleStatus(int position) {
        switch (position) {
            case 0:
                firstQV.setBadgeTextColor(selectColor);
                secondQV.setBadgeTextColor(noSelectColor);
                thirdQV.setBadgeTextColor(noSelectColor);
                fourthQV.setBadgeBackgroundColor(noSelectColor);
                fifthQV.setBadgeTextColor(noSelectColor);
                ivFirst.setImageResource(R.drawable.weijieche_on);
                tvFirst.setTextColor(selectColor);
                ivSecond.setImageResource(R.drawable.weitijiao_off);
                tvSecond.setTextColor(noSelectColor);
                ivThird.setImageResource(R.drawable.shenhezhong_off);
                tvThird.setTextColor(noSelectColor);
                ivFourth.setImageResource(R.drawable.shenhetuihui_off);
                tvFourth.setTextColor(noSelectColor);
                ivFifth.setImageResource(R.drawable.shenhetongguo_off);
                tvFifth.setTextColor(noSelectColor);
                break;
            case 1:
                firstQV.setBadgeTextColor(noSelectColor);
                secondQV.setBadgeTextColor(selectColor);
                thirdQV.setBadgeTextColor(noSelectColor);
                fourthQV.setBadgeBackgroundColor(noSelectColor);
                fifthQV.setBadgeTextColor(noSelectColor);
                ivFirst.setImageResource(R.drawable.weijieche_off);
                tvFirst.setTextColor(noSelectColor);
                ivSecond.setImageResource(R.drawable.weitijiao_on);
                tvSecond.setTextColor(selectColor);
                ivThird.setImageResource(R.drawable.shenhezhong_off);
                tvThird.setTextColor(noSelectColor);
                ivFourth.setImageResource(R.drawable.shenhetuihui_off);
                tvFourth.setTextColor(noSelectColor);
                ivFifth.setImageResource(R.drawable.shenhetongguo_off);
                tvFifth.setTextColor(noSelectColor);
                break;
            case 2:
                firstQV.setBadgeTextColor(noSelectColor);
                secondQV.setBadgeTextColor(noSelectColor);
                thirdQV.setBadgeTextColor(selectColor);
                fourthQV.setBadgeBackgroundColor(noSelectColor);
                fifthQV.setBadgeTextColor(noSelectColor);
                ivFirst.setImageResource(R.drawable.weijieche_off);
                tvFirst.setTextColor(noSelectColor);
                ivSecond.setImageResource(R.drawable.weitijiao_off);
                tvSecond.setTextColor(noSelectColor);
                ivThird.setImageResource(R.drawable.shenhezhong_on);
                tvThird.setTextColor(selectColor);
                ivFourth.setImageResource(R.drawable.shenhetuihui_off);
                tvFourth.setTextColor(noSelectColor);
                ivFifth.setImageResource(R.drawable.shenhetongguo_off);
                tvFifth.setTextColor(noSelectColor);
                break;
            case 3:
                firstQV.setBadgeTextColor(noSelectColor);
                secondQV.setBadgeTextColor(noSelectColor);
                thirdQV.setBadgeTextColor(noSelectColor);
                fourthQV.setBadgeBackgroundColor(selectColor);
                fifthQV.setBadgeTextColor(noSelectColor);
                ivFirst.setImageResource(R.drawable.weijieche_off);
                tvFirst.setTextColor(noSelectColor);
                ivSecond.setImageResource(R.drawable.weitijiao_off);
                tvSecond.setTextColor(noSelectColor);
                ivThird.setImageResource(R.drawable.shenhezhong_off);
                tvThird.setTextColor(noSelectColor);
                ivFourth.setImageResource(R.drawable.shenhetuihui_on);
                tvFourth.setTextColor(selectColor);
                ivFifth.setImageResource(R.drawable.shenhetongguo_off);
                tvFifth.setTextColor(noSelectColor);
                break;
            case 4:
                firstQV.setBadgeTextColor(noSelectColor);
                secondQV.setBadgeTextColor(noSelectColor);
                thirdQV.setBadgeTextColor(noSelectColor);
                fourthQV.setBadgeBackgroundColor(noSelectColor);
                fifthQV.setBadgeTextColor(selectColor);
                ivFirst.setImageResource(R.drawable.weijieche_off);
                tvFirst.setTextColor(noSelectColor);
                ivSecond.setImageResource(R.drawable.weitijiao_off);
                tvSecond.setTextColor(noSelectColor);
                ivThird.setImageResource(R.drawable.shenhezhong_off);
                tvThird.setTextColor(noSelectColor);
                ivFourth.setImageResource(R.drawable.shenhetuihui_off);
                tvFourth.setTextColor(noSelectColor);
                ivFifth.setImageResource(R.drawable.shenhetongguo_on);
                tvFifth.setTextColor(selectColor);
                break;
        }
    }

    private void selectJTitleStatus(int position) {
        switch (position) {
            case 0:
                oneQV.setBadgeTextColor(selectColor);
                twoQV.setBadgeTextColor(noSelectColor);
                threeQV.setBadgeBackgroundColor(noSelectColor);
                ivJFirst.setImageResource(R.drawable.waitingcaron);
                tvJFirst.setTextColor(selectColor);
                ivJThird.setImageResource(R.drawable.deliverycaroff);
                tvJThird.setTextColor(noSelectColor);
                tvJThird.setTextColor(noSelectColor);
                ivJFifth.setImageResource(R.drawable.failcaroff);
                tvJFifth.setTextColor(noSelectColor);
                break;
            case 1:
                oneQV.setBadgeTextColor(noSelectColor);
                twoQV.setBadgeTextColor(selectColor);
                threeQV.setBadgeBackgroundColor(noSelectColor);
                ivJFirst.setImageResource(R.drawable.waitingcaroff);
                tvJFirst.setTextColor(noSelectColor);
                ivJThird.setImageResource(R.drawable.deliverycaron);
                tvJThird.setTextColor(selectColor);
                ivJFifth.setImageResource(R.drawable.failcaroff);
                tvJFifth.setTextColor(noSelectColor);
                break;
            case 2:
                oneQV.setBadgeTextColor(noSelectColor);
                twoQV.setBadgeTextColor(noSelectColor);
                threeQV.setBadgeBackgroundColor(selectColor);
                ivJFirst.setImageResource(R.drawable.waitingcaroff);
                tvJFirst.setTextColor(noSelectColor);
                ivJThird.setImageResource(R.drawable.deliverycaroff);
                tvJThird.setTextColor(noSelectColor);
                ivJFifth.setImageResource(R.drawable.failcaron);
                tvJFifth.setTextColor(selectColor);
                break;
        }
    }

    private void selectBottomBarStatus(int position) {
        switch (position) {
            case 0:
                ivBottomJieChe.setImageResource(R.drawable.jieche_icon);
                tvBottomJieChe.setTextColor(selectColor);
                ivBottomJiaoChe.setImageResource(R.drawable.deliveryiconoff);
                tvBottomJiaoChe.setTextColor(noSelectColor);
                break;
            case 1:
                ivBottomJieChe.setImageResource(R.drawable.jieche_icon_off);
                tvBottomJieChe.setTextColor(noSelectColor);
                ivBottomJiaoChe.setImageResource(R.drawable.deliveryiconon);
                tvBottomJiaoChe.setTextColor(selectColor);
                break;
        }

    }

    private void initRecyclerView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mJSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
        mJSwipeRefreshLayout.setOnRefreshListener(this);
        JDatas = new ArrayList<>();
        datas = new ArrayList<>();
        mAdapter = new MainAdapter(mActivity, datas, true, this);
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                mPresenter.loadListByStatus(false, ADApplication.mSPUtils.getString(Api.USERID), mCustName,
                        mLicensePlateNO, mCarBrandName, CURRENT_TYPE + "", mPageCount + "", PAGE_SIZE + "");
            }
        });
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<CarTakeTaskList.DataBean.CarTakeTaskListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, final CarTakeTaskList.DataBean.CarTakeTaskListBean data, final int position) {
                switch (CURRENT_TYPE) {
                    case WEIJIECHE:
//                        new AlertDialog.Builder(mActivity)
//                                .setTitle("确认接车")
//                                .setMessage("申请编号：" + data.getApplyCD() + "\r\n" + "客户姓名：" + data.getCustName() + "\r\n" +
//                                        "车牌号码：" + data.getLicenseplateNO() + "\r\n" + "颜色：" + data.getColor())
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        itemOnClickPosition = position;
//                                        if (isDoCarTakeStoreConfirm) {
//                                            isDoCarTakeStoreConfirm = false;
//                                            mPresenter.GetCarTakeTaskList(true, ADApplication.mSPUtils.getString(Api.USERID),
//                                                    data.getCTTaskID() + "", data.getPlanctWhno(), data.getVin(), data.getCarID() + "");
//
//                                        }
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .create().show();
                        break;
                    case WEITIJIAO:
                    case SHENHEZHON:
                    case SHENHETONGUO:
                        Intent intent = new Intent(mActivity, DetailActivity.class);
                        intent.putExtra("CarTakeTaskListBean", data);
                        intent.putExtra("Type", CURRENT_TYPE);
                        startActivity(intent);
                        break;
                    case SHENHETUIHUI:
                        new AlertDialog.Builder(mActivity)
                                .setTitle("驳回原因")
                                .setMessage(data.getReturnReason())
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mActivity, DetailActivity.class);
                                        intent.putExtra("CarTakeTaskListBean", data);
                                        intent.putExtra("Type", CURRENT_TYPE);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create().show();
                        break;
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mJAdapter = new JMainAdapter(mActivity, JDatas, true, this);
        mJAdapter.setLoadingView(R.layout.load_loading_layout);
        mJAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                mPresenter.loadJListByStatus(false, ADApplication.mSPUtils.getString(Api.USERID), mJName,
                        mJCarCust, mJCarNo, JIAOCHETYPE + "", mPageCount + "", PAGE_SIZE + "");
            }
        });
        mJAdapter.setOnItemClickListener(new OnBaseItemClickListener<DeliveryTaskList.DataBean.CarDeliveryTaskListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, final DeliveryTaskList.DataBean.CarDeliveryTaskListBean data, final int position) {
                switch (JIAOCHETYPE) {
                    case DAIJIAOCHE:
                    case YIJIAOCHE:
                    case JIAOCHESHIBAI:
                        Intent intent = new Intent(mActivity, JDetailActivity.class);
                        intent.putExtra("CarDeliveryTaskListBean", data);
                        intent.putExtra("Type", JIAOCHETYPE);
                        startActivity(intent);
                        break;
//                    case JIAOCHESHIBAI:
//                        new AlertDialog.Builder(mActivity)
//                                .setTitle("失败原因")
//                                .setMessage(data.getReason())
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent intent = new Intent(mActivity, JDetailActivity.class);
//                                        intent.putExtra("CarDeliveryTaskListBean", data);
//                                        intent.putExtra("Type", JIAOCHETYPE);
//                                        startActivity(intent);
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .create().show();
//                        break;
                }
            }
        });
        mJRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mJRecyclerView.setAdapter(mJAdapter);

        onRefresh();
    }

    @Override
    protected void initData() {
        mActivity = (MainActivity) getActivity();
        selectColor = getResources().getColor(R.color.colorPrimary);
        noSelectColor = getResources().getColor(R.color.ad888888);
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;

        if (BIGTYPE == JIECHE) {
            datas.clear();
            mAdapter.setLoadingView(R.layout.load_loading_layout);
            mAdapter.notifyDataSetChanged();
            mPresenter.loadListByStatus(true, ADApplication.mSPUtils.getString(Api.USERID), mCustName,
                    mLicensePlateNO, mCarBrandName, CURRENT_TYPE + "", mPageCount + "", PAGE_SIZE + "");
        } else if (BIGTYPE == JIAOCHE) {
            JDatas.clear();
            mJAdapter.setLoadingView(R.layout.load_loading_layout);
            mJAdapter.notifyDataSetChanged();
            mPresenter.loadJListByStatus(true, ADApplication.mSPUtils.getString(Api.USERID), mJName,
                    mJCarCust, mJCarNo, JIAOCHETYPE + "", mPageCount + "", PAGE_SIZE + "");
        } else {

        }
    }

    private void showPopSearch() {
        if (mCardView.getWidth() > 0) {
            PopupWindow mSearchPopup = getPopupWindow(mActivity, R.layout.main_search_pop);
            rlHead.setBackgroundColor(Color.parseColor("#36000000"));
            flShadow.setVisibility(View.VISIBLE);
            mSearchPopup.showAsDropDown(ivPopupParent);
        }
    }

    private void closePopSearch() {
        rlHead.setBackgroundColor(Color.WHITE);
        flShadow.setVisibility(View.GONE);
    }

    private void showJPopSearch() {
        if (mCardView.getWidth() > 0) {
            PopupWindow mSearchPopup = getPopupWindow(mActivity, R.layout.jmain_search_pop);
            rlHead.setBackgroundColor(Color.parseColor("#36000000"));
            flShadow.setVisibility(View.VISIBLE);
            mSearchPopup.showAsDropDown(ivPopupParent);
        }
    }

    private void closeJPopSearch() {
        rlHead.setBackgroundColor(Color.WHITE);
        flShadow.setVisibility(View.GONE);
    }

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        if (layoutId == R.layout.main_search_pop) {
            final PopupWindow popupWindow = new PopupWindow(mCardView.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            final View popupView = LayoutInflater.from(context).inflate(layoutId, null);
            final EditText etPlateNO = (EditText) popupView.findViewById(R.id.et_PlateNO);
            final EditText etName = (EditText) popupView.findViewById(R.id.et_Name);
            final EditText etCarType = (EditText) popupView.findViewById(R.id.et_car_type);
            Button btn = (Button) popupView.findViewById(R.id.btn_search);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String plateNo = etPlateNO.getText().toString().trim();
                    String name = etName.getText().toString().trim();
                    String carType = etCarType.getText().toString().trim();
                    if (!TextUtils.isEmpty(plateNo) || !TextUtils.isEmpty(name) || !TextUtils.isEmpty(carType)) {
                        mLicensePlateNO = plateNo;
                        mCustName = name;
                        mCarBrandName = carType;
                        popupWindow.dismiss();
                        onRefresh();
                    } else {
//                        ToastUtils.showShort(R.string.input_params_prompt);
                        SnackbarUtils.showLongDisSnackBar(mDrawerLayout, R.string.input_params_prompt);
                    }
                }
            });
            popupWindow.setContentView(popupView);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    closePopSearch();
                }
            });
            popupWindow.update();
            return popupWindow;
        } else {
            final PopupWindow popupWindow = new PopupWindow(mCardView.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            final View popupView = LayoutInflater.from(context).inflate(layoutId, null);
            final EditText etPlateNO = (EditText) popupView.findViewById(R.id.et_PlateNO);
            final EditText etName = (EditText) popupView.findViewById(R.id.et_Name);
            final EditText etPhone = (EditText) popupView.findViewById(R.id.et_car_type);
            Button btn = (Button) popupView.findViewById(R.id.btn_search);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String plateNo = etPlateNO.getText().toString().trim();
                    String name = etName.getText().toString().trim();
                    String carType = etPhone.getText().toString().trim();
                    if (!TextUtils.isEmpty(plateNo) || !TextUtils.isEmpty(name) || !TextUtils.isEmpty(carType)) {
                        mJCarNo = plateNo;
                        mJName = name;
                        mJCarCust = carType;
                        popupWindow.dismiss();
                        onRefresh();
                    } else {
//                        ToastUtils.showShort(R.string.input_params_prompt);
                        SnackbarUtils.showLongDisSnackBar(mDrawerLayout, R.string.input_params_prompt);
                    }
                }
            });
            popupWindow.setContentView(popupView);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    closeJPopSearch();
                }
            });
            popupWindow.update();
            return popupWindow;
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showMainIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showJMainIndicator(final boolean active) {
        mJSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mJSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMainError() {
        if (isLoadMore) {
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showJMainError() {
        if (isLoadMore) {
            mJAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
            mJRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNoData() {
        if (isLoadMore) {
            mAdapter.setLoadEndView(R.layout.load_end_layout);
        } else {
            llNoData.setVisibility(View.VISIBLE);
            llSorry.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showJNoData() {
        if (isLoadMore) {
            mJAdapter.setLoadEndView(R.layout.load_end_layout);
        } else {
            llNoData.setVisibility(View.VISIBLE);
            llSorry.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMainTitle(CarTakeTaskList.DataBean dataBean) {
        firstQV.setBadgeNumber(dataBean.getNoTakeCarCount());
        secondQV.setBadgeNumber(dataBean.getNoCommitCount());
        thirdQV.setBadgeNumber(dataBean.getCommitCount());
        fourthQV.setBadgeNumber(dataBean.getApproveBackCount());
        fifthQV.setBadgeNumber(dataBean.getApproveCount());
    }

    @Override
    public void showJMainTitle(DeliveryTaskList.DataBean dataBean) {
        oneQV.setBadgeNumber(dataBean.getNoDeliveryTaskCount());
        twoQV.setBadgeNumber(dataBean.getDeliveryTaskCount());
        threeQV.setBadgeNumber(dataBean.getDeliveryFailureCount());
    }

    @Override
    public void showMainTitleError() {
        firstQV.setBadgeNumber(0);
        secondQV.setBadgeNumber(0);
        thirdQV.setBadgeNumber(0);
        fourthQV.setBadgeNumber(0);
        fifthQV.setBadgeNumber(0);
    }

    @Override
    public void showJMainTitleError() {
        oneQV.setBadgeNumber(0);
        twoQV.setBadgeNumber(0);
        threeQV.setBadgeNumber(0);
    }

    @Override
    public void showMainSuccess(boolean isSuccess, ArrayList<CarTakeTaskList.DataBean.CarTakeTaskListBean> carTakeTaskListBeanList) {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (isLoadMore) {
            mTempPageCount++;
            mAdapter.setLoadMoreData(carTakeTaskListBeanList);
        } else {
            mAdapter.setNewDataNoError(carTakeTaskListBeanList);
        }
        if (carTakeTaskListBeanList.size() < PAGE_SIZE) {
            mAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    @Override
    public void showJMainSuccess(boolean isSuccess, ArrayList<DeliveryTaskList.DataBean.CarDeliveryTaskListBean> carDeliveryTaskListBeans) {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        mJRecyclerView.setVisibility(View.VISIBLE);
        if (isLoadMore) {
            mTempPageCount++;
            mJAdapter.setLoadMoreData(carDeliveryTaskListBeans);
        } else {
            mJAdapter.setNewDataNoError(carDeliveryTaskListBeans);
        }
        if (carDeliveryTaskListBeans.size() < PAGE_SIZE) {
            mJAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    @Override
    public void showLogout(boolean isSuccess, String message) {
        isDoLogout = true;
        if (isSuccess) {
            ADApplication.mSPUtils.clear();
            startActivity(new Intent(mActivity, LoginActivity.class));
            mActivity.finish();
        }
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(mDrawerLayout, message);
    }

    @Override
    public void showGetCarTake(boolean isSuccess, String message) {
        isDoCarTakeStoreConfirm = true;
        if (isSuccess) {
            //只有未接车才能确认接车
//            if (itemOnClickPosition < datas.size()) {
//                Intent intent = new Intent(mActivity, DetailActivity.class);
//                intent.putExtra("CarTakeTaskListBean", datas.get(itemOnClickPosition));
//                intent.putExtra("Type", CURRENT_TYPE);
//                startActivity(intent);
//            }
            onRefresh();
        }
        if (!TextUtils.isEmpty(message))
//            ToastUtils.showShort(message);
            SnackbarUtils.showLongDisSnackBar(mDrawerLayout, message);
    }

    @Override
    public void showQRCodeStatus(boolean isSuccess, GetQRCodeData baseData) {
        if (baseData != null) {
            if (isSuccess) {
                if (baseData.getData() != null) {
                    final GetQRCodeData.DataBean dataBean = baseData.getData();
                    if (dataBean.getSateCode() == 1) {
                        ToastUtils.showShort("接车成功");
                        onRefresh();
                    } else if (dataBean.getSateCode() == 2) {
                        if (dataBean.getWareHourseNOList() != null && dataBean.getWareHourseNOList().size() > 0) {
                            if (dataBean.getWareHourseNOList().size() == 1) {
                                new AlertDialog.Builder(mActivity)
                                        .setTitle("接车")
                                        .setMessage("是否强制接车")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mPresenter.carTakeStoreConfirmByQRCode(true, ADApplication.mSPUtils.getString(Api.USERID),
                                                        currentQRCodeBean.getTCUserID(), currentQRCodeBean.getAgencyID(),
                                                        currentQRCodeBean.getApplyCD(), currentQRCodeBean.getLAT(),
                                                        currentQRCodeBean.getLON(), dataBean.getWareHourseNOList().get(0),currentQRCodeBean.getDatasource());
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .create().show();
                            } else {
                                new AlertDialog.Builder(mActivity)
                                        .setTitle("接车")
                                        .setMessage("是否强制接车")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                showSingleChoiceDialog(dataBean.getWareHourseNOList());
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .create().show();

                            }
                        } else {
//                            ToastUtils.showShort("数据有误");
                            SnackbarUtils.showLongDisSnackBar(mDrawerLayout, "数据有误");
                        }
                    }
                }
            } else {
                if (!TextUtils.isEmpty(baseData.getMsg())) {
//                    ToastUtils.showShort(baseData.getMsg());
                    SnackbarUtils.showLongDisSnackBar(mDrawerLayout, baseData.getMsg());
                }
            }
        }
    }

    @Override
    public void openOtherUi() {
//        ToastUtils.showShort("认证失败，请重新登录");
        SnackbarUtils.showLongDisSnackBar(mDrawerLayout, "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void updateApk() {
        openPermissions();
    }


    /**
     * 显示更新dialog
     */
    private void showUpdateDialog() {
//        UpdateFragment updateFragment = new UpdateFragment();
//        updateFragment.show(getChildFragmentManager(), updateFragment.getClass().getSimpleName());

        updateAPK();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (subscription != null)
            subscription.unsubscribe();

    }

    //接受详情提交后返回成功，然后首页刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshMainEvent(RefreshMainEvent event) {
        onRefresh();
    }

    //交车 基本信息中的交车完成和交车失败提交并且成功后，首页刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshJMainEvent(RefreshJMainEvent event){
        onRefresh();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private static final int REQUEST_STORAGE_GROUP = 1100;

    @AfterPermissionGranted(REQUEST_STORAGE_GROUP)
    private void openPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            showUpdateDialog();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.request_storage_group), REQUEST_STORAGE_GROUP, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.d("onPermissionsGranted");
        showUpdateDialog();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_STORAGE_GROUP) {
            new AppSettingsDialog.Builder(this)
                    .setRequestCode(REQUEST_STORAGE_GROUP)
                    .setTitle("权限获取失败")
                    .setRationale(R.string.setting_request_storage_group)
                    .build().show();
        }
//    }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_STORAGE_GROUP) {
            openPermissions();
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
//                    ToastUtils.showShort("二维码异常");
                } else {
                    Gson gson = new Gson();
                    try {
                        currentQRCodeBean = gson.fromJson(result.getContents(), QRCodeBean.class);
                        mPresenter.carTakeStoreConfirmByQRCode(true, ADApplication.mSPUtils.getString(Api.USERID), currentQRCodeBean.getTCUserID(),
                                currentQRCodeBean.getAgencyID(), currentQRCodeBean.getApplyCD(), currentQRCodeBean.getLAT(), currentQRCodeBean.getLON(), null,currentQRCodeBean.getDatasource());
                    } catch (JsonSyntaxException e) {
//                        ToastUtils.showShort("请正确扫描拖车二维码");
                        SnackbarUtils.showLongDisSnackBar(mDrawerLayout, "请正确扫描拖车二维码");
                        e.printStackTrace();
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private NumberFormat nt;
    private Subscription subscription;
    private boolean isDownloaded;

    //跟新apk
    private void updateAPK() {
        nt = NumberFormat.getPercentInstance();
        String parentDir = ADApplication.getmContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        final String apkPath = parentDir + File.separator + "kingkong_ad.apk";
        final UpdatePresenter presenter = new UpdatePresenter();
        presenter.start();
        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
        ///dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        dialog.setProgress(R.mipmap.ic_launcher);
//        dialog.setSecondaryProgress(R.mipmap.image002);//设置二级进度条的背景
        progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setIcon(R.mipmap.ic_launcher);//
        // 设置提示的title的图标，默认是没有的，需注意的是如果没有设置title的话只设置Icon是不会显示图标的
        progressDialog.setTitle("更新");
        progressDialog.setMax(100);
        // dismiss监听
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        // 监听Key事件被传递给dialog
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return false;
            }
        });
        // 监听cancel事件
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
//        设置可点击的按钮，最多有三个(默认情况下)
//        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if ("点击安装".equals(progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).getText())) {
//                            installApk(apkPath);
//                        }
//                    }
//                });
        progressDialog.setMessage("正在下载......");
        progressDialog.show();
//        progressDialog.incrementProgressBy(1);
//        progressDialog.incrementSecondaryProgressBy(15);//二级进度条更新方式

        presenter.downLoadAPK(apkPath, new ProgressListener() {
            @Override
            public void update(final long bytesRead, final long contentLength, boolean done) {
                if(bytesRead < contentLength){//done为true的进度会进来两次，这里判断了只执行第一次done
                    isDownloaded = false;
                }
                if (done && !isDownloaded) {
                    isDownloaded = true;
                    //这里下载完成以后延迟1秒再去安装下载好的apk
                    //防止安装apk的时候下载的包还没有准备好会出现解析包错误
                    Observable.timer(1, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    installApk(apkPath);
                                }
                            });
//                    installApk(apkPath);
//                    progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("点击安装");
                } else {
                    Observable<Long> updateObservable = Observable.create(new Observable.OnSubscribe<Long>() {
                        @Override
                        public void call(Subscriber<? super Long> subscriber) {
                            subscriber.onNext(Long.valueOf(bytesRead));
                            subscriber.onCompleted();
                        }
                    });
                    subscription = updateObservable
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    //设置百分数精确度2即保留两位小数
                                    nt.setMinimumFractionDigits(2);
                                    float baifen = (float) aLong / (float) contentLength * 100;
//                                    tvProgress.setText((int) baifen + " %");
//                                    progressBar.setProgress((int) baifen);
                                    progressDialog.setProgress((int) baifen);
                                }
                            });
                }
            }
        });
    }

    public void installApk(String apkPath) {
        File file = new File(apkPath);
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(getActivity(), "com.cango.adpickcar.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private String currentWHNO;

    private void showSingleChoiceDialog(List<String> arrayList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("选择库点");
        int size = arrayList.size();
        final String[] items = arrayList.toArray(new String[size]);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentWHNO = items[i];
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.carTakeStoreConfirmByQRCode(true, ADApplication.mSPUtils.getString(Api.USERID),
                        currentQRCodeBean.getTCUserID(), currentQRCodeBean.getAgencyID(),
                        currentQRCodeBean.getApplyCD(), currentQRCodeBean.getLAT(),
                        currentQRCodeBean.getLON(), currentWHNO,currentQRCodeBean.getDatasource());
            }
        })
                .setCancelable(false)
                .create().show();
    }
}
