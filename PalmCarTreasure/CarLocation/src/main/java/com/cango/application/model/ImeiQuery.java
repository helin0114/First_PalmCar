package com.cango.application.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.application.util.CommUtil;

/**
 * Created by cango on 2017/6/8.
 */

public class ImeiQuery {

    /**
     * Success : true
     * Code : 0
     * Data : {"userid":"string","IMEI":"string","customerName":"string","vin":"string","gpsstatus":"string","carVoltage":"string","deviceVoltage":"string","gpssignal":"string","gsmsignal":"string","latesttime":"string","address":"string","resultLAT":0,"resultLON":0}
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

    public static class DataBean implements Parcelable {
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
        private String serverTime;
        private String applyCD;

        public String getApplyCD() {
            return applyCD;
        }

        public void setApplyCD(String applyCD) {
            if (CommUtil.checkIsNull(applyCD))
                applyCD="";
            this.applyCD = applyCD;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            if (CommUtil.checkIsNull(userid))
                userid="";
            this.userid = userid;
        }

        public String getIMEI() {
            return IMEI;
        }

        public void setIMEI(String IMEI) {
            if (CommUtil.checkIsNull(IMEI))
                IMEI="";
            this.IMEI = IMEI;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            if (CommUtil.checkIsNull(customerName))
                customerName="";
            this.customerName = customerName;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            if (CommUtil.checkIsNull(vin))
                vin="";
            this.vin = vin;
        }

        public String getGpsstatus() {
            return gpsstatus;
        }

        public void setGpsstatus(String gpsstatus) {
            if (CommUtil.checkIsNull(gpsstatus))
                gpsstatus="";
            this.gpsstatus = gpsstatus;
        }

        public String getCarVoltage() {
            return carVoltage;
        }

        public void setCarVoltage(String carVoltage) {
            if (CommUtil.checkIsNull(carVoltage))
                carVoltage="";
            this.carVoltage = carVoltage;
        }

        public String getDeviceVoltage() {
            return deviceVoltage;
        }

        public void setDeviceVoltage(String deviceVoltage) {
            if (CommUtil.checkIsNull(deviceVoltage))
                deviceVoltage="";
            this.deviceVoltage = deviceVoltage;
        }

        public String getGpssignal() {
            return gpssignal;
        }

        public void setGpssignal(String gpssignal) {
            if (CommUtil.checkIsNull(gpssignal))
                gpssignal="";
            this.gpssignal = gpssignal;
        }

        public String getGsmsignal() {
            return gsmsignal;
        }

        public void setGsmsignal(String gsmsignal) {
            if (CommUtil.checkIsNull(gsmsignal))
                gsmsignal="";
            this.gsmsignal = gsmsignal;
        }

        public String getLatesttime() {
            return latesttime;
        }

        public void setLatesttime(String latesttime) {
            if (CommUtil.checkIsNull(latesttime))
                latesttime="";
            this.latesttime = latesttime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            if (CommUtil.checkIsNull(address))
                address="";
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userid);
            dest.writeString(this.IMEI);
            dest.writeString(this.customerName);
            dest.writeString(this.vin);
            dest.writeString(this.gpsstatus);
            dest.writeString(this.carVoltage);
            dest.writeString(this.deviceVoltage);
            dest.writeString(this.gpssignal);
            dest.writeString(this.gsmsignal);
            dest.writeString(this.latesttime);
            dest.writeString(this.address);
            dest.writeDouble(this.resultLAT);
            dest.writeDouble(this.resultLON);
            dest.writeString(this.serverTime);
            dest.writeString(this.applyCD);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.userid = in.readString();
            this.IMEI = in.readString();
            this.customerName = in.readString();
            this.vin = in.readString();
            this.gpsstatus = in.readString();
            this.carVoltage = in.readString();
            this.deviceVoltage = in.readString();
            this.gpssignal = in.readString();
            this.gsmsignal = in.readString();
            this.latesttime = in.readString();
            this.address = in.readString();
            this.resultLAT = in.readDouble();
            this.resultLON = in.readDouble();
            this.serverTime = in.readString();
            this.applyCD = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
