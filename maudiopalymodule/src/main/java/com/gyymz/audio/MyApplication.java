package com.gyymz.audio;

import android.app.Application;

import com.yangshao.base.BaseApplication;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/4/14/12:13
 **/
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.instance().initialize(this);
    }
}
