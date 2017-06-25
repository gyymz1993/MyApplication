package com.gyymz.audio.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;

import com.gyymz.audio.Pcm2Wav;
import com.gyymz.audio.R;
import com.yangshao.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * 录音和写入文件使用了两个不同的线程
 * 以免造成卡机现象 录音波形绘制
 */
public class WaveCanvas {
    int limit_size = 20;
    RecordBuffer buffer_listener;
    public static final int BUFFER_SIZE = 256;
    private LinkedList<Short> inBuf = new LinkedList<Short>();// 缓冲区数据
    private LinkedList<byte[]> write_data = new LinkedList<>();// 写入文件数据

    String kedus[] = new String[]{"  0", " -1", " -2", " -3", " -5", " -7", "-10", "-10", " -7", " -5",
            " -3", " -2", " -1", "  0"};
    int leftOffect = 24;

    public boolean isRecording = false;// 录音线程控制标记
    private boolean isWriting = false;// 录音线程控制标记

    public int circle_radius = 4;
    private int line_off;// 上下边距的距离

    public int rateX = 32;// 控制多少帧取一帧
    public int baseLine = 0;// Y轴基线

    int density = 2;
    private AudioRecord audioRecord;

    int recBufSize;
    private int marginRight = 20;// 波形图绘制距离右边的距离
    private int draw_time = 20;// 两次绘图间隔的时间
    private float divider = 0.2f;// 为了节约绘画时间，每0.2个像素画一个数据
    long c_time;
    private String savePcmPath;// 保存pcm文件路径
    private String saveWavPath;// 保存wav文件路径
    private Paint circlePaint;
    private Paint centerL, centerR;
    private Paint paintLine;
    private Paint paintProgress;
    private Paint paintWaveShade;
    long readLong = 0;
    private Paint mPaint;
    private Paint mTextPaint;
    float progress_x;
    private int readsize;
    public RecordTask task;
    int maxMin = 2;
    boolean isInit = false;
    float time_width;
    int removeSize = 0;
    boolean isPause;
    boolean isReady = false;
    public boolean isReady() {
        return isReady;
    }
    public void pause() {
        isPause = true;
    }

    public void resume() {
        isPause = false;
    }

    public boolean isPause() {
        return isPause;
    }

    /**
     * 开始录音
     *
     * @param audioRecord
     * @param recBufSize
     * @param sfv
     * @param audioName
     */
    public void Start(AudioRecord audioRecord, int recBufSize, WaveSurfaceView sfv,
                      String audioName, String path,
                      Callback callback) {
        this.audioRecord = audioRecord;
        isRecording = true;
        isWriting = true;
        this.recBufSize = recBufSize;
        savePcmPath = path + audioName + ".pcm";
        saveWavPath = path + audioName + ".wav";

        init(sfv);
        new Thread(new WriteRunnable()).start();// 开线程写文件
        task = new RecordTask(audioRecord, recBufSize, sfv, mPaint, callback);
        task.execute();
    }

    public void init(SurfaceView sfv) {
        if (isInit)
            return;
        circlePaint = new Paint();// 画圆
        circlePaint.setStrokeWidth(2);// 设置画笔粗细
        circlePaint.setColor(sfv.getResources().getColor(R.color.main_btn_));// 设置上圆的颜色

        centerL = new Paint();
        centerL.setColor(Color.parseColor("#7f8082"));// 画笔为color
        centerL.setStrokeWidth(2);// 设置画笔粗细
        centerL.setAntiAlias(true);
        centerL.setFilterBitmap(true);
        centerL.setStyle(Style.FILL);
        centerR = new Paint();
        centerR.setColor(sfv.getResources().getColor(R.color.main_btn_));// 画笔为color
        centerR.setStrokeWidth(2);// 设置画笔粗细
        centerR.setAntiAlias(true);
        centerR.setFilterBitmap(true);
        centerR.setStyle(Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.parseColor("#7f8082"));
        paintProgress = new Paint();

        paintProgress.setColor(Color.WHITE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true); // 消除锯齿
        mTextPaint.setStyle(Style.FILL);
        mTextPaint.setColor(Color.parseColor("#7f8082"));// 全透明paintWaveShade.setColor(Color.WHITE);

        paintWaveShade = new Paint();
        paintWaveShade.setAntiAlias(true); // 消除锯齿
        paintWaveShade.setStyle(Style.FILL);
        paintWaveShade.setColor(Color.parseColor("#22FFFFFF"));// 全透明paintWaveShade.setColor(Color.WHITE);

        mPaint = new Paint();
        // mPaint.setColor(Color.WHITE);// 画笔为color
        mPaint.setColor(sfv.getResources().getColor(R.color.main_btn_));
        mPaint.setStrokeWidth(2);// 设置画笔粗细
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Style.FILL);

        density = UIUtils.WHD()[2];
        marginRight = sfv.getWidth() / 2;
        circle_radius *= density;
        leftOffect *= density;
        paintProgress.setStrokeWidth(circle_radius / 2);
        line_off = ((WaveSurfaceView) sfv).getLine_off();
        Rect txtRect = new Rect();
        mTextPaint.setTextSize(UIUtils.sp2px(9));
        mTextPaint.getTextBounds("\t 00:00   \t", 0, "\t 00:00   \t".length(),
            txtRect);
        time_width = txtRect.width() * 2;// 计算每大格的宽度
        divider = time_width / ((1f * recBufSize / rateX) * (60));
        isInit = true;
    }

    /**
     * 停止录音
     */
    public void Stop() {
        Log.e("test", "stop start");
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
        isReady = false;
    }

    /**
     * 清楚数据
     */
    public void clear(SurfaceView sfv) {
        init(sfv);
        inBuf.clear();// 清除
        SimpleDraw(inBuf, sfv, true);
    }

    public void initDraw(SurfaceView sfv) {
        // 已经初始化过了,不做任何处理
        if (!isInit) {
            init(sfv);
            inBuf.clear();
        }
        SimpleDraw(inBuf, sfv, true);
    }

    public void balance(List<Short> source, int index) {
        short curVal = source.get(index);
        if (curVal == 0) return;
        short midile = calMiddle(source, index, curVal);
        if (Math.abs(curVal - midile) > 100) {
            source.set(index, midile);
        }
    }

    public short calMiddle(List<Short> source, int index, short curval) {
        int left = 0;
        int right = 0;
        for (int i = 1; i <= 5; i++) {
            left += getLefVal(source, index - i, curval);
            right += getRightVal(source, index + i, curval);
        }
        return (short) ((left + right) / 10);
    }

    public Short getLefVal(List<Short> source, int index, Short def) {
        if (index < 0 || index >= source.size()) return def;
        short val = source.get(index);
        if (val == 0) return getLefVal(source, index - 1, def);
        return val;
    }

    public Short getRightVal(List<Short> source, int index, Short def) {
        if (index < 0 || index >= source.size()) return def;
        short val = source.get(index);
        if (val == 0) return getLefVal(source, index + 1, def);
        return val;
    }

    /**
     * 绘制指定区域
     *
     * @param buf 缓冲区
     *            Y轴基线
     */

    void SimpleDraw(List<Short> buf, SurfaceView sfv, boolean isClear) {
        if (!isRecording && !isClear)
            return;
        int baseLine = sfv.getHeight() / 2;
        Canvas canvas = sfv.getHolder().lockCanvas(
            new Rect(0, 0, sfv.getWidth(), sfv.getHeight()));// 关键:获取画布
        if (canvas == null)
            return;
        canvas.drawColor(Color.parseColor("#2a2b2f"));// 清除背景
        int start = (int) ((buf.size()) * divider);
        float y = 0;
        if (sfv.getWidth() - (start + 1) <= marginRight) {// 如果超过预留的右边距距离
            start = sfv.getWidth() - marginRight;// 画的位置x坐标
            progress_x += ((1f * removeSize) * divider);// 每次会绘画移动的距离
        } else {
            progress_x = start;
        }


        int height = sfv.getHeight() - line_off;
        for (int i = 0; i < buf.size(); i++) {
            balance(buf, i);
            float val = (height) * (buf.get(i) / (65535f / 20));
            y = val + baseLine;// 调节缩小比例，调节基准线
            y = Math.min(y, height);
            y = Math.max(y, line_off / 2);
            float x = (i) * divider;
            if (sfv.getWidth() - (i - 1) * divider <= marginRight) {
                x = sfv.getWidth() - marginRight;
            }
            // 画线的方式很多，你可以根据自己要求去画。这里只是为了简单
            if (buf.get(i) != 0 && buf.get(i) != -32768
                && (buf.size() * divider > leftOffect)) {
                canvas.drawLine(x - ((WaveSurfaceView) sfv).getmOffset(), y
                        + line_off / 4,
                    x - ((WaveSurfaceView) sfv).getmOffset(),
                    sfv.getHeight() - (y - line_off / 4), mPaint);// 中间出波形
            }
        }

        Rect txtRect = new Rect();
        mTextPaint.setTextSize(UIUtils.sp2px(8));
        mTextPaint.getTextBounds("-10", 0, 2, txtRect);
        int centerLine_y = sfv.getHeight() / 2 + line_off / 4;
        int len = 6;
        int sfvH = sfv.getHeight();
        int padding = sfvH / 16;
        for (int i = len, j = 7; i >= 0; i -= 1, j++) {
            int yy = (len - i) * (padding);
            drawTxt(kedus[i], sfv.getWidth() - 15 * density, centerLine_y - yy
                - padding, canvas, mTextPaint);
            drawTxt(kedus[j], sfv.getWidth() - 15 * density, centerLine_y + yy,
                canvas, mTextPaint);
        }

        float prog_x_offect = Math.max(0,
            (progress_x + marginRight + 1) - sfv.getWidth());
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.set(Calendar.SECOND, 0);
        tmpCal.set(Calendar.MINUTE, 0);

        tmpCal.set(Calendar.SECOND, 00);
        mTextPaint.setTextSize(UIUtils.sp2px(9));
        mTextPaint.getTextBounds("\t 00:00   \t", 0, "\t 00:00   \t".length(),
            txtRect);
        for (int i = 0; tmpCal.get(Calendar.MINUTE) <= maxMin; i++) {
            String time = DateUtil.getStringByFormat(tmpCal.getTime(),
                "\t mm:ss   \t");
            float time_width = txtRect.width() * 2;
            float time_x = (-prog_x_offect) + i * time_width;
            time_x = time_x + leftOffect;
            canvas.drawText(time, time_x + 5, 5 + line_off / 6f + txtRect.height()
                / 2, mTextPaint);
            tmpCal.add(Calendar.SECOND, 01);
            if (i == 0) {
                canvas.drawLine(leftOffect / 2, line_off / (2.5f),
                    leftOffect / 2, line_off / 2, mTextPaint);
            }
            // 绘制刻度
            canvas.drawLine(time_x, line_off / 6f, time_x, line_off / 2,
                mTextPaint);
            canvas.drawLine(time_x + time_width / 4, line_off / (2.5f), time_x
                + time_width / 4, line_off / 2, mTextPaint);
            canvas.drawLine(time_x + time_width / 2, line_off / (2.5f), time_x
                + time_width / 2, line_off / 2, mTextPaint);
            canvas.drawLine(time_x + time_width / 4 * 3, line_off / (2.5f),
                time_x + time_width / 4 * 3, line_off / 2, mTextPaint);

        }
        drawBase(canvas, sfv, start, isClear);
        sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
    }

    public void drawTxt(String txt, float x, float y, Canvas canvas, Paint paint) {
        paint.setTextAlign(Align.LEFT);
        Rect bounds = new Rect();
        paint.getTextBounds(txt, 0, txt.length(), bounds);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (int) (y - (fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
        canvas.drawText(txt, x - bounds.width() / 2, baseline, paint);

    }

    public void drawBase(Canvas canvas, SurfaceView sfv, int start,
                         boolean isClear) {
        int leftOffect = this.leftOffect;
        canvas.drawLine(0, line_off / 2, sfv.getWidth(), line_off / 2,
            paintLine);// 最上面的那根线
        canvas.drawRect(0, sfv.getHeight() - circle_radius, sfv.getWidth(),
            sfv.getHeight(), paintProgress);// 底部留白
        if (start < leftOffect) {
            canvas.drawLine(0, sfv.getHeight() / 2 + line_off / 4, 0,
                sfv.getHeight() / 2 + line_off / 4, centerR);// 中心线,左边,红色
            canvas.drawLine(0, sfv.getHeight() / 2 + line_off / 4,
                sfv.getWidth(), sfv.getHeight() / 2 + line_off / 4, centerL);// 中心线右边灰色

            canvas.drawCircle(leftOffect, line_off / 2, circle_radius,
                circlePaint);// 上圆
            canvas.drawCircle(leftOffect, sfv.getHeight() - circle_radius,
                circle_radius, circlePaint);// 下圆
            canvas.drawLine(leftOffect, line_off / 2, leftOffect,
                sfv.getHeight(), circlePaint);// 垂直的线
        } else {
            isReady = true;
            canvas.drawLine(0, sfv.getHeight() / 2 + line_off / 4, start,
                sfv.getHeight() / 2 + line_off / 4, centerR);// 中心线,左边,红色
            canvas.drawLine(start, sfv.getHeight() / 2 + line_off / 4,
                sfv.getWidth(), sfv.getHeight() / 2 + line_off / 4, centerL);// 中心线右边灰色

            canvas.drawCircle(start, line_off / 2, circle_radius, circlePaint);// 上圆
            canvas.drawCircle(start, sfv.getHeight() - circle_radius,
                circle_radius, circlePaint);// 下圆
            canvas.drawLine(start, line_off / 2, start, sfv.getHeight(),
                circlePaint);// 垂直的线
        }
    }

    public byte[] getBytes(short s) {
        byte[] buf = new byte[2];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (s & 0x00ff);
            s >>= 8;
        }
        return buf;
    }

    /**
     * 异步录音程序
     *
     * @author cokus
     */
    public class RecordTask extends AsyncTask<Object, Object, Object> {
        private int recBufSize;
        private AudioRecord audioRecord;
        private SurfaceView sfv;// 画板
        private Paint mPaint;// 画笔
        private Callback callback;
        private boolean isStart = false;

        public RecordTask(AudioRecord audioRecord, int recBufSize,
                          SurfaceView sfv, Paint mPaint, Callback callback) {
            this.audioRecord = audioRecord;
            this.recBufSize = recBufSize;
            this.sfv = sfv;
            Rect txtRect = new Rect();
            mPaint.setTextSize(UIUtils.sp2px(9));
            mPaint.getTextBounds("\t 00:00 \t", 0, "\t 00:00 \t".length(),
                txtRect);
            ((WaveSurfaceView) sfv).setWaveCanvas(WaveCanvas.this);
            line_off = ((WaveSurfaceView) sfv).getLine_off();
            this.mPaint = mPaint;
            this.callback = callback;
            inBuf.clear();// 清除
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                short[] buffer = new short[recBufSize];
                audioRecord.startRecording();// 开始录制
                Message msg = new Message();
                msg.what = 1;
                callback.handleMessage(msg);
                while (isRecording) {
                    // 从MIC保存数据到缓冲区
                    readsize = audioRecord.read(buffer, 0, recBufSize);
                    synchronized (inBuf) {
                        for (int i = 0, j = 0; i < readsize; i += rateX, j++) {
                            int val = buffer[i];
                            if (readLong % limit_size == 0 && readLong > 200) {
                                inBuf.add((short) (val));
                            } else {
                                inBuf.add((short) 0);
                            }
                            readLong++;
                        }
                    }
                    publishProgress();
                    if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
                        synchronized (write_data) {
                            byte bys[] = new byte[readsize * 2];
                            // 因为arm字节序问题，所以需要高低位交换
                            for (int i = 0; i < readsize; i++) {
                                byte ss[] = getBytes(buffer[i]);
                                bys[i * 2] = ss[0];
                                bys[i * 2 + 1] = ss[1];
                            }
                            write_data.add(bys);
                        }
                    }
                }
                isWriting = false;
            } catch (Throwable t) {
                Message msg = new Message();
                msg.arg1 = -2;
                msg.what = -1;
                msg.obj = t.getMessage();
                callback.handleMessage(msg);
            }
            return null;
        }

        long oldtime = 0;
        @Override
        protected void onProgressUpdate(Object... values) {
            oldtime = System.currentTimeMillis();
            if (oldtime - c_time >= draw_time) {
                if (isPause())
                    return;
                LinkedList<Short> buf;
                synchronized (inBuf) {
                    if (inBuf.size() == 0)
                        return;
                    removeSize = 0;
                    while (inBuf.size() > (sfv.getWidth() - marginRight) / divider) {
                        removeSize++;
                        inBuf.remove(0);
                    }
                    buf = (LinkedList<Short>) inBuf.clone();// 保存
                }
                SimpleDraw(buf, sfv, false);// 把缓冲区数据画出来
                c_time = System.currentTimeMillis();
            }
            super.onProgressUpdate(values);
        }

    }

    /**
     * 异步写文件
     *
     * @author cokus
     */
    class WriteRunnable implements Runnable {
        @Override
        public void run() {
            try {
                FileOutputStream fos2wav = null;
                File file2wav = null;
                try {
                    file2wav = new File(savePcmPath);
                    if (file2wav.exists()) {
                        file2wav.delete();
                    }
                    fos2wav = new FileOutputStream(file2wav);// 建立一个可存取字节的文件
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (isWriting || write_data.size() > 0) {
                    byte[] buffer = null;
                    synchronized (write_data) {
                        if (write_data.size() > 0) {
                            buffer = write_data.get(0);
                            write_data.remove(0);
                        }
                    }
                    try {
                        if (buffer != null && !isPause()) {
                            fos2wav.write(buffer);
                            fos2wav.flush();
                            if (buffer_listener != null) {
                                buffer_listener.buffer(buffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                fos2wav.close();
                Pcm2Wav p2w = new Pcm2Wav();// 将pcm格式转换成wav 其实就尼玛加了一个44字节的头信息
                p2w.convertAudioFiles(savePcmPath, saveWavPath);
            } catch (Throwable t) {
            }
        }
    }

    public interface RecordBuffer {
        public void buffer(byte[] buffer);
    }

}
