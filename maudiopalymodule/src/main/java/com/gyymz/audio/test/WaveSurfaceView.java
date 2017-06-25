package com.gyymz.audio.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yangshao.utils.UIUtils;


/**
 * 该类只是一个初始化surfaceview的封装
 * 
 * @author cokus
 */
public class WaveSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder holder;
	private int line_off;// 上下边距距离
	GestureDetector mGestureDetector;
	private float mTouchStart;
	private int mTouchInitialOffset;
	private int mOffset;
	int density = 2;
	public int circle_radius = 3;

	public int getLine_off() {
		return line_off;
	}

	public void setLine_off(int line_off) {

		this.line_off = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, line_off, getResources()
						.getDisplayMetrics());
		// this.line_off = line_off;
	}

	public WaveSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.holder = getHolder();
		holder.addCallback(this);
		init();
	}

	public WaveSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.holder = getHolder();
		holder.addCallback(this);
		init();
	}

	private void init() {
		density = UIUtils.WHD()[2];
		circle_radius *= density;
		mGestureDetector = new GestureDetector(getContext(),
				new GestureDetector.SimpleOnGestureListener() {
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float vx, float vy) {

						return true;
					}
				});
	}

	boolean isInit = false;

	/**
	 * @author cokus init surfaceview
	 */
	public void initSurfaceView(final SurfaceView sfv) {

		new Thread() {
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (waveCanvas != null) {
					waveCanvas.initDraw(sfv);
				} else {
					 WaveSurfaceView.this.setLine_off(42);
					new WaveCanvas().initDraw(sfv);// 清屏
				}
			};
		}.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		isInit = true;
		initSurfaceView(this);
	}

	public int getmOffset() {
		return mOffset;
	}

	public void setmOffset(int mOffset) {
		this.mOffset = mOffset;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) { return true; }

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// mTouchStart = event.getX();
			// mTouchInitialOffset = mOffset;
			break;
		case MotionEvent.ACTION_MOVE:
			// mOffset=(int) (mTouchInitialOffset + (mTouchStart -
			// event.getX()));
			if (waveCanvas != null) {
				// waveCanvas.updateView();
			}
			// offsetLeftAndRight(-mOffset);
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	WaveCanvas waveCanvas;

	public void setWaveCanvas(WaveCanvas waveCanvas) {
		this.waveCanvas = waveCanvas;
	}

}
