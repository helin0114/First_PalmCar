package com.cango.palmcartreasure.model;

/**
 * Created by cango on 2017/4/18.
 */

public class LoginData {

    /**
     * Success : true
     * Msg : 登录成功
     * Code : 0
     * Data : {"token":"E8E4FFEB81A7A3ECCFCBA51690CA62593282A29CA0952EA85AEF1F1780ABA66A4F0F6243AAB0521922FC9EF5CEA4B2D86AD96BE6DD86435EA3D392B7D734E571D17372D379C36C82","role":"Emp","userid":"6202","result":null}
     */

    private boolean Success;
    private String Msg;
    private int Code;
    private DataBean Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
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

    public static class DataBean {
        /**
         * token : E8E4FFEB81A7A3ECCFCBA51690CA62593282A29CA0952EA85AEF1F1780ABA66A4F0F6243AAB0521922FC9EF5CEA4B2D86AD96BE6DD86435EA3D392B7D734E571D17372D379C36C82
         * role : Emp
         * userid : 6202
         * result : null
         */

        private String token;
        private String role;
        private String userid;
        private Object result;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }
}
