package com.cango.adpickcar.fc.fcs;


import android.graphics.Color;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseFragment;
import com.cango.adpickcar.customview.ADHistogramView;
import com.cango.adpickcar.model.HistogramData;

import java.util.ArrayList;

import butterknife.BindView;

public class FCSMainFragment extends BaseFragment {

    @BindView(R.id.custom_top)
    ADHistogramView adTop;
    @BindView(R.id.custom_bottom)
    ADHistogramView adBottom;

    public static FCSMainFragment newInstance() {
        return new FCSMainFragment();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_fcsmain;
    }

    @Override
    protected void initView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HistogramData> histogramData = new ArrayList<>();
                int allColor = Color.parseColor("#cbe876");
                int completeColor = Color.parseColor("#a3c95f");
                histogramData.add(new HistogramData("张杰", allColor, completeColor, 30, 5));
                histogramData.add(new HistogramData("国瑞", allColor, completeColor, 20, 8));
                histogramData.add(new HistogramData("杨李杨", allColor, completeColor, 25, 8));
                histogramData.add(new HistogramData("陈婷", allColor, completeColor, 34, 20));
                histogramData.add(new HistogramData("何霖", allColor, completeColor, 40, 36));
                histogramData.add(new HistogramData("昌全", allColor, completeColor, 12, 9));
                histogramData.add(new HistogramData("李力", allColor, completeColor, 30, 30));
                adTop.setHistogramData(histogramData);
                adTop.setTitle("初访完成情况");
                adTop.setmHeightScaleValue(50);
                adTop.requestLayout();

                ArrayList<HistogramData> histogramData1 = new ArrayList<>();
                histogramData1.add(new HistogramData("刀妹", allColor, completeColor, 60, 2));
                histogramData1.add(new HistogramData("诺克", allColor, completeColor, 40, 8));
                histogramData1.add(new HistogramData("刀锋之影", allColor, completeColor, 26, 20));
                histogramData1.add(new HistogramData("深渊巨口", allColor, completeColor, 14, 14));
                histogramData1.add(new HistogramData("熔岩巨兽", allColor, completeColor, 50, 46));
                adBottom.setHistogramData(histogramData1);
                adBottom.setTitle("跟进完成情况");
                adBottom.setmHeightScaleValue(60);
                adBottom.requestLayout();
                adBottom.postInvalidate();
            }
        });
    }

    @Override
    protected void initData() {

    }
}
