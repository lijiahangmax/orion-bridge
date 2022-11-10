package com.orion.bridge.utils;

import com.orion.bridge.constant.PropertiesConst;
import com.orion.lang.utils.codec.Base62s;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.lang.utils.crypto.enums.PaddingMode;
import com.orion.lang.utils.crypto.enums.WorkingMode;
import com.orion.lang.utils.crypto.symmetric.EcbSymmetric;
import com.orion.lang.utils.crypto.symmetric.SymmetricBuilder;

import java.util.Optional;

/**
 * 数据混入器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/14 23:27
 */
public class ValueMix {

    private static final EcbSymmetric ECB = SymmetricBuilder.aes()
            .workingMode(WorkingMode.ECB)
            .paddingMode(PaddingMode.PKCS5_PADDING)
            .generatorSecretKey(PropertiesConst.VALUE_MIX_SECRET_KEY)
            .buildEcb();

    private ValueMix() {
    }

    /**
     * 加密
     *
     * @param value 明文
     * @return 密文
     */
    public static String encrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            return ValueMix.ECB.encryptAsString(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加密
     *
     * @param value 明文
     * @param key   key
     * @return 密文
     */
    public static String encrypt(String value, String key) {
        if (value == null) {
            return null;
        }
        try {
            return SymmetricBuilder.aes()
                    .workingMode(WorkingMode.ECB)
                    .paddingMode(PaddingMode.PKCS5_PADDING)
                    .generatorSecretKey(key)
                    .buildEcb()
                    .encryptAsString(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param value 密文
     * @return 明文
     */
    public static String decrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            return ECB.decryptAsString(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param value 密文
     * @param key   key
     * @return 明文
     */
    public static String decrypt(String value, String key) {
        if (value == null) {
            return null;
        }
        try {
            return SymmetricBuilder.aes()
                    .workingMode(WorkingMode.ECB)
                    .paddingMode(PaddingMode.PKCS5_PADDING)
                    .generatorSecretKey(key)
                    .buildEcb()
                    .decryptAsString(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 明文 -> ecb -> base62 -> 密文
     *
     * @param value value
     * @param key   key
     * @return 密文
     */
    public static String base62ecbEnc(String value, String key) {
        return Optional.ofNullable(value)
                .map(v -> encrypt(value, key))
                .map(Base62s::encode)
                .orElse(null);
    }

    /**
     * 密文 -> base62 -> ecb -> 明文
     *
     * @param value value
     * @param key   key
     * @return 明文
     */
    public static String base62ecbDec(String value, String key) {
        return Optional.ofNullable(value)
                .map(Base62s::decode)
                .map(v -> decrypt(v, key))
                .orElse(null);
    }

    /**
     * 密码签名
     *
     * @param password password
     * @param salt     盐
     * @return 密文
     */
    public static String encPassword(String password, String salt) {
        return Signatures.md5(password, salt, 3);
    }

}
