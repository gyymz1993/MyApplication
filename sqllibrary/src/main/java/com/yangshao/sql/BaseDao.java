package com.yangshao.sql;

import android.database.sqlite.SQLiteDatabase;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/4/6/22:07
 * 主要做增删改查
 **/
public abstract class BaseDao<T> {

    private SQLiteDatabase mDatabase;
    /*User  实例所操作数据库所对应的java类型*/
    private Class<T> entityClass;
    /*保证实例化一次*/
    private boolean isInit=false;

    private String tableName;
    public int inser(T entityClass){
        return -1;
    }

    public int update(T entityClass,T where){
        return -1;
    }

    public synchronized boolean init(Class<T> entity, SQLiteDatabase database) {
        if (!isInit){
            this.mDatabase=database;
            if (entity.getAnnotation(Table_Name.class)==null){
                tableName=entity.getSimpleName();
            }else {
                tableName=entity.getAnnotation(Table_Name.class).value();
            }
            isInit=true;
        }
        return isInit;
    }
}
