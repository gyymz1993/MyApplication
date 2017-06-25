package com.yangshao.image.compross;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.yangshao.utils.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/4/25/19:45
 **/
public class ImageCompress implements IEngineBitmap {
    @Override
    public String imageCompress(Bitmap image, int quality) {
        File picFile=FileUtil.getImageOutputFile();
        if (picFile==null){
            new Exception("文件不存在");
        }
        try{
            FileOutputStream fos=new FileOutputStream(picFile);
            image.compress(Bitmap.CompressFormat.JPEG,quality,fos);
            fos.close();
            fos.flush();
            image.recycle();
            return picFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            Log.d("file:::", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("file:::", "Error accessing file: " + e.getMessage());
        }
        return picFile.getAbsolutePath();
    }

    @Override
    public Bitmap imageCompress(Bitmap image, int quality, int maxSize) {
        //压缩图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于1000kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）：
     */
    @Override
    public Bitmap imageDecodeFile(String imagePath){
        BitmapFactory.Options options=new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        options.inJustDecodeBounds=true;
        //Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds=false;
        int w=options.outWidth;
        int h=options.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比,只用高或者宽其中一个数据进行计算即可
        int be = 1;
        if (w>h&&w>ww){
            //如果宽度大的话根据宽度固定大小缩放
            be= (int) (options.outWidth/ww);
        }else if (w<h&&h>hh){
            //如果高度高的话根据宽度固定大小缩放
            be= (int) (options.outHeight/hh);
        }
        options.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）：
     * @param image
     * @return
     */
    @Override
    public Bitmap imageDecodeFile(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm ;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return imageCompress(bitmap,100,100);//压缩好比例大小后再进行质量压缩
    }
}
