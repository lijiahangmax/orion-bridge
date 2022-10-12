package com.orion.bridge.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置常量
 * <p>
 * 禁止手动赋值
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/14 17:11
 */
@Component
public class PropertiesConst {

    /**
     * 当前版本
     */
    public static String ORION_BRIDGE_VERSION;

    /**
     * 登陆 token 请求头
     */
    public static String LOGIN_TOKEN_HEADER;

    /**
     * 加密秘钥
     */
    public static String VALUE_MIX_SECRET_KEY;

    @Value("${app.version}")
    private void setVersion(String version) {
        PropertiesConst.ORION_BRIDGE_VERSION = version;
    }

    @Value("${login.token.header}")
    private void setLoginTokenHeader(String loginTokenHeader) {
        PropertiesConst.LOGIN_TOKEN_HEADER = loginTokenHeader;
    }

    @Value("${value.mix.secret.key}")
    private void setValueMixSecretKey(String valueMixSecretKey) {
        PropertiesConst.VALUE_MIX_SECRET_KEY = valueMixSecretKey;
    }

}
