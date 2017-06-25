package com.yangshao.image.compross;

import android.graphics.Bitmap;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/4/25/19:30
 **/
public interface IEngineBitmap {

    /**
     * 我们先质量压缩方法
     * 返回图片压缩路径
     * @param quality  Hint to the compressor, 0-100. 0 meaning compress for
     *                 small size, 100 meaning compress for max quality. Some
     *                 formats, like PNG which is lossless, will ignore the
     *                 quality setting
     */
    String imageCompress(Bitmap bitmap, int quality);


    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）：
     * @param bitmap
     * @param quality  压缩比
     * @param maxSize  设置图片大最大  KB
     * @return
     */
    Bitmap imageCompress(Bitmap bitmap,int quality,int maxSize);

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）：
     * @param imagePath
     * @return
     */
    Bitmap imageDecodeFile(String imagePath);

    Bitmap imageDecodeFile(Bitmap image);
}
