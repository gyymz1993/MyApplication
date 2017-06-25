package com.yangshao.http.volley;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yangshao.base.BaseApplication;
import com.yangshao.http.EngineCallBack;
import com.yangshao.http.IHttpEngine;


import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */

public class VolleyUtils implements IHttpEngine {
    /*请求超时时间*/
    protected static final int REQUEST_TIMEOUT=10000;
    /*请求Volley队列*/
    protected RequestQueue mQueue;
    @Override
    public void post(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(context);
        BaseApplication.instance().requestQueue.add(request);

    }

    @Override
    public void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack) {
    }


//    GetCookieRequestPurePage(this, "", listener, errorListener,
//            User.class);
    //获取网页或者特殊数据的时候  可以直接返回Object
    public static void GetCookieRequestPurePage (Context context, String url,final EngineCallBack callBack,Class clazz){
        CookieObjectRequest jsonObjectRequest=new CookieObjectRequest(Request.Method.GET, url, null, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(callBack!=null)
                    callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callBack!=null){
                    callBack.onError(error);
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setCookie();
        jsonObjectRequest.setTag(context);
        BaseApplication.instance().requestQueue.add(jsonObjectRequest);
    }

}
