package com.gyymz.audio.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.gyymz.audio.R;
import com.yangshao.utils.UIUtils;

/**
 * 录音按钮动画效果
 * 
 * @author WQ 下午2:03:24
 */
public class RecordAnimationView extends View implements OnClickListener {

	private Bitmap record_center;
	private Paint basePaint, otherPaint, progressPaint;
	int progressBgColor = Color.parseColor("#cfcfd2");
	boolean isPress = false;
	int progressWidth = 3;
	int progressRadius ;
	int currentProgress = 70, maxProgress = 100;
	private boolean hasProgress;
	public static final int STATUS_READY=1,STATUS_RECORDING=2,STATUS_SUCCEND=3;

	public RecordAnimationView(Context context) {
		this(context,null);
	}
	public RecordAnimationView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public RecordAnimationView(Context context, AttributeSet attrs,
							   int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public int getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(int currentProgress) {
		if(currentProgress>maxProgress)
			currentProgress=maxProgress;
		setHasProgress(true);
		this.currentProgress = currentProgress;
		postInvalidate();
	}

	int width,height;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int size= MeasureSpec.getSize(widthMeasureSpec);
		int model=MeasureSpec.getMode(widthMeasureSpec);
		if (model==MeasureSpec.EXACTLY){
			width=size;
		}else {
			width=  progressRadius*2+progressWidth;
		}
		size=MeasureSpec.getSize(heightMeasureSpec);
		model=MeasureSpec.getMode(heightMeasureSpec);
		if (model==MeasureSpec.EXACTLY){
			height=size;
		}else {
			height= progressRadius*2+progressWidth;
		}
		setMeasuredDimension(width,height);
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public void setHasProgress(boolean hasProgress) {
		this.hasProgress = hasProgress;
		postInvalidate();
	}

	int getAngle() {
		return (int) (360 * (currentProgress * 1f / maxProgress));
	}

	private void init() {
		basePaint = new Paint();
		basePaint.setAntiAlias(true);
		otherPaint = new Paint();
		otherPaint.setColor(Color.parseColor("#22000000"));// 全透明
		otherPaint.setStyle(Style.FILL);
		progressPaint = new Paint();
		progressPaint.setAntiAlias(true);
		progressBgColor=getResources().getColor(R.color.main_btn_);
		int density= UIUtils.WHD()[2];
		if (record_center == null) {
			record_center = zoomImage(R.drawable.record_center_2);
		}
		progressWidth=progressWidth*density;
		setBackgroundResource(R.drawable.record_anim);
	}

	
	public void setStatus(int status){
		switch (status) {
		case STATUS_READY:
			record_center = zoomImage(R.drawable.record_center);
			break;
		case STATUS_RECORDING:
			record_center = zoomImage(R.drawable.record_center_2);
			break;
		case STATUS_SUCCEND:
			record_center = zoomImage(R.drawable.record_center_3);
			break;
		default:
			break;
		}
		postInvalidate();
	}



	/***
	 * 图片的缩放方法
	 * @return
	 */
	float btWidth,btHeight;
	public Bitmap zoomImage(int res) {
		Bitmap bgimage = BitmapFactory.decodeResource(getResources(), res, null);
		// 获取这个图片的宽和高
		btWidth  = bgimage.getWidth();
		btHeight = bgimage.getHeight();
		progressRadius= (int) Math.min(btWidth,btHeight)/2;
		return bgimage;
	}

	public int getCenterX() {
		return getWidth() / 2;
	}

	public int getCenterY() {
		return getHeight() / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (record_center == null) {
			record_center = zoomImage(R.drawable.record_center);
		}
		canvas.drawBitmap(record_center,
				getWidth() / 2 - record_center.getWidth() / 2, getHeight() / 2
						- record_center.getWidth() / 2, basePaint);
		if (hasProgress) {
			drawProgress(canvas);
		}
		super.onDraw(canvas);
	}

	private void drawProgress(Canvas canvas) {
		//progressRadius=(int) Math.min(btWidth,btHeight);;
		progressRadius=(int) (getWidth()*(90./205.)+0.5);
		progressPaint.setStyle(Style.STROKE);
		progressPaint.setStrokeWidth(progressWidth);
		progressPaint.setColor(progressBgColor);
		progressPaint.setShader(null);
		canvas.drawArc(new RectF(getCenterX() - progressRadius, getCenterY()
				- progressRadius, getCenterX() + progressRadius, getCenterY()
				+ progressRadius), -90, getAngle(), false, progressPaint);

	}

	public void start() {
		setStatus(STATUS_RECORDING);
		AnimationDrawable drawable = (AnimationDrawable) getBackground();
		if (!drawable.isRunning()) {
			drawable.start();
		}
		invalidate();
	}
	public void stop() {
		setStatus(STATUS_READY);
		AnimationDrawable drawable = (AnimationDrawable) getBackground();
		if (drawable.isRunning()) {
			drawable.stop();
			drawable.selectDrawable(0);
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isPress = true;
			invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_UP:
			isPress = false;
			invalidate();
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {

	}

}
