package com.cango.adpickcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.adpickcar.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/11/27.
 */

public class DeliveryTaskList {
    /**
     * Code : 200
     * Msg : string
     * Data : {"NoDeliveryTaskCount":12,"DeliveryTaskCount":12,"DeliveryFailureCount":12,"NextPage":0,"CarDeliveryTaskList":[{"CDLVTasKID":2,"DisCarID":1,"ApplyCD":"string","CustName":"string","LicenseplateNO":"string","Color":"string","CarBrandName":"string","CarBrandPicURL":"string","CTPName":"string","CTPMobile":"string","CarModelName":"string","Reason":"string"}]}
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
         * NoDeliveryTaskCount : 12
         * DeliveryTaskCount : 12
         * DeliveryFailureCount : 12
         * NextPage : 0
         * CarDeliveryTaskList : [{"CDLVTasKID":2,"DisCarID":1,"ApplyCD":"string","CustName":"string",
         * "LicenseplateNO":"string","Color":"string","CarBrandName":"string","CarBrandPicURL":"string",
         * "CTPName":"string","CTPMobile":"string","CarModelName":"string","Reason":"string"}]
         */

        private int NoDeliveryTaskCount;
        private int DeliveryTaskCount;
        private int DeliveryFailureCount;
        private int NextPage;
        private List<CarDeliveryTaskListBean> CarDeliveryTaskList;

        public int getNoDeliveryTaskCount() {
            return NoDeliveryTaskCount;
        }

        public void setNoDeliveryTaskCount(int NoDeliveryTaskCount) {
            this.NoDeliveryTaskCount = NoDeliveryTaskCount;
        }

        public int getDeliveryTaskCount() {
            return DeliveryTaskCount;
        }

        public void setDeliveryTaskCount(int DeliveryTaskCount) {
            this.DeliveryTaskCount = DeliveryTaskCount;
        }

        public int getDeliveryFailureCount() {
            return DeliveryFailureCount;
        }

        public void setDeliveryFailureCount(int DeliveryFailureCount) {
            this.DeliveryFailureCount = DeliveryFailureCount;
        }

        public int getNextPage() {
            return NextPage;
        }

        public void setNextPage(int NextPage) {
            this.NextPage = NextPage;
        }

        public List<CarDeliveryTaskListBean> getCarDeliveryTaskList() {
            return CarDeliveryTaskList;
        }

        public void setCarDeliveryTaskList(List<CarDeliveryTaskListBean> CarDeliveryTaskList) {
            this.CarDeliveryTaskList = CarDeliveryTaskList;
        }

        public static class CarDeliveryTaskListBean implements Parcelable {
            /**
             * CDLVTasKID : 2
             * DisCarID : 1
             * ApplyCD : string
             * CustName : string
             * LicenseplateNO : string
             * Color : string
             * CarBrandName : string
             * CarBrandPicURL : string
             * CTPName : string
             * CTPMobile : string
             * CarModelName : string
             * Reason : string
             * PlanDLVTime : 计划交车时间
             * CanReturn : 是否可以交车退回
             */

            private int CDLVTasKID;
            private int DisCarID;
            private String ApplyCD;
            private String CustName;
            private String LicenseplateNO;
            private String Color;
            private String CarBrandName;
            private String CarBrandPicURL;
            private String CTPName;
            private String CTPMobile;
            private String CarModelName;
            private String Reason;
            private String PlanDLVTime;
            private boolean CanReturn;

            public int getCDLVTasKID() {
                return CDLVTasKID;
            }

            public void setCDLVTasKID(int CDLVTasKID) {
                this.CDLVTasKID = CDLVTasKID;
            }

            public int getDisCarID() {
                return DisCarID;
            }

            public void setDisCarID(int DisCarID) {
                this.DisCarID = DisCarID;
            }

            public String getApplyCD() {
                return ApplyCD;
            }

            public void setApplyCD(String ApplyCD) {
                if (CommUtil.checkIsNull(ApplyCD))
                    ApplyCD = "";
                this.ApplyCD = ApplyCD;
            }

            public String getCustName() {
                return CustName;
            }

            public void setCustName(String CustName) {
                if (CommUtil.checkIsNull(CustName))
                    CustName = "";
                this.CustName = CustName;
            }

            public String getLicenseplateNO() {
                return LicenseplateNO;
            }

            public void setLicenseplateNO(String LicenseplateNO) {
                if (CommUtil.checkIsNull(LicenseplateNO))
                    LicenseplateNO = "";
                this.LicenseplateNO = LicenseplateNO;
            }

            public String getColor() {
                return Color;
            }

            public void setColor(String Color) {
                if (CommUtil.checkIsNull(Color))
                    Color = "";
                this.Color = Color;
            }

            public String getCarBrandName() {
                return CarBrandName;
            }

            public void setCarBrandName(String CarBrandName) {
                if (CommUtil.checkIsNull(CarBrandName))
                    CarBrandName = "";
                this.CarBrandName = CarBrandName;
            }

            public String getCarBrandPicURL() {
                return CarBrandPicURL;
            }

            public void setCarBrandPicURL(String CarBrandPicURL) {
                if (CommUtil.checkIsNull(CarBrandPicURL))
                    CarBrandPicURL = "";
                this.CarBrandPicURL = CarBrandPicURL;
            }

            public String getCTPName() {
                return CTPName;
            }

            public void setCTPName(String CTPName) {
                if (CommUtil.checkIsNull(CTPName))
                    CTPName = "";
                this.CTPName = CTPName;
            }

            public String getCTPMobile() {
                return CTPMobile;
            }

            public void setCTPMobile(String CTPMobile) {
                if (CommUtil.checkIsNull(CTPMobile))
                    CTPMobile = "";
                this.CTPMobile = CTPMobile;
            }

            public String getCarModelName() {
                return CarModelName;
            }

            public void setCarModelName(String CarModelName) {
                if (CommUtil.checkIsNull(CarModelName))
                    CarModelName = "";
                this.CarModelName = CarModelName;
            }

            public String getReason() {
                return Reason;
            }

            public void setReason(String Reason) {
                if (CommUtil.checkIsNull(Reason))
                    Reason = "";
                this.Reason = Reason;
            }

            public String getPlanDLVTime() {
                return PlanDLVTime;
            }

            public void setPlanDLVTime(String PlanDLVTime) {
                if (CommUtil.checkIsNull(PlanDLVTime))
                    PlanDLVTime = "";
                this.PlanDLVTime = PlanDLVTime;
            }

            public boolean getCanReturn() {
                return CanReturn;
            }

            public void setCanReturn(boolean CanReturn) {
                this.CanReturn = CanReturn;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.CDLVTasKID);
                dest.writeInt(this.DisCarID);
                dest.writeString(this.ApplyCD);
                dest.writeString(this.CustName);
                dest.writeString(this.LicenseplateNO);
                dest.writeString(this.Color);
                dest.writeString(this.CarBrandName);
                dest.writeString(this.CarBrandPicURL);
                dest.writeString(this.CTPName);
                dest.writeString(this.CTPMobile);
                dest.writeString(this.CarModelName);
                dest.writeString(this.Reason);
                dest.writeString(this.PlanDLVTime);
                dest.writeInt(this.CanReturn ? 1 : 0);
            }

            public CarDeliveryTaskListBean() {
            }

            protected CarDeliveryTaskListBean(Parcel in) {
                this.CDLVTasKID = in.readInt();
                this.DisCarID = in.readInt();
                this.ApplyCD = in.readString();
                this.CustName = in.readString();
                this.LicenseplateNO = in.readString();
                this.Color = in.readString();
                this.CarBrandName = in.readString();
                this.CarBrandPicURL = in.readString();
                this.CTPName = in.readString();
                this.CTPMobile = in.readString();
                this.CarModelName = in.readString();
                this.Reason = in.readString();
                this.PlanDLVTime = in.readString();
                this.CanReturn = (in.readInt() == 1) ? true : false;
            }

            public static final Parcelable.Creator<CarDeliveryTaskListBean> CREATOR = new Parcelable.Creator<CarDeliveryTaskListBean>() {
                @Override
                public CarDeliveryTaskListBean createFromParcel(Parcel source) {
                    return new CarDeliveryTaskListBean(source);
                }

                @Override
                public CarDeliveryTaskListBean[] newArray(int size) {
                    return new CarDeliveryTaskListBean[size];
                }
            };
        }
    }
}
