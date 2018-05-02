package com.cango.palmcartreasure.api;

import com.cango.palmcartreasure.model.TackPictureInfo;
import com.cango.palmcartreasure.model.TaskPicInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by dell on 2018/3/15.
 */

public interface TaskPicturesService {
    String BASE_URL = Api.BASE_URL;

    @GET("trailer/erpimageinfo")
    Observable<TackPictureInfo> getErpimageinfo(@Query("userid") int userid, @Query("datasource") int datasource, @Query("applyCd") String applyCd);

    @GET
    Observable<TaskPicInfo> getPicInfo(@Url String url);
}
