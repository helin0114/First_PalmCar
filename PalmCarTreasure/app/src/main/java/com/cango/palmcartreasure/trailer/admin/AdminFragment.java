package com.cango.palmcartreasure.trailer.admin;


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
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.api.LoginService;
import com.cango.palmcartreasure.base.BaseFragment;
import com.cango.palmcartreasure.login.LoginActivity;
import com.cango.palmcartreasure.model.PersonalInfo;
import com.cango.palmcartreasure.net.NetManager;
import com.cango.palmcartreasure.net.RxSubscriber;
import com.cango.palmcartreasure.trailer.download.DownloadActivity;
import com.cango.palmcartreasure.update.ProgressListener;
import com.cango.palmcartreasure.update.UpdatePresenter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *管理员首页view，展示
 */
public class AdminFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks{

    private AdminActivity mActivity;
    @BindView(R.id.toolbar_manager)
    Toolbar mToolbar;
    @BindView(R.id.tv_exit)
    ImageView tvExit;
    @BindView(R.id.tv_popupParent)
    TextView tvPopupParent;
    @BindView(R.id.rl_shadow)
    RelativeLayout rlShadow;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.tv_exit,R.id.iv_employee_group,R.id.iv_task_allocation,R.id.iv_task_query})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_employee_group:
                Intent staiffIntent=new Intent(getActivity(),StaiffActivity.class);
                staiffIntent.putExtra(StaiffFragment.TYPE,StaiffFragment.SHOW_GROUP);
                mActivity.mSwipeBackHelper.forward(staiffIntent);
                break;
            case R.id.iv_task_allocation:
                Intent taskIntent = new Intent(getActivity(),AdminTasksActivity.class);
                taskIntent.putExtra(AdminTasksFragment.TYPE,AdminTasksFragment.ADMIN_UNABSORBED);
                mActivity.mSwipeBackHelper.forward(taskIntent);
                break;
            case R.id.iv_task_query:
                Intent groupTaskIntent = new Intent(getActivity(),AdminTasksActivity.class);
                groupTaskIntent.putExtra(AdminTasksFragment.TYPE,AdminTasksFragment.GROUP);
                mActivity.mSwipeBackHelper.forward(groupTaskIntent);
                break;
            case R.id.tv_exit:
//                MtApplication.mSPUtils.clear();
//                MtApplication.mSPUtils.put(Api.ISSHOWSTARTOVER,true);
//                Intent loginIntent = new Intent(mActivity, LoginActivity.class);
//                loginIntent.putExtra("isFromLogout", true);
//                mActivity.mSwipeBackHelper.forward(loginIntent);
                rlShadow.setVisibility(View.VISIBLE);
                mMenuPW.update();
//                mMenuPW.showAtLocation(mToolbar,Gravity.RIGHT|Gravity.BOTTOM,SizeUtil.dp2px(mActivity,15),0);
                mMenuPW.showAsDropDown(tvPopupParent);
                break;
        }
    }

    private PopupWindow mMenuPW;

    public static AdminFragment newInstance() {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_admin;
    }

    @Override
    protected void initView() {
        mActivity= (AdminActivity) getActivity();
        mActivity.setSupportActionBar(mToolbar);
        mMenuPW = getPopupWindow(mActivity, R.layout.admin_menu_pw);
        NetManager.getInstance().create(LoginService.class).getPersonalInfo(MtApplication.mSPUtils.getInt(Api.USERID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<PersonalInfo>() {
                    @Override
                    protected void _onNext(PersonalInfo o) {
                        if (o!=null){
                            int code = o.getCode();
                            if (code == Api.APP_UPDATE){
                                openPermissions();
                            }
                        }
                    }

                    @Override
                    protected void _onError() {

                    }
                });
    }

    @Override
    protected void initData() {
    }

    public PopupWindow getPopupWindow(Context context, final int layoutId) {
        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View popupView = LayoutInflater.from(context).inflate(layoutId, null);
        TextView tvDown = (TextView) popupView.findViewById(R.id.tv_admin_down);
        TextView tvExit = (TextView) popupView.findViewById(R.id.tv_admin_exit);
        tvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.mSwipeBackHelper.forward(DownloadActivity.class);
            }
        });
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MtApplication.mSPUtils.clear();
                MtApplication.mSPUtils.put(Api.ISSHOWSTARTOVER,true);
                popupWindow.dismiss();
                Intent loginIntent = new Intent(mActivity, LoginActivity.class);
                loginIntent.putExtra("isFromLogout", true);
                mActivity.mSwipeBackHelper.forward(loginIntent);
            }
        });
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
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
     * 显示更新dialog
     */
    private void showUpdateDialog() {
//        UpdateFragment updateFragment = new UpdateFragment();
//        updateFragment.show(getChildFragmentManager(), updateFragment.getClass().getSimpleName());

        updateAPK();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private static final int REQUEST_STORAGE_GROUP = 9100;

    @AfterPermissionGranted(REQUEST_STORAGE_GROUP)
    private void openPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            showUpdateDialog();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_group_and_storage), REQUEST_STORAGE_GROUP, perms);
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
                    .setRationale(R.string.setting_group_and_storage)
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
        }
    }

    private NumberFormat nt;
    private Subscription subscriptionUP;
    private boolean isDownloaded;
    //跟新apk
    private void updateAPK() {
        nt = NumberFormat.getPercentInstance();
        String parentDir = MtApplication.getmContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        final String apkPath = parentDir + File.separator + "kingkong.apk";
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
//                    progressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("点击安装");
                } else {
                    Observable<Long> updateObservable = Observable.create(new Observable.OnSubscribe<Long>() {
                        @Override
                        public void call(Subscriber<? super Long> subscriber) {
                            subscriber.onNext(Long.valueOf(bytesRead));
                            subscriber.onCompleted();
                        }
                    });
                    subscriptionUP = updateObservable
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
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(getActivity(), "com.cango.palmcartreasure.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
