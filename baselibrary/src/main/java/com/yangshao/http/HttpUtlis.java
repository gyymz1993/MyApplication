package com.yangshao.http;

import android.content.Context;

import com.yangshao.http.okhttp3.DefaultOkHttp;
import com.yangshao.http.okhttp3.PersistentCookieStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/14.
 */

public class HttpUtlis{

    /****/
    private String mUrl;
    /*提交方式*/
    private int mType;

    private static final int POST_TYPE=0X0011;
    private static final int GET_TYPE=0X0000;
    private Context mContext;

    /**默认引擎**/
    public static IHttpEngine iHttpEngine=new DefaultOkHttp();

    private PersistentCookieStore cookieStore;//永久化保存cookie
    private Map<String,Object> mPararm;
    public HttpUtlis(Context context){
        mContext=context;
        mPararm=new HashMap<>();
    }


    /*添加参数*/
    public HttpUtlis addUrl(String url){
        this.mUrl=url;
        return this;
    }

    /*添加参数*/
    public HttpUtlis addPararm(String key,Object value){
        mPararm.put(key,value);
        return this;
    }


    /*添加参数*/
    public HttpUtlis addPararm(Map<String,Object> map){
        mPararm.putAll(map);
        return this;
    }

    /*添加回调*/
    public void execute(EngineCallBack callBack){
        if (callBack==null){
            callBack=EngineCallBack.DEFAULT_CALL_BACK;
        }
        if (mType==POST_TYPE){
            post(mUrl,mPararm,callBack);
        }else {
            get(mUrl,mPararm,callBack);
        }
    }

    public static HttpUtlis with(Context context){
        return new HttpUtlis(context);
    }
    /**
     * 自己初始化引擎
     * **/
    public static void initEngine(IHttpEngine engine){
        iHttpEngine=engine;
    }

    /**
     * 自己切换引擎
     * **/
    public void exchangeEngine(IHttpEngine engine){
        iHttpEngine=engine;
    }

    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        iHttpEngine.post(mContext,url,params,callBack);
    }

    protected void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        iHttpEngine.post(mContext,url,params,callBack);
    }

    public HttpUtlis post(){
        mType=POST_TYPE;
        return this;
    }

    public HttpUtlis get(){
        mType=GET_TYPE;
        return this;
    }
}
