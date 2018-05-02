package com.cango.adpickcar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cango on 2017/10/16.
 */

public class PhotoResult implements Parcelable {

    /**
     * Msg : 操作成功
     * Code : 200
     * Data : {"PicFileID":178,"PicPath":"/VPIC/CAR_00056/1016160459099_1508141085089.jpg","ThumbPath":"/VPIC/CAR_00056/1016160459139_thumb_1508141085089.jpg"}
     */

    private String Msg;
    private String Code;
    private DataBean Data;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

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

    public static class DataBean implements Parcelable {
        /**
         * PicFileID : 178
         * PicPath : /VPIC/CAR_00056/1016160459099_1508141085089.jpg
         * ThumbPath : /VPIC/CAR_00056/1016160459139_thumb_1508141085089.jpg
         */

        private int PicFileID;
        private String PicPath;
        private String ThumbPath;

        public int getPicFileID() {
            return PicFileID;
        }

        public void setPicFileID(int PicFileID) {
            this.PicFileID = PicFileID;
        }

        public String getPicPath() {
            return PicPath;
        }

        public void setPicPath(String PicPath) {
            this.PicPath = PicPath;
        }

        public String getThumbPath() {
            return ThumbPath;
        }

        public void setThumbPath(String ThumbPath) {
            this.ThumbPath = ThumbPath;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.PicFileID);
            dest.writeString(this.PicPath);
            dest.writeString(this.ThumbPath);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.PicFileID = in.readInt();
            this.PicPath = in.readString();
            this.ThumbPath = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Msg);
        dest.writeString(this.Code);
        dest.writeParcelable(this.Data, flags);
    }

    public PhotoResult() {
    }

    protected PhotoResult(Parcel in) {
        this.Msg = in.readString();
        this.Code = in.readString();
        this.Data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<PhotoResult> CREATOR = new Parcelable.Creator<PhotoResult>() {
        @Override
        public PhotoResult createFromParcel(Parcel source) {
            return new PhotoResult(source);
        }

        @Override
        public PhotoResult[] newArray(int size) {
            return new PhotoResult[size];
        }
    };
}
