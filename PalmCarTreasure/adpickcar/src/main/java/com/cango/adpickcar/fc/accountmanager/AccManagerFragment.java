package com.cango.adpickcar.fc.accountmanager;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.customview.CalendarNewFcDialogFragment;
import com.cango.adpickcar.fc.billdetail.BillDetailActivity;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.ToastUtils;
import com.rd.Orientation;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 记账管理
 */
public class AccManagerFragment extends BaseFragment implements IAccManagerContract.IAccManagerView {

    private IAccManagerContract.IAccManagerPresenter mPresenter;
    private AccManagerActivity mActivity;
//    private CalendarAccMgrFG mCalendarDialog;
private CalendarNewFcDialogFragment mCalendarNewFcDialogFragment;
    /**
     * 选中的账目类型
     * 0:旅馆  1:餐费  2:出租/公交  3:火车/客车  4:汽车费  5:快递费  6:其他
     */
    private int chooseType = -1;
    private ArrayList<CheckBox> imageList;//账目类型集合

    @BindView(R.id.acc_mgr_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.vp_acc_manager)
    ViewPager mViewPager;
    @BindView(R.id.pageindicatorview)
    PageIndicatorView mPageIndicatorView;

    @OnClick({R.id.space_data, R.id.ll_tally_accounts, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tally_accounts:
                toOtherUi(BillDetailActivity.class);
                break;
            case R.id.space_data:
                showCalendarDialog();
                break;
            case R.id.tv_confirm:
                //隐藏软键盘
                ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                if(chooseType < 0){
                    ToastUtils.showShort("请选择账目类型");
                }else{
                    ToastUtils.showShort("账目类型:"+imageList.get(chooseType).getText().toString());
                }
                break;
                default:
                    break;
        }
    }

    public static AccManagerFragment newInstance() {
        AccManagerFragment accManagerFragment = new AccManagerFragment();
        return accManagerFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_acc_manager;
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

        initViewPager();
    }

    /**
     * 初始化记账管理的选择项
     */
    private void initViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        adapter.setData(createPageViewList());
        mViewPager.setAdapter(adapter);

        mPageIndicatorView.setViewPager(mViewPager);
        mPageIndicatorView.setOrientation(Orientation.HORIZONTAL);
    }

    private List<View> createPageViewList() {
        List<View> pageList = new ArrayList<>();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_acc_manager_item, null, false);
        imageList = new ArrayList<>();

        CheckBox iv_evection = (CheckBox) view.findViewById(R.id.iv_evection);//旅馆
        CheckBox iv_meal = (CheckBox) view.findViewById(R.id.iv_meal);//餐费
        CheckBox iv_traffic = (CheckBox) view.findViewById(R.id.iv_traffic);//出租/公交
        CheckBox iv_train = (CheckBox) view.findViewById(R.id.iv_train);//火车/客车
        CheckBox iv_crossing = (CheckBox) view.findViewById(R.id.iv_crossing);//汽车费
        CheckBox iv_express = (CheckBox) view.findViewById(R.id.iv_express);//快递费
        CheckBox iv_other = (CheckBox) view.findViewById(R.id.iv_other);//其他
        CheckBox iv_null = (CheckBox) view.findViewById(R.id.iv_null);
        imageList.add(iv_evection);
        imageList.add(iv_meal);
        imageList.add(iv_traffic);
        imageList.add(iv_train);
        imageList.add(iv_crossing);
        imageList.add(iv_express);
        imageList.add(iv_other);
        for(int i=0;i<imageList.size();i++){
            changeViewPagerChecked(imageList.get(i),i);
        }

        pageList.add(view);
        return pageList;
    }

    /**
     * 实现checkbox单选
     * @param mCheckBox 点击的checkbox
     */
    private void changeViewPagerChecked(CheckBox mCheckBox, final int type){
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                if(isChecked){
                    chooseType = type;//选中的账目类型赋值
                    for(CheckBox item:imageList){
                        if(item.getId() != buttonView.getId()){
                            item.setChecked(false);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        mActivity = (AccManagerActivity) getActivity();
    }

    @Override
    public void setPresenter(IAccManagerContract.IAccManagerPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void openLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void toAccountBook() {

    }

    @Override
    public void showConfirmResult(boolean isOK, Object object, String msg) {

    }

    @Override
    public void toOtherUi(Class tClass) {
        startActivity(new Intent(mActivity, tClass));
    }

    /**
     * 显示日历空间dialog
     */
    private void showCalendarDialog() {
        if (CommUtil.checkIsNull(mCalendarNewFcDialogFragment)) {
            mCalendarNewFcDialogFragment = CalendarNewFcDialogFragment.getInstance(false,0);
            mCalendarNewFcDialogFragment.setCalendarDilaogListener(new CalendarNewFcDialogFragment.CalendarDilaogListener() {
                @Override
                public void onCalendarClick(Date date) {

                }
            });
        }
        if (mCalendarNewFcDialogFragment.isVisible()) {

        } else {
            mCalendarNewFcDialogFragment.show(getFragmentManager(), "CalendarDialog");
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private List<View> viewList;

        public ViewPagerAdapter() {
            this.viewList = new ArrayList<>();
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = viewList.get(position);
            collection.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        void setData(@Nullable List<View> list) {
            this.viewList.clear();
            if (list != null && !list.isEmpty()) {
                this.viewList.addAll(list);
            }

            notifyDataSetChanged();
        }

        @NonNull
        List<View> getData() {
            if (viewList == null) {
                viewList = new ArrayList<>();
            }

            return viewList;
        }
    }
}
