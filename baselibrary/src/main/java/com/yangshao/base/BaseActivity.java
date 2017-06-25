package com.yangshao.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yangshao.annotation.InjectUtility;
import com.yangshao.inter.BaseView;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InjectUtility.initInjectedView(this);

        AppManager.getAppManager().addActivity(this);

        initSetContentView();

        //初始化View
        initView();

        //设置监听
        setListener();

        //初始化数据
        processLogic(savedInstanceState);
    }



    /**
     * 页面跳转
     * **/

    /***
     * 初始化布局文件
     * */
    public abstract  void initSetContentView();

    /**
     * 初始化View
     * **/
    public abstract  void initView();

    /**
     * 初始化数据
     * */
     public abstract void processLogic(Bundle saveInstanceState);
    public abstract  void setListener();

    public <T extends View> T viewFindById(int id) {
        return (T) findViewById(id);
    }


    public void openActivity(Class<?> pClass, Bundle bundle){
        Intent intent=new Intent(this,pClass);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
