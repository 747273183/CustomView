package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class RoundProgressBar extends View {

    //声明自定义控件的属性
    private int mColor;
    private int mProgress;
    private int mTextSize;
    private int mRadius;
    private int mLineWidth;

    //声明一个画笔
    private Paint mPaint;
    private RectF rectF;


    public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //获得自定义控件属性值
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.RoundProgressBar);
        mRadius= (int) ta.getDimension(R.styleable.RoundProgressBar_radius,dp2px(30));
        mColor=ta.getColor(R.styleable.RoundProgressBar_color,Color.RED);
        mLineWidth= (int) ta.getDimension(R.styleable.RoundProgressBar_line_width,dp2px(3));
        mTextSize= (int) ta.getDimension(R.styleable.RoundProgressBar_android_textSize,dp2px(16));
        mProgress=ta.getInt(R.styleable.RoundProgressBar_android_progress,0);

        //初始化画笔
        initPaint();



    }

    private float dp2px(int dpVal)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
    }

    private void initPaint() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //定义测量结果
        int measureedWidth = 0,measureedHeight = 0;

        //测量宽
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (modeWidth==MeasureSpec.EXACTLY)
        {
            measureedWidth=sizeWidth;
        }
        else
        {
            int needWidth=measureWidth()+getPaddingLeft()+getPaddingRight();
            if (modeWidth==MeasureSpec.AT_MOST)
            {
                measureedWidth=Math.min(needWidth,sizeWidth);
            }
            else
            {
                measureedWidth=needWidth;
            }
        }
        //测量高
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeHeight==MeasureSpec.EXACTLY)
        {
            measureedHeight=sizeWidth;
        }
        else
        {
            int needHeight=measureHeight()+getPaddingTop()+getPaddingBottom();
            if (modeWidth==MeasureSpec.AT_MOST)
            {
                measureedHeight=Math.min(needHeight,sizeHeight);
            }
            else
            {
                measureedHeight=needHeight;
            }
        }
        //设置宽和高的测量结果
        setMeasuredDimension(measureedWidth,measureedHeight);
    }

    private int measureHeight() {
        return mRadius*2;
    }


    private int measureWidth() {
        return mRadius*2;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        初始化画圆弧的矩形
        rectF=new RectF(0,0,w-getPaddingLeft()*2,h-getPaddingTop()*2);
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
       //画细圆
        mPaint.setStyle(Paint.Style.STROKE);//空心的
        mPaint.setStrokeWidth(mLineWidth/4.0f);//画笔粗细

        int width=getWidth();
        int height=getHeight();
        canvas.drawCircle(width/2,height/2,
                width/2-getPaddingLeft()-mPaint.getStrokeWidth()/2,
                mPaint);
        //画圆弧(粗圆)
        canvas.save();//画布将当前的状态保存
        canvas.translate(getPaddingLeft(),getPaddingTop());//移动画笔

        float angle=mProgress*1.0f/100*360;//计算弧度
        mPaint.setColor(mColor);//设置画笔颜色
        mPaint.setStrokeWidth(mPaint.getStrokeWidth()*6);//设置画笔粗细
        canvas.drawArc(rectF,0,angle,false,mPaint);
        canvas.restore();//画布取出原来保存的状态

        //画进度
        String text=mProgress+"%";
//        text="慕课网";
        mPaint.setTextAlign(Paint.Align.CENTER);//字体居中
        mPaint.setStrokeWidth(0);
        mPaint.setTextSize(mTextSize);
        int y= (int) (height/2.0) ;
        Rect bounds=new Rect();
        mPaint.getTextBounds(text,0,text.length(),bounds);
        int textHeight=bounds.height();//获得文本的高度

//        canvas.drawText(text,0,text.length(),width/2,y+textHeight/2-mPaint.descent()/2,mPaint);//如果text是中文

        canvas.drawText(text,0,text.length(),width/2,y+textHeight/2,mPaint);

        //画一个辅助线看进度文本是否居中
//        canvas.drawLine(0,height/2,getWidth(),height/2,mPaint);
//        canvas.drawLine(width/2,0,width/2,height,mPaint);


    }


    //状态的存储与恢复
    private  static  final String INSTANCE="instance";
    private static  final String KEY_PROGRESS="key_progress";
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putInt(KEY_PROGRESS,mProgress);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof  Bundle)
        {
            mProgress = ((Bundle) state).getInt(KEY_PROGRESS);
            Parcelable parcelable = ((Bundle) state).getParcelable(INSTANCE);
            super.onRestoreInstanceState(parcelable);
        }
        super.onRestoreInstanceState(state);
    }


    //对外提供改变进度和获得进度的方法
    public void setProgress(int progress)
    {
      mProgress=progress;
      invalidate();
    }

    public int getProgress()
    {
        return mProgress;
    }

}
