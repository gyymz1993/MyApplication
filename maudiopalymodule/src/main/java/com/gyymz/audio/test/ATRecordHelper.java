package com.gyymz.audio.test;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler.Callback;

import java.io.File;
import java.io.FilenameFilter;

/**
  * @author: gyymz1993
  * 创建时间：2017/4/22 10:42
  * @version 音频录制相关配置
  *
 **/
public class ATRecordHelper {
	private static final int FREQUENCY = 16000;// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	private static final int CHANNELCONGIFIGURATION = AudioFormat.CHANNEL_IN_MONO;// 设置单声道声道
	private static final int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;// 音频数据格式：每个样本16位
	private final static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;// 音频获取源
	private int recBufSize;  // 录音最小buffer大小
	private AudioRecord audioRecord;
	private static String DATA_DIRECTORY;
	private String mFileName = "record_" + System.currentTimeMillis();// 文件名
	private File mFile;// 文件名

	
	/**
	 * 创建录音临时文件,清理缓存文件
	 * @param context
	 */
	public File createRecordFile(Context context){
		DATA_DIRECTORY =context. getCacheDir().getAbsolutePath() + "/";
		FileUtils.delFile(new File(DATA_DIRECTORY), new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.startsWith("record_");
			}
		});
		mFile = new File(DATA_DIRECTORY + mFileName + ".wav");
		return mFile;
	}
	
	/**
	 * 初始化录音模块,初始化波形模块
	 * @param waveCanvas
	 * @param waveSfv
	 * @param callback
	 */
	public   void initAudio(WaveCanvas waveCanvas,WaveSurfaceView waveSfv,Callback callback) {
		try {
			recBufSize = AudioRecord.getMinBufferSize(FREQUENCY,
					CHANNELCONGIFIGURATION, AUDIOENCODING);// 录音组件
			audioRecord = new AudioRecord(AUDIO_SOURCE,// 指定音频来源，这里为麦克风
					FREQUENCY, // 16000HZ采样频率
					CHANNELCONGIFIGURATION,// 录制通道
					AUDIO_SOURCE,// 录制编码格式
					recBufSize);// 录制缓冲区大小 //先修改
			waveCanvas.Start(audioRecord, WaveCanvas.BUFFER_SIZE, waveSfv,
					mFileName, DATA_DIRECTORY, callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
