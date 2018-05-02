package com.cango.adpickcar.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cango.adpickcar.model.HistogramData;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : cango
 *     e-mail : lili92823@163.com
 *     time   : 2018/04/17
 *     desc   :
 * </pre>
 */
public class ADHistogramView extends View {

    /**
     * 控件的绝对宽度（去除padding）
     */
    private int mAbsWidth;
    /**
     * 控件的绝对高度（去除padding）
     */
    private int mAbsHeight;
    /**
     * 直方图控件的绝对宽度
     */
    private int mHistogramWidth;
    /**
     * 直方图控件的绝对高度
     */
    private int mHistogramHeight;

    /**
     * padding
     */
    private int paddingStart;
    private int paddingEnd;
    private int paddingTop;
    private int paddingBottom;

    /**
     * 文字距直方图的高度
     */
    private int mBottomTitleHeight;
    /**
     * 文字的高度
     */
    private int mBottomTextHeight;
    /**
     * 每个直方图之间的padding
     */
    private int mClassifyPadding;
    /**
     * 每个直方图的宽度
     */
    private int mClassifyWidth;
    /**
     * 直方图和padding的比例
     */
    private float mClassifyScale = 0.5f;
    /**
     * 直方图中的 X Y 轴
     */
    private Paint mHistogramLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 控件下方的title
     */
    private Paint mHistogramTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 每个直方图下面的文字
     */
    private Paint mHistogramTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 每个直方图
     */
    private Paint mHistogramClassifyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 完成直方图的画笔
     */
    private Paint mHistogramClassifyCompletePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 折线图点和线的颜色
     */
    private Paint mBrokenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void setHistogramData(List<HistogramData> histogramData) {
        this.histogramData = histogramData;
    }

    /**
     * 直方图数据
     */
    private List<HistogramData> histogramData = new ArrayList<>();
    /**
     * 直方图控件的高度默认
     */
    private int mHeightScaleValue = 50;
    private String title = "";
    /**
     * Y刻度尺的几个
     */
    private int mYWeight = 5;
    /**
     * Y刻度尺上的文字的宽度
     */
    private int mYTextWidth;
    private int mWidth;
    private int mHeight;

    {
        int lineColor = Color.parseColor("#d3d3d3");
        mHistogramLinePaint.setColor(lineColor);
        mHistogramLinePaint.setStrokeWidth(3);

        int titleColor = Color.parseColor("#333333");
        mHistogramTitlePaint.setColor(titleColor);
        mHistogramTitlePaint.setTextAlign(Paint.Align.CENTER);
        mHistogramTitlePaint.setTextSize(50);

        int textColor = Color.parseColor("#d3d3d3");
        mHistogramTextPaint.setColor(textColor);
        mHistogramTextPaint.setTextAlign(Paint.Align.CENTER);
        mHistogramTextPaint.setTextSize(28);


        int allColor = Color.parseColor("#cbe876");
        int completeColor = Color.parseColor("#a3c95f");

        mHistogramClassifyPaint.setColor(allColor);
        mHistogramClassifyPaint.setStyle(Paint.Style.FILL);

        mHistogramClassifyCompletePaint.setColor(completeColor);
        mHistogramClassifyCompletePaint.setStyle(Paint.Style.FILL);

        mBrokenLinePaint.setColor(Color.parseColor("#0db7d1"));
        mBrokenLinePaint.setStyle(Paint.Style.FILL);
        mBrokenLinePaint.setStrokeCap(Paint.Cap.ROUND);

//        histogramData.add(new HistogramData("张杰", allColor, completeColor, 30, 5));
//        histogramData.add(new HistogramData("国瑞", allColor, completeColor, 20, 8));
//        histogramData.add(new HistogramData("杨李杨", allColor, completeColor, 25, 8));
//        histogramData.add(new HistogramData("陈婷", allColor, completeColor, 34, 20));
//        histogramData.add(new HistogramData("何霖", allColor, completeColor, 40, 36));
//        histogramData.add(new HistogramData("昌全", allColor, completeColor, 12, 9));
//        histogramData.add(new HistogramData("李力", allColor, completeColor, 30, 30));
    }

    public ADHistogramView(Context context) {
        this(context, null);
    }

    public ADHistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ADHistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setmHeightScaleValue(int mHeightScaleValue) {
        this.mHeightScaleValue = mHeightScaleValue;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void init() {
        setPadding(50, 50, 50, 50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measureHandler(widthMeasureSpec);
        mHeight = measureHandler(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mWidth = getWidth();
//        mHeight = getHeight();
        paddingStart = getPaddingStart();
        paddingEnd = getPaddingEnd();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        mBottomTitleHeight = (int) (mAbsHeight * 0.1f);
        mBottomTextHeight = (int) (mAbsHeight * 0.08f);
        mAbsWidth = mWidth - paddingStart - paddingEnd;
        mAbsHeight = mHeight - paddingTop - paddingBottom;
        mYTextWidth = mBottomTextHeight / 2;
        mHistogramWidth = mAbsWidth;
        mHistogramHeight = mAbsHeight - mBottomTitleHeight - mBottomTextHeight;
        mClassifyWidth = (int) (mHistogramWidth / (histogramData.size() + (histogramData.size() + 1) * mClassifyScale));
        mClassifyPadding = (int) (mClassifyWidth * mClassifyScale);
    }

    private int measureHandler(int measureSpec) {
        int result = 0;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.max(size, result);
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        mWidth = getWidth();
//        mHeight = getHeight();
//        paddingStart = getPaddingStart();
//        paddingEnd = getPaddingEnd();
//        paddingTop = getPaddingTop();
//        paddingBottom = getPaddingBottom();
//        mAbsWidth = mWidth - paddingStart - paddingEnd;
//        mAbsHeight = mHeight - paddingTop - paddingBottom;
//        mBottomTitleHeight = (int) (mAbsHeight * 0.2f);
//        mBottomTextHeight = (int) (mAbsHeight * 0.08f);
//        mHistogramWidth = mAbsWidth;
//        mHistogramHeight = mAbsHeight - mBottomTitleHeight - mBottomTextHeight;
//        mClassifyWidth = (int) (mHistogramWidth / (histogramData.size() + (histogramData.size() + 1) * mClassifyScale));
//        mClassifyPadding = (int) (mClassifyWidth * mClassifyScale);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //保存中间点用来画折线图
        float[] points = new float[histogramData.size() * 2];
        //第一步画出title
        canvas.drawText(title, mWidth * 0.5f, mBottomTitleHeight, mHistogramTitlePaint);
        //第二步画出直方图
        for (int i = 0; i < histogramData.size(); i++) {
            //先画直方图
            RectF mRectF = new RectF();
            HistogramData histogramData = this.histogramData.get(i);
            float left = paddingStart + (i + 1) * mClassifyPadding + i * mClassifyWidth + mYTextWidth;
            float allTop = paddingTop + mBottomTitleHeight + mHistogramHeight - mHistogramHeight * histogramData.getAllValue() / mHeightScaleValue;
            float completeTop = paddingTop + mBottomTitleHeight + mHistogramHeight - mHistogramHeight * histogramData.getCompleteValue() / mHeightScaleValue;
            float right = paddingStart + mYTextWidth + (i + 1) * (mClassifyPadding + mClassifyWidth);
            float bottom = paddingTop + mBottomTitleHeight + mHistogramHeight;
            mRectF.set(left, allTop, right, bottom);
            canvas.drawRect(mRectF, mHistogramClassifyPaint);
            mRectF.set(left, completeTop, right, bottom);
            canvas.drawRect(mRectF, mHistogramClassifyCompletePaint);
            int size = i * 2;
            points[size] = left + (right - left) * 0.5f;
            points[size + 1] = allTop;
            //再画文字
            float mTextX = (i + 1) * (mClassifyPadding + mClassifyWidth) + paddingStart - mClassifyWidth * 0.5F + mYTextWidth;
            float mTextY = mHistogramHeight + paddingTop + mBottomTitleHeight + mBottomTextHeight;
            canvas.drawText(histogramData.getTitle(), mTextX, mTextY, mHistogramTextPaint);
        }
        //第三步画坐标系的
        canvas.drawLine(paddingStart + mYTextWidth, paddingTop + mBottomTitleHeight, paddingStart + mYTextWidth, mHistogramHeight + paddingTop + mBottomTitleHeight, mHistogramLinePaint);
        canvas.drawLine(paddingStart + mYTextWidth, mHistogramHeight + paddingTop + mBottomTitleHeight, mHistogramWidth + paddingStart + mYTextWidth,
                mHistogramHeight + paddingTop + mBottomTitleHeight, mHistogramLinePaint);
        //第四步画出y轴刻度尺
        float maxX = paddingStart;
        float maxY = paddingTop + mBottomTitleHeight;
        canvas.drawText(mHeightScaleValue + "", maxX, maxY + 10, mHistogramTextPaint);
//        canvas.drawLine(maxX+mYTextWidth,maxY,maxX+mYTextWidth+20,maxY,mHistogramTextPaint);
        for (int i = 0; i < mYWeight; i++) {
            float mY = mHistogramHeight * (i + 1) / mYWeight + paddingTop + mBottomTitleHeight;
            float mX = paddingStart;
            canvas.drawText(mHeightScaleValue * (mYWeight - i - 1) / mYWeight + "", mX, mY + 10, mHistogramTextPaint);
            canvas.drawLine(mX + mYTextWidth, mY, mX + mYTextWidth + 20, mY, mHistogramTextPaint);
        }
        //第五步画点和折线图
        mBrokenLinePaint.setStrokeWidth(mClassifyWidth / 10);
        canvas.drawPoints(points, mBrokenLinePaint);
        mBrokenLinePaint.setStrokeWidth(mClassifyWidth / 20);
        for (int i = 0; i < points.length - 3; i++) {
            canvas.drawLine(points[i], points[i + 1], points[i + 2], points[i + 3], mBrokenLinePaint);
            i++;
        }
    }
}


