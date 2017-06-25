package com.yangshao.skin;

import com.yangshao.utils.SpUtils;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/3/31/13:03
 **/
class SkinPreUtils {
    private static SkinPreUtils mInstance;

    public static SkinPreUtils getInstance(){
        if (mInstance==null){
            mInstance=new SkinPreUtils();
        }
        return mInstance;
    }
    private SkinPreUtils(){

    }

    public String getSkinPath() {
        return SpUtils.getInstance().getString(SkinConfig.SKIN_PATH);
    }

    public void saveSkinPath(String path) {
        SpUtils.getInstance().saveString(SkinConfig.SKIN_PATH,path);
    }

    public void clearSkinInfo() {
        saveSkinPath("");
    }

}
