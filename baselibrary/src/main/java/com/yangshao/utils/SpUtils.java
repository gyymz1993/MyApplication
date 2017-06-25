package com.yangshao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
  * @author: gyymz1993
  * 创建时间：2017/3/31 13:19
  * @version
  *
 **/
public class SpUtils {
    public static SharedPreferences sharedPreferences;
    public static SpUtils spUtils;
    public static SpUtils getInstance() {
        if (spUtils == null) {
            spUtils = new SpUtils();
        }
        return spUtils;
    }

    private SpUtils() {

    }

    public void init(Context context) {
        sharedPreferences = context
                .getSharedPreferences("config", 0);
    }
    /**
     * 得到配置文件
     *
     * @return
     */
    public SharedPreferences getSp() {
        return sharedPreferences;
    }

    /**
     * 保存string值到sp文件
     *
     * @param key
     * @param value
     */
    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 保存boolean键值到sp文件
     *
     * @param key
     * @param value
     */
    public void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 得到boolean值
     *
     * @param key
     * @param defValue
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 取字符串，默认“”
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }


}
