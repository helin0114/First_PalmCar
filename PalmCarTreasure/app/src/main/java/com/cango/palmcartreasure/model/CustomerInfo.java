package com.cango.palmcartreasure.model;

import android.icu.text.UFormat;

import com.cango.palmcartreasure.util.CommUtil;

/**
 * Created by cango on 2017/5/17.
 */

public class CustomerInfo {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"CustInfo":{"customername":"赖芳","sex":null,"paperno":"450702198003164520","mobile":13607862790,"maritalstatus":null,"province":"广西","city":"45","address":"江南区吴圩镇博济街8号","telarea":"0","tel":0,"residenceprovince":"广西","residencecity":"69","residenceaddress":"钦南区龙门港镇北村村委泥万村53号","residencetelarea":null,"residencetel":null,"workprovince":7,"workcity":"45","workaddress":"","workunitname":"","worktelarea":"","worktel":null,"worktelext":""},"MateInfo":{"partnername":null,"partnerpaperno":null}}
     */

    private boolean Success;
    private String Msg;
    private int Code;
    private DataBean Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * CustInfo : {"customername":"赖芳","sex":null,"paperno":"450702198003164520","mobile":13607862790,"maritalstatus":null,"province":"广西","city":"45","address":"江南区吴圩镇博济街8号","telarea":"0","tel":0,"residenceprovince":"广西","residencecity":"69","residenceaddress":"钦南区龙门港镇北村村委泥万村53号","residencetelarea":null,"residencetel":null,"workprovince":7,"workcity":"45","workaddress":"","workunitname":"","worktelarea":"","worktel":null,"worktelext":""}
         * MateInfo : {"partnername":null,"partnerpaperno":null}
         */

        private CustInfoBean CustInfo;
        private MateInfoBean MateInfo;

        public CustInfoBean getCustInfo() {
            return CustInfo;
        }

        public void setCustInfo(CustInfoBean CustInfo) {
            this.CustInfo = CustInfo;
        }

        public MateInfoBean getMateInfo() {
            return MateInfo;
        }

        public void setMateInfo(MateInfoBean MateInfo) {
            this.MateInfo = MateInfo;
        }

        public static class CustInfoBean {
            /**
             * customername : 赖芳
             * sex : null
             * paperno : 450702198003164520
             * mobile : 13607862790
             * maritalstatus : null
             * province : 广西
             * city : 45
             * address : 江南区吴圩镇博济街8号
             * telarea : 0
             * tel : 0
             * residenceprovince : 广西
             * residencecity : 69
             * residenceaddress : 钦南区龙门港镇北村村委泥万村53号
             * residencetelarea : null
             * residencetel : null
             * workprovince : 7
             * workcity : 45
             * workaddress :
             * workunitname :
             * worktelarea :
             * worktel : null
             * worktelext :
             */

            private String customername;
            private String sex;
            private String paperno;
            private String mobile;
            private String maritalstatus;
            private String province;
            private String city;
            private String address;
            private String telarea;
            private String tel;
            private String residenceprovince;
            private String residencecity;
            private String residenceaddress;
            private String residencetelarea;
            private String residencetel;
            private String workprovince;
            private String workcity;
            private String workaddress;
            private String workunitname;
            private String worktelarea;
            private String worktel;
            private String worktelext;

            public String getUrgencyName() {
                return urgencyName;
            }

            public void setUrgencyName(String urgencyName) {
                if (CommUtil.checkIsNull(urgencyName))
                    urgencyName="";
                this.urgencyName = urgencyName;
            }

            public String getUrgencyAddress() {
                return urgencyAddress;
            }

            public void setUrgencyAddress(String urgencyAddress) {
                if (CommUtil.checkIsNull(urgencyAddress))
                    urgencyAddress="";
                this.urgencyAddress = urgencyAddress;
            }

            public String getUrgencyTel() {
                return urgencyTel;
            }

            public void setUrgencyTel(String urgencyTel) {
                if (CommUtil.checkIsNull(urgencyTel))
                    urgencyTel="";
                this.urgencyTel = urgencyTel;
            }

            public String getUrgencyRelation() {
                return urgencyRelation;
            }

            public void setUrgencyRelation(String urgencyRelation) {
                if (CommUtil.checkIsNull(urgencyRelation))
                    urgencyRelation="";
                this.urgencyRelation = urgencyRelation;
            }

            public String getUrgency2Name() {
                return urgency2Name;
            }

            public void setUrgency2Name(String urgency2Name) {
                if (CommUtil.checkIsNull(urgency2Name))
                    urgency2Name="";
                this.urgency2Name = urgency2Name;
            }

            public String getUrgency2Address() {
                return urgency2Address;
            }

            public void setUrgency2Address(String urgency2Address) {
                if (CommUtil.checkIsNull(urgency2Address))
                    urgency2Address="";
                this.urgency2Address = urgency2Address;
            }

            public String getUrgency2Tel() {
                return urgency2Tel;
            }

            public void setUrgency2Tel(String urgency2Tel) {
                if (CommUtil.checkIsNull(urgency2Tel))
                    urgency2Tel="";
                this.urgency2Tel = urgency2Tel;
            }

            public String getUrgency2Relation() {
                return urgency2Relation;
            }

            public void setUrgency2Relation(String urgency2Relation) {
                if (CommUtil.checkIsNull(urgency2Relation))
                    urgency2Relation="";
                this.urgency2Relation = urgency2Relation;
            }

            public String getUrgency3Name() {
                return urgency3Name;
            }

            public void setUrgency3Name(String urgency3Name) {
                if (CommUtil.checkIsNull(urgency3Name))
                    urgency3Name="";
                this.urgency3Name = urgency3Name;
            }

            public String getUrgency3Address() {
                return urgency3Address;
            }

            public void setUrgency3Address(String urgency3Address) {
                if (CommUtil.checkIsNull(urgency3Address))
                    urgency3Address="";
                this.urgency3Address = urgency3Address;
            }

            public String getUrgency3Tel() {
                return urgency3Tel;
            }

            public void setUrgency3Tel(String urgency3Tel) {
                if (CommUtil.checkIsNull(urgency3Tel))
                    urgency3Tel="";
                this.urgency3Tel = urgency3Tel;
            }

            public String getUrgency3Relation() {
                return urgency3Relation;
            }

            public void setUrgency3Relation(String urgency3Relation) {
                if (CommUtil.checkIsNull(urgency3Relation))
                    urgency3Relation="";
                this.urgency3Relation = urgency3Relation;
            }

            //紧急联系人信息
            private String urgencyName;
            private String urgencyAddress;
            private String urgencyTel;
            private String urgencyRelation;
            private String urgency2Name;
            private String urgency2Address;
            private String urgency2Tel;
            private String urgency2Relation;
            private String urgency3Name;
            private String urgency3Address;
            private String urgency3Tel;
            private String urgency3Relation;

            public String getCustomername() {
                return customername;
            }

            public void setCustomername(String customername) {
                if (CommUtil.checkIsNull(customername))
                    customername="";
                this.customername = customername;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                if (CommUtil.checkIsNull(sex))
                    sex="";
                this.sex = sex;
            }

            public String getPaperno() {
                return paperno;
            }

            public void setPaperno(String paperno) {
                if (CommUtil.checkIsNull(paperno))
                    paperno="";
                this.paperno = paperno;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getMaritalstatus() {
                return maritalstatus;
            }

            public void setMaritalstatus(String maritalstatus) {
                if (CommUtil.checkIsNull(maritalstatus))
                    maritalstatus="";
                this.maritalstatus = maritalstatus;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                if (CommUtil.checkIsNull(province))
                    province="";
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                if (CommUtil.checkIsNull(city))
                    city="";
                this.city = city;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                if (CommUtil.checkIsNull(address))
                    address="";
                this.address = address;
            }

            public String getTelarea() {
                return telarea;
            }

            public void setTelarea(String telarea) {
                if (CommUtil.checkIsNull(telarea))
                    telarea="";
                this.telarea = telarea;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getResidenceprovince() {
                return residenceprovince;
            }

            public void setResidenceprovince(String residenceprovince) {
                if (CommUtil.checkIsNull(residenceprovince))
                    residenceprovince="";
                this.residenceprovince = residenceprovince;
            }

            public String getResidencecity() {
                return residencecity;
            }

            public void setResidencecity(String residencecity) {
                if (CommUtil.checkIsNull(residencecity))
                    residencecity="";
                this.residencecity = residencecity;
            }

            public String getResidenceaddress() {
                return residenceaddress;
            }

            public void setResidenceaddress(String residenceaddress) {
                if (CommUtil.checkIsNull(residenceaddress))
                    residenceaddress="";
                this.residenceaddress = residenceaddress;
            }

            public String getResidencetelarea() {
                return residencetelarea;
            }

            public void setResidencetelarea(String residencetelarea) {
                if (CommUtil.checkIsNull(residencetelarea))
                    residencetelarea="";
                this.residencetelarea = residencetelarea;
            }

            public String getResidencetel() {
                return residencetel;
            }

            public void setResidencetel(String residencetel) {
                if (CommUtil.checkIsNull(residencetel))
                    residencetel="";
                this.residencetel = residencetel;
            }

            public String getWorkprovince() {
                return workprovince;
            }

            public void setWorkprovince(String workprovince) {
                this.workprovince = workprovince;
            }

            public String getWorkcity() {
                return workcity;
            }

            public void setWorkcity(String workcity) {
                if (CommUtil.checkIsNull(workcity))
                    workcity="";
                this.workcity = workcity;
            }

            public String getWorkaddress() {
                return workaddress;
            }

            public void setWorkaddress(String workaddress) {
                if (CommUtil.checkIsNull(workaddress))
                    workaddress="";
                this.workaddress = workaddress;
            }

            public String getWorkunitname() {
                return workunitname;
            }

            public void setWorkunitname(String workunitname) {
                if (CommUtil.checkIsNull(workunitname))
                    workunitname="";
                this.workunitname = workunitname;
            }

            public String getWorktelarea() {
                return worktelarea;
            }

            public void setWorktelarea(String worktelarea) {
                if (CommUtil.checkIsNull(worktelarea))
                    worktelarea="";
                this.worktelarea = worktelarea;
            }

            public String getWorktel() {
                return worktel;
            }

            public void setWorktel(String worktel) {
                if (CommUtil.checkIsNull(worktel))
                    worktel="";
                this.worktel = worktel;
            }

            public String getWorktelext() {
                return worktelext;
            }

            public void setWorktelext(String worktelext) {
                if (CommUtil.checkIsNull(worktelext))
                    worktelext="";
                this.worktelext = worktelext;
            }
        }

        public static class MateInfoBean {
            /**
             * partnername : null
             * partnerpaperno : null
             */

            private String partnername;
            private String partnerpaperno;

            public String getPartnername() {
                return partnername;
            }

            public void setPartnername(String partnername) {
                if (CommUtil.checkIsNull(partnername))
                    partnername="";
                this.partnername = partnername;
            }

            public String getPartnerpaperno() {
                return partnerpaperno;
            }

            public void setPartnerpaperno(String partnerpaperno) {
                if (CommUtil.checkIsNull(partnerpaperno))
                    partnerpaperno="";
                this.partnerpaperno = partnerpaperno;
            }
        }
    }
}
