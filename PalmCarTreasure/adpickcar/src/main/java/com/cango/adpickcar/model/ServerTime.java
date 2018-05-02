package com.cango.adpickcar.model;

/**
 * Created by cango on 2017/10/12.
 */

public class ServerTime {

    /**
     * Code : 200
     * Msg :
     * Data : {"ServerTime":"2017-08-09 15:00:00"}
     */

    private String Code;
    private String Msg;
    private DataBean Data;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * ServerTime : 2017-08-09 15:00:00
         */

        private String ServerTime;

        public String getServerTime() {
            return ServerTime;
        }

        public void setServerTime(String ServerTime) {
            this.ServerTime = ServerTime;
        }
    }
}
