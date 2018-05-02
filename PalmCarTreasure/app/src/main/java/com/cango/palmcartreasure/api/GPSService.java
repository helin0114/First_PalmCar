package com.cango.palmcartreasure.api;

import com.cango.palmcartreasure.model.CarTrackQuery;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cango on 2017/6/8.
 */

public interface GPSService {
    String BASE_URL = Api.BASE_URL;

    //trailer/cartrackquery?userid={userid}&deviceId={deviceId}&startTime={startTime}&endTime={endTime}&ApiToken={APITOKEN} 车辆行驶轨迹查询
    @GET("trailer/cartrackquery")
    Observable<CarTrackQuery> carTrackQuery(@Query("userid") int userId, @Query("deviceId") String deviceId,
                                            @Query("startTime") String startTime, @Query("endTime") String endTime);
}
