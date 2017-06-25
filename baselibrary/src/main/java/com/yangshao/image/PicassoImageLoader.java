package com.yangshao.image;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * @version Picasso加载图片
 * @author: gyymz1993
 * 创建时间：2017/4/1 17:32
 **/
public class PicassoImageLoader implements IEngineIgLoader {

    private Picasso mPicasso;

    public PicassoImageLoader(Context context) {
        mPicasso = Picasso.with(context);
    }

    @Override
    public void displayImage(ImageView imageView, String imageUrl) {
        displayImage(imageView, imageUrl, null);
    }

    @Override
    public void displayImage(ImageView imageView, String imageUrl, DisplayOption option) {
        if (option != null) {
            if (option.placeHolderResId != DisplayOption.NONE
                && option.loadErrorResId != DisplayOption.NONE) {
                mPicasso.load(imageUrl)
                    .placeholder(option.placeHolderResId)
                    .error(option.loadErrorResId)
                    .into(imageView);
            }
        } else if (option.placeHolderResId != DisplayOption.NONE) {
            mPicasso.load(imageUrl)
                .placeholder(option.placeHolderResId).
                into(imageView);
        } else if (option.loadErrorResId != DisplayOption.NONE) {
            mPicasso.load(imageUrl)
                .error(option.placeHolderResId)
                .into(imageView);
        }
    }
}
