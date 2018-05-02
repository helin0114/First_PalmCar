package com.cango.adpickcar.net;

import android.text.TextUtils;

import com.cango.adpickcar.ADApplication;
import com.cango.adpickcar.api.Api;
import com.cango.adpickcar.update.ProgressListener;
import com.cango.adpickcar.update.ProgressResponseBody;
import com.cango.adpickcar.util.AppUtils;
import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.EncryptUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by cango on 2017/3/28.
 * 网络加载类
 */

public class NetManager {
    private static final int DEFAULT_TIMEOUT = 120;
    private static NetManager mNetManager;

    private NetManager() {

    }

    public static NetManager getInstance() {
        if (CommUtil.checkIsNull(mNetManager))
            mNetManager = new NetManager();
        return mNetManager;
    }

    public <T> T create(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getBaseUrl(service))
                .build();
        return retrofit.create(service);
    }

    private <T> String getBaseUrl(Class<T> service) {
        String baseUrl = null;
        try {
            Field base_url = service.getField("BASE_URL");
            baseUrl = (String) base_url.get(service);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseUrl;
    }

    public OkHttpClient getOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //配置log打印拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(loggingInterceptor);
        //配置request header 添加的token拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String appVersion = AppUtils.getVersionName(ADApplication.getmContext());
                Request.Builder newBuilder = chain.request().newBuilder();
                if (CommUtil.checkIsNull(appVersion)) {

                } else {
                    newBuilder.addHeader("APPVERSION", appVersion + "@" + Api.DEVICE_TYPE);
                }
                String token = ADApplication.mSPUtils.getString(Api.TOKEN);
                String serverTime = ADApplication.mSPUtils.getString(Api.SERVERTIME);
                if (CommUtil.checkIsNull(token) || CommUtil.checkIsNull(serverTime)) {

                } else {
                    String encryptString = null;
                    try {
                        encryptString = EncryptUtils.encrypt(Api.KEY, token + serverTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(encryptString)) {
                        newBuilder.addHeader("AUTHORIZATION", encryptString);
                    }
                }
                return chain.proceed(newBuilder.build());
            }
        });
        return builder.build();
    }

    public <T> T createUpdate(Class<T> service,ProgressListener progressListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(updateOkhttpClient(progressListener))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getBaseUrl(service))
                .build();
        return retrofit.create(service);
    }
    public OkHttpClient updateOkhttpClient(final ProgressListener progressListener){
        OkHttpClient.Builder builder=new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse  = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(),progressListener))
                        .build();
            }
        });
        return builder.build();
    }
}
