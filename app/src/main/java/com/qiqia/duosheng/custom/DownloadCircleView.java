package com.qiqia.duosheng.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.com.smallcake_utils.DpPxUtils;

public class DownloadCircleView extends View {
    Paint mBgPaint;
    Paint mStepPaint;
    Paint mTxtCirclePaint;
    Paint mTxtPaint;
    int outsideRadius=DpPxUtils.dp2px(100);
    int progressWidth =DpPxUtils.dp2px(2);
    float progressTextSize  = DpPxUtils.dp2px(12);
    Context context;
    public DownloadCircleView(Context context) {
        super(context);
    }
    public DownloadCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public DownloadCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);



        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = (int) ((2 * outsideRadius) + progressWidth);
        }
        size = MeasureSpec.getSize(heightMeasureSpec);
        mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = (int) ((2 * outsideRadius) + progressWidth);
        }
        setMeasuredDimension(width, height);
    }



    private void init(Context context) {
        int progressColor = Color.parseColor("#FF5836");//进度球颜色
        this.context = context;
        //灰色背景圆环
        mBgPaint = new Paint();
        mBgPaint.setStrokeWidth(progressWidth);
        mBgPaint.setColor(Color.GRAY);
        this.mBgPaint.setAntiAlias(true);
        this.mBgPaint.setStyle(Paint.Style.STROKE); //绘制空心圆
        //进度圆环
        mStepPaint = new Paint();
        mStepPaint.setStrokeWidth(progressWidth);
        mStepPaint.setColor(progressColor);
        this.mStepPaint.setAntiAlias(true);
        this.mStepPaint.setStyle(Paint.Style.STROKE); //绘制空心圆
        //进度卫星球
        mTxtCirclePaint = new Paint();
        mTxtCirclePaint.setColor(progressColor);
        this.mTxtCirclePaint.setAntiAlias(true);
        this.mTxtCirclePaint.setStyle(Paint.Style.FILL); //绘制实心圆
        //进度文字5%
        mTxtPaint = new Paint();
        mTxtPaint.setTextSize(progressTextSize);
        mTxtPaint.setColor(Color.WHITE);
        this.mTxtPaint.setAntiAlias(true);

    }
    float maxProgress=100f;
    float progress  =0f;

    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //灰色圆圈
        int circlePoint = getWidth() / 2;
        canvas.drawCircle(circlePoint, circlePoint, outsideRadius, mBgPaint); //画出圆
        //进度
        RectF oval = new RectF();
        oval.left=circlePoint - outsideRadius;
        oval.top=circlePoint - outsideRadius;
        oval.right=circlePoint + outsideRadius;
        oval.bottom=circlePoint + outsideRadius;
        float range = 360 * (progress / maxProgress);
        canvas.drawArc(oval, -90,  range, false, mStepPaint);  //根据进度画圆弧
        //轨道圆和文字
        double x1 = circlePoint + outsideRadius * Math.cos((range-90) * 3.14 / 180);
        double y1 = circlePoint + outsideRadius * Math.sin((range-90) * 3.14 / 180);
        canvas.drawCircle((float) x1, (float) y1, progressTextSize*1.3f, mTxtCirclePaint);
        String txt = (int) progress + "%";
        float strwid  = mTxtPaint.measureText(txt);//直接返回参数字符串所占用的宽度
        canvas.drawText(txt,(float) x1-strwid/2, (float) y1+progressTextSize/2-progressWidth/2,mTxtPaint);

    }
}
