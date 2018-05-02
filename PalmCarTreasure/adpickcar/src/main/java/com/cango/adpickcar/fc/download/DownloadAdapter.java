package com.cango.adpickcar.fc.download;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;

import com.cango.adpickcar.R;
import com.cango.adpickcar.baseAdapter.BaseAdapter;
import com.cango.adpickcar.baseAdapter.BaseHolder;
import com.cango.adpickcar.util.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by cango on 2017/5/8.
 * 我的文件界面的adapter
 */

public class DownloadAdapter extends BaseAdapter<File> {
    public DownloadAdapter(Context context, List<File> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.download_item_other;
    }

    @Override
    protected void convert(BaseHolder holder, final File data) {
        TextView tvName = holder.getView(R.id.tv_download_first_a);
        TextView tvSize = holder.getView(R.id.tv_download_file_size_a);
        TextView tvLook = holder.getView(R.id.tv_look);
        TextView tvDelete = holder.getView(R.id.tv_delete);
        tvName.setText(data.getName());
        tvSize.setText(FileUtils.GetFileSize(data));
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data!=null){
                    mDatas.remove(data);
                    data.delete();
                    notifyDataSetChanged();
                }
            }
        });
        tvLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享pdf文件
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri pdfUri;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pdfUri = FileProvider.getUriForFile(mContext, "com.cango.adpickcar.fileprovider", data);
                    List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        mContext.grantUriPermission(packageName, pdfUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                } else {
                    pdfUri = Uri.fromFile(data);
                }
                intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                intent.setType("application/pdf");
                try {
                    mContext.startActivity(Intent.createChooser(intent, data.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//                if (isKitKat&&DocumentsContract.isDocumentUri(mContext, pdfUri)){
//
//                    // ExternalStorageProvider
//                    if (isExternalStorageDocument(pdfUri)) {
//                        final String docId = DocumentsContract.getDocumentId(pdfUri);
//                        final String[] split = docId.split(":");
//                        final String type = split[0];
//
//                        if ("primary".equalsIgnoreCase(type)) {
//                            return Environment.getExternalStorageDirectory() + "/" + split[1];
//                        }
//                    }
//                }


            }
        });
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

//    public Intent getPdfFileIntent(File pdfFile) {

//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri pdfUri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            pdfUri = FileProvider.getUriForFile(mContext, "com.cango.palmcartreasure.fileprovider", pdfFile);
//            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//            for (ResolveInfo resolveInfo : resInfoList) {
//                String packageName = resolveInfo.activityInfo.packageName;
//                mContext.grantUriPermission(packageName, pdfUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            }
//        } else {
//            pdfUri = Uri.fromFile(pdfFile);
//        }
//        intent.setDataAndType(pdfUri, "application/pdf");
//        return intent;
//    }
}
