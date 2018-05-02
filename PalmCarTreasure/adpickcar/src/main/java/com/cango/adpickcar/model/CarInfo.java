package com.cango.adpickcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cango.adpickcar.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/10/13.
 */

public class CarInfo implements Parcelable {

    /**
     * Code : 200
     * Msg : string
     * Data : {"DisCarID":0,"ApplyCD":"","DisCarNo":"","FininstID":"","Vin":"","CustName":"","LicensePlateType":"","LicensePlateNo":"","CarcertNO":"","MfYear":"","EngineNO":"","CarRegNO":"","Color":"","CarModelText":"","IsErpMapping":"","FininstName":"","OrgName":"","CarBrandID":"","CarSeriesID":"","CarModelID":"","CarBrandName":"","CarSeriesName":"","CarModelName":""}
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
         * DisCarID : 0
         * ApplyCD :
         * DisCarNo :
         * FininstID :
         * Vin :
         * CustName :
         * LicensePlateType :
         * LicensePlateNo :
         * CarcertNO :
         * MfYear :
         * EngineNO :
         * CarRegNO :
         * Color :
         * CarModelText :
         * IsErpMapping :
         * RealCarModel : 手输车型
         * FininstName :
         * OrgName :
         * CarBrandID :
         * CarSeriesID :
         * CarModelID :
         * CarBrandName :
         * CarSeriesName :
         * CarModelName :
         */

        private int DisCarID;
        private String ApplyCD;
        private String DisCarNo;
        private String FininstID;
        private String Vin;
        private String CustName;
        private String LicensePlateType;
        private String LicensePlateNo;
        private String CarcertNO;
        private String MfYear;
        private String EngineNO;
        private String CarRegNO;
        private String Color;
        private String CarModelText;
        private String IsErpMapping;
        private String RealCarModel;
        private String FininstName;
        private String OrgName;
        private String CarBrandID;
        private String CarSeriesID;
        private String CarModelID;
        private String CarBrandName;
        private String CarSeriesName;
        private String CarModelName;
        private List<ModelListBean> CarModelList;


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

        public String getDisCarNo() {
            return DisCarNo;
        }

        public void setDisCarNo(String DisCarNo) {
            if (CommUtil.checkIsNull(DisCarNo))
                DisCarNo = "";
            this.DisCarNo = DisCarNo;
        }

        public String getFininstID() {
            return FininstID;
        }

        public void setFininstID(String FininstID) {
            if (CommUtil.checkIsNull(FininstID))
                FininstID = "";
            this.FininstID = FininstID;
        }

        public String getVin() {
            return Vin;
        }

        public void setVin(String Vin) {
            if (CommUtil.checkIsNull(Vin))
                Vin = "";
            this.Vin = Vin;
        }

        public String getCustName() {
            return CustName;
        }

        public void setCustName(String CustName) {
            if (CommUtil.checkIsNull(CustName))
                CustName = "";
            this.CustName = CustName;
        }

        public String getLicensePlateType() {
            return LicensePlateType;
        }

        public void setLicensePlateType(String LicensePlateType) {
            if (CommUtil.checkIsNull(LicensePlateType))
                LicensePlateType = "";
            this.LicensePlateType = LicensePlateType;
        }

        public String getLicensePlateNo() {
            return LicensePlateNo;
        }

        public void setLicensePlateNo(String LicensePlateNo) {
            if (CommUtil.checkIsNull(LicensePlateNo))
                LicensePlateNo = "";
            this.LicensePlateNo = LicensePlateNo;
        }

        public String getCarcertNO() {
            return CarcertNO;
        }

        public void setCarcertNO(String CarcertNO) {
            if (CommUtil.checkIsNull(CarcertNO))
                CarcertNO = "";
            this.CarcertNO = CarcertNO;
        }

        public String getMfYear() {
            return MfYear;
        }

        public void setMfYear(String MfYear) {
            if (CommUtil.checkIsNull(MfYear))
                MfYear = "";
            this.MfYear = MfYear;
        }

        public String getEngineNO() {
            return EngineNO;
        }

        public void setEngineNO(String EngineNO) {
            if (CommUtil.checkIsNull(EngineNO))
                EngineNO = "";
            this.EngineNO = EngineNO;
        }

        public String getCarRegNO() {
            return CarRegNO;
        }

        public void setCarRegNO(String CarRegNO) {
            if (CommUtil.checkIsNull(CarRegNO))
                CarRegNO = "";
            this.CarRegNO = CarRegNO;
        }

        public String getColor() {
            return Color;
        }

        public void setColor(String Color) {
            if (CommUtil.checkIsNull(Color))
                Color = "";
            this.Color = Color;
        }

        public String getCarModelText() {
            return CarModelText;
        }

        public void setCarModelText(String CarModelText) {
            if (CommUtil.checkIsNull(CarModelText))
                CarModelText = "";
            this.CarModelText = CarModelText;
        }

        public String getIsErpMapping() {
            return IsErpMapping;
        }

        public void setIsErpMapping(String IsErpMapping) {
            if (CommUtil.checkIsNull(IsErpMapping))
                IsErpMapping = "";
            this.IsErpMapping = IsErpMapping;
        }

        public String getRealCarModel() {
            return RealCarModel;
        }

        public void setRealCarModel(String RealCarModel) {
            if (CommUtil.checkIsNull(RealCarModel))
                RealCarModel = "";
            this.RealCarModel = RealCarModel;
        }

        public String getFininstName() {
            return FininstName;
        }

        public void setFininstName(String FininstName) {
            if (CommUtil.checkIsNull(FininstName))
                FininstName = "";
            this.FininstName = FininstName;
        }

        public String getOrgName() {
            return OrgName;
        }

        public void setOrgName(String OrgName) {
            if (CommUtil.checkIsNull(OrgName))
                OrgName = "";
            this.OrgName = OrgName;
        }

        public String getCarBrandID() {
            return CarBrandID;
        }

        public void setCarBrandID(String CarBrandID) {
            if (CommUtil.checkIsNull(CarBrandID))
                CarBrandID = "";
            this.CarBrandID = CarBrandID;
        }

        public String getCarSeriesID() {
            return CarSeriesID;
        }

        public void setCarSeriesID(String CarSeriesID) {
            if (CommUtil.checkIsNull(CarSeriesID))
                CarSeriesID = "";
            this.CarSeriesID = CarSeriesID;
        }

        public String getCarModelID() {
            return CarModelID;
        }

        public void setCarModelID(String CarModelID) {
            if (CommUtil.checkIsNull(CarModelID))
                CarModelID = "";
            this.CarModelID = CarModelID;
        }

        public String getCarBrandName() {
            return CarBrandName;
        }

        public void setCarBrandName(String CarBrandName) {
            if (CommUtil.checkIsNull(CarBrandName))
                CarBrandName = "";
            this.CarBrandName = CarBrandName;
        }

        public String getCarSeriesName() {
            return CarSeriesName;
        }

        public void setCarSeriesName(String CarSeriesName) {
            if (CommUtil.checkIsNull(CarSeriesName))
                CarSeriesName = "";
            this.CarSeriesName = CarSeriesName;
        }

        public String getCarModelName() {
            return CarModelName;
        }

        public void setCarModelName(String CarModelName) {
            if (CommUtil.checkIsNull(CarModelName))
                CarModelName = "";
            this.CarModelName = CarModelName;
        }

        public List<ModelListBean> getModelList() {
            return CarModelList;
        }

        public void setModelList(List<ModelListBean> modelList) {
            this.CarModelList = CarModelList;
        }

        public static class ModelListBean implements Parcelable {
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

            public ModelListBean() {
            }

            protected ModelListBean(Parcel in) {
                this.Id = in.readString();
                this.Value = in.readString();
            }

            public static final Parcelable.Creator<ModelListBean> CREATOR = new Parcelable.Creator<ModelListBean>() {
                @Override
                public ModelListBean createFromParcel(Parcel source) {
                    return new ModelListBean(source);
                }

                @Override
                public ModelListBean[] newArray(int size) {
                    return new ModelListBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.DisCarID);
            dest.writeString(this.ApplyCD);
            dest.writeString(this.DisCarNo);
            dest.writeString(this.FininstID);
            dest.writeString(this.Vin);
            dest.writeString(this.CustName);
            dest.writeString(this.LicensePlateType);
            dest.writeString(this.LicensePlateNo);
            dest.writeString(this.CarcertNO);
            dest.writeString(this.MfYear);
            dest.writeString(this.EngineNO);
            dest.writeString(this.CarRegNO);
            dest.writeString(this.Color);
            dest.writeString(this.CarModelText);
            dest.writeString(this.IsErpMapping);
            dest.writeString(this.RealCarModel);
            dest.writeString(this.FininstName);
            dest.writeString(this.OrgName);
            dest.writeString(this.CarBrandID);
            dest.writeString(this.CarSeriesID);
            dest.writeString(this.CarModelID);
            dest.writeString(this.CarBrandName);
            dest.writeString(this.CarSeriesName);
            dest.writeString(this.CarModelName);
            dest.writeTypedList(this.CarModelList);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.DisCarID = in.readInt();
            this.ApplyCD = in.readString();
            this.DisCarNo = in.readString();
            this.FininstID = in.readString();
            this.Vin = in.readString();
            this.CustName = in.readString();
            this.LicensePlateType = in.readString();
            this.LicensePlateNo = in.readString();
            this.CarcertNO = in.readString();
            this.MfYear = in.readString();
            this.EngineNO = in.readString();
            this.CarRegNO = in.readString();
            this.Color = in.readString();
            this.CarModelText = in.readString();
            this.IsErpMapping = in.readString();
            this.RealCarModel = in.readString();
            this.FininstName = in.readString();
            this.OrgName = in.readString();
            this.CarBrandID = in.readString();
            this.CarSeriesID = in.readString();
            this.CarModelID = in.readString();
            this.CarBrandName = in.readString();
            this.CarSeriesName = in.readString();
            this.CarModelName = in.readString();
            this.CarModelList = in.createTypedArrayList(ModelListBean.CREATOR);
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

    public CarInfo() {
    }

    protected CarInfo(Parcel in) {
        this.Code = in.readString();
        this.Msg = in.readString();
        this.Data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CarInfo> CREATOR = new Parcelable.Creator<CarInfo>() {
        @Override
        public CarInfo createFromParcel(Parcel source) {
            return new CarInfo(source);
        }

        @Override
        public CarInfo[] newArray(int size) {
            return new CarInfo[size];
        }
    };
}
