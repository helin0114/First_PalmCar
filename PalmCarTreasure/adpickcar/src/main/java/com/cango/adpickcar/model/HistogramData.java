package com.cango.adpickcar.model;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/04/17
 *     desc   :
 * </pre>
 */
public class HistogramData {
    private String title;
    private int allColor;
    private int completeColor;
    private int allValue;
    private int completeValue;

    public HistogramData(String title, int allColor, int completeColor, int allValue, int completeValue) {
        this.title = title;
        this.allColor = allColor;
        this.completeColor = completeColor;
        this.allValue = allValue;
        this.completeValue = completeValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAllColor() {
        return allColor;
    }

    public void setAllColor(int allColor) {
        this.allColor = allColor;
    }

    public int getCompleteColor() {
        return completeColor;
    }

    public void setCompleteColor(int completeColor) {
        this.completeColor = completeColor;
    }

    public int getAllValue() {
        return allValue;
    }

    public void setAllValue(int allValue) {
        this.allValue = allValue;
    }

    public int getCompleteValue() {
        return completeValue;
    }

    public void setCompleteValue(int completeValue) {
        this.completeValue = completeValue;
    }

}
