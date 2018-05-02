package com.cango.adpickcar.api;

import com.cango.adpickcar.model.LoginData;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cango on 2017/10/12.
 */

public interface LoginService {
    String BASE_URL = Api.BASE_URL;

    @POST("api/user/login")
    Observable<LoginData> login(@Body Map<String, Object> requestContent);
}
