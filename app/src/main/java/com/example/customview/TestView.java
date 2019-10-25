package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TestView extends View {
    private static final String TAG = "TestView";
    private  int enumTest;
    private  float dimensionTest;
    private  int integerTest;
    private  String stringTest="imooc";
    private  boolean booleanTest ;

    //画笔
    private Paint mPaint;

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //初始化画笔
        initPaint();
        //获得自定义控件
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.TestView);

        //第一种获取属性的方式(当String类型的变量,没有设置该属性时,默认为null)
//        boolean booleanTest = ta.getBoolean(R.styleable.TestView_test_boolean, false);
//        String stringTest = ta.getString(R.styleable.TestView_test_string);
//        int integerTest = ta.getInteger(R.styleable.TestView_test_integer, -1);
//        float dimensionTest = ta.getDimension(R.styleable.TestView_test_dimension, -1);
//        int enumTest = ta.getInt(R.styleable.TestView_test_enum, 1);
        //第二种获取属性和方式(当配置文件中没有设置某个属性时,会使用默认值)
        int count = ta.getIndexCount();//返回的是设置过的属性的个数
        Log.d(TAG, "TestView: "+count);
        for (int i=0;i<count;i++)
        {
            int index = ta.getIndex(i);
            switch (index)
            {
                case R.styleable.TestView_test_boolean:
                    booleanTest = ta.getBoolean(index, true);
                    break;
                case R.styleable.TestView_test_string:
                    stringTest = ta.getString(index);
                    break;
                case R.styleable.TestView_test_integer:
                    integerTest = ta.getInteger(index,-1);
                    break;
                case R.styleable.TestView_test_dimension:
                    dimensionTest = ta.getDimension(index,1.0f);
                    break;
                case R.styleable.TestView_test_enum:
                    enumTest = ta.getInt(index,1);
                    break;
            }
        }


        Log.d(TAG, "booleanTest:"+booleanTest+",sringTest:"+stringTest+
                ",integerTest:"+integerTest+",dimensionTest:"+dimensionTest
                +",enumTest:"+enumTest
            );
        //释放
        ta.recycle();
    }

    private void initPaint() {
        mPaint=new Paint();
        //设置画笔
        mPaint.setColor(Color.RED);//画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);//stroke空心的圆 ,fill实心的圆
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//抗抖动

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量宽度
        int width = 0;//测量后的宽度
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (modeWidth==MeasureSpec.EXACTLY)//测试模式为Exactly,代表用户设置是具体的数值
        {
            width=sizeWidth;//测试结果就等于size
        }
        else
        {
            int  needWidth=measuredWidth()+getPaddingLeft()+getPaddingRight();
            if (modeWidth==MeasureSpec.AT_MOST)
            {
                width=Math.min(needWidth,sizeWidth);
            }
        }

        //测量高度
        int height = 0;
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeHeight==MeasureSpec.EXACTLY)//测试模式为Exactly,代表用户设置是具体的数值
        {
            height=sizeHeight;//测试结果就等于size
        }
        else
        {
            int  needHeight=measuredHeight()+getPaddingTop()+getPaddingBottom();
            if (modeWidth==MeasureSpec.AT_MOST)//传入的值是wrap_content
            {
                height=Math.min(needHeight,sizeHeight);//传入的值的父控件是可以滚动的,如scrollview
            }
        }
        setMeasuredDimension(width,height);//设置测试后的大小
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);//不重写也可以,会调用父类的方法
    }

    //根据控件实际情况测量
    private int measuredHeight() {
        return  0;
    }

    private int measuredWidth() {
        return 0;
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx=getWidth()/2;
        float cy=getHeight()/2;
        float radius=getWidth()/2-mPaint.getStrokeWidth()/2;//半径减去画笔宽度的一半
        //画圆 cy,cy圆心坐标,radius半径
//        canvas.drawCircle(cx,cy,radius,mPaint);
//
//        //沿圆心画一条横线
//        mPaint.setStrokeWidth(1);
//        canvas.drawLine(0,getHeight()/2,getWidth(),getHeight()/2,mPaint);
//        //沿圆心画一条纵线
//        canvas.drawLine(getWidth()/2,0,getWidth()/2,getHeight(),mPaint);

        //绘制文本
        mPaint.setStyle(Paint.Style.FILL);
//      mPaint.setStrokeWidth(3);//绘制文本时不需要设置画笔的宽度
        mPaint.setTextSize(36);
        canvas.drawText(stringTest,0,stringTest.length(),getWidth()/2-stringTest.length()/2,getHeight()/2,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        stringTest="8888";
        invalidate();//重绘其自身,viewGroup才会重绘制所有内容
        return true;
    }

    //状态存储
    private static  final  String INSTANCE="instance";
    private static  final  String KEY_TEXT="key_text";

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putString(KEY_TEXT,stringTest);//存储我们自己的值
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());//存储父控件的值
        return bundle;
    }

    //状态恢复(一定要给自定义view设置一个id)

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof  Bundle)
        {
            Bundle bundle= (Bundle) state;
            Parcelable parcelable = bundle.getParcelable(INSTANCE);
            super.onRestoreInstanceState(parcelable);
            stringTest  = (String) bundle.get(KEY_TEXT);
            return;
        }

        super.onRestoreInstanceState(state);
    }
}
