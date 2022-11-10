package com.orion.bridge.utils;

import com.orion.bridge.constant.Const;
import com.orion.bridge.constant.PropertiesConst;
import com.orion.bridge.model.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 环境工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/2 9:52
 */
public class Currents {

    private Currents() {
    }

    /**
     * 获取当前登录 token
     *
     * @param request request
     * @return token
     */
    public static String getLoginToken(HttpServletRequest request) {
        return request.getHeader(PropertiesConst.LOGIN_TOKEN_HEADER);
    }

    /**
     * 获取当前登录用户
     * <p>
     * 可以匿名登陆的接口并且用户未登录获取的是null
     *
     * @return 用户
     */
    public static UserDTO getUser() {
        return UserHolder.get();
    }

    /**
     * 获取当前登录用户id
     *
     * @return id
     */
    public static Long getUserId() {
        return Optional.ofNullable(UserHolder.get())
                .map(UserDTO::getId)
                .orElse(null);
    }

    /**
     * 获取当前登录用户username
     *
     * @return username
     */
    public static String getUserName() {
        return Optional.ofNullable(UserHolder.get())
                .map(UserDTO::getUsername)
                .orElse(null);
    }

    /**
     * 是否为管理员
     *
     * @return true 管理员
     */
    public static boolean isAdministrator() {
        UserDTO user = UserHolder.get();
        if (user == null) {
            return false;
        }
        return Const.ENABLE.equals(user.getIsAdmin());
    }

}
