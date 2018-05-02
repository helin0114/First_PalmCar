package com.cango.adpickcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.adpickcar.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/10/12.
 */

public class CarTakeTaskList {

    /**
     * Code : 200
     * Msg : string
     * Data : {"NoTakeCarCount":12,"NoCommitCount":12,"CommitCount":12,"ApproveCount":12,"ApproveBackCount":12,"NextPage":0,"CarTakeTaskList":[{"CTTaskID":2,"DisCarID":1,"CTSID":1,"CarID":1,"ApplyCD":"string","CustName":"string","CTMan":"string","CTMobile":"string","CTPlace":"string","PlanctWhno":"string","LicenseplateNO":"string","Vin":"string","Color":"string","CarBrandName":"string","CarBrandPicURL":"string","ReturnReason":"string"}]}
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
         * NoTakeCarCount : 12
         * NoCommitCount : 12
         * CommitCount : 12
         * ApproveCount : 12
         * ApproveBackCount : 12
         * NextPage : 0
         * CarTakeTaskList : [{"CTTaskID":2,"DisCarID":1,"CTSID":1,"CarID":1,"ApplyCD":"string","CustName":"string","CTMan":"string","CTMobile":"string","CTPlace":"string","PlanctWhno":"string","LicenseplateNO":"string","Vin":"string","Color":"string","CarBrandName":"string","CarBrandPicURL":"string","ReturnReason":"string"}]
         */

        private int NoTakeCarCount;
        private int NoCommitCount;
        private int CommitCount;
        private int ApproveCount;
        private int ApproveBackCount;
        private int NextPage;
        private List<CarTakeTaskListBean> CarTakeTaskList;

        public int getNoTakeCarCount() {
            return NoTakeCarCount;
        }

        public void setNoTakeCarCount(int NoTakeCarCount) {
            this.NoTakeCarCount = NoTakeCarCount;
        }

        public int getNoCommitCount() {
            return NoCommitCount;
        }

        public void setNoCommitCount(int NoCommitCount) {
            this.NoCommitCount = NoCommitCount;
        }

        public int getCommitCount() {
            return CommitCount;
        }

        public void setCommitCount(int CommitCount) {
            this.CommitCount = CommitCount;
        }

        public int getApproveCount() {
            return ApproveCount;
        }

        public void setApproveCount(int ApproveCount) {
            this.ApproveCount = ApproveCount;
        }

        public int getApproveBackCount() {
            return ApproveBackCount;
        }

        public void setApproveBackCount(int ApproveBackCount) {
            this.ApproveBackCount = ApproveBackCount;
        }

        public int getNextPage() {
            return NextPage;
        }

        public void setNextPage(int NextPage) {
            this.NextPage = NextPage;
        }

        public List<CarTakeTaskListBean> getCarTakeTaskList() {
            return CarTakeTaskList;
        }

        public void setCarTakeTaskList(List<CarTakeTaskListBean> CarTakeTaskList) {
            this.CarTakeTaskList = CarTakeTaskList;
        }

        public static class CarTakeTaskListBean implements Parcelable {
            /**
             * CTTaskID : 2
             * DisCarID : 1
             * CTSID : 1
             * CarID : 1
             * ApplyCD : string
             * CustName : string
             * CTMan : string
             * CTMobile : string
             * CTPlace : string
             * PlanctWhno : string
             * LicenseplateNO : string
             * Vin : string
             * Color : string
             * CarBrandName : string
             * CarBrandPicURL : string
             * ReturnReason : string
             */

            private int CTTaskID;
            private int DisCarID;
            private int CTSID;
            private int CarID;
            private String ApplyCD;
            private String CustName;
            private String CTMan;
            private String CTMobile;
            private String CTPlace;
            private String PlanctWhno;
            private String LicensePlateNo;
            private String Vin;
            private String Color;
            private String CarBrandName;
            private String CarBrandPicURL;
            private String ReturnReason;
            private String TowingcoShortName;
            private String OperTime;
            private String CarModelName;

            public String getCarModelName() {
                return CarModelName;
            }

            public void setCarModelName(String carModelName) {
                if (CommUtil.checkIsNull(carModelName))
                    carModelName = "";
                CarModelName = carModelName;
            }

            public String getOperTime() {
                return OperTime;
            }

            public void setOperTime(String operTime) {
                if (CommUtil.checkIsNull(operTime))
                    operTime = "";
                OperTime = operTime;
            }

            public String getTowingcoShortName() {
                return TowingcoShortName;
            }

            public void setTowingcoShortName(String towingcoShortName) {
                if (CommUtil.checkIsNull(towingcoShortName))
                    towingcoShortName = "";
                TowingcoShortName = towingcoShortName;
            }

            public int getCTTaskID() {
                return CTTaskID;
            }

            public void setCTTaskID(int CTTaskID) {
                this.CTTaskID = CTTaskID;
            }

            public int getDisCarID() {
                return DisCarID;
            }

            public void setDisCarID(int DisCarID) {
                this.DisCarID = DisCarID;
            }

            public int getCTSID() {
                return CTSID;
            }

            public void setCTSID(int CTSID) {
                this.CTSID = CTSID;
            }

            public int getCarID() {
                return CarID;
            }

            public void setCarID(int CarID) {
                this.CarID = CarID;
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

            public String getCTMan() {
                return CTMan;
            }

            public void setCTMan(String CTMan) {
                if (CommUtil.checkIsNull(CTMan))
                    CTMan = "";
                this.CTMan = CTMan;
            }

            public String getCTMobile() {
                return CTMobile;
            }

            public void setCTMobile(String CTMobile) {
                if (CommUtil.checkIsNull(CTMobile))
                    CTMobile = "";
                this.CTMobile = CTMobile;
            }

            public String getCTPlace() {
                return CTPlace;
            }

            public void setCTPlace(String CTPlace) {
                if (CommUtil.checkIsNull(CTPlace))
                    CTPlace = "";
                this.CTPlace = CTPlace;
            }

            public String getPlanctWhno() {
                return PlanctWhno;
            }

            public void setPlanctWhno(String PlanctWhno) {
                if (CommUtil.checkIsNull(PlanctWhno))
                    PlanctWhno = "";
                this.PlanctWhno = PlanctWhno;
            }

            public String getLicenseplateNO() {
                return LicensePlateNo;
            }

            public void setLicenseplateNO(String licenseplateNO) {
                if (CommUtil.checkIsNull(licenseplateNO)) {
                    licenseplateNO = "";
                }
                this.LicensePlateNo = licenseplateNO;
            }

            public String getVin() {
                return Vin;
            }

            public void setVin(String Vin) {
                if (CommUtil.checkIsNull(Vin))
                    Vin = "";
                this.Vin = Vin;
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

            public String getReturnReason() {
                return ReturnReason;
            }

            public void setReturnReason(String ReturnReason) {
                if (CommUtil.checkIsNull(ReturnReason))
                    ReturnReason = "";
                this.ReturnReason = ReturnReason;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.CTTaskID);
                dest.writeInt(this.DisCarID);
                dest.writeInt(this.CTSID);
                dest.writeInt(this.CarID);
                dest.writeString(this.ApplyCD);
                dest.writeString(this.CustName);
                dest.writeString(this.CTMan);
                dest.writeString(this.CTMobile);
                dest.writeString(this.CTPlace);
                dest.writeString(this.PlanctWhno);
                dest.writeString(this.LicensePlateNo);
                dest.writeString(this.Vin);
                dest.writeString(this.Color);
                dest.writeString(this.CarBrandName);
                dest.writeString(this.CarBrandPicURL);
                dest.writeString(this.ReturnReason);
                dest.writeString(this.TowingcoShortName);
                dest.writeString(this.OperTime);
                dest.writeString(this.CarModelName);
            }

            public CarTakeTaskListBean() {
            }

            protected CarTakeTaskListBean(Parcel in) {
                this.CTTaskID = in.readInt();
                this.DisCarID = in.readInt();
                this.CTSID = in.readInt();
                this.CarID = in.readInt();
                this.ApplyCD = in.readString();
                this.CustName = in.readString();
                this.CTMan = in.readString();
                this.CTMobile = in.readString();
                this.CTPlace = in.readString();
                this.PlanctWhno = in.readString();
                this.LicensePlateNo = in.readString();
                this.Vin = in.readString();
                this.Color = in.readString();
                this.CarBrandName = in.readString();
                this.CarBrandPicURL = in.readString();
                this.ReturnReason = in.readString();
                this.TowingcoShortName = in.readString();
                this.OperTime = in.readString();
                this.CarModelName = in.readString();
            }

            public static final Parcelable.Creator<CarTakeTaskListBean> CREATOR = new Parcelable.Creator<CarTakeTaskListBean>() {
                @Override
                public CarTakeTaskListBean createFromParcel(Parcel source) {
                    return new CarTakeTaskListBean(source);
                }

                @Override
                public CarTakeTaskListBean[] newArray(int size) {
                    return new CarTakeTaskListBean[size];
                }
            };
        }
    }
}
