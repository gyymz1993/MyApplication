package com.skin.attr;

import android.view.View;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/3/30/23:41
 *  资源名称  以及资源的类型（背景。文本，图片）
 **/
public class SkinAttr {
    /*资源名称*/
    private String mResName;
    /*资源类型*/
    private SkinType mType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName=resName;
        this.mType=skinType;
    }

    public void skin(View view) {
        mType.skin(view,mResName);
    }

}
