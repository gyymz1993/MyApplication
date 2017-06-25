/*
    ShengDao Android Client, BaseApplication
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.sd.one.activity;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sd.core.common.APPOnCrashListener;
import com.sd.core.common.AppCrashHandler;
import com.sd.core.common.CacheManager;
import com.sd.core.utils.NLog;
import com.sd.one.R;
import com.sd.one.utils.CommonUtils;

/** 
 * [系统Application类，设置全局变量以及初始化组件]
 * 
 * @author devin.hu
 * @version 1.0
 * @date 2013-9-17
 * 
 **/
public class BaseApplication extends Application implements APPOnCrashListener {

	private final String tag = BaseApplication.class.getSimpleName();
	private static DisplayImageOptions options;
	
	@Override
	public void onCreate() {
		init();
	}
	
	/**
	 * 初始化
	 */
	private void init(){
		//初始化debug模式
		String flag = CommonUtils.getProperty(getApplicationContext(), "debug");
		if (!TextUtils.isEmpty(flag)) {
			Boolean isDebug = Boolean.parseBoolean(flag);
			NLog.setDebug(isDebug);
			NLog.e(tag, "isDebug: " + isDebug);
		}
		
		//设置默认缓存路径
		CacheManager.setSysCachePath(getCacheDir().getPath());
		
		//初始化默认异常处理组件
		if(!NLog.isDebug()){
			AppCrashHandler crashHandler = AppCrashHandler.getInstance(getApplicationContext());
			crashHandler.setOnCrashListener(this);
		}
	       
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.showImageOnLoading(R.drawable.ic_launcher)
		.displayer(new FadeInBitmapDisplayer(300)) 
		//只有配置了这两句话，缓存才起作用
        .cacheInMemory(true)
        .cacheOnDisk(true)
		.build();
		
		//初始化图片下载组件
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.threadPriority(Thread.NORM_PRIORITY - 2)  
		.denyCacheImageMultipleSizesInMemory()
        .diskCacheSize(50 * 1024 * 1024)
        .diskCacheFileCount(200)
        .diskCacheFileNameGenerator(new Md5FileNameGenerator()) 
		.defaultDisplayImageOptions(options)
		.build();
		
		//Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onCrashDialog(Context context) {
		
	}

	@Override
	public void onCrashPost(String crashReport, File file) {
		
	}

}
