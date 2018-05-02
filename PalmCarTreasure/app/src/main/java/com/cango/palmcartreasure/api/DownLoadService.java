package com.cango.palmcartreasure.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by cango on 2017/7/10.
 */

public interface DownLoadService {
    //http://gps.cangoonline.com:83/content/kingkong.apk
    String BASE_URL = Api.ABOUT_URL;
//    String BASE_URL = "http://gps.cangoonline.com:83/";
    @GET("content/kingkong.apk")
    Call<ResponseBody> downLoadAPK();

}
