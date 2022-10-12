package com.orion.bridge.annotation;

import java.lang.annotation.*;

/**
 * 需要超级管理员角色
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/22 17:50
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireAdminRole {

}
