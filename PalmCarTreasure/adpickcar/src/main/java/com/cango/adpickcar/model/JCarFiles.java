package com.cango.adpickcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.adpickcar.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/11/27.
 */

public class JCarFiles {

    /**
     * Code : 200
     * Msg : string
     * Data : {"DeliveryFileList":[{"PicFileID":0,"DisCarPicID":0,"PicPath":"","ThumbPath":"","PicInst":"","PicDtlDesc":"","SubCategory":"","SubID":"","SubName":"","IsRequire":false,"PicGroup":"","DisCarID":""}]}
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
        private List<DeliveryFileListBean> DeliveryFileList;

        public List<DeliveryFileListBean> getDeliveryFileList() {
            return DeliveryFileList;
        }

        public void setDeliveryFileList(List<DeliveryFileListBean> DeliveryFileList) {
            this.DeliveryFileList = DeliveryFileList;
        }

        public static class DeliveryFileListBean implements Parcelable {
            /**
             * PicFileID : 0
             * DisCarPicID : 0
             * PicPath :
             * ThumbPath :
             * PicInst :
             * PicDtlDesc :
             * SubCategory :
             * SubID :
             * SubName :
             * IsRequire : false
             * PicGroup :
             * DisCarID :
             */

            private int PicFileID;
            private int DisCarPicID;
            private String PicPath;
            private String ThumbPath;
            private String PicInst;
            private String PicDtlDesc;
            private String SubCategory;
            private String SubID;
            private String SubName;
            private boolean IsRequire;
            private String PicGroup;
            private String DisCarID;

            public int getPicFileID() {
                return PicFileID;
            }

            public void setPicFileID(int PicFileID) {
                this.PicFileID = PicFileID;
            }

            public int getDisCarPicID() {
                return DisCarPicID;
            }

            public void setDisCarPicID(int DisCarPicID) {
                this.DisCarPicID = DisCarPicID;
            }

            public String getPicPath() {
                return PicPath;
            }

            public void setPicPath(String PicPath) {
                if (CommUtil.checkIsNull(PicPath))
                    PicPath = "";
                this.PicPath = PicPath;
            }

            public String getThumbPath() {
                return ThumbPath;
            }

            public void setThumbPath(String ThumbPath) {
                if (CommUtil.checkIsNull(ThumbPath))
                    ThumbPath = "";
                this.ThumbPath = ThumbPath;
            }

            public String getPicInst() {
                return PicInst;
            }

            public void setPicInst(String PicInst) {
                if (CommUtil.checkIsNull(PicInst))
                    PicInst = "";
                this.PicInst = PicInst;
            }

            public String getPicDtlDesc() {
                return PicDtlDesc;
            }

            public void setPicDtlDesc(String PicDtlDesc) {
                if (CommUtil.checkIsNull(PicDtlDesc))
                    PicDtlDesc = "";
                this.PicDtlDesc = PicDtlDesc;
            }

            public String getSubCategory() {
                return SubCategory;
            }

            public void setSubCategory(String SubCategory) {
                if (CommUtil.checkIsNull(SubCategory))
                    SubCategory = "";
                this.SubCategory = SubCategory;
            }

            public String getSubID() {
                return SubID;
            }

            public void setSubID(String SubID) {
                if (CommUtil.checkIsNull(SubID))
                    SubID = "";
                this.SubID = SubID;
            }

            public String getSubName() {
                return SubName;
            }

            public void setSubName(String SubName) {
                if (CommUtil.checkIsNull(SubName))
                    SubName = "";
                this.SubName = SubName;
            }

            public boolean isIsRequire() {
                return IsRequire;
            }

            public void setIsRequire(boolean IsRequire) {
                this.IsRequire = IsRequire;
            }

            public String getPicGroup() {
                return PicGroup;
            }

            public void setPicGroup(String PicGroup) {
                if (CommUtil.checkIsNull(PicGroup))
                    PicGroup = "";
                this.PicGroup = PicGroup;
            }

            public String getDisCarID() {
                return DisCarID;
            }

            public void setDisCarID(String DisCarID) {
                if (CommUtil.checkIsNull(DisCarID))
                    DisCarID = "";
                this.DisCarID = DisCarID;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.PicFileID);
                dest.writeInt(this.DisCarPicID);
                dest.writeString(this.PicPath);
                dest.writeString(this.ThumbPath);
                dest.writeString(this.PicInst);
                dest.writeString(this.PicDtlDesc);
                dest.writeString(this.SubCategory);
                dest.writeString(this.SubID);
                dest.writeString(this.SubName);
                dest.writeByte(this.IsRequire ? (byte) 1 : (byte) 0);
                dest.writeString(this.PicGroup);
                dest.writeString(this.DisCarID);
            }

            public DeliveryFileListBean() {
            }

            protected DeliveryFileListBean(Parcel in) {
                this.PicFileID = in.readInt();
                this.DisCarPicID = in.readInt();
                this.PicPath = in.readString();
                this.ThumbPath = in.readString();
                this.PicInst = in.readString();
                this.PicDtlDesc = in.readString();
                this.SubCategory = in.readString();
                this.SubID = in.readString();
                this.SubName = in.readString();
                this.IsRequire = in.readByte() != 0;
                this.PicGroup = in.readString();
                this.DisCarID = in.readString();
            }

            public static final Parcelable.Creator<DeliveryFileListBean> CREATOR = new Parcelable.Creator<DeliveryFileListBean>() {
                @Override
                public DeliveryFileListBean createFromParcel(Parcel source) {
                    return new DeliveryFileListBean(source);
                }

                @Override
                public DeliveryFileListBean[] newArray(int size) {
                    return new DeliveryFileListBean[size];
                }
            };
        }
    }
}
