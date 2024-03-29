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
(2) 测量onMeasure          

    知道View在页面的宽高.       
(3) 绘制onDraw      

    知道它的尺寸后要知道它的样子 ,使用onDraw方法绘制.             
(4) 状态的存储与恢复        
    
    我们的Activity是有可能被置于后台的,置于后台就有一个风险,它有可能被系统去杀死.        
    当杀死后,当用户回到Activity会进行重建,重建就涉及到一个问题:     
    比如:用户正在编辑一篇文章,是通过EditText编辑,此时用户来了一个电话,不幸的是他在接电话        
    过程中由于内存的紧张,系统将们的activity回收了,当用户再次回到activity时,那个activity会重建,     
    但是如果没有view的存储与恢复机制的话,那么可用户编辑的文章就不见了,这将是一个非常差的用户体验.      
    
    所有我们系统中的一些app上的一些文本都有存储与恢复的机制,当我们去旋转手机去测试它,当activity重建      
    之后文本是不会消失的.    
           
### 2-2 自定义属性的声明和获取
#### 1.分析需要的自定义属性
#### 2.在res/values/attrs.xml中定义声明
#### 3.在Layout.xml文件中进行使用
#### 4.在view的构造方法中进行获取
大致的代码是这样的:          
![attrs.xml文件](/readme/img/a9.png)
### 2-3 测量onMeasure
#### 1.exactly,at_most,unspecified(三种测试模式)
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
  
#### 2.MeasureSpec
        通过这个类我们可以拿到父控件传入的模式以及传入的值的大小,
#### 3.setMeasuredDimension
        当view测量完成后,不要忘记为view设置它测量完后的宽度和高度.
#### 4.requestLayout
        比如我的view是显示文本,当我们的文本大小发生了改变后,可以调用requestLayout重新测量以及布局.
        
接下来看一个大致的实例     
![attrs.xml文件](/readme/img/a10.png)
### 2-4 绘制onDraw
调用onDraw方法
#### 1. 绘制内容区域
#### 2. invalidate(),postInvalidate
#### 3. Canvas.drawXXX
        掌握绘制的一些api 
#### 4. translate,rotate,scale,skew(斜的)
        通过这些方法对它进行变形
#### 5. save(),restore()
        在使用上面的方法之前,不要忘记首先save下,最后restore一下
        因为它有可能改变绘制的默认的坐标系等等.
### 2-5 状态的存储与恢复
#### 1.onSaveInstanceState
#### 2.onRestoreInstanceState
![attrs.xml文件](/readme/img/a11.png)     
凡是自定义view一定不要忘记去重写这两个方法,然后去考虑你的view是否有属性是需要存储与恢复的
比如说你写了一个进度条,那么你的progress是一定要存储与恢复的.你写了一个EditText,那么你的Text文本是
一定要存储和恢复的.要把握好哪些东西在activity重建以后需要去恢复它,那么就去复写这两个方法.

## 第三章 案例:TestView(绘制一个圆)
### 3-1 自定义属性文件的声明和获取
1. 自定义属性文件values/attrs.xml      
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
2. 获取自定义属性文件的属性值        
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
### 3-2 测量onMeasure
```
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
```
### 3-3 绘制onDraw
```
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

  

```
### 3-4 状态存储与恢复
```
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
```
## 第四章 案例:百分比进度条
![attrs.xml文件](/readme/img/a4.gif)
### 4-1 自定义属性文件的声明和获取
```
<declare-styleable name="RoundProgressBar">
        <attr name="color" format="color"></attr>
        <attr name="android:progress" ></attr>
        <attr name="android:textSize" ></attr>
        <attr name="radius" format="dimension"></attr>
        <attr name="line_width" format="dimension"></attr>
    </declare-styleable>
```
### 4-2 测量onMeasure
```
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

```
### 4-3 绘制onDraw
```
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
        int textHeight=bounds.height();

//        canvas.drawText(text,0,text.length(),width/2,y+textHeight/2-mPaint.descent()/2,mPaint);//如果text是中文

        canvas.drawText(text,0,text.length(),width/2,y+textHeight/2,mPaint);

        //画一个辅助线看进度文本是否居中
//        canvas.drawLine(0,height/2,getWidth(),height/2,mPaint);
//        canvas.drawLine(width/2,0,width/2,height,mPaint);
    }
```
### 4-4 状态存储与恢复
```
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
```
### 4-5 测试
1. 在RoundProgressBar中提供方法可以修改和获得进度
```
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
```
2. 在MainActivtiy使用属性动画进行测试

- 布局文件
```
    <com.example.customview.RoundProgressBar
        android:id="@+id/roundProgressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:padding="10dp"
        android:progress="0"
        android:textSize="72dp"
        app:radius="90dp"
        app:color="#ff0000"
        />
```
  
- java代码
```
 final View view = findViewById(R.id.roundProgressBar);

       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ObjectAnimator.ofInt(view,"progress",0,100).setDuration(3000).start();
           }
       });
```
## 第五章 案例
### 5-1 课程总结
1. 首先我们通过一个TestView演示自定义View的四个步骤
2. 然后我们复制一个TestView的代码修改为RoundProgressBar,          
   修改时我们真正需要改变的有以下几点:
- 构造方法中获得属性,因为每个自定义view所对应的属性都是不一样的
- onMeasure几乎没有修改,唯一点就是measureWidth和measureHeight,就是不同的view,决定自己宽高的写法肯定是不一样的
- 绘制onDraw需要根据view的特点进行绘制,需要掌握就是canvas的API,Paint的一些设置
  包括canvas.translate(getPaddingLeft(),getPaddingTop());//移动画笔,
  canvas.restore();//画布取出原来保存的状态      
  重点是要知道在绘制Text时,y的参数是设置文本的baseLine,而不是文字的最底部,所以注意在绘制中文和英文时
  字体上下并不是居中的            
```
    int y= (int) (height/2.0) ;
    Rect bounds=new Rect();
    mPaint.getTextBounds(text,0,text.length(),bounds);
    int textHeight=bounds.height();//获得文本的高度
  canvas.drawText(text,0,text.length(),width/2,y+textHeight/2-mPaint.descent()/2,mPaint);//如果text是中文
  canvas.drawText(text,0,text.length(),width/2,y+textHeight/2,mPaint);
```
![文本的基线](/readme/img/a12.png)       
![文本的基线2](/readme/img/timg.jpg)
- 最后是状态的存储与恢复,不要忘记存储和恢复父view已经存储和恢复过的状态
```
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

```
对于状态的存储与恢复基本上就是上面的格式,唯一的就是找到自定义view那些状态是需要存储与恢复的.       
**需要注意的是,要成功的实现view的状态与恢复,需要给view添加一个id**
