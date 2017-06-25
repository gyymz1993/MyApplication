package com.skin.attr;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skin.SkinManager;
import com.skin.SkinResource;
import com.yangshao.utils.L_;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/3/30/23:32
 *  需要换的类型
 **/
public enum SkinType {
    BAGEGROUND("background") {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void skin(View view, String resName) {
            /*背景可能是图片也可能是颜色*/
            SkinResource skinResource=getSkinResources();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable!=null){
                ImageView imageView= (ImageView) view;
                imageView.setBackgroundDrawable(drawable);
                return;
            }
            /*可能是颜色*/
            int color = skinResource.getColorByName(resName);
            if (color!=0){
                view.setBackgroundColor(color);
                return;
            }
        }
    }, TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource=getSkinResources();
            int color = skinResource.getColorByName(resName);
            if (color==0){
                return;
            }
            TextView textView= (TextView) view;
            textView.setTextColor(color);
        }
    },SRC("src") {
        @Override
        public void skin(View view, String resName) {
            //获取资源设置
            SkinResource skinResource=getSkinResources();
            Drawable drawable = skinResource.getDrawableByName(resName);
            L_.e("TAG",drawable+"");
            if (drawable!=null){
                ImageView imageView= (ImageView) view;
                imageView.setImageDrawable(drawable);
                return;
            }
        }
    };

    public SkinResource getSkinResources(){
        return SkinManager.getInstance().getSkinResources();
    }

    private String mResName;
    SkinType(String resName) {
        this.mResName=resName;
    }

    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }

    @Override
    public String toString() {
        return "SkinType{" +
            "mResName='" + mResName + '\'' +
            '}';
    }
}
