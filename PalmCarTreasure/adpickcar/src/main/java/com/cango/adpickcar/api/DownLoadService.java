package com.cango.adpickcar.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by cango on 2017/7/10.
 */

public interface DownLoadService {
    String BASE_URL = Api.BASE_URL;

    @GET("/content/kingkong_ad.apk")
    Call<ResponseBody> downLoadAPK();

}
