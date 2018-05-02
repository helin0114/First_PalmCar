package com.cango.adpickcar.model;

import com.cango.adpickcar.util.CommUtil;

/**
 * Created by cango on 2017/11/27.
 */

public class JCarInfo {
    /**
     * Code : 200
     * Msg : string
     * Data : {"DisCarID":"处置车辆ID","DisCarNo":"处置车辆编号","ApplyCD":"申请编号","FininstID":"金融公司","OrgName":"区域公司","Vin":"VIN码","CustName":"客户名称","LicensePlateType":"牌照类型","LicensePlateNo":"牌照号码","CarcertNO":"车辆合格证号","MfYear":"出厂年份","EngineNO":"发动机号","CarRegNO":"机动车登记证书号","CarBrandName":"品牌","CarSeriesName":"车系","CarModelName":"车型","Color":"颜色","CarModelText":"车型描述","CarGrade":"配置","CarDISPLACEMENT":"排量","GUIDEPrice":"厂商指导价","InvoicePrice":"开票价","InvoiceDate":"开票日期","InvoiceNO":"发票号码","PurchaseTax":"购置税","LicenseRegDate":"上牌日期","InsurancePONO":"保单号码","InsuranceENDDate":"保单到期日","InsuranceCO":"保险公司","InspectionDate":"最近年检日期","EVAPrice":"车辆评估价","WHName":"库点","WHPosition":"库位","StockStateTitle":"库存状态","CarPropertyTitle":"车辆属性","CarStateTitle":"车辆状态","CarOwnershipTitle":"资产所有权"}
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
         * DisCarID : 处置车辆ID
         * DisCarNo : 处置车辆编号
         * ApplyCD : 申请编号
         * FininstID : 金融公司ID
         * FininstName : 金融公司名称
         * OrgName : 区域公司
         * Vin : VIN码
         * CustName : 客户名称
         * LicensePlateType : 牌照类型
         * LicensePlateNo : 牌照号码
         * CarcertNO : 车辆合格证号
         * MfYear : 出厂年份
         * EngineNO : 发动机号
         * CarRegNO : 机动车登记证书号
         * CarBrandName : 品牌
         * CarSeriesName : 车系
         * CarModelName : 车型
         * Color : 颜色
         * CarModelText : 车型描述
         * CarGrade : 配置
         * CarDISPLACEMENT : 排量
         * GUIDEPrice : 厂商指导价
         * InvoicePrice : 开票价
         * InvoiceDate : 开票日期
         * InvoiceNO : 发票号码
         * PurchaseTax : 购置税
         * LicenseRegDate : 上牌日期
         * InsurancePONO : 保单号码
         * InsuranceENDDate : 保单到期日
         * InsuranceCO : 保险公司
         * InspectionDate : 最近年检日期
         * EVAPrice : 车辆评估价
         * WHName : 库点
         * WHPosition : 库位
         * StockStateTitle : 库存状态
         * CarPropertyTitle : 车辆属性
         * CarStateTitle : 车辆状态
         * CarOwnershipTitle : 资产所有权
         * IsErpMapping : 是否与ERP车型相同，"0"不同，"1"相同
         * RealCarModel : 手输车型
         */

        private String DisCarID;
        private String DisCarNo;
        private String ApplyCD;
        private String FininstID;
        private String FininstName;
        private String OrgName;
        private String Vin;
        private String CustName;
        private String LicensePlateType;
        private String LicensePlateNo;
        private String CarcertNO;
        private String MfYear;
        private String EngineNO;
        private String CarRegNO;
        private String CarBrandName;
        private String CarSeriesName;
        private String CarModelName;
        private String Color;
        private String CarModelText;
        private String CarGrade;
        private String CarDISPLACEMENT;
        private String GUIDEPrice;
        private String InvoicePrice;
        private String InvoiceDate;
        private String InvoiceNO;
        private String PurchaseTax;
        private String LicenseRegDate;
        private String InsurancePONO;
        private String InsuranceENDDate;
        private String InsuranceCO;
        private String InspectionDate;
        private String EVAPrice;
        private String WHName;
        private String WHPosition;
        private String StockStateTitle;
        private String CarPropertyTitle;
        private String CarStateTitle;
        private String CarOwnershipTitle;
        private String IsErpMapping;
        private String RealCarModel;

        public String getDisCarID() {
            return DisCarID;
        }

        public void setDisCarID(String DisCarID) {
            if (CommUtil.checkIsNull(DisCarID))
                DisCarID = "";
            this.DisCarID = DisCarID;
        }

        public String getDisCarNo() {
            return DisCarNo;
        }

        public void setDisCarNo(String DisCarNo) {
            if (CommUtil.checkIsNull(DisCarNo))
                DisCarNo = "";
            this.DisCarNo = DisCarNo;
        }

        public String getApplyCD() {
            return ApplyCD;
        }

        public void setApplyCD(String ApplyCD) {
            if (CommUtil.checkIsNull(ApplyCD))
                ApplyCD = "";
            this.ApplyCD = ApplyCD;
        }

        public String getFininstID() {
            return FininstID;
        }

        public void setFininstID(String FininstID) {
            if (CommUtil.checkIsNull(FininstID))
                FininstID = "";
            this.FininstID = FininstID;
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

        public String getCarGrade() {
            return CarGrade;
        }

        public void setCarGrade(String CarGrade) {
            if (CommUtil.checkIsNull(CarGrade))
                CarGrade = "";
            this.CarGrade = CarGrade;
        }

        public String getCarDISPLACEMENT() {
            return CarDISPLACEMENT;
        }

        public void setCarDISPLACEMENT(String CarDISPLACEMENT) {
            if (CommUtil.checkIsNull(CarDISPLACEMENT))
                CarDISPLACEMENT = "";
            this.CarDISPLACEMENT = CarDISPLACEMENT;
        }

        public String getGUIDEPrice() {
            return GUIDEPrice;
        }

        public void setGUIDEPrice(String GUIDEPrice) {
            if (CommUtil.checkIsNull(GUIDEPrice))
                GUIDEPrice = "";
            this.GUIDEPrice = GUIDEPrice;
        }

        public String getInvoicePrice() {
            return InvoicePrice;
        }

        public void setInvoicePrice(String InvoicePrice) {
            if (CommUtil.checkIsNull(InvoicePrice))
                InvoicePrice = "";
            this.InvoicePrice = InvoicePrice;
        }

        public String getInvoiceDate() {
            return InvoiceDate;
        }

        public void setInvoiceDate(String InvoiceDate) {
            if (CommUtil.checkIsNull(InvoiceDate))
                InvoiceDate = "";
            this.InvoiceDate = InvoiceDate;
        }

        public String getInvoiceNO() {
            return InvoiceNO;
        }

        public void setInvoiceNO(String InvoiceNO) {
            if (CommUtil.checkIsNull(InvoiceNO))
                InvoiceNO = "";
            this.InvoiceNO = InvoiceNO;
        }

        public String getPurchaseTax() {
            return PurchaseTax;
        }

        public void setPurchaseTax(String PurchaseTax) {
            if (CommUtil.checkIsNull(PurchaseTax))
                PurchaseTax = "";
            this.PurchaseTax = PurchaseTax;
        }

        public String getLicenseRegDate() {
            return LicenseRegDate;
        }

        public void setLicenseRegDate(String LicenseRegDate) {
            if (CommUtil.checkIsNull(LicenseRegDate))
                LicenseRegDate = "";
            this.LicenseRegDate = LicenseRegDate;
        }

        public String getInsurancePONO() {
            return InsurancePONO;
        }

        public void setInsurancePONO(String InsurancePONO) {
            if (CommUtil.checkIsNull(InsurancePONO))
                InsurancePONO = "";
            this.InsurancePONO = InsurancePONO;
        }

        public String getInsuranceENDDate() {
            return InsuranceENDDate;
        }

        public void setInsuranceENDDate(String InsuranceENDDate) {
            if (CommUtil.checkIsNull(InsuranceENDDate))
                InsuranceENDDate = "";
            this.InsuranceENDDate = InsuranceENDDate;
        }

        public String getInsuranceCO() {
            return InsuranceCO;
        }

        public void setInsuranceCO(String InsuranceCO) {
            if (CommUtil.checkIsNull(InsuranceCO))
                InsuranceCO = "";
            this.InsuranceCO = InsuranceCO;
        }

        public String getInspectionDate() {
            return InspectionDate;
        }

        public void setInspectionDate(String InspectionDate) {
            if (CommUtil.checkIsNull(InspectionDate))
                InspectionDate = "";
            this.InspectionDate = InspectionDate;
        }

        public String getEVAPrice() {
            return EVAPrice;
        }

        public void setEVAPrice(String EVAPrice) {
            if (CommUtil.checkIsNull(EVAPrice))
                EVAPrice = "";
            this.EVAPrice = EVAPrice;
        }

        public String getWHName() {
            return WHName;
        }

        public void setWHName(String WHName) {
            if (CommUtil.checkIsNull(WHName))
                WHName = "";
            this.WHName = WHName;
        }

        public String getWHPosition() {
            return WHPosition;
        }

        public void setWHPosition(String WHPosition) {
            if (CommUtil.checkIsNull(WHPosition))
                WHPosition = "";
            this.WHPosition = WHPosition;
        }

        public String getStockStateTitle() {
            return StockStateTitle;
        }

        public void setStockStateTitle(String StockStateTitle) {
            if (CommUtil.checkIsNull(StockStateTitle))
                StockStateTitle = "";
            this.StockStateTitle = StockStateTitle;
        }

        public String getCarPropertyTitle() {
            return CarPropertyTitle;
        }

        public void setCarPropertyTitle(String CarPropertyTitle) {
            if (CommUtil.checkIsNull(CarPropertyTitle))
                CarPropertyTitle = "";
            this.CarPropertyTitle = CarPropertyTitle;
        }

        public String getCarStateTitle() {
            return CarStateTitle;
        }

        public void setCarStateTitle(String CarStateTitle) {
            if (CommUtil.checkIsNull(CarStateTitle))
                CarStateTitle = "";
            this.CarStateTitle = CarStateTitle;
        }

        public String getCarOwnershipTitle() {
            return CarOwnershipTitle;
        }

        public void setCarOwnershipTitle(String CarOwnershipTitle) {
            if (CommUtil.checkIsNull(CarOwnershipTitle))
                CarOwnershipTitle = "";
            this.CarOwnershipTitle = CarOwnershipTitle;
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
    }
}
