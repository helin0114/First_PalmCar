package com.cango.application.api;

import com.cango.application.model.CarTrackQuery;
import com.cango.application.model.ImeiQuery;
import com.cango.application.model.LocationQuery;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cango on 2017/6/8.
 */

public interface GPSService {
    String BASE_URL = Api.BASE_URL;

    @GET("gps/imeiquery")
    Observable<ImeiQuery> ImeiQuery(@Query("userid") int userId, @Query("IMEI") String IMEI);

    //开始时间 增加开始时间得到一定时间间隔内的轨迹
    @GET("gps/imeiquery")
    Observable<LocationQuery> locationQuery(@Query("userid") int userId, @Query("IMEI") String IMEI, @Query("startTime") String startTime);

    //GET /gps/cartrackquery?userid={userid}&IMEI={IMEI}&startTime={startTime}&endTime={endTime}&dependMinute={dependMinute}&ApiToken={APITOKEN} 轨迹查询
    @GET("gps/cartrackquery")
    Observable<CarTrackQuery> carTrackQuery(@Query("userid") int userId, @Query("IMEI") String IMEI,
                                            @Query("startTime") String startTime, @Query("endTime") String endTime,
                                            @Query("dependMinute") int dependMinute);
}
