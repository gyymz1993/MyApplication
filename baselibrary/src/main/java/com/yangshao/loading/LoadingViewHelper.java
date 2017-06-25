package com.yangshao.loading;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/3/17.
 */
 class LoadingViewHelper {
    private SparseArray<WeakReference<View>> mViews;
    public LoadingViewHelper() {
        mViews=new SparseArray<>();
    }

    public void setText(View contentView,int viewId, CharSequence s) {
        TextView textView=getView(contentView,viewId);
        if (textView!=null){
            textView.setText(s);
        }
    }

    public void setBackgroundResource(View contentView,int viewId, int imageId) {
        ImageView imageView=getView(contentView,viewId);
        if (imageView!=null){
            imageView.setBackgroundResource(imageId);
        }
    }

    public void setTextColor(View contentView,int viewId, int colorid) {
        TextView textView=getView(contentView,viewId);
        if (textView!=null){
            textView.setTextColor(colorid);
        }
    }

    public void setImageResource(View contentView,int viewId, int imageId) {
        ImageView imageView=getView(contentView,viewId);
        if (imageView!=null){
            imageView.setImageResource(imageId);
        }
    }

    public void setBackgroundColor(View contentView,int viewId, int colorId) {
        ImageView imageView=getView(contentView,viewId);
        if (imageView!=null){
            imageView.setBackgroundColor(colorId);
        }
    }
    public void setTextBackgroundResource(View contentView, int viewId, int bgid) {
        TextView imageView=getView(contentView,viewId);
        if (imageView!=null){
            imageView.setBackgroundResource(bgid);
        }
    }

    public void setTextSize(View contentView,int viewId, int textSize) {
        TextView textView=getView(contentView,viewId);
        if (textView!=null){
            textView.setTextSize(textSize);
        }
    }

    public void setHeight(View contentView,int viewId, int height) {
        TextView textView=getView(contentView,viewId);
        if (textView!=null){
            textView.setHeight(height);
        }
    }

    public void setWidth(View contentView,int viewId, int width) {
        TextView textView=getView(contentView,viewId);
        if (textView!=null){
            textView.setWidth(width);
        }
    }

    public void setViewBackgroundColor(View view, int bg) {
        if (view!=null){
            view.setBackgroundColor(bg);
        }
    }

    public void setOnclickListener(View contentView,int viewId, View.OnClickListener onClickListener) {
        TextView textView=getView(contentView,viewId);
        if (textView!=null){
            textView.setOnClickListener(onClickListener);
        }
    }

    public <T extends View> T getView(View contentView,int viewId){
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view=null;
        if (viewWeakReference!=null){
            view=viewWeakReference.get();
        }
        if (view==null){
            view=contentView.findViewById(viewId);
            if (view!=null){
                mViews.put(viewId,new WeakReference<>(view));
            }
        }
        return (T) view;
    }


}
