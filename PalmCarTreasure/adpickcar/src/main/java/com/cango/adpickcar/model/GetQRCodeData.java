package com.cango.adpickcar.model;

import java.util.List;

/**
 * Created by cango on 2017/10/25.
 */

public class GetQRCodeData {
    /**
     * Code : 200
     * Msg : string
     * Data : {"SateCode":1,"WareHourseNOList":["上海1号","上海2号"]}
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
         * SateCode : 1
         * WareHourseNOList : ["上海1号","上海2号"]
         */

        private int SateCode;
        private List<String> WareHourseNOList;

        public int getSateCode() {
            return SateCode;
        }

        public void setSateCode(int SateCode) {
            this.SateCode = SateCode;
        }

        public List<String> getWareHourseNOList() {
            return WareHourseNOList;
        }

        public void setWareHourseNOList(List<String> WareHourseNOList) {
            this.WareHourseNOList = WareHourseNOList;
        }
    }
}
