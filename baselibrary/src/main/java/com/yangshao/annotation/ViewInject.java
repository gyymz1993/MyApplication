package com.yangshao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewInject {
	int value() default -1;
	String click() default "";
	String longClick() default "";
	String itemClick() default "";
	String itemLongClick() default "";
	String format() default "%s";
}
