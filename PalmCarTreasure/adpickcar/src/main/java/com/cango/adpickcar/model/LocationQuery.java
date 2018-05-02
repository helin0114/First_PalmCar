package com.cango.adpickcar.model;

import com.cango.adpickcar.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/6/27.
 */

public class LocationQuery {

    /**
     * Success : true
     * Code : 0
     * Data : {"userid":"string","IMEI":"string","customerName":"string","vin":"string","gpsstatus":"string","carVoltage":"string","deviceVoltage":"string","gpssignal":"string","gsmsignal":"string","latesttime":"string","address":"string","resultLAT":0,"resultLON":0,"trackList":[{"stopRemark":"string","trackTime":"2017-06-27T02:55:27.315Z","resultLAT":0,"resultLON":0}]}
     * Msg : string
     */

    private String Code;
    private DataBean Data;
    private String Msg;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
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
        this.Msg = Msg;
    }

    public static class DataBean {
        /**
         * userid : string
         * IMEI : string
         * customerName : string
         * vin : string
         * gpsstatus : string
         * carVoltage : string
         * deviceVoltage : string
         * gpssignal : string
         * gsmsignal : string
         * latesttime : string
         * address : string
         * resultLAT : 0
         * resultLON : 0
         * trackList : [{"stopRemark":"string","trackTime":"2017-06-27T02:55:27.315Z","resultLAT":0,"resultLON":0}]
         */

        private String IMEI;
        private String CustomerName;
        private String Gpsstatus;
        private String ServerTime;
        private String Latesttime;
        private String Address;
        private double ResultLAT;
        private double ResultLON;
        private List<TrackListBean> TrackList;

        public String getIMEI() {
            return IMEI;
        }

        public void setIMEI(String IMEI) {
            this.IMEI = IMEI;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String CustomerName) {
            if (CommUtil.checkIsNull(CustomerName))
                CustomerName = "";
            this.CustomerName = CustomerName;
        }

        public String getGpsstatus() {
            return Gpsstatus;
        }

        public void setGpsstatus(String Gpsstatus) {
            this.Gpsstatus = Gpsstatus;
        }

        public String getServerTime() {
            return ServerTime;
        }

        public void setServerTime(String serverTime) {
            ServerTime = serverTime;
        }

        public String getLatesttime() {
            return Latesttime;
        }

        public void setLatesttime(String Latesttime) {
            this.Latesttime = Latesttime;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            if (CommUtil.checkIsNull(Address))
                Address = "";
            this.Address = Address;
        }

        public double getResultLAT() {
            return ResultLAT;
        }

        public void setResultLAT(double ResultLAT) {
            this.ResultLAT = ResultLAT;
        }

        public double getResultLON() {
            return ResultLON;
        }

        public void setResultLON(double ResultLON) {
            this.ResultLON = ResultLON;
        }

        public List<TrackListBean> getTrackList() {
            return TrackList;
        }

        public void setTrackList(List<TrackListBean> TrackList) {
            this.TrackList = TrackList;
        }

        public static class TrackListBean {
            /**
             * stopRemark : string
             * trackTime : 2017-06-27T02:55:27.315Z
             * resultLAT : 0
             * resultLON : 0
             */

            private String StopRemark;
            private String TrackTime;
            private double ResultLAT;
            private double ResultLON;

            public String getStopRemark() {
                return StopRemark;
            }

            public void setStopRemark(String StopRemark) {
                this.StopRemark = StopRemark;
            }

            public String getTrackTime() {
                return TrackTime;
            }

            public void setTrackTime(String TrackTime) {
                this.TrackTime = TrackTime;
            }

            public double getResultLAT() {
                return ResultLAT;
            }

            public void setResultLAT(double ResultLAT) {
                this.ResultLAT = ResultLAT;
            }

            public double getResultLON() {
                return ResultLON;
            }

            public void setResultLON(double ResultLON) {
                this.ResultLON = ResultLON;
            }
        }
    }
}
