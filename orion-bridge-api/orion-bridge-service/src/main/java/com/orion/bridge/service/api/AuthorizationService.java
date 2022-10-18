package com.orion.bridge.service.api;

import com.orion.bridge.model.dto.UserDTO;
import com.orion.bridge.model.request.AuthorizationRequest;
import com.orion.bridge.model.vo.AuthorizationVO;

/**
 * 认证服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/14 16:30
 */
public interface AuthorizationService {

    /**
     * 登录
     *
     * @param request request
     * @return wrapper
     */
    AuthorizationVO login(AuthorizationRequest request);

    /**
     * 登出
     */
    void logout();

    /**
     * 重置密码
     *
     * @param request request
     */
    void resetPassword(AuthorizationRequest request);

    /**
     * 通过 token 获取用户信息
     *
     * @param token   token
     * @param address address
     * @return user
     */
    UserDTO getUserByToken(String token, String address);

}
