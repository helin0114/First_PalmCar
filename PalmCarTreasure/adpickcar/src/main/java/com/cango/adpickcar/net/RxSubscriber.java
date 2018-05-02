package com.cango.adpickcar.net;

import android.content.Context;
import android.widget.Toast;

import com.cango.adpickcar.ADApplication;

import java.io.IOException;
import java.net.HttpRetryException;

import rx.Subscriber;

/**
 * Created by cango on 2017/4/11.
 * 观察者，处理onerror等情况
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {
    private Context mContext;

    public RxSubscriber() {
        mContext = ADApplication.getmContext();
    }

    protected abstract void _onNext(T o);

    protected abstract void _onError();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        //统一处理请求异常的情况
        if (e instanceof IOException) {
            Toast.makeText(mContext, "网络连接异常", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        e.printStackTrace();
        _onError();
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }
}
