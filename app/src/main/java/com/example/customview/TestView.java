package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class TestView extends View {
    private static final String TAG = "TestView";
    private  int enumTest;
    private  float dimensionTest;
    private  int integerTest;
    private  String stringTest="imooc";
    private  boolean booleanTest ;

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
}
