package com.cango.adpickcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.adpickcar.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/11/28.
 */

public class JCarBaseInfo implements Parcelable {

    /**
     * Code : 200
     * Msg : string
     * Data : {"CDLVTaskID":"交车任务ID","DisCarID":"处置车辆ID","DeliveryWay":"交车方式","PlanDLVTime":"计划交车时间","PlanDLVPlace":"计划交车地点","CTPIDCHKFlag":"取车身份验证（1-验证/0-不验证）","CTPName":"取车人姓名","CTPIDNO":"取车人身份证号","CTPCallNO":"取车人手机号","DLVRequire":"交车要求","CarPayedFLag":"确认款项已到（1-是/0-否）","CFMUser":"确认人","CFMTime":"确认时间","DLVMemo":"交车情况备注","Status":"状态（10-编辑/20-通过/30-退回/90-废弃）","AuditFlag":"审批标志（00-未提交/10-已提交/20-审批通过/30-审批退回）","CDLVTaskFlag":"任务完成标志（10-未生效/20-待交车/30-完成交车/40-交车失败）","DLVList":[{"Id":"1","Value":"A"}],"CTPList":[{"Id":"1","Value":"A"}]}
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

    public static class DataBean implements Parcelable {
        /**
         * CDLVTaskID : 交车任务ID
         * DisCarID : 处置车辆ID
         * DeliveryType : 交车类型（10-赎回交车/20-拍卖交车）
         * DeliveryWay : 交车方式
         * PlanDLVTime : 计划交车时间
         * PlanDLVPlace : 计划交车地点
         * CTPIDCHKFlag : 取车身份验证（1-验证/0-不验证）
         * CTPName : 取车人姓名
         * CTPIDNO : 取车人身份证号
         * CTPCallNO : 取车人手机号
         * DLVRequire : 交车要求
         * CarPayedFLag : 确认款项已到（1-是/0-否）
         * FinRegUserName : 收款人
         * ACPTDate : 收款时间
         * DLVMile : 交车时里程数（公里）
         * DLVMemo : 交车情况备注
         * Status : 状态（10-编辑/20-通过/30-退回/90-废弃）
         * AuditFlag : 审批标志（00-未提交/10-已提交/20-审批通过/30-审批退回）
         * CDLVTaskFlag : 任务完成标志（10-未生效/20-待交车/30-完成交车/40-交车失败）
         * DLVList : [{"Id":"1","Value":"A"}]
         * CTPList : [{"Id":"1","Value":"A"}]
         */

        private String CDLVTaskID;
        private String DisCarID;
        private String DeliveryType;
        private String DeliveryWay;
        private String PlanDLVTime;
        private String PlanDLVPlace;
        private String CTPIDCHKFlag;
        private String CTPName;
        private String CTPIDNO;
        private String CTPCallNO;
        private String DLVRequire;
        private String CarPayedFLag;
        private String FinRegUserName;
        private String ACPTDate;
        private String DLVMile;
        private String DLVMemo;
        private String Status;
        private List<DLVListBean> DLVList;
        private List<DLVListBean> CTPList;

        public String getCDLVTaskID() {
            return CDLVTaskID;
        }

        public void setCDLVTaskID(String CDLVTaskID) {
            if (CommUtil.checkIsNull(CDLVTaskID))
                CDLVTaskID = "";
            this.CDLVTaskID = CDLVTaskID;
        }

        public String getDisCarID() {
            return DisCarID;
        }

        public void setDisCarID(String DisCarID) {
            if (CommUtil.checkIsNull(DisCarID))
                DisCarID = "";
            this.DisCarID = DisCarID;
        }

        public String getDeliveryType() {
            return DeliveryType;
        }

        public void setDeliveryType(String DeliveryType) {
            if (CommUtil.checkIsNull(DeliveryType))
                DeliveryType = "";
            this.DeliveryType = DeliveryType;
        }

        public String getDeliveryWay() {
            return DeliveryWay;
        }

        public void setDeliveryWay(String DeliveryWay) {
            if (CommUtil.checkIsNull(DeliveryWay))
                DeliveryWay = "";
            this.DeliveryWay = DeliveryWay;
        }

        public String getPlanDLVTime() {
            return PlanDLVTime;
        }

        public void setPlanDLVTime(String PlanDLVTime) {
            if (CommUtil.checkIsNull(PlanDLVTime))
                PlanDLVTime = "";
            this.PlanDLVTime = PlanDLVTime;
        }

        public String getPlanDLVPlace() {
            return PlanDLVPlace;
        }

        public void setPlanDLVPlace(String PlanDLVPlace) {
            if (CommUtil.checkIsNull(PlanDLVPlace))
                PlanDLVPlace = "";
            this.PlanDLVPlace = PlanDLVPlace;
        }

        public String getCTPIDCHKFlag() {
            return CTPIDCHKFlag;
        }

        public void setCTPIDCHKFlag(String CTPIDCHKFlag) {
            if (CommUtil.checkIsNull(CTPIDCHKFlag))
                CTPIDCHKFlag = "";
            this.CTPIDCHKFlag = CTPIDCHKFlag;
        }

        public String getCTPName() {
            return CTPName;
        }

        public void setCTPName(String CTPName) {
            if (CommUtil.checkIsNull(CTPName))
                CTPName = "";
            this.CTPName = CTPName;
        }

        public String getCTPIDNO() {
            return CTPIDNO;
        }

        public void setCTPIDNO(String CTPIDNO) {
            if (CommUtil.checkIsNull(CTPIDNO))
                CTPIDNO = "";
            this.CTPIDNO = CTPIDNO;
        }

        public String getCTPCallNO() {
            return CTPCallNO;
        }

        public void setCTPCallNO(String CTPCallNO) {
            if (CommUtil.checkIsNull(CTPCallNO))
                CTPCallNO = "";
            this.CTPCallNO = CTPCallNO;
        }

        public String getDLVRequire() {
            return DLVRequire;
        }

        public void setDLVRequire(String DLVRequire) {
            if (CommUtil.checkIsNull(DLVRequire))
                DLVRequire = "";
            this.DLVRequire = DLVRequire;
        }

        public String getCarPayedFLag() {
            return CarPayedFLag;
        }

        public void setCarPayedFLag(String CarPayedFLag) {
            if (CommUtil.checkIsNull(CarPayedFLag))
                CarPayedFLag = "";
            this.CarPayedFLag = CarPayedFLag;
        }

        public String getCFMUser() {
            return FinRegUserName;
        }

        public void setCFMUser(String CFMUser) {
            if (CommUtil.checkIsNull(CFMUser))
                CFMUser = "";
            this.FinRegUserName = CFMUser;
        }

        public String getCFMTime() {
            return ACPTDate;
        }

        public void setCFMTime(String CFMTime) {
            if (CommUtil.checkIsNull(CFMTime))
                CFMTime = "";
            this.ACPTDate = CFMTime;
        }

        public String getDLVMemo() {
            return DLVMemo;
        }

        public void setDLVMemo(String DLVMemo) {
            if (CommUtil.checkIsNull(DLVMemo))
                DLVMemo = "";
            this.DLVMemo = DLVMemo;
        }

        public String getDLVMile() {
            return DLVMile;
        }

        public void setDLVMile(String DLVMile) {
            if (CommUtil.checkIsNull(DLVMile))
                DLVMile = "";
            this.DLVMile = DLVMile;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            if (CommUtil.checkIsNull(Status))
                Status = "";
            this.Status = Status;
        }

        public List<DLVListBean> getDLVList() {
            return DLVList;
        }

        public void setDLVList(List<DLVListBean> DLVList) {
            this.DLVList = DLVList;
        }

        public List<DLVListBean> getCTPList() {
            return CTPList;
        }

        public void setCTPList(List<DLVListBean> CTPList) {
            this.CTPList = CTPList;
        }

        public static class DLVListBean implements Parcelable {
            /**
             * Id : 1
             * Value : A
             */

            private String Id;
            private String Value;

            @Override
            public String toString() {
                return Value;
            }

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String Value) {
                if (CommUtil.checkIsNull(Value))
                    Value = "";
                this.Value = Value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.Id);
                dest.writeString(this.Value);
            }

            public DLVListBean() {
            }

            protected DLVListBean(Parcel in) {
                this.Id = in.readString();
                this.Value = in.readString();
            }

            public static final Parcelable.Creator<DLVListBean> CREATOR = new Parcelable.Creator<DLVListBean>() {
                @Override
                public DLVListBean createFromParcel(Parcel source) {
                    return new DLVListBean(source);
                }

                @Override
                public DLVListBean[] newArray(int size) {
                    return new DLVListBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.CDLVTaskID);
            dest.writeString(this.DisCarID);
            dest.writeString(this.DeliveryType);
            dest.writeString(this.DeliveryWay);
            dest.writeString(this.PlanDLVTime);
            dest.writeString(this.PlanDLVPlace);
            dest.writeString(this.CTPIDCHKFlag);
            dest.writeString(this.CTPName);
            dest.writeString(this.CTPIDNO);
            dest.writeString(this.CTPCallNO);
            dest.writeString(this.DLVRequire);
            dest.writeString(this.CarPayedFLag);
            dest.writeString(this.FinRegUserName);
            dest.writeString(this.ACPTDate);
            dest.writeString(this.DLVMile);
            dest.writeString(this.DLVMemo);
            dest.writeString(this.Status);
            dest.writeTypedList(this.DLVList);
            dest.writeTypedList(this.CTPList);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.CDLVTaskID = in.readString();
            this.DisCarID = in.readString();
            this.DeliveryType = in.readString();
            this.DeliveryWay = in.readString();
            this.PlanDLVTime = in.readString();
            this.PlanDLVPlace = in.readString();
            this.CTPIDCHKFlag = in.readString();
            this.CTPName = in.readString();
            this.CTPIDNO = in.readString();
            this.CTPCallNO = in.readString();
            this.DLVRequire = in.readString();
            this.CarPayedFLag = in.readString();
            this.FinRegUserName = in.readString();
            this.ACPTDate = in.readString();
            this.DLVMile = in.readString();
            this.DLVMemo = in.readString();
            this.Status = in.readString();
            this.DLVList = in.createTypedArrayList(DLVListBean.CREATOR);
            this.CTPList = in.createTypedArrayList(DLVListBean.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Code);
        dest.writeString(this.Msg);
        dest.writeParcelable(this.Data, flags);
    }

    public JCarBaseInfo() {
    }

    protected JCarBaseInfo(Parcel in) {
        this.Code = in.readString();
        this.Msg = in.readString();
        this.Data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<JCarBaseInfo> CREATOR = new Parcelable.Creator<JCarBaseInfo>() {
        @Override
        public JCarBaseInfo createFromParcel(Parcel source) {
            return new JCarBaseInfo(source);
        }

        @Override
        public JCarBaseInfo[] newArray(int size) {
            return new JCarBaseInfo[size];
        }
    };
}
