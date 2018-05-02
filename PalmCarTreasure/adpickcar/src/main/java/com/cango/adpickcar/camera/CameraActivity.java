package com.cango.adpickcar.camera;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.R;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.detail.DetailActivity;
import com.cango.adpickcar.login.LoginActivity;
import com.cango.adpickcar.model.CarFilesInfo;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.model.DeliveryTaskList;
import com.cango.adpickcar.model.JCarFiles;
import com.cango.adpickcar.model.PhotoResult;
import com.cango.adpickcar.util.SnackbarUtils;
import com.cango.adpickcar.util.ToastUtils;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

// AspectRatioFragment.Listener
public class CameraActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback ,AspectRatioFragment.Listener, CameraContract.View{

    private static final String TYPE = "type";

    /**
     * 跳过来的界面标记申明
     */
    public static final int FLAG_FACADE = 0;
    public static final int FLAG_PARTICULAR = 1;
    public static final int FLAG_SUPPLEMENT = 2;
    public static final int FLAG_JCARIMAGE = 3;
    public static final int FLAG_ITEMINFO = 4;

    private CarFilesInfo.DataBean.SurfaceFileListBean bean;
    private JCarFiles.DataBean.DeliveryFileListBean jbean;
    private CarTakeTaskList.DataBean.CarTakeTaskListBean mCarTakeTaskListBean;
    private DeliveryTaskList.DataBean.CarDeliveryTaskListBean mCarDeliveryTaskListBean;
    private int fromFragmentFlag;//跳过来的界面标记
    private AVLoadingIndicatorView mLoadView;

    /**
     * 0 : from itemInfoFG -1: 通用的
     */
    private int currentType = -1;
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String FRAGMENT_DIALOG = "dialog";

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private CarFilesInfo.DataBean.SurfaceFileListBean surfaceFileListBean;
    private int mCurrentFlash;
    private CameraView mCameraView;
    private ImageView ivCancal, ivFlash, ivNo, ivOk, ivResult, ivShadow, ivPromptLeft;
    private TextView tvPromptBottom;
    private ImageView fab;
    private RelativeLayout rlLeft, rlCenter, rlRight, rlPrompt;
    private ImageView ivPrompt;
    private boolean isFlash, isAnimOpen;

    private String mPath;
    private Handler mBackgroundHandler;

    //UI handler
    private Handler mHandler = new Handler();
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        if (mCameraView.isCameraOpened()) {
                            mCameraView.takePicture();
                        } else {
                            ToastUtils.showShort(R.string.camera_is_opening);
                        }
                    }
                    break;
                case R.id.iv_camera_prompt:
                    if (isAnimOpen) {
                        stopPrompt();
                    } else {
                        startPromptAnim();
                    }
                    break;
            }
        }
    };

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if (getIntent() != null) {
            currentType = getIntent().getIntExtra(TYPE, -1);
            surfaceFileListBean = getIntent().getParcelableExtra("SurfaceFileListBean");
            bean = getIntent().getParcelableExtra("bean");
            jbean = getIntent().getParcelableExtra("jbean");
            mCarTakeTaskListBean = getIntent().getParcelableExtra("mCarTakeTaskListBean");
            mCarDeliveryTaskListBean = getIntent().getParcelableExtra("mCarDeliveryTaskListBean");
            fromFragmentFlag = getIntent().getIntExtra("fromFragmentFlag",-1);
        }
        mLoadView = (AVLoadingIndicatorView) findViewById(R.id.avl_login_indicator);
        ivShadow = (ImageView) findViewById(R.id.iv_shadow);
        ivPromptLeft = (ImageView) findViewById(R.id.iv_camera_prompt_left);
        tvPromptBottom = (TextView) findViewById(R.id.tv_camera_prompt_bottom);
        ivCancal = (ImageView) findViewById(R.id.iv_camera_cancal);
        ivFlash = (ImageView) findViewById(R.id.iv_camera_flash);
        ivResult = (ImageView) findViewById(R.id.iv_result);
        ivNo = (ImageView) findViewById(R.id.iv_camera_no);
        ivOk = (ImageView) findViewById(R.id.iv_camera_ok);
        rlLeft = (RelativeLayout) findViewById(R.id.rl_camera_left);
        rlCenter = (RelativeLayout) findViewById(R.id.rl_camera_center);
        rlRight = (RelativeLayout) findViewById(R.id.rl_camera_right);
        rlPrompt = (RelativeLayout) findViewById(R.id.rl_camera_prompt);
        ivPrompt = (ImageView) findViewById(R.id.iv_camera_prompt);
        ivResult.setVisibility(View.GONE);
        ivNo.setVisibility(View.GONE);
        ivOk.setVisibility(View.GONE);
        mCameraView = (CameraView) findViewById(R.id.camera);

        showIndicator(false);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        mCameraView.setAspectRatio(AspectRatio.of(16,9));

        fab = (ImageView) findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }
        ivPrompt.setOnClickListener(mOnClickListener);

        if (currentType == 0) {
            isAnimOpen = false;
            rlCenter.setVisibility(View.GONE);
            ivShadow.setVisibility(View.GONE);
        } else {
            isAnimOpen = true;
        }

        RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.root);
        rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnimOpen) {
                    stopPrompt();
                }
            }
        });

        if (surfaceFileListBean != null) {
            Glide.with(this)
                    .load(surfaceFileListBean.getMongoliaPath())
                    .into(ivShadow);
            Glide.with(this)
                    .load(surfaceFileListBean.getMongoliaIconPath())
                    .into(ivPromptLeft);
            tvPromptBottom.setText(surfaceFileListBean.getMongoliaDesc());
            ivShadow.setVisibility(View.VISIBLE);
            ivPromptLeft.setVisibility(View.VISIBLE);
            ivPrompt.setVisibility(View.VISIBLE);
        }else{
            ivShadow.setVisibility(View.GONE);
            ivPromptLeft.setVisibility(View.GONE);
            ivPrompt.setVisibility(View.GONE);
        }
        new CameraPresenter(this);
    }

    private void stopPrompt() {
        rlPrompt.setVisibility(View.INVISIBLE);
        rlPrompt.setPivotX(rlPrompt.getWidth());
        rlPrompt.setPivotY(rlPrompt.getHeight());
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(rlPrompt, "scaleX", 1.0F, 0.0F)
                .setDuration(100);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                rlPrompt.setAlpha(cVal);
                rlPrompt.setScaleX(cVal);
                rlPrompt.setScaleY(cVal);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimOpen = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    private void startPromptAnim() {
        rlPrompt.setVisibility(View.VISIBLE);
        rlPrompt.setPivotX(rlPrompt.getWidth());
        rlPrompt.setPivotY(rlPrompt.getHeight());
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(rlPrompt, "scaleX", 0.0F, 1.0F)
                .setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                rlPrompt.setAlpha(cVal);
                rlPrompt.setScaleX(cVal);
                rlPrompt.setScaleY(cVal);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimOpen = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    public void cancal(View view) {
        finish();
    }

    public void flash(View view) {
        if (mCameraView != null) {
            if (isFlash) {
                mCameraView.setFlash(CameraView.FLASH_OFF);
                isFlash = false;
                ivFlash.setImageResource(R.drawable.flashoff);
            } else {
                mCameraView.setFlash(CameraView.FLASH_ON);
                isFlash = true;
                ivFlash.setImageResource(R.drawable.flashon);
            }
        }
    }

    public void cameraNo(View view) {
        deleteImageFile();
        rlLeft.setVisibility(View.VISIBLE);
        rlCenter.setVisibility(View.VISIBLE);
        rlRight.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        ivResult.setVisibility(View.GONE);
        ivNo.setVisibility(View.GONE);
        ivOk.setVisibility(View.GONE);

        if (currentType == 0) {
            isAnimOpen = false;
            rlCenter.setVisibility(View.GONE);
            ivShadow.setVisibility(View.GONE);
        } else {
            isAnimOpen = false;
        }
    }

    public void cameraOk(View view) {
//        Intent intent = new Intent(CameraActivity.this, DetailActivity.class);
//        intent.putExtra("path", mPath);
//        setResult(Activity.RESULT_OK, intent);
//        finish();

        //上传图片
        switch (fromFragmentFlag){
            case FLAG_FACADE:
                if(currentType == 0){
                    zipPicture(mPath, ADApplication.mSPUtils.getString(Api.USERID),
                            mCarTakeTaskListBean.getDisCarID() + "", "50", "10",
                            null, null);
                }else {
                    zipPicture(mPath, ADApplication.mSPUtils.getString(Api.USERID),
                            mCarTakeTaskListBean.getDisCarID() + "", "50", bean.getSubCategory(),
                            bean.getSubID(), bean.getPicFileID() + "");
                }
                break;
            case FLAG_PARTICULAR:
                zipPicture(mPath, ADApplication.mSPUtils.getString(Api.USERID),
                        mCarTakeTaskListBean.getDisCarID() + "", "60", bean.getSubCategory(),
                        bean.getSubID(), bean.getPicFileID() + "");
                break;
            case FLAG_SUPPLEMENT:
                if(currentType == 0){
                    zipPicture(mPath, ADApplication.mSPUtils.getString(Api.USERID),
                            mCarTakeTaskListBean.getDisCarID() + "", "70", "30",
                            null, null);
                }else {
                    zipPicture(mPath, ADApplication.mSPUtils.getString(Api.USERID),
                            mCarTakeTaskListBean.getDisCarID() + "", "70", bean.getSubCategory(),
                            bean.getSubID(), bean.getPicFileID() + "");
                }
                break;
            case FLAG_JCARIMAGE:
                if(currentType == 0){
                    zipPicture(mPath, ADApplication.mSPUtils.getString(Api.USERID),
                            mCarDeliveryTaskListBean.getDisCarID() + "","30", "30",
                            null, null);
                }else {
                    zipPicture(mPath, ADApplication.mSPUtils.getString(Api.USERID),
                            mCarDeliveryTaskListBean.getDisCarID() + "", "30", jbean.getSubCategory(),
                            jbean.getSubID(), jbean.getPicFileID() + "");
                }
                break;
            case FLAG_ITEMINFO:
                zipPicture(mPath, ADApplication.mSPUtils.getString(Api.USERID),
                        mCarTakeTaskListBean.getDisCarID() + "", "25", null, null, null);
                break;
            default:
                break;
        }
    }

    boolean isFirst = true;

    @Override
    protected void onResume() {
        if (isFirst) {
            isFirst = false;
            stopPrompt();
        }
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 3 || grantResults.length != 3) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    boolean isOk = false;
//                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                            "picture.jpg");
                    File file = null;
                    try {
                        file = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    OutputStream os = null;
                    try {
                        isOk = true;
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                updateCameraDo();
//                            }
//                        });
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                        if (isOk) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateCameraDo();
                                }
                            });
                        } else {
                            ToastUtils.showShort("图片解码异常");
//                            Intent intent = new Intent(CameraActivity.this, TrailerActivity.class);
//                            setResult(Activity.RESULT_CANCELED, intent);
//                            finish();
                        }
                    }
                }
            });
        }

    };

    private void updateCameraDo() {
        Logger.d("updateCameraDo" + mPath);
        fab.setVisibility(View.GONE);
        rlLeft.setVisibility(View.GONE);
        rlCenter.setVisibility(View.GONE);
        rlRight.setVisibility(View.GONE);
        ivResult.setVisibility(View.VISIBLE);
        ivNo.setVisibility(View.VISIBLE);
        ivOk.setVisibility(View.VISIBLE);
        Glide.with(this).load(mPath).into(ivResult);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mPath = image.getAbsolutePath();
        return image;
    }

    private boolean deleteImageFile() {
        if (mPath != null) {
            File emptyFile = new File(mPath);
            if (emptyFile.exists())
                return emptyFile.delete();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aspect_ratio:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (mCameraView != null
                        && fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    AspectRatioFragment.newInstance(ratios, currentRatio)
                            .show(fragmentManager, FRAGMENT_DIALOG);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            mCameraView.setAspectRatio(ratio);
            //让ivshadow适应cameraview分辨率的预览大小
            ViewGroup.LayoutParams layoutParams = mCameraView.getLayoutParams();
            ivShadow.setLayoutParams(layoutParams);
            ivShadow.requestLayout();
        }
    }

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }

    }

    @Override
    public void setPresenter(CameraContract.Presenter presenter) {
        mPresenter = presenter;
    }
    private boolean isDoSaveDisCarFile = true;
    private CameraContract.Presenter mPresenter;

    public void zipPicture(String mImgPath, final String UserID, final String DisCarID, final String PicGroup, final String SubCategory,
                           final String SubID, final String PicFileID) {
        if (!isDoSaveDisCarFile) {
            return;
        }
        isDoSaveDisCarFile = false;
        final File tempFile = new File(mImgPath);
        Luban.with(CameraActivity.this)
                .load(tempFile)                     //传人要压缩的图片
                .ignoreBy(100)
//                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        showIndicator(true);
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
                        showIndicator(false);
                        isDoSaveDisCarFile = true;
                    }
                }).launch();    //启动压缩
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
    public void openOtherUi() {
//        ToastUtils.showShort("认证失败，请重新登录");
        SnackbarUtils.showLongDisSnackBar(findViewById(R.id.root), "认证失败，请重新登录");
        ADApplication.mSPUtils.clear();
        startActivity(new Intent(CameraActivity.this, LoginActivity.class));
    }

    @Override
    public void savePhotoResult(boolean isSuccess,PhotoResult mPhotoResult) {
        if(isSuccess){
            Intent intent = new Intent(CameraActivity.this, DetailActivity.class);
            intent.putExtra("path", mPath);
            intent.putExtra("mPhotoResult",mPhotoResult);
            intent.putExtra("isSuccess",isSuccess);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else{

        }
    }

}
