package com.skin.attr;

import android.view.View;

import java.util.List;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/3/30/23:41
 *
 * 设置所有资源文件 包括背景.文本
 **/
public class SkinView {
    private View mView;
    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView=view;
        this.mSkinAttrs=skinAttrs;
    }

    public void skin(){
        for (SkinAttr attr : mSkinAttrs) {
            attr.skin(mView);
        }
    }
}
