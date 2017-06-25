package com.yangshao.http;

/**
 * Created by Administrator on 2017/3/14.
 */

public interface EngineCallBack<T> {

    void onError(Exception e);

    void onSuccess(T result);

    EngineCallBack DEFAULT_CALL_BACK=new EngineCallBack() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(Object result) {

        }
    };
}
