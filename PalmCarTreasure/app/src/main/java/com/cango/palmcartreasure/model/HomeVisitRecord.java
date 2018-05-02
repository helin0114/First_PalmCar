package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/5/17.
 */

public class HomeVisitRecord {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : [[{"visitTime":"2016/5/27 0:00:00","visitUser":"汪华业","redueTrueReason":"1：客户居住地与资料相符。绵阳游仙区民安路158号附6号，自建房，客户名下。\r\n2：客户工作单位与资料相符，绵阳游仙区民安路158号附6号，公司就在自己家里。\r\n3：家访见到客户，客户正在绵阳市区一酒店搞改装施工，跟朋友承包的，客户表示包工资金优点紧张，6月5号前一定还款。保证以后不再逾期。\r\n4：车辆GPS正常，家访加装GPS10036943，\r\n ，"}]]
     */

    private boolean Success;
    private String Msg;
    private int Code;
    private List<List<DataBean>> Data;

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

    public List<List<DataBean>> getData() {
        return Data;
    }

    public void setData(List<List<DataBean>> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * visitTime : 2016/5/27 0:00:00
         * visitUser : 汪华业
         * redueTrueReason : 1：客户居住地与资料相符。绵阳游仙区民安路158号附6号，自建房，客户名下。
         2：客户工作单位与资料相符，绵阳游仙区民安路158号附6号，公司就在自己家里。
         3：家访见到客户，客户正在绵阳市区一酒店搞改装施工，跟朋友承包的，客户表示包工资金优点紧张，6月5号前一定还款。保证以后不再逾期。
         4：车辆GPS正常，家访加装GPS10036943，
         ，
         */

        private String visitTime;
        private String visitUser;
        private String redueTrueReason;

        public String getVisitTime() {
            return visitTime;
        }

        public void setVisitTime(String visitTime) {
            if (CommUtil.checkIsNull(visitTime))
                visitTime="";
            this.visitTime = visitTime;
        }

        public String getVisitUser() {
            return visitUser;
        }

        public void setVisitUser(String visitUser) {
            if (CommUtil.checkIsNull(visitUser))
                visitUser="";
            this.visitUser = visitUser;
        }

        public String getRedueTrueReason() {
            return redueTrueReason;
        }

        public void setRedueTrueReason(String redueTrueReason) {
            if (CommUtil.checkIsNull(redueTrueReason))
                redueTrueReason="";
            this.redueTrueReason = redueTrueReason;
        }
    }
}
