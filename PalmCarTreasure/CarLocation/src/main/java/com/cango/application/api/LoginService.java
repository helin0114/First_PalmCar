package com.cango.application.api;

import com.cango.application.model.LoginData;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cango on 2017/6/8.
 */

public interface LoginService {
    String BASE_URL = Api.BASE_URL;

    @POST("user/login")
    Observable<LoginData> login(@Body Map<String, Object> objectMap);
}
