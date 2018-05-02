package com.cango.adpickcar.detail.imageinfo;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.detail.DetailActivity;
import com.cango.adpickcar.detail.DetailFragment;
import com.cango.adpickcar.detail.DetailPresenter;
import com.cango.adpickcar.model.CarFilesInfo;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.model.PhotoResult;

import java.util.ArrayList;

import butterknife.BindView;

public class ImageInfoFragment extends BaseFragment {
    public static ImageInfoFragment getInstance(CarTakeTaskList.DataBean.CarTakeTaskListBean bean) {
        ImageInfoFragment imageInfoFragment = new ImageInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean",bean);
        imageInfoFragment.setArguments(bundle);
        return imageInfoFragment;
    }

    @BindView(R.id.tl_image)
    TabLayout mTabLayout;
    @BindView(R.id.vp_image)
    public ViewPager mViewPager;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    private DetailActivity mActivity;
    private DetailPresenter presenter;
    private CarTakeTaskList.DataBean.CarTakeTaskListBean mCarTakeTaskListBean;
    private boolean isEdit;
    public CarFilesInfo mCarFilesInfo;
    private ArrayList<String> tabList = new ArrayList<>();
    private ArrayList<Fragment> fgList = new ArrayList<>();
    private FacadeFragment mFacadeFragment;
    private ParticularFragment mParticularFragment;
    private SupplementFragment mSupplementFragment;
    private MyAdapter mAdapter;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_image_info_other;
    }

    @Override
    protected void initView() {
        initTabLayout();
    }

    public void getData() {
        presenter.getCarFilesInfo(true, mCarTakeTaskListBean.getDisCarID() + "");
    }

    public void updateUI(CarFilesInfo carFilesInfo) {
        mCarFilesInfo = carFilesInfo;
//        initTabLayout();
        mFacadeFragment.updateUI(mCarFilesInfo);
        mParticularFragment.updateUI(mCarFilesInfo);
        mSupplementFragment.updateUI(mCarFilesInfo);

        mTabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
    }

    public void showError() {
        mTabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    public void showNoData() {
        mTabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    private void initTabLayout() {
        tabList.clear();
        fgList.clear();
        tabList.add("外观照");
        tabList.add("详细照");
        tabList.add("补充照");
        mFacadeFragment = new FacadeFragment();
        mParticularFragment = new ParticularFragment();
        mSupplementFragment = new SupplementFragment();
        fgList.add(mFacadeFragment);
        fgList.add(mParticularFragment);
        fgList.add(mSupplementFragment);
        mAdapter = new MyAdapter(getChildFragmentManager(), tabList, fgList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    public void updateAddFacadePhoto(PhotoResult photoResult) {
        if (mFacadeFragment != null) {
            mFacadeFragment.updateAddPhoto(photoResult);
        }
    }

    public void updateDelteFacadePhoto() {
        if (mFacadeFragment != null) {
            mFacadeFragment.updateDeletePhoto();
        }
    }

    public void updateAddPartPhoto(PhotoResult photoResult) {
        if (mParticularFragment != null) {
            mParticularFragment.updateAddPhoto(photoResult);
        }
    }

    public void updateDeltePartPhoto() {
        if (mParticularFragment != null) {
            mParticularFragment.updateDeletePhoto();
        }
    }

    public void updateAddSupplePhoto(PhotoResult photoResult) {
        if (mSupplementFragment != null) {
            mSupplementFragment.updateAddPhoto(photoResult);
        }
    }

    public void updateDelteSupplePhoto() {
        if (mSupplementFragment != null) {
            mSupplementFragment.updateDeletePhoto();
        }
    }

    @Override
    protected void initData() {
        mActivity = (DetailActivity) getActivity();
        presenter = (DetailPresenter) ((DetailFragment) getParentFragment()).mPresenter;
        mCarTakeTaskListBean = getArguments().getParcelable("bean");
        isEdit = ((DetailFragment) getParentFragment()).isEdit;
    }

    public class MyAdapter extends FragmentStatePagerAdapter {

        private ArrayList<String> titleList;
        private ArrayList<Fragment> fragmentList;

        public MyAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

}
