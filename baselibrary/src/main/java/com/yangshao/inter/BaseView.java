package com.yangshao.inter;

import android.os.Bundle;
import android.view.View;

/***
 *  初始化试图所用到的所有功能
 *
 * */

public interface  BaseView {


    /**
     * 页面跳转
     * **/
    void openActivity(Class<?> pClass, Bundle bundle);

    /***
     * 初始化布局文件
     * */
    void initSetContentView();

    /**
     * 初始化View
     * **/
    void initView();

    /**
     * 初始化数据
     * */
    void processLogic(Bundle saveInstanceState);

    void setListener();

    <T extends View> T viewFindById(int id);

}
