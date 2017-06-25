package com.yangshao.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/3/17.
 */
 class AlertController {
    AlertDialog mAlertDialog;
    Window mWindow;
    public AlertController(AlertDialog alertDialog, Window window) {
        this.mAlertDialog=alertDialog;
        this.mWindow=window;

    }

    public AlertDialog getmAlertDialog(){
        return mAlertDialog;
    }
    public Window getmWindow(){
        return mWindow;
    }

    public static class AlertParams {
        boolean mCancelable;
        Context mContext;
        SparseArray<CharSequence> mTextArray=new SparseArray<>();
        SparseArray<View.OnClickListener> mListnerArray=new SparseArray<>();
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public View mView;
        public int mViewLayoutResId;

        public DialogInterface.OnKeyListener mOnKeyListener;
        public int mWidth= ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight= ViewGroup.LayoutParams.WRAP_CONTENT;;
        public int mGravity= Gravity.CENTER;
        public int mAnimation=0;


        public AlertParams(Context context, int themeRsId){
            this.mContext=context;
            this.mViewLayoutResId=themeRsId;
        }

        public void apply(AlertController mAlert) {
            DialogViewHelper mViewHelper=null;

            if (mViewLayoutResId!=0){
                mViewHelper=new DialogViewHelper(mContext,mViewLayoutResId);
            }
            if (mView!=null){
                mViewHelper=new DialogViewHelper();
                mViewHelper.setmContentView(mView);
            }
            if (mViewHelper==null){
                throw new IllegalArgumentException("");
            }

            //设置布局
            mAlert.getmAlertDialog().setContentView(mViewHelper.getContentView());
            //设置文本
            int textSize=mTextArray.size();
            for (int i=0;i<textSize;i++){
                mViewHelper.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }

            int cliclArraySize=mListnerArray.size();
            for (int i=0;i<cliclArraySize;i++){
                mViewHelper.setOnclickListener(mListnerArray.keyAt(i),mListnerArray.valueAt(i));
            }

            /*自定义dialog功能添加*/
            Window window = mAlert.getmWindow();
            window.setGravity(mGravity);
            if (mAnimation!=0){
                window.setWindowAnimations(mAnimation);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width=mWidth;
            params.height=mHeight;
            window.setAttributes(params);

        }

    }
}
