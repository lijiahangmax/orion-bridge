package com.orion.bridge.config;

import com.orion.bridge.constant.Const;
import lombok.Getter;

/**
 * 系统配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/14 17:25
 */
@Getter
public enum SystemConfig {

    /**
     * 是否允许多端登陆
     */
    ALLOW_MULTIPLE_LOGIN(Const.ENABLE),

    /**
     * 登陆绑定ip
     */
    LOGIN_BIND_ADDRESS(Const.DISABLE),

    /**
     * 登陆凭证有效期 (时)
     */
    LOGIN_TOKEN_EXPIRE(24),

    /**
     * 是否启用登陆失败锁定
     */
    LOGIN_FAILURE_LOCK(Const.ENABLE),

    /**
     * 登陆失败锁定阈值 (次)
     */
    LOGIN_FAILURE_LOCK_THRESHOLD(5),

    /**
     * 是否启用凭证自动续签
     */
    LOGIN_TOKEN_AUTO_RENEW(Const.ENABLE),

    /**
     * 登陆自动续签阈值 (时)
     */
    LOGIN_TOKEN_AUTO_RENEW_THRESHOLD(2),

    ;

    SystemConfig(Object defaultValue) {
        this.key = name().toLowerCase();
        this.value = defaultValue.toString();
    }

    private final String key;

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public static SystemConfig of(String key) {
        if (key == null) {
            return null;
        }
        for (SystemConfig value : values()) {
            if (value.key.equals(key)) {
                return value;
            }
        }
        return null;
    }

}
