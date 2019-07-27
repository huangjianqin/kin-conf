package org.kin.conf.client.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjianqin
 * @date 2019/7/19
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Conf {
    /**
     * 配置key
     */
    String value();

    /**
     * 配置默认值
     */
    String defaultValue() default "";

    /**
     * 是否监听配置变化
     */
    boolean watch() default true;
}
