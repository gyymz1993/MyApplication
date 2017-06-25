package com.yangshao.http.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义 Request 往里添加同一cookie
 * 基于无论是返回JsonObject还是String,封装返回Object
 * Velloy为我们提供的请求 都是无Cookie管理的
 */
public class CookieObjectRequest<T> extends StringRequest {
    public static final String CONTENT_TYPE = "Content-Type";
	private Map<String, String> mHeaders = new HashMap<String, String>();
	public static String cookie = "";// 静态标示 cookie
	public static Boolean isTruncate = true;// 截取第一次获得的cookie
    //实体对象字节码
    private  Class<T> mClazz;

	public CookieObjectRequest(int method, String url, Class<T> clazz,
                               Response.Listener listener, Response.ErrorListener errorListener) {
        super(method,url,listener,errorListener);
        this.mClazz=clazz;
	}

	public void setCookie() {
		Log.e("","cookie:" + cookie);
		mHeaders.put("accept", "*/*");
		mHeaders.put("connection", "Keep-Alive");
		if (!cookie.equals(""))
			mHeaders.put("Cookie", cookie);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String TYPE_UTF8_CHARSET = "charset=UTF-8";
		try {
			String type = response.headers.get(CONTENT_TYPE);
			if (type == null) {
				type = TYPE_UTF8_CHARSET;
				response.headers.put(CONTENT_TYPE, type);
			}
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			 //使用正则表达式从reponse的头中提取cookie内容的子串
			Pattern pattern = Pattern.compile("Set-Cookie.*?;");
			Matcher m = pattern.matcher(response.headers.toString());
			if (m.find()) {
				// 去掉cookie末尾的分号
				if (isTruncate) {
					isTruncate = false;
					cookie =  m.group().substring(11,  m.group().length() - 1);
				}
			}
			//L_.e("LOG", "cookie substring " + cookie);
			return Response.success(jsonString,
					HttpHeaderParser.parseCacheHeaders(response));
     //       Object o = ResponseEntityToModule.parseJsonToModule(jsonString, mClazz);
       //     return (Response<T>) Response.success(o, HttpHeaderParser.parseCacheHeaders(response));
            /*将返回的Json字符串直接转化为对象*/
        } catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}



}
