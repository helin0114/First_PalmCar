package com.cango.adpickcar;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cango.adpickcar.util.CommUtil;
import com.cango.adpickcar.util.SPUtils;
import com.cango.adpickcar.util.ToastUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cango on 2017/9/18.
 */

public class ADApplication extends Application {
    private static Context mContext;
    public static SPUtils mSPUtils;
    public static List<AppCompatActivity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mSPUtils = new SPUtils("AD_CAR");
        initUmengPush();
//        initBugly();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void initUmengPush() {
        UMConfigure.init(mContext, "5add4ebbf29d98789e000011", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "a9cf164e9aef9dfbf7afc684e0dba743");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //默认情况下，同一台设备在1分钟内收到同一个应用的多条通知时，不会重复提醒，
        // 同时在通知栏里新的通知会替换掉旧的通知。可以通过如下方法来设置冷却时间
        mPushAgent.setMuteDurationSeconds(0);
        //服务端控制：通过服务端推送状态来设置客户端响铃、震动、呼吸灯的状态
        // 客户端允许：不关心服务端推送状态，客户端都会响铃、震动、呼吸灯亮
        // 客户端禁止：不关心服务端推送状态，客户端不会响铃、震动、呼吸灯亮
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE); //声音
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);//呼吸灯
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);//振动

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                ToastUtils.showShort(msg.custom);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort(msg.custom);
                    }
                });
            }
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                //只要接收到通知都走这里统一处理
                Logger.d(uMessage.text);
                return super.getNotification(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Logger.d("deviceToken = " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
        PushAgent.getInstance(mContext).onAppStart();
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void addActivity(AppCompatActivity activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        activityList.add(activity);
    }

    public static void clearLastActivity() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            activityList.remove(activityList.size() - 1);
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
        }
    }

    /**
     * 初始化bugly参数
     */
    private void initBugly() {
        //增加bugly上报进程控制
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = CommUtil.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        strategy.setAppVersion("1.0.2");      //App的版本
//        strategy.setAppChannel("");         //设置渠道
//        strategy.setAppPackageName("");     //App的包名
        //Bugly会在启动10s后联网同步数据。若您有特别需求，可以修改这个时间。
//        strategy.setAppReportDelay(20000);  //改为20s

        //连接buyly初始化
        /**
         * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
         * 输出详细的Bugly SDK的Log；
         * 每一条Crash都会被立即上报；
         * 自定义日志将会在Logcat中输出
         * 建议在测试阶段建议设置成true，发布时设置为false。
         */
        CrashReport.initCrashReport(getApplicationContext(), "8ca94448c1", true, strategy);
    }
}
