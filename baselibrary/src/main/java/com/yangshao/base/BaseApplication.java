package com.yangshao.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.alipay.euler.andfix.patch.PatchManager;
import com.android.volley.RequestQueue;
import com.yangshao.ExceptionCrashHander;
import com.yangshao.fix.DexFixUtils;
import com.yangshao.http.volley.VolleySingleton;
import com.yangshao.utils.SpUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/3/14.
 */

public  class BaseApplication  {
    private static Looper mMainThreadLooper = null;
    private static Handler mMainThreadHandler = null;
    private static int mMainThreadId;
    private static Thread mMainThread = null;
    private static Application mApplication;
    private static BaseApplication mBaseApplication;
    // volley请求队列
    public RequestQueue requestQueue = null;
    public PatchManager patchManager;
    private BaseApplication(){
    }

    public static BaseApplication instance(){
        if (mBaseApplication==null){
            synchronized (BaseApplication.class){
                if (mApplication==null){
                    mBaseApplication=new BaseApplication();
                }
            }
        }
        return mBaseApplication;
    }

    public  void initialize(Application application) {
        mApplication=application;
        if (mApplication!=null){
            mMainThreadLooper = mApplication.getMainLooper();
            mMainThreadHandler = new Handler();
            mMainThreadId = android.os.Process.myTid();
            mMainThread = Thread.currentThread();
            initVolley();
            initException();
            initSputils();
           // initAliRedPatch();
            //initFixDex();
        }
    }

    private void initSputils() {
        SpUtils.getInstance().init(mApplication);
    }


    /**
    * 阿里热修复
    * */
    private void initAliRedPatch() {
        patchManager  = new PatchManager(mApplication);
        patchManager.init(AppHelper.getAppVersion(mApplication));//current version
        /**加载之前修复的**/
        patchManager.loadPatch();
    }


    public DexFixUtils mDexFixUtils;
    /**
     * 阿里热修复
     * */
    private void initFixDex(){
        mDexFixUtils = new DexFixUtils();
        mDexFixUtils.init(mApplication);//current version
        /**加载之前修复的**/
        mDexFixUtils.loadFixDex();
    }


    /***
     * 全局异常处理
     * */
    public void initException(){
           /* 全局异常崩溃处理 */
        ExceptionCrashHander.getInstance().init(mApplication);
        // 获取上次的崩溃信息
        File crashFile = ExceptionCrashHander.getInstance().getCrashFile();
        // 上传到服务器，后面再说.......
    }

    //初始化 volley请求队列
    private void initVolley() {
        requestQueue = VolleySingleton.getInstance(mApplication.getApplicationContext()).getRequestQueue();
    }


    public static Application getApplication() {
        if (mApplication==null)
            throw new NullPointerException("mApplication 为空");
            return mApplication;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public  static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

}
