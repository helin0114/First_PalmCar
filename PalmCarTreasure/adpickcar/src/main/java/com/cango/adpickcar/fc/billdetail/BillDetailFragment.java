package com.cango.adpickcar.fc.billdetail;


import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.OnLoadMoreListener;
import com.cango.adpickcar.customview.SectionBaseAdapter;
import com.cango.adpickcar.customview.SlidingButtonView;
import com.cango.adpickcar.util.ScreenUtil;
import com.cango.adpickcar.util.SizeUtil;
import com.cango.adpickcar.util.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class BillDetailFragment extends BaseFragment implements BillDetailContract.BillDetailView {

    @BindView(R.id.bill_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_bill_detail)
    RecyclerView mRecyclerView;
    @BindView(R.id.av_loading)
    AVLoadingIndicatorView avLoading;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    private BillDetailActivity mActivity;
    private SectionBaseAdapter mAdapter;
    private List<Section> mBillSectionList = new ArrayList<>();
    private int mPageCount = 1, mTempPageCount = 2;
    static int PAGE_SIZE = 5;
    private boolean isLoadMore;
    int rvPadding;

    public static BillDetailFragment newInstance() {
        return new BillDetailFragment();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_bill_detail;
    }

    @Override
    protected void initView() {
        showAVLoading(false);
        rvPadding = SizeUtil.dp2px(mActivity, 10);
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
    }

    private void initRecyclerView() {
        mAdapter = new SectionBaseAdapter(mActivity, true, mBillSectionList);
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mPageCount = mTempPageCount;
                            Thread.sleep(2 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<String> list = new ArrayList<>();
                                list.add("1");
                                list.add("2");
                                int a = mAdapter.getItemCount();
                                BillSection section = new BillSection(String.valueOf(2), list);
                                mAdapter.addSection(section);
                                mBillSectionList.add(section);
                                mAdapter.notifyItemRangeInserted(a - 1, 3);
                                int b = mAdapter.getItemCount();
//                                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 2);
                                mTempPageCount++;
                            }
                        });
                    }
                }).start();
            }
        });

        for (char alphabet = 'A'; alphabet <= 'C'; alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet);

            if (contacts.size() > 0) {
                BillSection billSection = new BillSection(String.valueOf(alphabet), contacts);
                mAdapter.addSection(billSection);
                mBillSectionList.add(billSection);

            }
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

    }

    private List<String> getContactsWithLetter(char letter) {
        List<String> contacts = new ArrayList<>();

        for (String contact : getResources().getStringArray(R.array.names)) {
            if (contact.charAt(0) == letter) {
                contacts.add(contact);
            }
        }

        return contacts;
    }

    @Override
    protected void initData() {
        mActivity = (BillDetailActivity) getActivity();
    }

    @Override
    public void setPresenter(BillDetailContract.BillDetailPresenter presenter) {

    }

    @Override
    public void showAVLoading(boolean isShow) {
        if (isShow)
            avLoading.smoothToShow();
        else
            avLoading.smoothToHide();
    }

    @Override
    public void updateUi() {
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
//        if (isLoadMore) {
//            mTempPageCount++;
//            mAdapter.setLoadMoreData(carTakeTaskListBeanList);
//        } else {
//            mAdapter.setNewDataNoError(carTakeTaskListBeanList);
//        }
//        if (carTakeTaskListBeanList.size() < PAGE_SIZE) {
//            mAdapter.setLoadEndView(R.layout.load_end_layout);
//        }
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
    public void showSorry() {
        if (isLoadMore) {
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            llSorry.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    class BillSection extends StatelessSection implements SlidingButtonView.IonSlidingButtonListener {

        String title;
        List<String> list;
        private SlidingButtonView mMenu = null;

        public BillSection(String title, List<String> list) {
            super(R.layout.bill_detail_head, R.layout.bill_detail_content_side);
            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)headerHolder.rootView.getLayoutParams();
            //如果某一项的数据为空，那么整项都要影藏消失
            if (list.size() <= 0){
                headerHolder.rootView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }else{
                param.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                param.width = RecyclerView.LayoutParams.MATCH_PARENT;
                headerHolder.rootView.setVisibility(View.VISIBLE);
            }
            headerHolder.rootView.setLayoutParams(param);
            headerHolder.tvName.setText(title);
        }

        @Override
        public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            final String name = list.get(position);
            itemHolder.tvName.setText(name);
            if (position == list.size() - 1) {
                itemHolder.rootView.setBackgroundResource(R.drawable.bill_detail_content_bg);
                itemHolder.line.setVisibility(View.GONE);
            } else {
                itemHolder.rootView.setBackgroundColor(Color.WHITE);
                itemHolder.line.setVisibility(View.VISIBLE);
            }
//            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtils.showShort(R.string.date_prompt);
//                }
//            });
            itemHolder.clContent.getLayoutParams().width = ScreenUtil.getScreenWidth(mActivity) - rvPadding * 2;
            itemHolder.clContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否有删除菜单打开
                    if (menuIsOpen()) {
                        closeMenu();//关闭菜单
                    } else {
                        int n = itemHolder.getLayoutPosition();
                        ToastUtils.showShort("onItemClick:" + n + name);
                    }
                }
            });
            itemHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMenu();
                    int n = itemHolder.getLayoutPosition();
                    int listSize = list.size();
                    int headPosition = n - position-1;
//                    ToastUtils.showShort("onDelete:" + n);
//                    list.remove(position);
//                    mAdapter.notifyItemRemoved(n);
//                    mAdapter.notifyItemRangeChanged(headPosition, listSize);

                    list.remove(position);
                    mAdapter.notifyItemRemoved(holder.getAdapterPosition());
                    //原本上一步骤的notifyItemRemoved方法已经调用过刷新，这个时候adapterposition已经改变了，
                    // 但是position并不会改变因为删除了一行，并没有不会出发别的行的onbindholder方法，所以position
                    //并没有刷新还是上一次的position，因为list删除的话不能使用getAdapterPosition，所以我需要
                    //把headpostion至这个list全部的刷新一遍，那么就会重新调用onbind方法那么positon就会改变，
                    //list.remove删除的话就不会数组越界，madpter删除的话就不需要了，因为删除的position都是
                    //getAdapterPosition,所有的notifiy方法都必须确定删除的position是正确的下表，只要下表没错的话
                    //madapter怎么删除都不会错误，出错误的地方在于list集合删除position的下表不正确
                    mAdapter.notifyItemRangeChanged(headPosition,listSize);
                    mAdapter.scrollLoadMore();

                }
            });
            itemHolder.rootView.setSlidingButtonListener(BillSection.this);
        }

        //删除菜单打开信息接收
        @Override
        public void onMenuIsOpen(View view) {
            mMenu = (SlidingButtonView) view;
        }

        /**
         * 滑动或者点击了Item监听
         *
         * @param slidingButtonView
         */
        @Override
        public void onDownOrMove(SlidingButtonView slidingButtonView) {
            if (menuIsOpen()) {
//                if(mMenu != slidingButtonView){
                closeMenu();
//                }
            }
        }

        //关闭菜单
        public void closeMenu() {
            mMenu.closeMenu();
            mMenu = null;
        }

        //判断是否有菜单打开
        public Boolean menuIsOpen() {
            if (mMenu != null) {
                return true;
            }
            return false;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvName;

        public HeaderViewHolder(View view) {
            super(view);
            rootView = view;
            tvName = (TextView) view.findViewById(R.id.tv_head_name);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final SlidingButtonView rootView;
        private final ImageView ivIcon;
        private final TextView tvName;
        private final TextView tvDate;
        private final TextView tvMoney;
        private final View line;
        private final ConstraintLayout clContent;
        private final TextView tvDelete;

        public ItemViewHolder(View view) {
            super(view);
            rootView = (SlidingButtonView) view;
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvMoney = (TextView) view.findViewById(R.id.tv_money);
            line = view.findViewById(R.id.tv_line);
            clContent = (ConstraintLayout) view.findViewById(R.id.cl_content);
            tvDelete = (TextView) view.findViewById(R.id.tv_delete);
        }
    }
}
