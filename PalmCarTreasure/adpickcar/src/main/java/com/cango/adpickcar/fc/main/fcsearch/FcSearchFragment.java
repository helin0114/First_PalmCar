package com.cango.adpickcar.fc.main.fcsearch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseAdapter;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.baseAdapter.OnLoadMoreListener;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页搜索页
 */
public class FcSearchFragment extends BaseFragment implements FcSearchContract.View, SwipeRefreshLayout.OnRefreshListener{
    private static final int PAGE_SIZE = 10;

    @BindView(R.id.toolbar_fc_search)
    Toolbar mToolbar;
    @BindView(R.id.fc_srl_search)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fc_recyclerview_search)
    RecyclerView mRecyclerView;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.et_PlateNO)
    EditText etPlateNO;//车牌号码
    @BindView(R.id.et_Name)
    EditText etName;//客户姓名
    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;//手机号
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;//搜索框
    @BindView(R.id.layout_shadow)
    View layoutShadow;//阴影层

    private FcSearchActivity mActivity;

    private boolean isLoadMore;
    private FcSearchContract.Presenter mPresenter;
    private int mPageCount = 1, mTempPageCount = 2;

    private List<FcVisitTaskList.TaskListBean> mDatas;
    private FcSearchAdapter mFcSearchAdapter;

    @OnClick({R.id.iv_search, R.id.layout_shadow, R.id.layout_search})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_search://点击右上角搜索按钮
                layoutShadow.setVisibility(View.VISIBLE);
                layoutSearch.setVisibility(View.VISIBLE);
                //车牌号码输入框得到焦点
                etPlateNO.requestFocus();
                //弹出键盘
                ((InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(etPlateNO, 0);
                break;
            case R.id.layout_shadow://点击阴影层
                layoutShadow.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.GONE);
                break;
            case R.id.layout_search:
                break;
                default:
                    break;
        }
    }

    public static FcSearchFragment getInstance(){
        FcSearchFragment mFcSearchFragment = new FcSearchFragment();
        Bundle bundle = new Bundle();
        mFcSearchFragment.setArguments(bundle);
        return mFcSearchFragment;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_fc_search;
    }

    @Override
    protected void initView() {
        showLoadView(false);
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

        initRecyclerView();
        etPlateNO.requestFocus();//车牌号码输入框得到焦点
        //监听车牌号码输入框的搜索按钮
        editTextListener(etPlateNO);
    }
    private void initRecyclerView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mDatas = new ArrayList<>();
        mFcSearchAdapter = new FcSearchAdapter(mActivity, mDatas, true);
        mFcSearchAdapter.setLoadingView(R.layout.load_loading_layout);
        mFcSearchAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                getData();
            }
        });
        mFcSearchAdapter.setOnItemClickListener(new OnBaseItemClickListener<FcVisitTaskList.TaskListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, final FcVisitTaskList.TaskListBean data, final int position) {
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mFcSearchAdapter);
    }

    /**
     * 监听输入框的搜索按钮
     * @param mEditText
     */
    private void editTextListener(EditText mEditText){
        //监听车牌号码输入框的搜索按钮
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    layoutShadow.setVisibility(View.GONE);
                    layoutSearch.setVisibility(View.GONE);
                    getData();
                }
                return false;
            }
        });
    }

    private void getData(){
        mPresenter.getSearchResultTaskList(ADApplication.mSPUtils.getString(Api.USERID), mPageCount+"", PAGE_SIZE+"");
    }

    @Override
    protected void initData() {
        mActivity = (FcSearchActivity) getActivity();
    }

    @Override
    public void setPresenter(FcSearchContract.Presenter presenter) {
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
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void openOtherUi() {

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
    public void showError() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    @Override
    public void showNoData() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTasksSuccess(List<FcVisitTaskList.TaskListBean> data) {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (data!=null && data.size() == 0){
            if (isLoadMore){
                mFcSearchAdapter.setLoadEndView(R.layout.load_end_layout);
            } else {
                showNoData();
            }
            return;
        }
        if (isLoadMore) {
            mTempPageCount++;
            mFcSearchAdapter.setLoadMoreData(data);
        } else {
            mFcSearchAdapter.setNewDataNoError(data);
        }
        if (data.size() < PAGE_SIZE) {
            mFcSearchAdapter.setLoadEndView(R.layout.load_end_layout);
        }
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mPageCount = 1;
        mTempPageCount = 2;
        showMainIndicator(false);
        mDatas.clear();
        mFcSearchAdapter.setLoadingView(R.layout.load_loading_layout);
        mFcSearchAdapter.notifyDataSetChanged();
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        getData();
    }

    private class FcSearchAdapter extends BaseAdapter<FcVisitTaskList.TaskListBean> {

        public FcSearchAdapter(Context context, List<FcVisitTaskList.TaskListBean> datas, boolean isOpenLoadMore) {
            super(context, datas, isOpenLoadMore);
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.fc_tasks_item;
        }

        @Override
        protected void convert(BaseHolder holder, final FcVisitTaskList.TaskListBean data) {
            TextView tvId = holder.getView(R.id.fc_item_id);
            TextView tvOverdueTerm = holder.getView(R.id.tv_overdue_term);
            TextView tvOverdueDay = holder.getView(R.id.tv_overdue_day);
            TextView tvOverdueMoney = holder.getView(R.id.tv_overdue_money);
            TextView tvName = holder.getView(R.id.tv_fc_tasks_item_name);
            TextView tvPlate = holder.getView(R.id.tv_fc_tasks_item_plate);
            TextView tvAllotment = holder.getView(R.id.tv_tasks_item_allotment);
            TextView tvDistance = holder.getView(R.id.tv_fc_tasks_item_distance);
            TextView tvDate = holder.getView(R.id.tv_fc_tasks_item_date);
            TextView tvLabelOne = holder.getView(R.id.tv_fc_item_label_one);
            TextView tvLabelTwo = holder.getView(R.id.tv_fc_item_label_two);
            TextView tvLabelSubmit = holder.getView(R.id.tv_fc_item_label_submit);
        }
    }
}
