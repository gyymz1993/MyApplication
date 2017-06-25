package com.yangshao.ioc;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.yangshao.utils.L_;
import com.yangshao.utils.NetUtils;
import com.yangshao.utils.T_;

import org.xutils.common.util.LogUtil;
import org.xutils.view.*;
import org.xutils.view.annotation.ContentView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: gyymz1993
 * 创建时间：2017/3/28 18:23
 **/
public class ViewDagger {
    public static void inject(Activity activity) {
        //获取Activity的ContentView的注解
        Class<?> clazActivity = activity.getClass();
        try {
            ContentView contentView = findContentView(clazActivity);
            if (contentView != null) {
                int viewId = contentView.value();
                if (viewId > 0) {
                    Method setContentViewMethod = clazActivity.getMethod("setContentView", int.class);
                    setContentViewMethod.invoke(activity, viewId);
                }
            }
        } catch (Throwable ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
        injectView(activity, clazActivity, new ViewFinder(activity));
        injectOnclick(activity, clazActivity, new ViewFinder(activity));
    }

    private static void injectOnclick(Activity activity, Class<?> handlerType, ViewFinder viewFinder) {
        // inject view
        Method[] methods = handlerType.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                ViewOnClick viewOnClick = method.getAnnotation(ViewOnClick.class);
                if (viewOnClick != null) {
                    try {
                        int[] valueIds = viewOnClick.value();
                        if (valueIds != null && valueIds.length > 0) {
                            for (int valueId : valueIds) {
                                View view = viewFinder.findViewById(valueId);
                                /*配置是否检查网络*/
                                boolean isCheckNet = method.getAnnotation(NetWorkState.class) != null ? true : false;
                                if (view != null) {
                                    view.setOnClickListener(new DeclaredOnClickListener(method, activity, isCheckNet));
                                }
                            }
                        }
                    } catch (Throwable ex) {
                        LogUtil.e(ex.getMessage(), ex);
                    }
                }

            }
        }
    }

    private static void injectView(Activity activity, Class<?> handlerType, ViewFinder viewFinder) {
        // inject view
        Field[] fields = handlerType.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                ViewById viewById = field.getAnnotation(ViewById.class);
                if (viewById != null) {
                    try {
                        View view = viewFinder.findViewById(viewById.value(), viewById.parentID());
                        if (view != null) {
                            field.setAccessible(true);
                            field.set(activity, view);
                        } else {
                            throw new RuntimeException("Invalid @ViewInject for "
                                + handlerType.getSimpleName() + "." + field.getName());
                        }
                    } catch (Throwable ex) {
                        LogUtil.e(ex.getMessage(), ex);
                    }
                }
            }
        }
    }


    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Method mMethod;
        private Object mHandlerType;
        private boolean mIsCheckNet = false;

        public DeclaredOnClickListener(Method method, Object handlerType, boolean isCheckNet) {
            mMethod = method;
            mHandlerType = handlerType;
            mIsCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View v) {
            if (mIsCheckNet) {
                if (!NetUtils.isConnected()) {
                    T_.showToastReal("请检查网络");
                    return;
                }
            }
            try {
                mMethod.setAccessible(true);
                //点击调用该方法
                mMethod.invoke(mHandlerType, v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                   // mMethod.invoke(mHandlerType, null);
                    mMethod.invoke(mHandlerType);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 从父类获取注解View
     */
    private static ContentView findContentView(Class<?> thisCls) {
        if (thisCls == null) {
            return null;
        }
        ContentView contentView = thisCls.getAnnotation(ContentView.class);
        if (contentView == null) {
            return findContentView(thisCls.getSuperclass());
        }
        return contentView;
    }

    final static class ViewFinder {

        private View view;
        private Activity activity;

        public ViewFinder(View view) {
            this.view = view;
        }

        public ViewFinder(Activity activity) {
            this.activity = activity;
        }

        public View findViewById(int id) {
            if (view != null) return view.findViewById(id);
            if (activity != null) return activity.findViewById(id);
            return null;
        }

        public View findViewById(int id, int pid) {
            View pView = null;
            if (pid > 0) {
                pView = this.findViewById(pid);
            }
            View view = null;
            if (pView != null) {
                view = pView.findViewById(id);
            } else {
                view = this.findViewById(id);
            }
            return view;
        }
    }
}
