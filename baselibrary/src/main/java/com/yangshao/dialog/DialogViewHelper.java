package com.yangshao.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/3/17.
 */
 class DialogViewHelper {

    private SparseArray<WeakReference<View>> mViews;
    private View mContentView=null;
    public DialogViewHelper(Context context, int mViewLayoutId) {
       // Log.e("TAH",context+":"+mViewLayoutId+mContentView);
        this();
        mContentView=LayoutInflater.from(context).inflate(mViewLayoutId,null);

    }

    public DialogViewHelper() {
        mViews=new SparseArray<>();
    }

    public void setmContentView(View mContentView) {
        this.mContentView = mContentView;
    }

    public void setText(int viewId, CharSequence s) {
        TextView textView=getView(viewId);
        if (textView!=null){
            textView.setText(s);
        }

    }

    public void setOnclickListener(int viewId, View.OnClickListener onClickListener) {
        TextView textView=getView(viewId);
        if (textView!=null){
            textView.setOnClickListener(onClickListener);
        }
    }

    public <T extends View> T getView(int viewId){
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view=null;
        if (viewWeakReference!=null){
            view=viewWeakReference.get();
        }
        if (view==null){
            view=mContentView.findViewById(viewId);
            if (view!=null){
                mViews.put(viewId,new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    public View getContentView() {
        return mContentView;
    }
}
