package com.yangshao.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.yangsho.baselib.R;

/**
 * Created by Administrator on 2017/3/17.
 */

public class AlertDialog extends Dialog {


    private AlertController mAlert;
    public AlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this,this.getWindow());
    }



    public static class Builder {
        private  AlertController.AlertParams P;

        /**
         * Creates a builder for an alert dialog that uses the default alert
         * dialog theme.
         * <p>
         * The default alert dialog theme is defined by
         * {@link android.R.attr#alertDialogTheme} within the parent
         * {@code context}'s theme.
         *
         * @param context the parent context
         */
        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams( context, themeResId);
        }
        public Builder setText(int viewId,CharSequence text) {
            P.mTextArray.put(viewId,text);
            return this;
        }
        public Builder setOnclickListener(int viewId, View.OnClickListener onClickListener) {
            P.mListnerArray.put(viewId,onClickListener);
            return this;
        }
        private AlertDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final AlertDialog dialog = new AlertDialog(P.mContext,P.mViewLayoutResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }


        public Builder setContentView(int layoutId) {
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }


        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /*全屏*/
        public Builder fullWith() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fromBouttm(boolean isAnimation) {
            if (isAnimation){
               // P.mAnimation=R.style.dialog;设置默认动画
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        public Builder setWidthAndHeight(int width,int heigth){
            P.mWidth=width;
            P.mHeight=heigth;
            return this;
        }

        public Builder addDefaultAnimation(int width,int heigth){
            P.mAnimation=R.style.dialog;   //设置默认动画
            return this;
        }

        public Builder setAnimation(int style){
            P.mAnimation=style;   //设置默认动画
            return this;
        }



        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }


        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this; 
        }

    }

}
