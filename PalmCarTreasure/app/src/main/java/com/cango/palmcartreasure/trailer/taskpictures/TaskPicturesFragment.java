package com.cango.palmcartreasure.trailer.taskpictures;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.PreviewActivity;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.TaskPicturesService;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.baseAdapter.MemberItemDecoration;
import com.cango.palmcartreasure.baseAdapter.OnBaseItemClickListener;
import com.cango.palmcartreasure.model.TackPictureInfo;
import com.cango.palmcartreasure.model.TaskPicInfo;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.trailer.taskdetail.TaskDetailFragment;
import com.cango.palmcartreasure.util.SizeUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dell on 2018/3/14.
 */

public class TaskPicturesFragment extends BaseFragment implements TaskPicturesContract.View {

    @BindView(R.id.toolbar_taskpicture)
    Toolbar mToolbar;
    @BindView(R.id.avl_login_indicator)
    AVLoadingIndicatorView mLoadView;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.recyclerView_taskpicture)
    RecyclerView mRecyclerView;

    private TaskPicturesContract.Presenter mPresenter;
    private TaskPicturesActivity mActivity;
    private List<TackPictureInfo.FileItemInfo> mDatas;
    private TaskPicturesAdapter mAdapter;
    private TypeTaskData.DataBean.TaskListBean mTaskListBean;
    private TaskPicturesService mService;


    public static TaskPicturesFragment newInstance(TypeTaskData.DataBean.TaskListBean taskListBean) {
        TaskPicturesFragment fragment = new TaskPicturesFragment();
        Bundle args = new Bundle();
        args.putParcelable(TaskDetailFragment.TASKLISTBEAN, taskListBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_taskpictures;
    }

    @Override
    protected void initView() {
        showLoadingView(false);
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.admin_return);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mService = NetManager.getInstance().create(TaskPicturesService.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mDatas = new ArrayList<>();
        mAdapter = new TaskPicturesAdapter(mActivity, mDatas, false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<TackPictureInfo.FileItemInfo>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, TackPictureInfo.FileItemInfo data, int position) {
                Intent intent = new Intent(mActivity, PreviewActivity.class);
//                MtApplication.TASKBYTES = data.getBytes();
                MtApplication.TASKBYTESLIST = fileItemInfos;
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mRecyclerView.addItemDecoration(new MemberItemDecoration(3, SizeUtil.dp2px(mActivity, 8), true));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getErpimageinfo(mTaskListBean.getDatasource(), mTaskListBean.getApplyCD());
    }

    @Override
    protected void initData() {
        mActivity = (TaskPicturesActivity) getActivity();
        mTaskListBean = getArguments().getParcelable(TaskDetailFragment.TASKLISTBEAN);
    }

    @Override
    public void setPresenter(TaskPicturesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadingView(boolean isShow) {
        if (isShow)
            mLoadView.smoothToShow();
        else
            mLoadView.smoothToHide();
    }

    @Override
    public void showNoData() {
        llNoData.setVisibility(View.VISIBLE);
        llSorry.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        llNoData.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    ArrayList<TackPictureInfo.FileItemInfo> fileItemInfos;

    @Override
    public void success(TackPictureInfo pictureInfo) {
        fileItemInfos = pictureInfo.getData().getFileList();
        mDatas.addAll(fileItemInfos);
        //遍历后可以得到每一个的bean，这么就可以给每一个bean设置bytes，那么就会有了对应关系，就不会错乱
        for (final TackPictureInfo.FileItemInfo bean : fileItemInfos) {
            mService.getPicInfo(bean.getFileUrl())
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<TaskPicInfo, byte[]>() {
                        @Override
                        public byte[] call(TaskPicInfo taskPicInfo) {
                            return Base64.decode(taskPicInfo.getBase64Content(), Base64.DEFAULT);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<byte[]>() {
                        @Override
                        protected void _onNext(byte[] o) {
                            bean.setBytes(o);
//                            mHandler.obtainMessage(SETBYTESOK).sendToTarget();

                            //
                            int position = fileItemInfos.indexOf(bean);
                            mAdapter.notifyItemChanged(position);
                        }

                        @Override
                        protected void _onError() {
                            bean.setBytes(new byte[]{});
//                            mHandler.obtainMessage(SETBYTESOK).sendToTarget();
                            int position = fileItemInfos.indexOf(bean);
                            mAdapter.notifyItemChanged(position);
                        }
                    });
        }
//        Observable.from(fileItemInfos)
//                .subscribeOn(Schedulers.io())
//                .flatMap(new Func1<TackPictureInfo.FileItemInfo, Observable<TaskPicInfo>>() {
//                    @Override
//                    public Observable<TaskPicInfo> call(TackPictureInfo.FileItemInfo fileItemInfo) {
//                        return mService.getPicInfo(fileItemInfo.getFileUrl());
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .map(new Func1<TaskPicInfo, byte[]>() {
//                    @Override
//                    public byte[] call(TaskPicInfo taskPicInfo) {
//                        return Base64.decode(taskPicInfo.getBase64Content(), Base64.DEFAULT);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxSubscriber<byte[]>() {
//                    @Override
//                    protected void _onNext(byte[] o) {
//
//                    }
//
//                    @Override
//                    protected void _onError() {
//
//                    }
//                });
    }

    public class TaskPicturesAdapter extends BaseAdapter<TackPictureInfo.FileItemInfo> {
        public TaskPicturesAdapter(Context context, List<TackPictureInfo.FileItemInfo> datas, boolean isOpenLoadMore) {
            super(context, datas, isOpenLoadMore);
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.task_pictures_item;
        }

        @Override
        protected void convert(BaseHolder holder, final TackPictureInfo.FileItemInfo data) {
            final ImageView mPhotoView = holder.getView(R.id.photo_view);
            Glide.with(mContext)
                    .load(data.getBytes())
                    .placeholder(R.drawable.loadfailure)
                    .error(R.drawable.loadfailure)
                    .into(mPhotoView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MtApplication.TASKBYTESLIST = null;
    }
}
