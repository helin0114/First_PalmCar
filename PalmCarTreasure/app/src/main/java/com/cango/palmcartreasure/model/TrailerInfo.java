package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/6/23.
 */

public class TrailerInfo {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"trailerInfo":{"agencyID":"549","isFirstAssign":"0","entrustDate":"2015-08-28 00:00","result":null,"taskDate":"","agencyComment":null,"callbackReason":null,"assignComment":null},"trailerRemarks":[{"remarkUser":"系统管理员","remarkContent":"æ\u008b\u0096æ\u0088\u0090æ\u0088\u0090å\u008a\u009f","remarkTime":"2017-05-26 19:13"},{"remarkUser":"系统管理员","remarkContent":"æ\u008b\u0096æ\u0088\u0090æ\u0088\u0090å\u008a\u009f","remarkTime":"2017-05-26 20:50"},{"remarkUser":"系统管理员","remarkContent":"è\u0087ªå·±æ\u0094¾å¼\u0083","remarkTime":"2017-05-31 16:11"}]}
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
         * trailerInfo : {"agencyID":"549","isFirstAssign":"0","entrustDate":"2015-08-28 00:00","result":null,"taskDate":"","agencyComment":null,"callbackReason":null,"assignComment":null}
         * trailerRemarks : [{"remarkUser":"系统管理员","remarkContent":"æ\u008b\u0096æ\u0088\u0090æ\u0088\u0090å\u008a\u009f","remarkTime":"2017-05-26 19:13"},{"remarkUser":"系统管理员","remarkContent":"æ\u008b\u0096æ\u0088\u0090æ\u0088\u0090å\u008a\u009f","remarkTime":"2017-05-26 20:50"},{"remarkUser":"系统管理员","remarkContent":"è\u0087ªå·±æ\u0094¾å¼\u0083","remarkTime":"2017-05-31 16:11"}]
         */

        private TrailerInfoBean trailerInfo;
        private List<TrailerRemarksBean> trailerRemarks;

        public TrailerInfoBean getTrailerInfo() {
            return trailerInfo;
        }

        public void setTrailerInfo(TrailerInfoBean trailerInfo) {
            this.trailerInfo = trailerInfo;
        }

        public List<TrailerRemarksBean> getTrailerRemarks() {
            return trailerRemarks;
        }

        public void setTrailerRemarks(List<TrailerRemarksBean> trailerRemarks) {
            this.trailerRemarks = trailerRemarks;
        }

        public static class TrailerInfoBean {
            /**
             * agencyID : 549
             * isFirstAssign : 0
             * entrustDate : 2015-08-28 00:00
             * result : null
             * taskDate :
             * agencyComment : null
             * callbackReason : null
             * assignComment : null
             */

            private String agencyID;
            private String isFirstAssign;
            private String entrustDate;
            private String result;
            private String taskDate;
            private String agencyComment;
            private String callbackReason;
            private String assignComment;

            public String getAgencyID() {
                return agencyID;
            }

            public void setAgencyID(String agencyID) {
                if (CommUtil.checkIsNull(agencyID))
                    agencyID="";
                this.agencyID = agencyID;
            }

            public String getIsFirstAssign() {
                return isFirstAssign;
            }

            public void setIsFirstAssign(String isFirstAssign) {
                if (CommUtil.checkIsNull(isFirstAssign))
                    isFirstAssign="";
                this.isFirstAssign = isFirstAssign;
            }

            public String getEntrustDate() {
                return entrustDate;
            }

            public void setEntrustDate(String entrustDate) {
                if (CommUtil.checkIsNull(entrustDate))
                    entrustDate="";
                this.entrustDate = entrustDate;
            }

            public String getResult() {
                return result;
            }

            public void setResult(String result) {
                if (CommUtil.checkIsNull(result))
                    result="";
                this.result = result;
            }

            public String getTaskDate() {
                return taskDate;
            }

            public void setTaskDate(String taskDate) {
                if (CommUtil.checkIsNull(taskDate))
                    taskDate="";
                this.taskDate = taskDate;
            }

            public String getAgencyComment() {
                return agencyComment;
            }

            public void setAgencyComment(String agencyComment) {
                if (CommUtil.checkIsNull(agencyComment))
                    agencyComment="";
                this.agencyComment = agencyComment;
            }

            public String getCallbackReason() {
                return callbackReason;
            }

            public void setCallbackReason(String callbackReason) {
                if (CommUtil.checkIsNull(callbackReason))
                    callbackReason="";
                this.callbackReason = callbackReason;
            }

            public String getAssignComment() {
                return assignComment;
            }

            public void setAssignComment(String assignComment) {
                if (CommUtil.checkIsNull(assignComment))
                    assignComment="";
                this.assignComment = assignComment;
            }
        }

        public static class TrailerRemarksBean {
            /**
             * remarkUser : 系统管理员
             * remarkContent : æææå
             * remarkTime : 2017-05-26 19:13
             */

            private String remarkUser;
            private String remarkContent;
            private String remarkTime;

            public String getRemarkUser() {
                return remarkUser;
            }

            public void setRemarkUser(String remarkUser) {
                if (CommUtil.checkIsNull(remarkUser))
                    remarkUser="";
                this.remarkUser = remarkUser;
            }

            public String getRemarkContent() {
                return remarkContent;
            }

            public void setRemarkContent(String remarkContent) {
                if (CommUtil.checkIsNull(remarkContent))
                    remarkContent="";
                this.remarkContent = remarkContent;
            }

            public String getRemarkTime() {
                return remarkTime;
            }

            public void setRemarkTime(String remarkTime) {
                if (CommUtil.checkIsNull(remarkTime))
                    remarkTime="";
                this.remarkTime = remarkTime;
            }
        }
    }
}
