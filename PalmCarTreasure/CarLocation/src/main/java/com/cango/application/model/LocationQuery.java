package com.cango.application.model;

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

        private String userid;
        private String IMEI;
        private String customerName;
        private String vin;
        private String gpsstatus;
        private String carVoltage;
        private String deviceVoltage;
        private String gpssignal;
        private String gsmsignal;
        private String latesttime;
        private String address;
        private double resultLAT;
        private double resultLON;
        private List<TrackListBean> trackList;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getIMEI() {
            return IMEI;
        }

        public void setIMEI(String IMEI) {
            this.IMEI = IMEI;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getGpsstatus() {
            return gpsstatus;
        }

        public void setGpsstatus(String gpsstatus) {
            this.gpsstatus = gpsstatus;
        }

        public String getCarVoltage() {
            return carVoltage;
        }

        public void setCarVoltage(String carVoltage) {
            this.carVoltage = carVoltage;
        }

        public String getDeviceVoltage() {
            return deviceVoltage;
        }

        public void setDeviceVoltage(String deviceVoltage) {
            this.deviceVoltage = deviceVoltage;
        }

        public String getGpssignal() {
            return gpssignal;
        }

        public void setGpssignal(String gpssignal) {
            this.gpssignal = gpssignal;
        }

        public String getGsmsignal() {
            return gsmsignal;
        }

        public void setGsmsignal(String gsmsignal) {
            this.gsmsignal = gsmsignal;
        }

        public String getLatesttime() {
            return latesttime;
        }

        public void setLatesttime(String latesttime) {
            this.latesttime = latesttime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getResultLAT() {
            return resultLAT;
        }

        public void setResultLAT(double resultLAT) {
            this.resultLAT = resultLAT;
        }

        public double getResultLON() {
            return resultLON;
        }

        public void setResultLON(double resultLON) {
            this.resultLON = resultLON;
        }

        public List<TrackListBean> getTrackList() {
            return trackList;
        }

        public void setTrackList(List<TrackListBean> trackList) {
            this.trackList = trackList;
        }

        public static class TrackListBean {
            /**
             * stopRemark : string
             * trackTime : 2017-06-27T02:55:27.315Z
             * resultLAT : 0
             * resultLON : 0
             */

            private String stopRemark;
            private String trackTime;
            private double resultLAT;
            private double resultLON;

            public String getStopRemark() {
                return stopRemark;
            }

            public void setStopRemark(String stopRemark) {
                this.stopRemark = stopRemark;
            }

            public String getTrackTime() {
                return trackTime;
            }

            public void setTrackTime(String trackTime) {
                this.trackTime = trackTime;
            }

            public double getResultLAT() {
                return resultLAT;
            }

            public void setResultLAT(double resultLAT) {
                this.resultLAT = resultLAT;
            }

            public double getResultLON() {
                return resultLON;
            }

            public void setResultLON(double resultLON) {
                this.resultLON = resultLON;
            }
        }
    }
}
