package com.gyymz.audio.test;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyymz.audio.AHandler;
import com.gyymz.audio.R;
import com.yangshao.annotation.ViewInject;
import com.yangshao.base.BaseActivity;

import java.io.File;
import java.util.Calendar;

@ViewInject(R.layout.ui_wave_record)
public class RecordWaveAct extends BaseActivity implements
    OnClickListener {
    @ViewInject(R.id.wavesfv)
    WaveSurfaceView waveSfv;
    @ViewInject(R.id.switchbtn)
    RecordAnimationView switchBtn;
    @ViewInject(R.id.status)
    TextView status;
    @ViewInject(R.id.tip_text)
    TextView tip_text;

    /**
     * 音频录制相关配置
     */
    public WaveCanvas waveCanvas;
    ATRecordHelper recordHelper = new ATRecordHelper();
    public File mFile;// 文件名
    AHandler.Task timeTask, uploadTask;
    public int maxRecordTime = 60;// 最大录音时间60s
    public int currentNo = 1;
    public String discernContent = "";
    int MAX_PROGRESS = 102;

    @Override
    protected void onCreate(Bundle arg0) {
        final Window win = getWindow();
        // 保持界面常亮,录音状态不熄屏
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(arg0);
    }

    @Override
    public void initSetContentView() {

    }

    @Override
    public void initView() {
        currentNo = getIntent().getIntExtra("currentNo", currentNo);
        mFile = recordHelper.createRecordFile(this);
        if (currentNo <= 2) {
            maxRecordTime = 45;
        } else {
            maxRecordTime = 60;
        }
        if (waveSfv != null) {
            waveSfv.setLine_off(42);
            // 解决surfaceView黑色闪动效果
            waveSfv.setZOrderOnTop(true);
            waveSfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        switchBtn.setOnClickListener(this);

    }

    @Override
    public void processLogic(Bundle saveInstanceState) {

    }

    @Override
    public void setListener() {

    }



    public boolean isReady() {
        return waveCanvas != null ? waveCanvas.isReady() : false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
    }

    int count = 0;

    void recordStop() {
        if (waveCanvas != null && waveCanvas.isRecording) {
            waveCanvas.Stop();
            switchBtn.stop();
            tip_text.setText("上传中..");
            switchBtn.setMaxProgress(MAX_PROGRESS);
            switchBtn.setCurrentProgress(0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (count * 10 <= 110) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        switchBtn.setCurrentProgress(count * 10);
                        count++;
                    }
                    switchBtn.setStatus(switchBtn.STATUS_SUCCEND);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tip_text.setText("上传成功");
                        }
                    });
                }
            }).start();
            switchBtn.setEnabled(false);
        }
        AHandler.cancel(timeTask);
        AHandler.cancel(uploadTask);
    }

    void recordReset() {
        recordStop();
        tip_text.setText("点击开始录制");
        waveCanvas.clear(waveSfv);// 清屏
        status.setText("00:00");
        switchBtn.setEnabled(true);
        switchBtn.setHasProgress(false);
        switchBtn.setStatus(1);
        discernContent = "";
    }

    void recordStart() {
        status.setText("00:00");
        switchBtn.start();
        waveSfv.setVisibility(View.VISIBLE);
        tip_text.setText("点击结束");
        switchBtn.setHasProgress(false);
        switchBtn.setEnabled(true);
        timeTask = new AHandler.Task() {
            Calendar tmpCal = Calendar.getInstance();

            {
                tmpCal.set(Calendar.SECOND, 0);
                tmpCal.set(Calendar.MINUTE, 0);
            }

            @Override
            public void task() {
                if (isReady())
                    tmpCal.add(Calendar.SECOND, 1);
            }

            public void update() {
                if (isReady()) {
                    status.setText(DateUtil.getStringByFormat(tmpCal.getTime(),
                        " mm:ss "));
                    if (getSECOND(tmpCal) >= maxRecordTime) {
                        if (waveCanvas == null || !waveCanvas.isRecording) {
                            initAudio();
                        } else {
                            recordStop();
                        }
                        return;
                    }
                }

            }
        };
        AHandler.runTask(timeTask, 1000, 1000);
    }

    public static long getSECOND(Calendar cal) {
        return cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switchbtn:
                if (waveCanvas == null || !waveCanvas.isRecording) {
                    initAudio();
                } else {
                    recordStop();
                }
                break;
        }
    }


    private void initAudio() {
        waveCanvas = new WaveCanvas();
        waveCanvas.baseLine = waveSfv.getHeight() / 2;
        recordHelper.initAudio(waveCanvas, waveSfv, new Handler.Callback() {
            @Override
            public boolean handleMessage(final Message msg) {
                if (msg.what == 1) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            recordStart();
                        }
                    });
                } else if (msg.what == -1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
                return true;
            }
        });

    }
}
