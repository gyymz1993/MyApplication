package com.yangshao.title;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.Window.ID_ANDROID_CONTENT;

/**
  * @author: gyymz1993
  * 创建时间：2017/4/26 16:54
  * @version
  *  new DefaultTitleBar.Builder(this).setLeftIcon(R.drawable.back_h).
  *  setTitleText("发表").setRightText("发表").onCreate();
  *
 **/
public abstract  class NavigationBar<P extends NavigationBar.Builder.BarParams> implements INavigation {
    P mBarParams;
    View mView;
    public NavigationBar(P params) {
        mBarParams = params;
        createAndBind();
    }

    protected P getParams() {
        return mBarParams;
    }

    /**
     * 创建和绑定布局
     */
    public void createAndBind() {
        if (mBarParams == null) {
            return;
        }
        if (mBarParams.parent==null){
            ViewGroup rootView= (ViewGroup) ((Activity) (mBarParams.context))
                .findViewById(ID_ANDROID_CONTENT);
            mBarParams.parent= (ViewGroup) rootView.getChildAt(0);
        }
        mView = LayoutInflater.from(mBarParams.context).inflate(bindLayoutId(), mBarParams.parent,false);
        mBarParams.parent.addView(mView, 0);
        applyView();
    }

    public <T extends View> T viewFindById(int id) {
        return (T) mView.findViewById(id);
    }

    protected void setTextViewText(int viewId,CharSequence text){
        TextView textView=viewFindById(viewId);
        if (textView!=null&&text!=null){
            textView.setText(text);
        }
    }
    protected void setBackgroundColor(int viewId,int color){
        View view=viewFindById(viewId);
        if (view!=null&&color!=0){
            view.setBackgroundColor(color);
        }
    }

    protected void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = viewFindById(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * 设置背景资源
     * @param viewId
     * @param resourceId
     */
    protected void setImageResource(int viewId, int resourceId) {
        ImageView imageView = viewFindById(viewId);
        if (imageView != null) {
            imageView.setImageResource(resourceId);
        }
    }


    public  abstract  static class Builder{
        // 构建导航条方法
        public abstract NavigationBar onCreate();
        public abstract static class  BarParams {
            public ViewGroup parent;
            public Context context;
            public BarParams(ViewGroup parent, Context context) {
                this.parent = parent;
                this.context = context;
            }
        }
    }

}
