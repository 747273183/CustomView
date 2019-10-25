# 自定义View
## 第一章 课程介绍
### 1-1 概述
## 第二章 自定义控件(重点)
### 2-1 自定义View的步骤
1.  为什么要自定义View     
    (1)特定的显示风格  
    
            我想在我的系统中显示一个非常炫酷的进度条.       
    (2)处理特有的用户的交互       
    
            我的应用中有一具特殊的用户交互,比如说拖拽,排序,我可能需要用特殊的方式实现它.        
    (3)优化我们的布局.             
    
            我们还可以通过自定义view的方式去优化我们的布局,比如说我们现在的布局文件是一个listView,
            内部有一个imageView,imageView下面有一个TextView对它进行介绍,这样的控件完全可以由一个
            自定义view对它进行替代.      
    (4)封装等...       
    
            有的时候我们需要在项目中对有些控件进行封装.比如我们标题栏
2. 如何自定义控件  
           (1) 自定义控件属性的声明和获取        
                
                抽取一些可定制的属性.  
                大致的代码是这样的:          
        ![attrs.xml文件](/readme/img/a9.png)    
           (2) 测量onMeasure          
           
                知道View在页面的宽高.   
        **exactly,at_most,unspecified(三种测试模式)**
        - Exactly        
                用户为我们的控件声明了一个具体的宽和高
                对于这种情况直接获得用户所声明的值,直接获取就可以了.
        - at_Most       
        当用户设置了wrap_content的值时,我们在view的测量方法中可以拿测量模式叫at_most,
        我们还可以拿到父控件所传入的值,这个值并不是让我们直接去使用的.一般遇到at_most,我们会
        自己去测试自己的宽度和高度,测试完后我们会与父控件所传入的值做比较,意思是你们可以去测量自己的
        大小,但不是你不能超过父控件给你传入的值的大小,你至多是我传入的大小.
        - unspecified       
          这种view一般都是有滚动条的view,比如listview,scrollview
          ,这类view由于它内部是可以滚动的,所有它基本上是不限定子view的宽度或高度.
          比如说scrollview,你子view再高都没有关系,因为可以滚动. 
          
          它和at_most最大的区别,at_most子控件不能超过父控件传入的值.
          而unspecified是子控件想要多大就可以多大.
          
        **MeasureSpec**     
        
            通过这个类我们可以拿到父控件传入的模式以及传入的值的大小,       
        **setMeasuredDimension**        
        
            当view测量完成后,不要忘记为view设置它测量完后的宽度和高度.
        **requestLayout**
        
            比如我的view是显示文本,当我们的文本大小发生了改变后,可以调用requestLayout重新测量以及布局.
                
        **接下来看一个大致的实例**  
        ![attrs.xml文件](/readme/img/a10.png)
      
   (3) 绘制onDraw      
   
        知道它的尺寸后要知道它的样子 ,使用onDraw方法绘制.    
    
         1. 绘制内容区域
         2. invalidate(),postInvalidate
         3. Canvas.drawXXX
                掌握绘制的一些api 
         4. translate,rotate,scale,skew(斜的)
                通过这些方法对它进行变形
         5. save(),restore()
                在使用上面的方法之前,不要忘记首先save下,最后restore一下
                因为它有可能改变绘制的默认的坐标系等等.         
  (4) 状态的存储与恢复
        
        我们的Activity是有可能被置于后台的,置于后台就有一个风险,它有可能被系统去杀死.        
        当杀死后,当用户回到Activity会进行重建,重建就涉及到一个问题:     
        比如:用户正在编辑一篇文章,是通过EditText编辑,此时用户来了一个电话,不幸的是他在接电话        
        过程中由于内存的紧张,系统将们的activity回收了,当用户再次回到activity时,那个activity会重建,     
        但是如果没有view的存储与恢复机制的话,那么可用户编辑的文章就不见了,这将是一个非常差的用户体验.      
        
        所有我们系统中的一些app上的一些文本都有存储与恢复的机制,当我们去旋转手机去测试它,当activity重建      
        之后文本是不会消失的.    
        
        #### 1.onSaveInstanceState
        #### 2.onRestoreInstanceState
        ![attrs.xml文件](/readme/img/a11.png)     
        凡是自定义view一定不要忘记去重写这两个方法,然后去考虑你的view是否有属性是需要存储与恢复的
        比如说你写了一个进度条,那么你的progress是一定要存储与恢复的.你写了一个EditText,那么你的Text文本是
        一定要存储和恢复的.要把握好哪些东西在activity重建以后需要去恢复它,那么就去复写这两个方法.
           
### 2-2 自定义属性的声明和获取
#### 1.分析需要的自定义属性
#### 2.在res/values/attrs.xml中定义声明
```
    <?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="TestView">
        <attr name="test_boolean" format="boolean"></attr>
        <attr name="test_string" format="string"></attr>
        <attr name="test_integer" format="integer"></attr>
        <attr name="test_enum" format="enum">
            <enum name="top" value="1"></enum>
            <enum name="bottom" value="2"></enum>
        </attr>
        <attr name="test_dimension" format="dimension"></attr>
    </declare-styleable>
</resources>
```
#### 3.在Layout.xml文件中进行使用
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
    <com.example.customview.TestView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:test_string="自定义控件"
        app:test_integer="110"
        app:test_enum="bottom"
        app:test_dimension="200dp"
        app:test_boolean="true"
        />
</LinearLayout>
```
#### 4.在view的构造方法中进行获取      
```
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

```

### 2-3 测量onMeasure


### 2-4 绘制onDraw

### 2-5 状态的存储与恢复


## 第三章 案例
### 3-1 案例(上)

### 3-2 案例(下)
## 第四章 案例
### 4-1 课程总结
