package com.yangshao.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  * @author: gyymz1993
  * 创建时间：2017/3/28 17:52
  * @version
  *    Target 作用域
 *     Retention 运行时
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    int value();
    int parentID() default 0;
}
