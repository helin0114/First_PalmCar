package com.cango.adpickcar.api;

import com.cango.adpickcar.model.FcVisitTaskList;
import com.cango.adpickcar.model.ServerTime;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dell on 2017/12/13.
 */

public interface FcTasksService {
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

    /**
     * 获取今日分配任务列表
     * @param UserID
     * @return
     */
    @GET("api/visittask/getvisittasklisttoday")
    Observable<FcVisitTaskList> getVisitTaskListToday(@Query("UserID") String UserID);

    /**
     * 获取本周任务列表
     * @param UserID
     * @return
     */
    @GET("api/visttask/getvisittasklistweek")
    Observable<FcVisitTaskList> getVisitTaskListWeek(@Query("UserID") String UserID);

    /**
     * 获取历史任务列表
     * @param UserID
     * @return
     */
    @GET("api/visittask/getvisittasklisthis")
    Observable<FcVisitTaskList> getVisitTaskListHis(@Query("UserID") String UserID);
}
