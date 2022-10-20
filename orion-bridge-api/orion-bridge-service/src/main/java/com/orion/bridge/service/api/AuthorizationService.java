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
     * 重置密码-自己
     *
     * @param request request
     */
    void resetMinePassword(AuthorizationRequest request);

    /**
     * 重置密码-他人
     *
     * @param request request
     */
    void resetOtherPassword(AuthorizationRequest request);

    /**
     * 验证登陆信息
     *
     * @return 用户验证信息
     */
    AuthorizationVO validAuthorized();

    /**
     * 通过 token 获取用户信息
     *
     * @param token   token
     * @param address address
     * @return user
     */
    UserDTO getUserByToken(String token, String address);

}
