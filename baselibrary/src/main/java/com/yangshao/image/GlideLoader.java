package com.yangshao.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * @version Gide加载图片
 * @author: gyymz1993
 * 创建时间：2017/4/1 17:31
 **/
public class GlideLoader implements IEngineIgLoader {
    private RequestManager mRequestManager;

    public GlideLoader(Context context) {
        mRequestManager = Glide.with(context);
    }

    @Override
    public void displayImage(ImageView imageView, String imageUrl) {
        displayImage(imageView, imageUrl, null);
    }

    @Override
    public void displayImage(ImageView imageView, String imageUrl, DisplayOption option) {
        DrawableTypeRequest<String> drawableTypeRequest =
            mRequestManager.load(imageUrl);
        if (option != null) {
            if (option.placeHolderResId != DisplayOption.NONE
                && option.loadErrorResId != DisplayOption.NONE) {
                drawableTypeRequest.placeholder(option.placeHolderResId)
                    .error(option.loadErrorResId)
                    .into(imageView);
            }
        } else if (option.placeHolderResId != DisplayOption.NONE) {
            drawableTypeRequest.placeholder(option.placeHolderResId).
                into(imageView);
        } else if (option.loadErrorResId != DisplayOption.NONE) {
            drawableTypeRequest.error(option.placeHolderResId)
                .into(imageView);
        }
    }
}
