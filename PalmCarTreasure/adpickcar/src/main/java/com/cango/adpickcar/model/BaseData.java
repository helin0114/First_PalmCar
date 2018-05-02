package com.cango.adpickcar.model;

/**
 * Created by cango on 2017/10/12.
 */

public class BaseData {

    /**
     * Code : 200
     * Msg : string
     * Data : {}
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
    }
}
