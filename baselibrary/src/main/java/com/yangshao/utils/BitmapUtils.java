package com.yangshao.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.yangshao.base.BaseApplication;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yangshao on 2015/10/10.
 */
public class BitmapUtils {
    public static BitmapUtils bimpUtils;
    public static BitmapUtils getInstace() {
        if (bimpUtils == null) {
            bimpUtils = new BitmapUtils();
        }
        return bimpUtils;
    }
    private BitmapUtils(){
    }


    /**
     *  查询图片路径
     * @param context
     * @param uri
     * @return
     */
    public String getRealPath(Context context, Uri uri) {
        if (uri == null) return null;
        String data = null;
        String scheme = uri.getScheme();
        if (scheme == null){
            data = uri.getPath();
        }else if(ContentResolver.SCHEME_FILE.equals(scheme)){
            data = uri.getPath();
        }else if(ContentResolver.SCHEME_CONTENT.equals(scheme)){
            Cursor cursor = context.getContentResolver().query(uri,new String[]{MediaStore.Images.ImageColumns.DATA},null,null,null);
            if (cursor != null && cursor.moveToFirst()){
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                if (index > -1){
                    data = cursor.getString(index);
                }
            }
            cursor.close();
        }
        return data;
    }


    public String getCompressImagePaht(String path) {
        Bitmap bmp = null;
        String dir = null;
        try {
            //压缩图片
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                    new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= 1000)
                        && (options.outHeight >> i <= 1000)) {
                    in = new BufferedInputStream(
                            new FileInputStream(new File(path)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bmp = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i += 1;
            }

            //裁剪图片
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            if (width > height) {
                bmp = Bitmap.createBitmap(bmp, (width - height) / 2, 0, height, height);
            } else if (width < height) {
                bmp = Bitmap.createBitmap(bmp, 0, (height - width) / 2, width, width);
            }

            //写入缓存 文件
            dir = BaseApplication.getApplication().getCacheDir().getAbsolutePath() + File.separator
                    + System.currentTimeMillis() + ".jpg";
            File file = new File(dir);
            FileOutputStream fos = new FileOutputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bmp.recycle();
        bmp=null;
        return dir;
    }



    /**
     * 获取sd卡路径
     *
     * @return
     */
    private static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            // 这里可以修改为你的路径
            sdDir = new File(Environment.getExternalStorageDirectory()
                    + "/DCIM/Cache");
        }
        return sdDir;
    }


        /**
         * 清空文件
         */
        public void clearAll(){
            File file= getSDPath();
            if (file.exists()) {
                file.delete();
            }
        }

    	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}




    /**
     * 返回一个压缩之后的bitmap路径
     * **/

    /**
     *
     * 根据路径压缩图片
     *
     * **/

    /**
     *    压缩图片 返回一个压缩大小的bitmap
     */

}
