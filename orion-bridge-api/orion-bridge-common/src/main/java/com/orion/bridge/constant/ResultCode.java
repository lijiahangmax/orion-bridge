package com.orion.bridge.constant;

import com.orion.lang.define.wrapper.CodeInfo;

/**
 * wrapper 返回 code
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/2 9:48
 */
public enum ResultCode implements CodeInfo {

    /**
     * 未认证
     */
    UNAUTHORIZED(700, MessageConst.UNAUTHORIZED),

    /**
     * 无权限
     */
    NO_PERMISSION(710, MessageConst.NO_PERMISSION),

    /**
     * IP封禁
     */
    IP_BAN(720, MessageConst.IP_BAN),

    ;

    private final int code;

    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
