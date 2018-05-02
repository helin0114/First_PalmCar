package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/5/18.
 */

public class NavigationCar {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"connectflag":"在线","CACHETIME":"2017/5/8 17:36:12","resultLAT":25.0748749,"resultLON":104.94355}
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
         * connectflag : 在线
         * CACHETIME : 2017/5/8 17:36:12
         * resultLAT : 25.0748749
         * resultLON : 104.94355
         */

        private String connectflag;
        private String CACHETIME;
        private double resultLAT;
        private double resultLON;
        private String resultAddress;
        private String resultDeviceId;
        private String serverTime;
        private List<TrackListBean> trackList;

        public String getResultDeviceId() {
            return resultDeviceId;
        }

        public void setResultDeviceId(String resultDeviceId) {
            if (CommUtil.checkIsNull(resultDeviceId))
                resultDeviceId="";
            this.resultDeviceId = resultDeviceId;
        }

        public String getResultAddress() {
            return resultAddress;
        }

        public void setResultAddress(String resultAddress) {
            if (CommUtil.checkIsNull(resultAddress))
                resultAddress="";
            this.resultAddress = resultAddress;
        }

        public String getConnectflag() {
            return connectflag;
        }

        public void setConnectflag(String connectflag) {
            if (CommUtil.checkIsNull(connectflag))
                connectflag="";
            this.connectflag = connectflag;
        }

        public String getCACHETIME() {
            return CACHETIME;
        }

        public void setCACHETIME(String CACHETIME) {
            if (CommUtil.checkIsNull(CACHETIME))
                CACHETIME="";
            this.CACHETIME = CACHETIME;
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

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            if (CommUtil.checkIsNull(serverTime))
                serverTime="";
            this.serverTime = serverTime;
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
                if (CommUtil.checkIsNull(stopRemark))
                    stopRemark="";
                this.stopRemark = stopRemark;
            }

            public String getTrackTime() {
                return trackTime;
            }

            public void setTrackTime(String trackTime) {
                if (CommUtil.checkIsNull(trackTime))
                    trackTime="";
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
