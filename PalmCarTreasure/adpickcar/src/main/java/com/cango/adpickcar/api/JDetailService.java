package com.cango.adpickcar.api;

import com.cango.adpickcar.model.BaseData;
import com.cango.adpickcar.model.JCarBaseInfo;
import com.cango.adpickcar.model.JCarFiles;
import com.cango.adpickcar.model.JCarInfo;
import com.cango.adpickcar.model.PhotoResult;
import com.cango.adpickcar.model.ServerTime;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cango on 2017/11/27.
 */

public interface JDetailService {
    /**
     * POST 方法数据统一加密传输。
     * 加密要求：
     * 1.登录方法 用每日变动密钥加密
     * 2.其它POST方法使用用户TOKEN加密
     * 3.加密请求JSON数据格式：
     * {
     * "RequestContent":"加密后的请求内容"
     * }
     */
    String BASE_URL = Api.BASE_URL;

    @GET("api/user/getservertime")
    Observable<ServerTime> getServerTime();

    //获取交车基本信息
//    CDLVTaskID											交车任务ID									string
    @GET("api/cardelivery/getcardeliverytaskinfo")
    Observable<JCarBaseInfo> GetCarDeliveryTaskInfo(@Query("CDLVTaskID") String CDLVTaskID);

    //获取车辆信息（可复用）
//    DisCarID											车辆ID									string
    @GET("api/cardelivery/getdiscarinfo")
    Observable<JCarInfo> GetCarInfo(@Query("DisCarID") String DisCarID);

    //获取交车影像信息
//    DisCarID											处置车辆ID									string
    @GET("api/cardelivery/getcardeliverytaskfiles")
    Observable<JCarFiles> GetCarDeliveryTaskFiles(@Query("DisCarID") String DisCarID);

    //交车任务提交
//    CDLVTaskID											交车任务ID									string				○
//    CDLVTaskFlag											状态									string				○			30/交车完成、40/交车失败
//    LAT											交车经度									string				○			CDLVTASKFLAG=30时必填
//    LNG											交车纬度									string				○			CDLVTASKFLAG=30时必填
//    DLVMemo											交车情况备注									string				○			CDLVTASKFLAG=40时必填
    @POST("api/cardelivery/savecardeliverytask")
    Observable<BaseData> SaveCarDeliveryTask(@Body Map<String,Object> requestContent);

    //交车退回
//    CDLVTaskID											交车任务ID									string
    @POST("api/cardelivery/restorecardeliverytask")
    Observable<BaseData> RestoreCarDeliveryTask(@Body Map<String,Object> requestContent);

    //上传文件
    @POST("api/discarinfo/savediscarfile")
    @Multipart
    Observable<PhotoResult> saveDisCarFile(@Part("RequestContent") RequestBody requestContent, @Part MultipartBody.Part photo);

    //删除文件
    @POST("api/discarinfo/deletediscarfile")
    Observable<BaseData> deleteDisCarFile(@Body Map<String, Object> requestContent);
}
