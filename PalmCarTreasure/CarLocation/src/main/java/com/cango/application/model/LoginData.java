package com.cango.application.model;

import com.cango.application.util.CommUtil;

/**
 * Created by cango on 2017/4/18.
 */

public class LoginData {

    /**
     * Success : true
     * Code : 0
     * Data : {"token":"string","userid":"string","role":"string"}
     * Msg : string
     */

    private boolean Success;
    private int Code;
    private DataBean Data;
    private String Msg;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        if (CommUtil.checkIsNull(Msg))
            Msg="";
        this.Msg = Msg;
    }

    public static class DataBean {
        /**
         * token : string
         * userid : string
         * role : string
         */

        private String token;
        private String userid;
        private String role;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            if (CommUtil.checkIsNull(token))
                token="";
            this.token = token;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            if (CommUtil.checkIsNull(userid))
                userid="";
            this.userid = userid;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            if (CommUtil.checkIsNull(role))
                role="";
            this.role = role;
        }
    }
}
