package com.yangshao.fix;

import android.content.Context;
import android.util.Log;

import com.alipay.euler.andfix.patch.Patch;
import com.alipay.euler.andfix.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by Administrator on 2017/3/25.
 */

public class DexFixUtils {

    private Context mContext;
    private File mDexDir;


    public void init(Context context) {
        this.mContext = context;
        /**本地dex 补丁路径**/
        mDexDir = new File(mContext.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath());
        Log.e("TAg",mDexDir.getAbsolutePath()+":"+mDexDir.getName());
        // /data/data/com.example.myapplication/app_odex:app_odex
    }

    /**
     * patchFilePath
     * 补丁所在文件夹(尽量是SD卡的路径, 否则会打开文件失败)
     */
    public void addFixDex(String fixDexPatch) throws Exception {
        /**获取下载好的补丁**/
        File patchFile = new File(fixDexPatch);
        /**将服务器的修复包拷贝到本地**/
        File destFile = new File(mDexDir, patchFile.getName());
        /**dex, 优化后的路径, 必须在要App data目录下, 否则会没有权限*/
        /**dex 补丁文件路径(文件夹)*/
        if (!patchFile.exists()) {
            throw new FileNotFoundException("没有dex补丁文件" + patchFile);
        }
        if (destFile.exists()) {
            Log.d("TAG", "patch [" + destFile + "] has be loaded.");
            return;
        }
        FileUtil.copyFile(patchFile, destFile);// copy to patch's directory
        //ClassLoader读取dex路径，加入到集合，可能是已经拷贝到本地需要修复的
        //但是服务器已经删除，本地已经存在的
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);
        fixDex(fixDexFiles);
    }

    /**
     * 把dexElement注入到已运行classLoader中
     *
     * @param classLoader
     * @param dexElement
     * @throws Exception
     */
    private void injectDexElements(ClassLoader classLoader, Object dexElement) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, dexElement);
    }

    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }


    private Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object dexElements = dexElementsField.get(pathList);

        return dexElements;
    }

    /**
     * 加载之前修复的
     */
    public void loadFixDex(){
        File[] files = mDexDir.listFiles();
        List<File> fixDexFiles=new ArrayList<>();  //加载本地目录下的修复包
        for (File dexFile : files) {
            if (dexFile.getName().endsWith(".dex")){
                fixDexFiles.add(dexFile);
            }
        }
        try {
            fixDex(fixDexFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fixDex(List<File> fixDexFiles) throws Exception{
        /**先获取以及运行的dexElement**/
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Object applicationDexEelmets = getDexElementByClassLoader(applicationClassLoader);

        File optimizedDirectory = new File(mDexDir, "odex");
        if (!optimizedDirectory.exists()) {
            optimizedDirectory.mkdirs();
        }

        /***
         * 逐个修复
         * */
        for (File fixDexFile : fixDexFiles) {
            /**
             * String dexPath, 必须在应用目录下的文件中，odex,dex路径
             * File optimizedDirectory, 解压路径
             * String librarySearchPath, so,文件路径
             * ClassLoader parent 父loader
             */
            ClassLoader classLoader = new BaseDexClassLoader(
                fixDexFile.getAbsolutePath(),
                optimizedDirectory,
                null,
                applicationClassLoader
            );
            Object fixDexElements = getDexElementByClassLoader(classLoader);
            /**把补丁的dexElement插入到已经运行的dexElement的前面合并**/
            applicationDexEelmets=combineArray(fixDexElements,applicationDexEelmets);
        }
        /*合并完成注入到以前的类中*/
        injectDexElements(applicationClassLoader,applicationDexEelmets);


    }

    public class PatchManager {

}

}
