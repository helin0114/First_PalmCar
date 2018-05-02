package com.cango.adpickcar.api;

import com.cango.adpickcar.model.BaseData;
import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.model.ServerTime;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dell on 2017/12/11.
 */

public interface FcMainService {
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

    @POST("api/user/loginout")
    Observable<BaseData> logout(@Body Map<String, Object> requestContent);

    /**
     * 获取家访任务列表
     * @param UserID
     * @param PageIndex
     * @param PageSize
     * @param LAT   经度
     * @param LON   纬度
     * @return
     */
    @GET("api/visittask/getvisittasklist")
    Observable<FcVisitTaskList> getVisitTaskList(@Query("UserID") String UserID, @Query("PageIndex") String PageIndex,
                                                 @Query("PageSize") String PageSize, @Query("LAT") String LAT,
                                                 @Query("LON") String LON);
}
