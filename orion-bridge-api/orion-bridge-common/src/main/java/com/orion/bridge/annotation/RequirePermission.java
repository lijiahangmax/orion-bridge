package com.orion.bridge.annotation;

import java.lang.annotation.*;

/**
 * 需要权限注解
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/20 10:18
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 所需权限
     */
    int value();

}
