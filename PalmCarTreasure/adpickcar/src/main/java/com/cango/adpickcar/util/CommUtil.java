package com.cango.adpickcar.util;

import android.text.TextUtils;

import com.cango.adpickcar.api.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by cango on 2017/3/15.
 */

public class CommUtil {
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

    public static <T> boolean checkIsNull(T reference) {
        if (reference == null)
            return true;
        else
            return false;
    }

    public static String getParmasMapToJsonByEncrypt(Map<String, Object> paramsMap) {
        String encrypt = null;
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String source = gson.toJson(paramsMap);
        try {
            encrypt = EncryptUtils.encrypt(Api.KEY, source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    public static String setParamsToJsonByEncrypt(Object object) {
        String encrypt = null;
        String source = new Gson().toJson(object);
//        Logger.d(source);
        try {
            encrypt = EncryptUtils.encrypt(Api.KEY, source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    public static boolean handingCodeLogin(String code){
        boolean isReturn = false;
        if ("212".equals(code)){
            isReturn = true;
        }
        return isReturn;
    }
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
