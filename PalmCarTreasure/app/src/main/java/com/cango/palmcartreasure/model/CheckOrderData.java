package com.cango.palmcartreasure.model;

import java.util.List;

/**
 * Created by cango on 2017/9/7.
 */

public class CheckOrderData {

    /**
     * Success : true
     * Msg : 操作成功
     * Code : 0
     * Data : {"questionList":[{"index":0,"value":1,"answerList":["谷人","常志强","吴子婧"]},{"index":1,"value":1,"answerList":["吉AX55A9","吉BLN228","吉AF0W56"]}]}
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
        private List<QuestionListBean> questionList;

        public List<QuestionListBean> getQuestionList() {
            return questionList;
        }

        public void setQuestionList(List<QuestionListBean> questionList) {
            this.questionList = questionList;
        }

        public static class QuestionListBean {
            /**
             * index : 0
             * value : 1
             * answerList : ["谷人","常志强","吴子婧"]
             */

            private int index;
            private int value;
            private List<String> answerList;

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public List<String> getAnswerList() {
                return answerList;
            }

            public void setAnswerList(List<String> answerList) {
                this.answerList = answerList;
            }
        }
    }
}
