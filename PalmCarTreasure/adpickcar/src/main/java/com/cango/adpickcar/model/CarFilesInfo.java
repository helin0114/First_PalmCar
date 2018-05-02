package com.cango.adpickcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.adpickcar.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/10/14.
 */

public class CarFilesInfo {

    /**
     * Code : 200
     * Msg : string
     * Data : {"SurfaceFileList":[{"PicFileID":0,"DisCarPicID":0,"PicPath":"","ThumbPath":"","PicInst":"","PicDtlDesc":"","SubCategory":"","SubID":"","SubName":"","IsRequire":false}],"DetailList":[{"PicFileID":0,"DisCarPicID":0,"PicPath":"","ThumbPath":"","PicInst":"","PicDtlDesc":"","SubCategory":"","SubID":"","SubName":"","IsRequire":false}],"SupplementList":[{"PicFileID":0,"DisCarPicID":0,"PicPath":"","ThumbPath":"","PicInst":"","PicDtlDesc":"","SubCategory":"","SubID":"","SubName":"","IsRequire":false}],"TakeCarList":[{"PicFileID":0,"DisCarPicID":0,"PicPath":"","ThumbPath":"","PicInst":"","PicDtlDesc":"","SubCategory":"","SubID":"","SubName":"","IsRequire":false}]}
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
        private List<SurfaceFileListBean> SurfaceFileList;
        private List<SurfaceFileListBean> DetailList;
        private List<SurfaceFileListBean> SupplementList;

        public List<SurfaceFileListBean> getSurfaceFileList() {
            return SurfaceFileList;
        }

        public void setSurfaceFileList(List<SurfaceFileListBean> SurfaceFileList) {
            this.SurfaceFileList = SurfaceFileList;
        }

        public List<SurfaceFileListBean> getDetailList() {
            return DetailList;
        }

        public void setDetailList(List<SurfaceFileListBean> DetailList) {
            this.DetailList = DetailList;
        }

        public List<SurfaceFileListBean> getSupplementList() {
            return SupplementList;
        }

        public void setSupplementList(List<SurfaceFileListBean> SupplementList) {
            this.SupplementList = SupplementList;
        }

        public static class SurfaceFileListBean implements Parcelable {
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
            private String MongoliaPath;
            private String MongoliaIconPath;
            private String MongoliaDesc;

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

            public String getMongoliaPath() {
                return MongoliaPath;
            }

            public void setMongoliaPath(String mongoliaPath) {
                MongoliaPath = mongoliaPath;
            }

            public String getMongoliaIconPath() {
                return MongoliaIconPath;
            }

            public void setMongoliaIconPath(String mongoliaIconPath) {
                MongoliaIconPath = mongoliaIconPath;
            }

            public String getMongoliaDesc() {
                return MongoliaDesc;
            }

            public void setMongoliaDesc(String mongoliaDesc) {
                if (!CommUtil.checkIsNull(mongoliaDesc))
                    mongoliaDesc = "";
                MongoliaDesc = mongoliaDesc;
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
                dest.writeString(this.MongoliaPath);
                dest.writeString(this.MongoliaIconPath);
                dest.writeString(this.MongoliaDesc);
            }

            public SurfaceFileListBean() {
            }

            protected SurfaceFileListBean(Parcel in) {
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
                this.MongoliaPath = in.readString();
                this.MongoliaIconPath = in.readString();
                this.MongoliaDesc = in.readString();
            }

            public static final Parcelable.Creator<SurfaceFileListBean> CREATOR = new Parcelable.Creator<SurfaceFileListBean>() {
                @Override
                public SurfaceFileListBean createFromParcel(Parcel source) {
                    return new SurfaceFileListBean(source);
                }

                @Override
                public SurfaceFileListBean[] newArray(int size) {
                    return new SurfaceFileListBean[size];
                }
            };
        }
    }
}
