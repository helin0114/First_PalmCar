package com.cango.palmcartreasure.update;

import com.cango.palmcartreasure.api.DownLoadService;
import com.cango.palmcartreasure.base.BasePresenter;
import com.cango.palmcartreasure.net.NetManager;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cango on 2017/7/10.
 */

public class UpdatePresenter implements BasePresenter {
    DownLoadService mService;

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {

    }

    public void downLoadAPK(final String apkPath, ProgressListener progressListener) {
        mService = NetManager.getInstance().createUpdate(DownLoadService.class, progressListener);
        mService.downLoadAPK()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len;
                        FileOutputStream fos = null;
                        try {
                            if (response.isSuccessful()) {
                                File file = new File(apkPath);
                                is = response.body().byteStream();
                                fos = new FileOutputStream(file);
                                while ((len = is.read(buf)) != -1) {
                                    fos.write(buf, 0, len);
                                }
                                fos.flush();
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (is != null)
                                    is.close();
                            } catch (IOException e) {
                            }
                            try {
                                if (fos != null)
                                    fos.close();
                            } catch (IOException e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Logger.d(t.getMessage());
                    }
                });
    }
}
