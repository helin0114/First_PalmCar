package com.cango.adpickcar.api;

import com.cango.adpickcar.model.PhotoResult;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/01/17
 *     desc   :
 * </pre>
 */
public interface FcDetailService {
    String BASE_URL = Api.BASE_URL;

    //下载家访催收函\下载家访任务书

    @GET("api/visittask/docdownload")
    Call<ResponseBody> docDownLoad(@Query("UserID") String UserID, @Query("VisitID") String VisitID, @Query("datasource") String datasource,
                                   @Query("ActionID") String ActionID, @Query("CustomerType") String CustomerType);

    //上传图片接口
    @POST("api")
    @Multipart
    Observable<PhotoResult> uploadPhoto(@Part("RequestContent") RequestBody requestContent,
                                           @Part MultipartBody.Part photo);
}
