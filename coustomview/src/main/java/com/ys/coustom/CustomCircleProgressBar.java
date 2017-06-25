package com.ys.coustom;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/4/23/15:02
 **/
public class CustomCircleProgressBar extends View {

    Paint mPaintCircleProgress;
    private int maxProgress=100;
    float currentProgress=0;
    boolean hasDrawPorgress=false;
    private String mProgressText;
    float outSideRadius=120.0f;  //半径
    float progressWidth=10.0f;  //边宽
    int inSideColor;
    int circlePoint;  /**圆心在屏幕中心**/

    /*初始化状态*/
    private int bpWidth;
    private int bpHeight;
    private int width;
    private int height;
    private Bitmap mBitmap;
    private int status;
    private int READY=1,PROGRESS=2,SUCCEED=3;


    public CustomCircleProgressBar(Context context) {
        this(context,null);
    }

    public CustomCircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomCircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initBitmap();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    private List<Float> radiousList = Collections.synchronizedList(new ArrayList<Float>());//存储半径的集合
    private void initBitmap() {
        mBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.record_center);
        bpHeight=mBitmap.getHeight();
        bpWidth=mBitmap.getWidth();
       // outSideRadius = Math.min(bpWidth,bpHeight)/2;
        radiousList.add(outSideRadius);
    }

    public void setHasDrawPorgress(boolean hasDrawPorgress) {
        this.hasDrawPorgress = hasDrawPorgress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size= MeasureSpec.getSize(widthMeasureSpec);
        int model=MeasureSpec.getMode(widthMeasureSpec);
        if (model==MeasureSpec.EXACTLY){
            width=size;
        }else {
            width= (int) (outSideRadius*2+progressWidth);
        }
        size=MeasureSpec.getSize(heightMeasureSpec);
        model=MeasureSpec.getMode(heightMeasureSpec);
        if (model==MeasureSpec.EXACTLY){
            height=size;
        }else {
            height= (int) (outSideRadius*2+progressWidth);
        }
        setMeasuredDimension(width,height);
    }

    private void initPaint() {
        mPaintCircleProgress=new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getStatus()==READY){
            drawReady(canvas);
           } if (getStatus()==PROGRESS){
            drawStatrProgress(canvas);
        } if (getStatus()==SUCCEED){

        }

    }

    private void drawStatrProgress(Canvas canvas) {
        circlePoint =getWidth()/2;
        //第一步画内圆 设置空心 设置园的宽度 消除锯齿
        mPaintCircleProgress.setColor(Color.parseColor("#7f8082"));
        mPaintCircleProgress.setStyle(STROKE);
        mPaintCircleProgress.setStrokeWidth(progressWidth);
        mPaintCircleProgress.setAntiAlias(true);
        canvas.drawBitmap(mBitmap,width/2-bpWidth/2,height/2-bpHeight/2,null);
        canvas.drawCircle(circlePoint,circlePoint,outSideRadius,mPaintCircleProgress);
        if (hasDrawPorgress){
            drawProgress(canvas);
            drawPorgressText(canvas);
        }
    }

    Handler mHandler=new Handler();

    private long intervaTime=1000; //间隔时间
    private long currentTime=System.currentTimeMillis(); //系统时间
    /*准备状态  图片加波纹*/
    private void drawReady(Canvas canvas) {
        mPaintCircleProgress.setAntiAlias(true);
        mPaintCircleProgress.setStrokeWidth(2);
        mPaintCircleProgress.setStyle(FILL);
        mPaintCircleProgress.setColor(Color.parseColor("#0099cc"));
        //int alpha = 255-255*(raduis-bpWidth/2)/(width/2-bpWidth/2);
        //mPaintCircleProgress.setAlpha(alpha);
        //canvas.drawCircle(width/2,height/2,raduis,mPaintCircleProgress);
        for(int i=0;i<radiousList.size();i++){
            float r = radiousList.get(i);
            int alpha = (int) (255-255*(r-bpWidth/2)/(width/2-bpWidth/2));
            mPaintCircleProgress.setAlpha(alpha);
            canvas.drawCircle(width/2,height/2,r,mPaintCircleProgress);
        }
        canvas.drawBitmap(mBitmap,width/2-bpWidth/2,height/2-bpHeight/2,null);
        mHandler.post(runable);
    }
    private Runnable runable=new Runnable() {
        @Override
        public void run() {
            postInvalidate();
            if(System.currentTimeMillis()-currentTime>intervaTime){
                radiousList.add((float) (Math.min(bpWidth,bpHeight)/2));
                currentTime = System.currentTimeMillis();
            }
            if (radiousList.size()==0){
                radiousList.add((float) (Math.min(bpWidth,bpHeight)/2));
            }
            for(int i=0;i<radiousList.size();i++){//改变每个半径的值
                radiousList.set(i,radiousList.get(i)+3);
            }
            //判断半径是否超过预先设定的值
            Iterator<Float> iterator = radiousList.iterator();//使用这个迭代防止出现并发修改的异常
            while (iterator.hasNext()) {
                Float r = iterator.next();
                if(r>=(Math.min(bpWidth,bpHeight)/2+31)){
                    radiousList.remove(r);
                }
            }
//            for (int i=0;i<radiousList.size();i++){
//                Integer r = radiousList.get(i);
//                if(r>=(Math.min(bpWidth,bpHeight)/2+31)){
//                    radiousList.remove(r);
//                }
//            }

            mHandler.postDelayed(runable,20);
        }
    };


    private void drawProgress(Canvas canvas){
        /**第二部画圆弧  不连接圆心**/
        RectF oval=new RectF();
        oval.left=circlePoint-outSideRadius;
        oval.top=circlePoint-outSideRadius;
        oval.right=circlePoint+outSideRadius;
        oval.bottom=circlePoint+outSideRadius;
        mPaintCircleProgress.setColor(Color.parseColor("#e91b7d"));  //设置进度的颜色
        canvas.drawArc(oval,270.0f,getAngle(),false,mPaintCircleProgress);
    }

    private void drawPorgressText(Canvas canvas) {
        Rect rect = new Rect();
        mPaintCircleProgress.setColor(Color.parseColor("#303F9F"));
        mPaintCircleProgress.setTextSize(32);
        mPaintCircleProgress.setStrokeWidth(0);
        String porgressText = getProgressText();
        mPaintCircleProgress.getTextBounds(porgressText,0,porgressText.length(),rect);
        Paint.FontMetricsInt fontMetricsInt=mPaintCircleProgress.getFontMetricsInt();
        int baseLine=(getMeasuredHeight()-fontMetricsInt.bottom+fontMetricsInt.top)/2-fontMetricsInt.top;
        canvas.drawText(porgressText,getMeasuredWidth()/2-rect.width()/2,baseLine,mPaintCircleProgress);
    }

    public synchronized int getMaxProgress() {
        return maxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress<0){
            return;
        }
        this.maxProgress = maxProgress;
    }

    public synchronized float getCurrentProgress() {
        return currentProgress;
    }

    public synchronized void setCurrentProgress(float currentProgress) {
        if (currentProgress<0){
            return;
        }
        if (currentProgress>maxProgress){
            currentProgress=maxProgress;
        }
        this.currentProgress=currentProgress;
        setHasDrawPorgress(true);
        postInvalidate();
        //startPorgressAnim(currentProgress);
    }

    int getAngle() {
        return (int) (360 * (currentProgress * 1f / maxProgress));
    }

    private void startPorgressAnim(float startProgress) {
        final ValueAnimator animator = ObjectAnimator.ofFloat(0, startProgress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentProgress= (float) animator.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setStartDelay(500);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void nofityUpdate(float progress) {
        setCurrentProgress((int) (progress));
    }

    public String getProgressText() {
        return  (int) ((currentProgress / maxProgress) * 100) + "%";
    }
}
