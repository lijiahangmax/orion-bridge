package com.orion.bridge.service.api;

import com.orion.bridge.model.vo.AuthorizedDeviceVO;
import com.orion.bridge.model.vo.LoginHistoryVO;

import java.util.List;

/**
 * 用户管理服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/21 1:20
 */
public interface UserManagerService {

    /**
     * 获取认证设备
     *
     * @param userId userId
     * @return rows
     */
    List<AuthorizedDeviceVO> getAuthorizedDevices(Long userId);

    /**
     * 下线认证设备
     *
     * @param userId    userId
     * @param timestamp timestamp
     */
    void offlineDevice(Long userId, Long timestamp);

    /**
     * 获取登陆历史
     *
     * @param userId userId
     * @param limit  limit
     * @return rows
     */
    List<LoginHistoryVO> getLoginHistory(Long userId, Integer limit);

}
