package com.yangshao.hook;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/3/25.
 */

public class HookViewClickUtil {
    static HookViewClickUtil mViewOnClickListener;
    static Context mContext;
    private HookViewClickUtil(Context context){
        this.mContext=context;
    }
    public static HookViewClickUtil getInstance(Context context){
        if (mViewOnClickListener==null){
            synchronized (HookViewClickUtil.class){
                if (mViewOnClickListener==null){
                    mViewOnClickListener=new HookViewClickUtil(context);
                }
            }
        }
        return mViewOnClickListener;
    }

    public static void hookView(View view) {
      //  getWindow().getDecorView().post(new Runnable() {}
        try {
            Class mClass = Class.forName("android.view.View");
                 /*找到ListenerInfo getListenerInfo方法*/
            Method listenerInfoMethod  = mClass.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()){
                listenerInfoMethod.setAccessible(true);//允许修改
            }

            /*调用*/
            Object listenerInfoObj  = listenerInfoMethod.invoke(view);
            /*得到 static class ListenerInfo类 */
            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
            /*得到View$ListenerInfo类中mOnClickListener属性*/
            Field mOnClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
            if (!mOnClickListenerField.isAccessible()){
                mOnClickListenerField.setAccessible(true);
            }
            View.OnClickListener mOnClickListener = (View.OnClickListener) mOnClickListenerField.get(listenerInfoObj);
            /*使用自定义代理方法*/
            View.OnClickListener mOnClickListenerProxy=new OnClickListenerProxy(mOnClickListener);
            //更换
            mOnClickListenerField.set(listenerInfoObj, mOnClickListenerProxy);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }


    static class OnClickListenerProxy implements View.OnClickListener {
        private View.OnClickListener object;
        private int MIN_CLICK_DELAY_TIME = 5000;
        private long lastClickTime = 0;

        public OnClickListenerProxy(View.OnClickListener mOnClickListener) {
            this.object = mOnClickListener;
        }

        @Override
        public void onClick(View v) {
            //点击时间控制
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                Log.e("OnClickListenerProxy", "OnClickListenerProxy");
                if (object != null) object.onClick(v);
            }else {
                Log.e("OnClickListenerProxy", "剩余时间:"+(currentTime - lastClickTime)/1000+"秒");
            }
        }
    }
}

