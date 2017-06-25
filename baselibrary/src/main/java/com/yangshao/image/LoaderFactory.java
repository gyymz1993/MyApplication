package com.yangshao.image;

import android.content.Context;

/**
  * @author: gyymz1993
  * 创建时间：2017/4/1 17:50
  * @version  imagerLoader生成器
  *
 **/
public class LoaderFactory {
    private static volatile IEngineIgLoader sImageLoader;
    public static void initLoaderFactory(Context context){
        if (sImageLoader==null){
            synchronized (LoaderFactory.class){
                if (sImageLoader==null){
                    sImageLoader=new PicassoImageLoader(context);
                }
            }
        }
    }
    public static IEngineIgLoader getInstance(){
        if (sImageLoader==null){
            throw new NullPointerException("请先初始化initLoaderFactory");
        }
        return sImageLoader;
    }
}
