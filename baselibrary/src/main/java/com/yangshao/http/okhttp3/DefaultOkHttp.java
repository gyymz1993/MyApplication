package com.yangshao.http.okhttp3;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yangshao.http.EngineCallBack;
import com.yangshao.http.IHttpEngine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/14.
 */

public class DefaultOkHttp implements IHttpEngine {

    static DefaultOkHttp mDefaultOkHttp;
    private static final String TAG = "";
    private Handler mainHandler;
    private final int READ_TIMEOUT = 10;
    private final int WRITE_TIMEOUT = 10;
    private final int CONNECT_TIMEOUT = 1000 * 60;
    private final String HTTP_CACHE_FILENAME= "HttpCache";
    //json请求
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");
    private OkHttpClient mOkHttpClient;
    private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .removeHeader("Pragma").header("Cache-Control", String.format("max-age=%d", 60))
                    .build();
        }
    };

    public DefaultOkHttp() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器（后面讲解），注意这里需要使用.addNetworkInterceptor
                .build();
                 mainHandler = new Handler(Looper.getMainLooper());
    }



    //需要Cookie的时候调用
    public DefaultOkHttp init(Context context){
        setContent(context);
        return this;
    }

    Context mContext;
    private PersistentCookieStore cookieStore;//永久化保存cookie
    private void setContent(Context content){
        this.mContext=content;
        cookieStore =new PersistentCookieStore(content);
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        FormBody.Builder builder=new FormBody.Builder();
        if (params!=null){
            for (Map.Entry<String,Object> entry:params.entrySet()){
                builder.add(entry.getKey(),entry.getValue().toString());
            }
        }
        RequestBody body=builder.build();
        Request request=new Request.Builder().url(url).post(body).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    callBack.onSuccess(response.body().toString());
                }else {
                    callBack.onSuccess(response.message());
                }
            }
        });
    }

    @Override
    public void get(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        //RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body().toString());
                } else {
                    callBack.onSuccess(response.message());
                }
            }
        });
    }
}
