package com.cango.palmcartreasure.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cango on 2017/10/23.
 */

public class QRCodeBean implements Parcelable {
    /**
     * TCUserID : 拖车人员ID
     * AgencyID : 拖车任务ID
     * ApplyCD : 申请编号
     * LAT : 维度
     * LON : 精度
     */

    private String TCUserID;
    private String AgencyID;
    private String ApplyCD;
    private String Lat;
    private String Lon;
    private String datasource;

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getTCUserID() {
        return TCUserID;
    }

    public void setTCUserID(String TCUserID) {
        this.TCUserID = TCUserID;
    }

    public String getAgencyID() {
        return AgencyID;
    }

    public void setAgencyID(String AgencyID) {
        this.AgencyID = AgencyID;
    }

    public String getApplyCD() {
        return ApplyCD;
    }

    public void setApplyCD(String ApplyCD) {
        this.ApplyCD = ApplyCD;
    }

    public String getLAT() {
        return Lat;
    }

    public void setLAT(String LAT) {
        this.Lat = LAT;
    }

    public String getLON() {
        return Lon;
    }

    public void setLON(String LON) {
        this.Lon = LON;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TCUserID);
        dest.writeString(this.AgencyID);
        dest.writeString(this.ApplyCD);
        dest.writeString(this.Lat);
        dest.writeString(this.Lon);
        dest.writeString(this.datasource);
    }

    public QRCodeBean() {
    }

    protected QRCodeBean(Parcel in) {
        this.TCUserID = in.readString();
        this.AgencyID = in.readString();
        this.ApplyCD = in.readString();
        this.Lat = in.readString();
        this.Lon = in.readString();
        this.datasource = in.readString();
    }

    public static final Parcelable.Creator<QRCodeBean> CREATOR = new Parcelable.Creator<QRCodeBean>() {
        @Override
        public QRCodeBean createFromParcel(Parcel source) {
            return new QRCodeBean(source);
        }

        @Override
        public QRCodeBean[] newArray(int size) {
            return new QRCodeBean[size];
        }
    };
}
