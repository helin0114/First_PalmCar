package com.cango.palmcartreasure;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.TackPictureInfo;
import com.cango.palmcartreasure.model.TrailerEvent;
import com.cango.palmcartreasure.trailer.main.TrailerActivity;
import com.cango.palmcartreasure.trailer.message.MessageActivity;
import com.cango.palmcartreasure.trailer.message.MessageFragment;
import com.cango.palmcartreasure.util.CommUtil;
import com.cango.palmcartreasure.util.SPUtils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created by cango on 2017/3/31.
 */

public class MtApplication extends Application {
    private static Context mContext;
    public static SPUtils mSPUtils;
    /**
     * 因为intent传递值有大小限制，byte数组太大了
     */
//    public static byte[] TASKBYTES;
    public static ArrayList<TackPictureInfo.FileItemInfo> TASKBYTESLIST;
    public static List<AppCompatActivity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
//        Logger.init().logLevel(LogLevel.NONE);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setMuteDurationSeconds(0);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        Logger.init("LLI").logLevel(LogLevel.NONE);
        mContext = getApplicationContext();
        mSPUtils = new SPUtils("cango_vps");
        BGASwipeBackManager.getInstance().init(this);
//        AutoLayoutConifg.getInstance().useDeviceSize();

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                //用于处理api推送的自定义行为的通知
                Logger.d(msg.custom);
                //推送消息关联消息列表
                AppCompatActivity lastActvity = getLastActvity();
                if (lastActvity != null) {
                    if (lastActvity instanceof MessageActivity) {
                        MessageFragment fragment = (MessageFragment) lastActvity.getSupportFragmentManager().findFragmentById(R.id.fl_message_contains);
                        if (!CommUtil.checkIsNull(fragment))
                            fragment.onRefresh();
                    } else {
                        Intent startTaobao = new Intent(context, MessageActivity.class);
                        startTaobao.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(startTaobao);
                    }
                } else {
                    Intent startTaobao = new Intent(context, MessageActivity.class);
                    startTaobao.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startTaobao);
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                //只要接收到通知都走这里统一处理
                Logger.d(uMessage.text);
                AppCompatActivity lastActvity = getLastActvity();
                if (lastActvity != null) {
                    if (lastActvity instanceof TrailerActivity) {
                        EventBus.getDefault().post(new TrailerEvent("ok"));
                    } else if (lastActvity instanceof MessageActivity) {
                        MessageFragment fragment = (MessageFragment) lastActvity.getSupportFragmentManager().findFragmentById(R.id.fl_message_contains);
                        if (!CommUtil.checkIsNull(fragment))
                            fragment.onRefresh();
                    } else if (lastActvity instanceof ShowQRActivity) {
                        if (uMessage.extra != null) {
                            String key1 = uMessage.extra.get("key1");
                            if ("app_sendinstoreqrcode".equals(key1)) {
                                clearExceptFirstActivitys();
                            }
                        }
                    }
                }
                return super.getNotification(context, uMessage);
            }

            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {
                Logger.d(uMessage.custom);
                super.dealWithCustomMessage(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
//        默认情况下，同一台设备在1分钟内收到同一个应用的多条通知时，不会重复提醒，同时在通知栏里新的通知会替换掉旧的通知。可以通过如下方法来设置冷却时间：
        mPushAgent.setMuteDurationSeconds(0);
        //参数number可以设置为0~10之间任意整数。当参数为0时，表示不合并通知。
        mPushAgent.setDisplayNotificationNumber(0);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Logger.d("deviceToken" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void addActivity(AppCompatActivity activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        activityList.add(activity);
        Logger.d(activityList.size());
    }

    public static void clearActivitys() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            for (AppCompatActivity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        }
    }

    public static void clearLastActivity() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            activityList.remove(activityList.size() - 1);
            Logger.d(activityList.size());
        }
    }

    public static void clearExceptLastActivitys() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            for (int i = 0; i < activityList.size() - 1; i++) {
                if (activityList.get(i) != null) {
                    activityList.get(i).finish();
                }
            }
            AppCompatActivity activity = activityList.get(activityList.size() - 1);
            activityList.clear();
            activityList.add(activity);
            Logger.d(activityList.size());
        }
    }

    public static void clearExceptFirstActivitys() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            if (activityList.size() > 1) {
//                for (int i = 1; i < activityList.size(); i++) {
//                    if (activityList.get(i) != null) {
//                        activityList.get(i).finish();
////                        BaseActivity baseActivity = (BaseActivity) activityList.get(i);
////                        baseActivity.mSwipeBackHelper.swipeBackward();
//                    }
//                }
                for (int i = activityList.size() - 1; i > 0; i--) {
                    if (activityList.get(i) != null) {
//                        activityList.get(i).finish();
                        BaseActivity baseActivity = (BaseActivity) activityList.get(i);
                        baseActivity.mSwipeBackHelper.swipeBackward();
                    }
                }
                AppCompatActivity activity = activityList.get(activityList.size() - 1);
                activityList.clear();
                activityList.add(activity);
            }
            Logger.d(activityList.size());
        }
    }

    private static AppCompatActivity getLastActvity() {
        if (activityList == null || activityList.size() == 0) {
            return null;
        } else {
            return activityList.get(activityList.size() - 1);
        }
    }

    public static void clearSecondLastActivity() {
        if (activityList == null || activityList.size() == 0 || activityList.size() < 2) {

        } else {
            if (activityList.size() >= 2) {
                AppCompatActivity activity = activityList.get(activityList.size() - 2);
                activityList.remove(activity);
                activity.finish();
            }
            Logger.d(activityList.size());
        }
    }

    private static boolean isHasTrailerActivity() {
        boolean isHas = false;
        if (activityList == null || activityList.size() == 0) {
            isHas = false;
        } else {
            for (int i = 0; i < activityList.size(); i++) {
                AppCompatActivity activity = activityList.get(i);
                if (activity instanceof TrailerActivity) {
                    isHas = true;
                    break;
                }
            }
        }
        return isHas;
    }
}
