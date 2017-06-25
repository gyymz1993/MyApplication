package com.yangshao.ioc;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewOnClick {

    /**
     * 控件的id集合, id小于1时不执行ui事件绑定.
     *
     * @return
     */
    int[] value();

    /**
     * 控件的parent控件的id集合, 组合为(value[i], parentId[i] or 0).
     *
     * @return
     */
    int[] parentId() default 0;



}