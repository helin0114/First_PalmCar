package com.cango.adpickcar.fc.detail;

import android.net.Uri;

import com.cango.adpickcar.api.FcDetailService;
import com.cango.adpickcar.net.NetManager;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/01/17
 *     desc   :
 * </pre>
 */
public class WebDetailPresenter implements WebDetailContract.Presenter{
    private WebDetailContract.View mView;
    private FcDetailService mService;
    public WebDetailPresenter(WebDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(FcDetailService.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void onDetach() {

    }

    /**
     * 下载家纺催收函、下载家纺任务书
     * @param userId       用户ID
     * @param VisitID      家访ID
     * @param datasource   数据来源
     * @param ActionID     接口类型  1：催收函；2：任务书
     * @param CustomerType 客户类型  1：申请人；2：共同申请人；3：担保人（催收函必传）
     * @param parentDir    下载文件保存路径
     */
    @Override
    public void docDownload(String userId, String VisitID, String datasource, String ActionID, String CustomerType, final String parentDir) {
        mView.downFileNow();
        mService.docDownLoad(userId,VisitID,datasource,ActionID,CustomerType)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len;
                        FileOutputStream fos = null;
//                        // 储存下载文件的目录
//                        String savePath = isExistDir(saveDir);
                        try {
                            if (response.isSuccessful()) {
                                Headers headers = response.headers();
                                if(headers.get("Content-Disposition") == null){
                                    mView.downFileFailed();
                                    return;
                                }
                                String[] strings = headers.get("Content-Disposition").split(";");
                                String fileName = strings[1].replaceAll("filename=", "").replaceAll("\"","");
                                String decode = Uri.decode(fileName);
                                File file = new File(parentDir, decode.trim());
                                if(!file.exists()){                //判断文件是否存在
                                    File filePath = new File(parentDir);
                                    filePath.mkdir();              //创建文件目录
                                    file.createNewFile();          //创建新文件
                                }
                                is = response.body().byteStream();
                                long total = response.body().contentLength();
                                fos = new FileOutputStream(file);
                                long sum = 0;
                                while ((len = is.read(buf)) != -1) {
                                    fos.write(buf, 0, len);
                                    sum += len;
//                                    int progress = (int) (sum * 1.0f / total * 100);
//                                    System.out.println("progress:"+progress);
                                    // 下载中
//                                    listener.onDownloading(progress);
                                }
                                fos.flush();
                                // 下载完成
                                mView.downFileSuccess();
//                                listener.onDownloadSuccess(file);
//                                if (mView.isActive()) {
//                                    mView.showTaskDetailIndicator(false);
//                                }
                            } else {
                                mView.downFileFailed();
//                                listener.onDownloadFailed("下载失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.downFileFailed();
//                            listener.onDownloadFailed(e.getMessage());
//                            if (mView.isActive()) {
//                                mView.showTaskDetailIndicator(false);
//                            }
                        } finally {
//                            if (mView.isActive()) {
//                                mView.showTaskDetailIndicator(false);
//                            }
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
                        mView.downFileFailed();
//                        if (mView.isActive()) {
//                            mView.showTaskDetailIndicator(false);
//                        }
                    }
                });
    }
}
