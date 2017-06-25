package com.yangshao.title;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yangsho.baselib.R;


/**
 * 创建人：gyymz1993
 * 创建时间：$date$ $time$
 **/
public class DefaultTitleBar<D extends NavigationBar.Builder.BarParams>
    extends NavigationBar<DefaultTitleBar.Builder.DefaultBarParams> {
    public DefaultTitleBar(Builder.DefaultBarParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.navigation_default;
    }

    @Override
    public void applyView() {
        // 给我们的导航条绑定资源
        setImageResource(R.id.iv_left, getParams().leftIcon);
        setImageResource(R.id.iv_right, getParams().rightIcon);
        setTextViewText(R.id.title_tv, getParams().titleText);
        setTextViewText(R.id.tv_left, getParams().letfText);
        setTextViewText(R.id.tv_right, getParams().rightText);
       // setBackgroundColor(R.id.title_bar, getParams().bgColor);
        setOnClickListener(R.id.iv_left, getParams().leftOnClickListener);
        setOnClickListener(R.id.iv_right, getParams().rightOnClickListener);
    }

    /*构建导航栏*/
    public static class Builder extends NavigationBar.Builder{
        public DefaultBarParams mBarParams;

        public Builder(Context context) {
            mBarParams = new DefaultBarParams(context, null);
        }
        public Builder(Context context, ViewGroup parent) {
            mBarParams = new DefaultBarParams(context, parent);
        }

        public Builder setTitleText(String titleText){
            mBarParams.titleText=titleText;
            return this;
        }
        public Builder setLeftText(String leftText){
            mBarParams.letfText=leftText;
            return this;
        }
        public Builder setRightText(String rightText){
            mBarParams.rightText=rightText;
            return this;
        }
        public Builder setLeftIcon(int leftIcon){
            mBarParams.leftIcon=leftIcon;
            return this;
        }
        public Builder setRightIcon(int rightIcon){
            mBarParams.rightIcon=rightIcon;
            return this;
        }

        public Builder setTitleBackgroundColor(int bgColor) {
            mBarParams.bgColor = bgColor;
            return this;
        }

        public Builder setLeftOnClickListener(View.OnClickListener onClickListener) {
            mBarParams.leftOnClickListener = onClickListener;
            return this;
        }

        public Builder setRightOnClickListener(View.OnClickListener onClickListener) {
            mBarParams.rightOnClickListener = onClickListener;
            return this;
        }

        @Override
        public DefaultTitleBar<BarParams> onCreate() {
            DefaultTitleBar<BarParams> navigation = new DefaultTitleBar<>(mBarParams);
            return navigation;
        }
        public static class DefaultBarParams extends BarParams{
            public String letfText;
            public String rightText;
            public int leftIcon;
            public int rightIcon;
            public int bgColor;
            public View.OnClickListener leftOnClickListener;
            public View.OnClickListener rightOnClickListener;
            public String titleText;

            public DefaultBarParams(Context context,ViewGroup parent) {
                super(parent, context);
            }
        }
    }


}
