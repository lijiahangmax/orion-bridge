package com.orion.bridge.config;

/**
 * 缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/14 17:43
 */
public class CacheKeys {

    private CacheKeys() {
    }

    /**
     * 认证信息 key
     * <p>
     * auth:login:{id}
     */
    public static final String AUTHORIZATION_INFO_KEY = "auth:info:{}";

    /**
     * 认证绑定 key
     * <p>
     * auth:bind:{id}:{timestamp}
     */
    public static final String AUTHORIZATION_BIND_KEY = "auth:bind:{}:{}";

    /**
     * 认证失败次数 key
     * <p>
     * auth:fail:count:{username}
     */
    public static final String AUTHORIZATION_FAIL_COUNT_KEY = "auth:fail:count:{}";

}
