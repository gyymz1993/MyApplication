package com.yangshao.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yangshao.annotation.InjectUtility;
import com.yangshao.inter.BaseView;

/**
 * Created by Administrator on 2017/3/14.
 */

public abstract class BaseFragment extends Fragment implements BaseView {

    protected boolean isFirstCreate=true;
    protected View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return loadViewLayout(inflater,container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InjectUtility.initInjectedView(this,view);
        rootView=view;
        //绑定View
        bindView(view);
        //数据处理
        processLogic(savedInstanceState);
          //设置监听
        setListener();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            onVisible();
        }else {
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            onInvisible();
        }else {
            onVisible();
        }
    }

    protected  void onVisible(){
        if (isFirstCreate){
            lazyLoad();
            isFirstCreate=false;
        }
    }

    @Override
    public <T extends View> T viewFindById(int id) {
        return (T) rootView.findViewById(id);
    }

    protected  void lazyLoad(){}

    protected  void onInvisible(){}

    protected abstract View loadViewLayout(LayoutInflater inflater, ViewGroup viewGrop);

    protected abstract void bindView(View view);

}
