package com.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.yangshao.utils.L_;

import java.lang.reflect.Method;

/**
  * @author: gyymz1993
  * 创建时间：2017/3/30 22:02
  * @version  资源文件
  *
 **/
public class SkinResource {
    private static final String TAG="SkinResource";
    private String mPackageName;
    private Resources mSkinResource;
   public SkinResource(Context context,String path){
       //mPackageName=context.getPackageName();
       mPackageName=context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).packageName;
       L_.e("TAG"+mPackageName);
        //加载自己的资源
       try {
           Resources supResources=context.getResources();
           AssetManager asset =AssetManager.class.newInstance();
           mSkinResource=new Resources(asset,supResources.getDisplayMetrics(),supResources.getConfiguration());
           Method method=AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
//           String mSrc= Environment.getExternalStorageDirectory().getAbsolutePath()
//               + File.separator+"red.skin";
           method.invoke(asset,path);// src 修改字参数的文件路径为自己的本地文件
           //获取资源ID

       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    public Drawable getDrawableByName(String resName){
       // int drawable=mSkinResource.getIdentifier("图片名称","文件类型drawable","资源包名");
        L_.e("TAG"+TAG+"  --->resName:"+resName+"mPackageName   --->"+mPackageName);
        try {
            int drawableId = mSkinResource.getIdentifier(resName, "drawable", mPackageName);
            L_.e("TAG"+TAG+"  --->drawableId:"+drawableId);
            return mSkinResource.getDrawable(drawableId);
        }catch (Exception e){
            return null;
        }

    }

    public int getColorByName(String resName){
        try {

            int colorId = mSkinResource.getIdentifier(resName, "color", mPackageName);
            L_.e("TAG"+TAG+"  --->resName:"+resName+"colorId  --->"+colorId);
            return mSkinResource.getColor(colorId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }

}
