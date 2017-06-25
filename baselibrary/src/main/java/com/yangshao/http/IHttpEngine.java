package com.yangshao.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/14.
 */

public interface IHttpEngine {
    //post
    void post(Context context,String url, Map<String,Object> params, EngineCallBack callBack);

    //get
    void get(Context context,String url, Map<String,Object> params,EngineCallBack callBack);

    //文件下载

    //文件上传

    //https证书
}
