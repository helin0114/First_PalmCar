package com.cango.application.model;

import com.cango.application.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/6/12.
 */

public class CarTrackQuery {

    /**
     * Success : true
     * Code : 0
     * Data : {"trackList":[{"stopRemark":"string","trackTime":"2017-06-12T02:00:14.993Z","resultLAT":0,"resultLON":0}]}
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
        private List<TrackListBean> trackList;

        public List<TrackListBean> getTrackList() {
            return trackList;
        }

        public void setTrackList(List<TrackListBean> trackList) {
            this.trackList = trackList;
        }

        public static class TrackListBean {
            /**
             * stopRemark : string
             * trackTime : 2017-06-12T02:00:14.993Z
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
