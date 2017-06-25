package com.yangshao.image;

import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface IEngineIgLoader {
    void displayImage(ImageView imageView,String imageUrl);
    void displayImage(ImageView imageView, String imageUrl, DisplayOption option);
    class DisplayOption{
         static final int NONE=0X111;
        /*加载资源失败的ID*/
         final int loadErrorResId=NONE;
        /*占位资源ID*/
         final int placeHolderResId=NONE;
    }
}
