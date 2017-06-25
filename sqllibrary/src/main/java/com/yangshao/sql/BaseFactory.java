package com.yangshao.sql;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/4/6/22:09
 **/
public class BaseFactory {
    private SQLiteDatabase mDatabase;
    private String sqlLitePath;
    private static  BaseFactory instance;
    private BaseFactory(){
        sqlLitePath= Environment.getDataDirectory().getAbsolutePath()+"user.db";
        openDatabase();
    }

    public synchronized <T extends BaseDao<M>,M> T getDataHelper(Class<T> clazz,Class<M> entity){
        BaseDao baseDao=null;
        baseDao.init(entity,mDatabase);
        return (T) baseDao;

    }

    private void openDatabase() {
    }

    private static BaseFactory getInstance(){
        if (instance==null){
            synchronized (BaseFactory.class){
                if (instance==null){
                    instance=new BaseFactory();
                }
            }
        }
        return instance;
    }


}
