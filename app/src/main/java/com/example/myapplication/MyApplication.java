package com.example.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.yangshao.base.BaseApplication;
import com.yangshao.hook.HookUtil;
import com.yangshao.loading.BaseLoadingLayout;
import com.yangshao.skin.SkinManager;

import org.xutils.x;

import java.util.Iterator;

/**
 * Created by Administrator on 2017/3/14.
 */

public class MyApplication extends Application {


    private static MyApplication _application;// 全局上下文对象
    /**
     * 获得当前程序全局上下文实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        if (_application == null)
            throw new RuntimeException("MyApplication 未被正常初始化!");
        return _application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _application = this;
        BaseApplication.instance().initialize(this);
        HookUtil hookUtil = new HookUtil(MainActivity.class, this);
        hookUtil.hookAms();
        SkinManager.getInstance().init(this);
//        LoadingLayout.getConfig()
//                .setErrorText("出错啦~请稍后重试！")
//                .setEmptyText("抱歉，暂无数据")
//                .setNoNetworkText("无网络连接，请检查您的网络···")
//                .setErrorImage(R.mipmap.define_error)
//                .setEmptyImage(R.mipmap.define_empty)
//                .setNoNetworkImage(R.mipmap.define_nonetwork)
//                .setAllTipTextColor(R.color.gray)
//                .setAllTipTextSize(14)
//                .setReloadButtonText("点我重试哦")
//                .setReloadButtonTextSize(14)
//                .setReloadButtonTextColor(R.color.gray)
//                .setReloadButtonWidthAndHeight(150,40)
//                .setAllPageBackgroundColor(R.color.background);


        //.setLoadingPageLayout(R.layout.define_loading_page)

//        HttpUtlis.with(this).addPararm("", "").get().execute(new EngineCallBack() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onSuccess(Object result) {
//
//            }
//        });
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        BaseLoadingLayout.getmLoadConfiger()
            .setErrorText("出错啦~请稍后重试！")
            .setEmptyText("抱歉，暂无数据")
            .setNoNetworkText("无网络连接，请检查您的网络···")
            .setErrorImage(R.mipmap.define_error)
            .setEmptyImage(R.mipmap.define_empty)
            .setNoNetworkImage(R.mipmap.define_nonetwork)
            .setAllTipTextColor(R.color.gray)
            .setAllTipTextSize(14)
            .setReloadButtonText("点我重试哦")
            .setReloadButtonTextSize(14)
            .setReloadButtonTextColor(R.color.gray)
            .setReloadButtonWidthAndHeight(150, 40)
            .setAllPageBackgroundColor(R.color.background);
    }


    /**
     * 关闭指定Activity
     *
     * @param closes
     *            待关闭的Activity列表
     */
    public void closeActivitys(Class<?>... closes) {

    }
}
