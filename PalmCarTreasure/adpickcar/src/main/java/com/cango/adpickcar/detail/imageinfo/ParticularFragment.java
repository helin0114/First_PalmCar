package com.cango.adpickcar.detail.imageinfo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.PreviewActivity;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.baseAdapter.BaseAdapter;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.baseAdapter.GridSpacingItemDecoration;
import com.cango.adpickcar.baseAdapter.OnBaseItemClickListener;
import com.cango.adpickcar.camera.CameraActivity;
import com.cango.adpickcar.detail.DetailActivity;
import com.cango.adpickcar.detail.DetailFragment;
import com.cango.adpickcar.detail.DetailPresenter;
import com.cango.adpickcar.model.CarFilesInfo;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.model.PhotoResult;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.SizeUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ParticularFragment extends BaseFragment {
    private static final int REQUEST_IMAGE_CAPTURE_PARTICULAR = 1002;

    public static FacadeFragment getInstance() {
        FacadeFragment fragment = new FacadeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.rv_particular)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.ll_sorry)
    LinearLayout llSorry;

    private DetailActivity mActivity;
    private DetailFragment detailFragment;
    private DetailPresenter presenter;
    private CarTakeTaskList.DataBean.CarTakeTaskListBean mCarTakeTaskListBean;
    private CarFilesInfo mCarFilesInfo;
    private boolean isEdit;
    private ArrayList<CarFilesInfo.DataBean.SurfaceFileListBean> mDatas;
    private ParticularFragment.ParticularAdapter mAdapter;
    private int currentPostion;
    private boolean isOver;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_particular;
    }

    @Override
    protected void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mDatas = new ArrayList<>();
        mAdapter = new ParticularFragment.ParticularAdapter(mActivity, mDatas, false);
        mAdapter.setOnItemClickListener(new OnBaseItemClickListener<CarFilesInfo.DataBean.SurfaceFileListBean>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, CarFilesInfo.DataBean.SurfaceFileListBean data, int position) {
                if (!isEdit || TextUtils.isEmpty(data.getPicPath()))
                    return;
//                currentPostion = position;
//                Intent cameraIntent = new Intent(mActivity, CameraActivity.class);
//                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE_PARTICULAR);
                Intent intent = new Intent(mActivity, PreviewActivity.class);
                intent.putExtra("preview_path",data.getPicPath());
                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, SizeUtil.dp2px(mActivity, 10), true));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mActivity = (DetailActivity) getActivity();
        detailFragment = (DetailFragment) getParentFragment().getParentFragment();
        presenter = (DetailPresenter) ((DetailFragment) (getParentFragment().getParentFragment())).mPresenter;
        mCarTakeTaskListBean = ((DetailFragment) (getParentFragment().getParentFragment())).mCarTakeTaskListBean;
        isEdit = ((DetailFragment) (getParentFragment().getParentFragment())).isEdit;
    }

    public void updateUI(CarFilesInfo carFilesInfo) {
        mCarFilesInfo = carFilesInfo;
        mDatas.clear();
        mAdapter.notifyDataSetChanged();
        if (CommUtil.checkIsNull(mCarFilesInfo.getData().getDetailList())) {
            showNoData();
        } else {
            mDatas.addAll(carFilesInfo.getData().getDetailList());
            mAdapter.notifyDataSetChanged();
        }
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
    }

    public void showError() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
    }

    public void showNoData() {
        mRecyclerView.setVisibility(View.GONE);
        llSorry.setVisibility(View.GONE);
        llNoData.setVisibility(View.VISIBLE);
    }

    public void updateAddPhoto(PhotoResult photoResult) {
        //更新有subname的图片地址
        CarFilesInfo.DataBean.SurfaceFileListBean bean = mDatas.get(currentPostion);
        bean.setPicFileID(photoResult.getData().getPicFileID());
        bean.setPicPath(photoResult.getData().getPicPath());
        bean.setThumbPath(photoResult.getData().getThumbPath());
        mAdapter.notifyItemChanged(currentPostion);
    }

    public void updateDeletePhoto() {
        CarFilesInfo.DataBean.SurfaceFileListBean bean = mDatas.get(currentPostion);
        if (bean.getPicFileID() == -1) {
            //类型是拍摄更多图片的按钮
        } else if (TextUtils.isEmpty(bean.getSubName())) {
            //类型拍摄更多后的图片
            mDatas.remove(currentPostion);
            mAdapter.notifyItemRemoved(currentPostion);
            mAdapter.notifyItemRangeChanged(currentPostion, mDatas.size());
        } else {
            //类型是有btn按钮的图片
            bean.setPicFileID(0);
            bean.setPicPath("");
            bean.setThumbPath("");
            mAdapter.notifyItemChanged(currentPostion);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mDatas",mDatas);
        outState.putInt("currentPostion",currentPostion);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null){
            mDatas = savedInstanceState.getParcelableArrayList("mDatas");
            currentPostion = savedInstanceState.getInt("currentPostion");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String photoPath = null;
        if (requestCode == REQUEST_IMAGE_CAPTURE_PARTICULAR) {
            if (resultCode == Activity.RESULT_OK) {
                PhotoResult mPhotoResult = data.getParcelableExtra("mPhotoResult");
                if(mPhotoResult != null){
                    updateAddPhoto(mPhotoResult);
                }
//                photoPath = data.getStringExtra("path");
//                if (!TextUtils.isEmpty(photoPath)) {
////                    mDatas.get(currentPostion).setPicPath(photoPath);
////                    mAdapter.notifyItemChanged(currentPostion);
//                    CarFilesInfo.DataBean.SurfaceFileListBean bean = mDatas.get(currentPostion);
//                    detailFragment.zipPicture(1, 1, photoPath, ADApplication.mSPUtils.getString(Api.USERID),
//                            mCarTakeTaskListBean.getDisCarID() + "", "60", bean.getSubCategory(),
//                            bean.getSubID(), bean.getPicFileID() + "");
//                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                if (photoPath != null) {
                    File emptyFile = new File(photoPath);
                    if (emptyFile.exists()) emptyFile.delete();
                }
            }
        }
    }

    public class ParticularAdapter extends BaseAdapter<CarFilesInfo.DataBean.SurfaceFileListBean> {
        CenterCrop centerCrop;
        ParticularFragment.GlideRoundTransform glideRoundTransform;
        RequestOptions options;

        public ParticularAdapter(Context context, List<CarFilesInfo.DataBean.SurfaceFileListBean> datas, boolean isOpenLoadMore) {
            super(context, datas, isOpenLoadMore);
            centerCrop = new CenterCrop(mContext);
            glideRoundTransform = new ParticularFragment.GlideRoundTransform(mContext, 5);
            options = new RequestOptions()
                    .placeholder(R.drawable.photosimg)
                    .error(R.drawable.loadfailure)
                    .transforms(centerCrop, glideRoundTransform);
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.image_card_item;
        }

        @Override
        protected void convert(final BaseHolder holder, final CarFilesInfo.DataBean.SurfaceFileListBean data) {
            ImageView ivContent = holder.getView(R.id.iv_image_card_item);
            Button btnPrompt = holder.getView(R.id.btn_image_prompt);
            ImageView ivClose = holder.getView(R.id.iv_image_close);
            btnPrompt.setText(data.getSubName());
            if (!TextUtils.isEmpty(data.getPicPath())) {
                Glide.with(mContext).load(data.getPicPath())
//                        .placeholder(R.drawable.photosimg)
//                        .error(R.drawable.loadfailure)
//                        .transform(centerCrop, glideRoundTransform)
                        .apply(options)
                        .into(ivContent);
//                ivContent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mActivity, PreviewActivity.class);
//                        intent.putExtra("preview_path",data.getPicPath());
//                        startActivity(intent);
//                    }
//                });
            } else {
                Glide.with(mContext).load(R.drawable.photosimg)
//                        .placeholder(R.drawable.photosimg)
//                        .error(R.drawable.loadfailure)
//                        .transform(centerCrop, glideRoundTransform)
                        .apply(options)
                        .into(ivContent);
            }
            btnPrompt.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(data.getPicPath())) {
                ivClose.setVisibility(View.VISIBLE);
            } else {
                ivClose.setVisibility(View.INVISIBLE);
            }
            if (isEdit){
                ivClose.setVisibility(View.VISIBLE);
            }else {
                ivClose.setVisibility(View.INVISIBLE);
            }
            btnPrompt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEdit)
                        return;
                    currentPostion = holder.getAdapterPosition();
                    Intent cameraIntent = new Intent(mActivity, CameraActivity.class);
                    cameraIntent.putExtra("SurfaceFileListBean",mDatas.get(currentPostion));
                    cameraIntent.putExtra("fromFragmentFlag", CameraActivity.FLAG_PARTICULAR);
                    cameraIntent.putExtra("bean", mDatas.get(currentPostion));
                    cameraIntent.putExtra("mCarTakeTaskListBean", mCarTakeTaskListBean);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE_PARTICULAR);
                }
            });
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (data.getPicFileID() == -1) {
//                        //类型是拍摄更多图片的按钮
//                    } else if (TextUtils.isEmpty(data.getSubName())) {
//                        //类型拍摄更多后的图片
//                        int deletePosition = holder.getAdapterPosition();
//                        mDatas.remove(deletePosition);
//                        mAdapter.notifyItemRemoved(deletePosition);
//                    } else {
//                        //类型是有btn按钮的图片
//                        int changePosition = holder.getAdapterPosition();
//                        mDatas.get(changePosition).setPicPath("");
//                        mAdapter.notifyItemChanged(changePosition);
//                    }
                    if (!isEdit || data.getPicFileID() == 0)
                        return;
                    currentPostion = holder.getAdapterPosition();
                    detailFragment.DeletePhoto(1, 1, true, ADApplication.mSPUtils.getString(Api.USERID), data.getPicFileID() + "");
                }
            });
        }
    }

    public class GlideRoundTransform extends BitmapTransformation {

        private float radius = 0f;

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

//        @Override
//        public String getId() {
//            return getClass().getName() + Math.round(radius);
//        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }
}
