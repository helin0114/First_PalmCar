package com.cango.adpickcar.api;

import com.cango.adpickcar.model.BaseData;
import com.cango.adpickcar.model.CarTakeTaskList;
import com.cango.adpickcar.model.DeliveryTaskList;
import com.cango.adpickcar.model.GetQRCodeData;
import com.cango.adpickcar.model.ServerTime;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cango on 2017/10/9.
 */

public interface MainService {
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

    @GET("api/cartake/getcartaketasklist")
    Observable<CarTakeTaskList> getDatasByStatus(@Query("UserID") String UserID, @Query("CustName") String CustName,
                                                 @Query("LicensePlateNO") String LicensePlateNO,
                                                 @Query("CarBrandName") String CarBrandName,
                                                 @Query("QueryType") String QueryType,
                                                 @Query("PageIndex") String PageIndex,
                                                 @Query("PageSize") String PageSize);

    //确认接车
    @POST("api/cartake/cartakestoreconfirm")
    Observable<BaseData> carTakeStoreConfirm(@Body Map<String, Object> requestContent);

    //确认接车（二维码）
    @POST("api/cartake/cartakestoreconfirmbyqrcode")
    Observable<GetQRCodeData> cartakestoreconfirmbyqrcode(@Body Map<String, Object> requestContent);

    //获取接车任务列表以及各列表数据量
//    UserID											用户ID									string
//    CTPName											取车人名称									string
//    CTPMobile											取撤人手机									string
//    LicensePlateNO											车牌号									string
//    QueryType											查询类型（1：待交车 2.已交车 3：交车失败 ）									string
//    PageIndex											页码									string
//    PageSize											页大小									string
    @GET("api/cardelivery/getcardeliverytasklist")
    Observable<DeliveryTaskList> GetCarDeliveryTaskList(@Query("UserID") String UserID, @Query("CTPName") String CTPName,
                                                        @Query("CTPMobile") String CTPMobile, @Query("LicensePlateNO") String LicensePlateNO,
                                                        @Query("QueryType") String QueryType, @Query("PageIndex") String PageIndex,
                                                        @Query("PageSize") String PageSize);
}
