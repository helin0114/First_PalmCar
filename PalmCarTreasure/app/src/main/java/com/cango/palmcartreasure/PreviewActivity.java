package com.cango.palmcartreasure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cango.palmcartreasure.api.TaskPicturesService;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.TackPictureInfo;
import com.cango.palmcartreasure.model.TaskPicInfo;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.util.ToastUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PreviewActivity extends BaseActivity {

//    private PhotoView mPhotoView;
    private ViewPager mViewpager;
    private ArrayList<TackPictureInfo.FileItemInfo> fileItemInfos;
    private TaskPicturesService mService;
    private ArrayList<View> imagesList;
    private RelativeLayout rlShadow;
    private PopupWindow mPopupWindow;
    private int currentPostion;//当前显示的图片位置

    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int SAVE_BEGIN = 2;//开始保存图片

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_BEGIN:
                    ToastUtils.showShort("开始保存图片...");
                    break;
                case SAVE_SUCCESS:
                    ToastUtils.showShort("图片保存成功,请到相册查找");
                    break;
                case SAVE_FAILURE:
                    ToastUtils.showShort("图片保存失败,请稍后再试...");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
//        mPhotoView = (PhotoView) findViewById(R.id.photo_view);
//        byte[] bytes = MtApplication.TASKBYTES;
//        Glide.with(this)
//                .load(bytes)
//                .placeholder(R.drawable.loadfailure)
//                .error(R.drawable.loadfailure)
//                .into(mPhotoView);
//        mPhotoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PreviewActivity.this.finish();
//            }
//        });
//        pathList = getIntent().getStringArrayListExtra("pathList");
//
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        rlShadow = (RelativeLayout) findViewById(R.id.rl_shadow);

        fileItemInfos = MtApplication.TASKBYTESLIST;
        mService = NetManager.getInstance().create(TaskPicturesService.class);
        mPopupWindow = getPopupWindow(PreviewActivity.this);

        imagesList = new ArrayList<>();
        for(TackPictureInfo.FileItemInfo item:fileItemInfos) {
            if(item.getBytes() == null || item.getBytes().length <= 0){
                item.setLoader(false);
            }else{
                item.setLoader(true);
            }
            View view = LayoutInflater.from(this).inflate(R.layout.layout_preview_item, null);
            PhotoView mPhotoView = (PhotoView) view.findViewById(R.id.photo_view);
            TextView textView = (TextView) view.findViewById(R.id.tv_name);
            textView.setText(item.getFileName());
            Glide.with(this)
                    .load(item.getBytes())
                    .placeholder(R.drawable.loadfailure)
                    .error(R.drawable.loadfailure)
                    .into(mPhotoView);
            mPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreviewActivity.this.finish();
                }
            });
            mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mPopupWindow.showAtLocation(PreviewActivity.this.findViewById(R.id.layout_main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    rlShadow.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            imagesList.add(view);
        }
        mViewpager.setAdapter(new ViewPagerAdapter(imagesList,PreviewActivity.this));
        int position = getIntent().getIntExtra("position",0);
        currentPostion = position;
        mViewpager.setCurrentItem(position);
        getPictureUrl(position);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(final int position) {
                currentPostion = position;
                getPictureUrl(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 判断图片是否被下载过，如果没有下载过要给它下载然后显示
     * @param position
     */
    private void getPictureUrl(final int position){
        if(fileItemInfos.get(position).getBytes() == null || fileItemInfos.get(position).getBytes().length <= 0){
            mService.getPicInfo(fileItemInfos.get(position).getFileUrl())
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
                            fileItemInfos.get(position).setBytes(o);
                            fileItemInfos.get(position).setLoader(true);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if(!isDestroyed()){
                                    Glide.with(PreviewActivity.this)
                                            .load(fileItemInfos.get(position).getBytes())
                                            .placeholder(R.drawable.loadfailure)
                                            .error(R.drawable.loadfailure)
                                            .into((PhotoView)imagesList.get(position).findViewById(R.id.photo_view));
                                }
                            }
                        }

                        @Override
                        protected void _onError() {
                            fileItemInfos.get(position).setBytes(new byte[]{});
                        }
                    });
        } else {
            if(!fileItemInfos.get(position).isLoader()){
                Glide.with(PreviewActivity.this)
                        .load(fileItemInfos.get(position).getBytes())
                        .placeholder(R.drawable.loadfailure)
                        .error(R.drawable.loadfailure)
                        .into((PhotoView)imagesList.get(position).findViewById(R.id.photo_view));
                fileItemInfos.get(position).setLoader(true);
            }
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {
        //        private ArrayList<String> pathList;
        private ArrayList<View> images = new ArrayList<>();
//        private Context context;

        public ViewPagerAdapter(ArrayList<View> images, Context context) {
            this.images = images;
//            this.context = context;
        }

        @Override
        public int getCount() {
            return images == null ? 0 : images.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(images.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }
    }

    /**
     * 显示下载的popwindow
     * @param context
     * @return
     */
    public PopupWindow getPopupWindow(Context context) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.layout_preview_pop, null);
        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvSave = (TextView) popupView.findViewById(R.id.tv_save);
        TextView tvCancel = (TextView) popupView.findViewById(R.id.tv_cancel);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileItemInfos.get(currentPostion).getBytes() == null || fileItemInfos.get(currentPostion).getBytes().length <= 0){
                    ToastUtils.showShort("图片未加载完成，请稍后重试");
                }else{
                    if (checkDownOver()) {
                        ToastUtils.showShort("你已经下载过此图片了");
                    } else {
                        downLoadedPicture();
                    }
                }
                mPopupWindow.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#36000000")));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlShadow.setVisibility(View.GONE);
            }
        });
        popupWindow.update();
        return popupWindow;
    }

    /**
     * 检查是否已经下载过此图片
     * @return
     */
    private boolean checkDownOver() {
        File storageDir = new File(Environment.getExternalStorageDirectory(), "金刚影像信息");
        File file = new File(storageDir, fileItemInfos.get(currentPostion).getFileName()+".jpg");
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 下载当前图片到图册
     */
    private void downLoadedPicture() {
        //保存图片必须在子线程中操作，是耗时操作
        //获取内部存储状态
        String state = Environment.getExternalStorageState();
        //如果状态不是mounted，无法读写
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.obtainMessage(SAVE_BEGIN).sendToTarget();
                    Bitmap bp = returnBitMap(fileItemInfos.get(currentPostion).getBytes());
                    saveImageToPhotos(PreviewActivity.this, bp, fileItemInfos.get(currentPostion).getFileName());
                }
            }).start();
        }
    }

    /**
     * 保存图片到本地相册
     */
    private void saveImageToPhotos(Context context, Bitmap bmp, String fileName) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "金刚影像信息");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        fileName = fileName + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();
    }

    /**
     * 将base64图片转化成bitmap形式
     *
     * @param decode
     */
    private Bitmap returnBitMap(byte[] decode) {
        // 设置inJustDecodeBounds = true ,表示先不加载图片
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(decode, 0, decode.length, options);

        // 调用方法计算合适的 inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 800, 600);

        // inJustDecodeBounds 置为 false 真正开始加载图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(decode, 0, decode.length, options);
    }

    /**
     * 计算 BitmapFactpry 的 inSimpleSize的值的方法
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        // 获取图片原生的宽和高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // 如果原生的宽高大于请求的宽高,那么将原生的宽和高都置为原来的一半
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 主要计算逻辑
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
