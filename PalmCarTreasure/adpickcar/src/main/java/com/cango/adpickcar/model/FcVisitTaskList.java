package com.cango.adpickcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.adpickcar.util.CommUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务列表数据类型
 */

public class FcVisitTaskList implements Parcelable {
    private String Code;
    private String Msg;
    private FcVisitTaskBean Data;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public FcVisitTaskBean getData() {
        return Data;
    }

    public void setData(FcVisitTaskBean data) {
        Data = data;
    }

    public static class FcVisitTaskBean implements Parcelable {
        private List<TaskListBean> TaskList;
        private int nextPage;

        public List<TaskListBean> getTaskList() {
            return TaskList;
        }

        public void setTaskList(List<TaskListBean> taskList) {
            TaskList = taskList;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.TaskList);
            dest.writeInt(this.nextPage);
        }

        public FcVisitTaskBean() {
        }

        protected FcVisitTaskBean(Parcel in) {
            this.TaskList = new ArrayList<TaskListBean>();
            in.readList(this.TaskList, TaskListBean.class.getClassLoader());
            this.nextPage = in.readInt();
        }

        public static final Creator<FcVisitTaskBean> CREATOR = new Creator<FcVisitTaskBean>() {
            @Override
            public FcVisitTaskBean createFromParcel(Parcel source) {
                return new FcVisitTaskBean(source);
            }

            @Override
            public FcVisitTaskBean[] newArray(int size) {
                return new FcVisitTaskBean[size];
            }
        };
    }
    public static class TaskListBean implements Parcelable {
        /**
         * ApplyID : string          申请ID
         * ApplyCD : string          申请编号
         * CaseID : string           案件ID
         * VisitID : string          家访ID
         * CustomerFlg : string      客户类型 1：个人 2：机构
         * datasource : string       数据来源 1：ERP 2:CSWB
         * LegalID : string          法务ID
         * AgencyID : string         拖车ID
         * DueTerms : string         逾期期数
         * OverDueDays : string      逾期天数
         * DueAmount : string        逾期总金额
         * CustomerName : string     客户名称
         * LicensePlateNO : string   车牌号码
         * Distance : string         距离车辆距离
         * FininstName : string      金融机构名称
         * CallFlg : string          是否显示电催  0：不显示 1：显示
         * CallFlgName : string      电催显示名称
         * VisitFlg : string         是否显示家访  0：不显示 1：显示
         * VisitFlgName : string     家访显示名称
         * AgencyFlg : string        是否显示拖车  0：不显示 1：显示
         * AgencyFlgName : string    拖车显示名称
         * LegalFlg : string         是否显示法务  0：不显示 1：显示
         * LegalFlgName : string     法务显示名称
         * BankType : string         银行类型
         * FinImei : "1698012628",
         * FinImei2 : ""
         */
        private String ApplyID;
        private String ApplyCD;
        private String CaseID;
        private String VisitID;
        private String CustomerFlg;
        private String datasource;
        private String LegalID;
        private String AgencyID;
        private String DueTerms;
        private String OverDueDays;
        private String DueAmount;
        private String CustomerName;
        private String LicensePlateNO;
        private String Distance;
        private String FininstName;
        private String CallFlg;
        private String CallFlgName;
        private String VisitFlg;
        private String VisitFlgName;
        private String AgencyFlg;
        private String AgencyFlgName;
        private String LegalFlg;
        private String LegalFlgName;
        private String BankType;
        private String FinImei;
        private String FinImei2;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getFinImei() {
            return FinImei;
        }

        public void setFinImei(String finImei) {
            if (CommUtil.checkIsNull(finImei))
                finImei = "";
            FinImei = finImei;
        }

        public String getFinImei2() {
            return FinImei2;
        }

        public void setFinImei2(String finImei2) {
            if (CommUtil.checkIsNull(finImei2))
                finImei2 = "";
            FinImei2 = finImei2;
        }

        public String getApplyID() {
            return ApplyID;
        }

        public void setApplyID(String applyID) {
            if (CommUtil.checkIsNull(applyID))
                applyID = "";
            ApplyID = applyID;
        }

        public String getApplyCD() {
            return ApplyCD;
        }

        public void setApplyCD(String applyCD) {
            if (CommUtil.checkIsNull(applyCD))
                applyCD = "";
            ApplyCD = applyCD;
        }

        public String getCaseID() {
            return CaseID;
        }

        public void setCaseID(String caseID) {
            if (CommUtil.checkIsNull(caseID))
                caseID = "";
            CaseID = caseID;
        }

        public String getVisitID() {
            return VisitID;
        }

        public void setVisitID(String visitID) {
            if (CommUtil.checkIsNull(visitID))
                visitID = "";
            VisitID = visitID;
        }

        public String getCustomerFlg() {
            return CustomerFlg;
        }

        public void setCustomerFlg(String customerFlg) {
            if (CommUtil.checkIsNull(customerFlg))
                customerFlg = "";
            CustomerFlg = customerFlg;
        }

        public String getDatasource() {
            return datasource;
        }

        public void setDatasource(String datasource) {
            if (CommUtil.checkIsNull(datasource))
                datasource = "";
            this.datasource = datasource;
        }

        public String getLegalID() {
            return LegalID;
        }

        public void setLegalID(String legalID) {
            if (CommUtil.checkIsNull(legalID))
                legalID = "";
            LegalID = legalID;
        }

        public String getAgencyID() {
            return AgencyID;
        }

        public void setAgencyID(String agencyID) {
            if (CommUtil.checkIsNull(agencyID))
                agencyID = "";
            AgencyID = agencyID;
        }

        public String getDueTerms() {
            return DueTerms;
        }

        public void setDueTerms(String dueTerms) {
            if (CommUtil.checkIsNull(dueTerms))
                dueTerms = "";
            DueTerms = dueTerms;
        }

        public String getOverDueDays() {
            return OverDueDays;
        }

        public void setOverDueDays(String overDueDays) {
            if (CommUtil.checkIsNull(overDueDays))
                overDueDays = "";
            OverDueDays = overDueDays;
        }

        public String getDueAmount() {
            return DueAmount;
        }

        public void setDueAmount(String dueAmount) {
            if (CommUtil.checkIsNull(dueAmount))
                dueAmount = "";
            DueAmount = dueAmount;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            if (CommUtil.checkIsNull(customerName))
                customerName = "";
            CustomerName = customerName;
        }

        public String getLicensePlateNO() {
            return LicensePlateNO;
        }

        public void setLicensePlateNO(String licensePlateNO) {
            if (CommUtil.checkIsNull(licensePlateNO))
                licensePlateNO = "";
            LicensePlateNO = licensePlateNO;
        }

        public String getDistance() {
            return Distance;
        }

        public void setDistance(String distance) {
            if (CommUtil.checkIsNull(distance))
                distance = "";
            Distance = distance;
        }

        public String getFininstName() {
            return FininstName;
        }

        public void setFininstName(String fininstName) {
            if (CommUtil.checkIsNull(fininstName))
                fininstName = "";
            FininstName = fininstName;
        }

        public String getCallFlg() {
            return CallFlg;
        }

        public void setCallFlg(String callFlg) {
            if (CommUtil.checkIsNull(callFlg))
                callFlg = "";
            CallFlg = callFlg;
        }

        public String getCallFlgName() {
            return CallFlgName;
        }

        public void setCallFlgName(String callFlgName) {
            if (CommUtil.checkIsNull(callFlgName))
                callFlgName = "";
            CallFlgName = callFlgName;
        }

        public String getVisitFlg() {
            return VisitFlg;
        }

        public void setVisitFlg(String visitFlg) {
            if (CommUtil.checkIsNull(visitFlg))
                visitFlg = "";
            VisitFlg = visitFlg;
        }

        public String getVisitFlgName() {
            return VisitFlgName;
        }

        public void setVisitFlgName(String visitFlgName) {
            if (CommUtil.checkIsNull(visitFlgName))
                visitFlgName = "";
            VisitFlgName = visitFlgName;
        }

        public String getAgencyFlg() {
            return AgencyFlg;
        }

        public void setAgencyFlg(String agencyFlg) {
            if (CommUtil.checkIsNull(agencyFlg))
                agencyFlg = "";
            AgencyFlg = agencyFlg;
        }

        public String getAgencyFlgName() {
            return AgencyFlgName;
        }

        public void setAgencyFlgName(String agencyFlgName) {
            if (CommUtil.checkIsNull(agencyFlgName))
                agencyFlgName = "";
            AgencyFlgName = agencyFlgName;
        }

        public String getLegalFlg() {
            return LegalFlg;
        }

        public void setLegalFlg(String legalFlg) {
            if (CommUtil.checkIsNull(legalFlg))
                legalFlg = "";
            LegalFlg = legalFlg;
        }

        public String getLegalFlgName() {
            return LegalFlgName;
        }

        public void setLegalFlgName(String legalFlgName) {
            if (CommUtil.checkIsNull(legalFlgName))
                legalFlgName = "";
            LegalFlgName = legalFlgName;
        }

        public String getBankType() {
            return BankType;
        }

        public void setBankType(String bankType) {
            if (CommUtil.checkIsNull(bankType))
                bankType = "";
            BankType = bankType;
        }

        public TaskListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ApplyID);
            dest.writeString(this.ApplyCD);
            dest.writeString(this.CaseID);
            dest.writeString(this.VisitID);
            dest.writeString(this.CustomerFlg);
            dest.writeString(this.datasource);
            dest.writeString(this.LegalID);
            dest.writeString(this.AgencyID);
            dest.writeString(this.DueTerms);
            dest.writeString(this.OverDueDays);
            dest.writeString(this.DueAmount);
            dest.writeString(this.CustomerName);
            dest.writeString(this.LicensePlateNO);
            dest.writeString(this.Distance);
            dest.writeString(this.FininstName);
            dest.writeString(this.CallFlg);
            dest.writeString(this.CallFlgName);
            dest.writeString(this.VisitFlg);
            dest.writeString(this.VisitFlgName);
            dest.writeString(this.AgencyFlg);
            dest.writeString(this.AgencyFlgName);
            dest.writeString(this.LegalFlg);
            dest.writeString(this.LegalFlgName);
            dest.writeString(this.BankType);
            dest.writeString(this.FinImei);
            dest.writeString(this.FinImei2);
            dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        }

        protected TaskListBean(Parcel in) {
            this.ApplyID = in.readString();
            this.ApplyCD = in.readString();
            this.CaseID = in.readString();
            this.VisitID = in.readString();
            this.CustomerFlg = in.readString();
            this.datasource = in.readString();
            this.LegalID = in.readString();
            this.AgencyID = in.readString();
            this.DueTerms = in.readString();
            this.OverDueDays = in.readString();
            this.DueAmount = in.readString();
            this.CustomerName = in.readString();
            this.LicensePlateNO = in.readString();
            this.Distance = in.readString();
            this.FininstName = in.readString();
            this.CallFlg = in.readString();
            this.CallFlgName = in.readString();
            this.VisitFlg = in.readString();
            this.VisitFlgName = in.readString();
            this.AgencyFlg = in.readString();
            this.AgencyFlgName = in.readString();
            this.LegalFlg = in.readString();
            this.LegalFlgName = in.readString();
            this.BankType = in.readString();
            this.FinImei = in.readString();
            this.FinImei2 = in.readString();
            this.isChecked = in.readByte() != 0;
        }

        public static final Creator<TaskListBean> CREATOR = new Creator<TaskListBean>() {
            @Override
            public TaskListBean createFromParcel(Parcel source) {
                return new TaskListBean(source);
            }

            @Override
            public TaskListBean[] newArray(int size) {
                return new TaskListBean[size];
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

    public FcVisitTaskList() {
    }

    protected FcVisitTaskList(Parcel in) {
        this.Code = in.readString();
        this.Msg = in.readString();
        this.Data = in.readParcelable(FcVisitTaskBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<FcVisitTaskList> CREATOR = new Parcelable.Creator<FcVisitTaskList>() {
        @Override
        public FcVisitTaskList createFromParcel(Parcel source) {
            return new FcVisitTaskList(source);
        }

        @Override
        public FcVisitTaskList[] newArray(int size) {
            return new FcVisitTaskList[size];
        }
    };
}
