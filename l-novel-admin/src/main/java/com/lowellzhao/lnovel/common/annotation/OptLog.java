package com.lowellzhao.lnovel.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 *
 * @author lowellzhao
 * @since 2022/9/7
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptLog {

    /**
     * @return 操作模块
     */
    String optModule() default "";

    /**
     * @return 操作类型
     */
    String optType() default "";

    /**
     * @return 需要返回结果
     */
    boolean needReturn() default true;

}
