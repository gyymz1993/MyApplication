package com.yangshao.skin.attr;

import android.view.View;

import com.yangshao.utils.L_;

import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/3/30/23:41
 *  我们需要切换皮肤的所有View
 *
 *  包含mView  以及View的所有的属性
 *  背景.文本颜色  图片
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
            L_.e("我开始换肤");
            attr.skin(mView);
        }
    }
}
