package com.cango.adpickcar.fc.download;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;

/**
 * 下载界面
 */
public class DownloadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        DownloadFragment downloadFragment = (DownloadFragment) getSupportFragmentManager().findFragmentById(R.id.fl_download_contains);
        if (CommUtil.checkIsNull(downloadFragment)) {
            downloadFragment = DownloadFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_download_contains, downloadFragment);
            transaction.commit();
        }
        new DownloadPresenter(downloadFragment);
    }

}
