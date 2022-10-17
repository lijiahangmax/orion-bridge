package com.orion.bridge.utils;

import com.orion.bridge.constant.PropertiesConst;
import com.orion.bridge.model.dto.AuthorizationTokenDTO;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.crypto.Signatures;

import java.util.Arrays;
import java.util.Optional;

/**
 * 登陆工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/17 18:50
 */
public class LoginUtils {

    private LoginUtils() {
    }

    /**
     * 密码验签
     *
     * @param password password
     * @param salt     salt
     * @param sign     密码签名
     * @return 是否正确
     */
    public static boolean validPassword(String password, String salt, String sign) {
        return sign.equals(Signatures.md5(password, salt, 3));
    }

    /**
     * 生成 loginToken
     *
     * @param uid       uid
     * @param timestamp 时间戳
     * @return token
     */
    public static String createLoginToken(Long uid, Long timestamp) {
        return ValueMix.base62ecbEnc(uid + "_" + timestamp, PropertiesConst.VALUE_MIX_SECRET_KEY);
    }

    /**
     * 验证 loginToken
     *
     * @param token token
     * @return true合法
     */
    public static boolean validLoginToken(String token) {
        return ValueMix.base62ecbDec(token, PropertiesConst.VALUE_MIX_SECRET_KEY) != null;
    }

    /**
     * 获取 loginToken 的 uid
     *
     * @param token token
     * @return uid
     */
    public static Long getLoginTokenUserId(String token) {
        return Optional.of(token)
                .map(LoginUtils::getLoginTokenInfo)
                .map(AuthorizationTokenDTO::getId)
                .orElse(null);
    }

    /**
     * 获取 loginToken 的信息
     *
     * @param token token
     * @return tokenInfo
     */
    public static AuthorizationTokenDTO getLoginTokenInfo(String token) {
        return Optional.ofNullable(ValueMix.base62ecbDec(token, PropertiesConst.VALUE_MIX_SECRET_KEY))
                .map(s -> s.split("_"))
                .filter(s -> Arrays.stream(s).allMatch(Strings::isInteger))
                .map(s -> Arrays1.mapper(s, Long[]::new, Long::valueOf))
                .map(s -> {
                    AuthorizationTokenDTO info = new AuthorizationTokenDTO();
                    info.setId(s[0]);
                    info.setTimestamp(s[1]);
                    return info;
                }).orElse(null);
    }

}
