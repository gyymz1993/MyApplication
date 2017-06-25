package com.yangshao.http.okhttp3;

import java.net.CookieStore;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


public class CookieJarImpl implements CookieJar {
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null)
            throw new IllegalArgumentException(String.format("cookieStore can not be null."));
        this.cookieStore = cookieStore;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return null;
    }
}