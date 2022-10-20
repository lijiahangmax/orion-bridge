package com.orion.bridge.service.api;

import com.orion.bridge.model.dto.UserDTO;
import com.orion.bridge.model.request.AuthorizationRequest;
import com.orion.bridge.model.vo.AuthorizationVO;
import com.orion.bridge.model.vo.AuthorizedDeviceVO;
import com.orion.bridge.model.vo.LoginHistoryVO;

import java.util.List;

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
    AuthorizationVO checkAuthorized();

    /**
     * 获取认证设备
     *
     * @return rows
     */
    List<AuthorizedDeviceVO> getAuthorizedDevices();

    /**
     * 下线认证设备
     *
     * @param timestamp timestamp
     */
    void offlineDevice(Long timestamp);

    /**
     * 获取登陆历史
     *
     * @param limit limit
     * @return rows
     */
    List<LoginHistoryVO> getLoginHistory(Long limit);

    /**
     * 通过 token 获取用户信息
     *
     * @param token   token
     * @param address address
     * @return user
     */
    UserDTO getUserByToken(String token, String address);

}
