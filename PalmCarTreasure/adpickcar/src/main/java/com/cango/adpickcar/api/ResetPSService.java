package com.cango.adpickcar.api;

import com.cango.adpickcar.model.BaseData;
import com.cango.adpickcar.model.ServerTime;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cango on 2017/10/12.
 */

public interface ResetPSService {

    String BASE_URL = Api.BASE_URL;

    @GET("api/user/getservertime")
    Observable<ServerTime> getServerTime();

    /**
     * 修改密码
     * UserID											用户姓名									string
     Password											用户密码									string
     NewPassword											用户新密码									string
     ConfirmPassword											用户确认密码									string
     * @param requestContent
     * @return
     */
    @POST("api/user/changepassword")
    Observable<BaseData> changePassword(@Body Map<String, Object> requestContent);
}
