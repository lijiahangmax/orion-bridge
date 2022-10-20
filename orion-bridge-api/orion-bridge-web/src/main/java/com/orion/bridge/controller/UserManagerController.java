package com.orion.bridge.controller;

import com.orion.bridge.annotation.RestWrapper;
import com.orion.bridge.model.request.AuthorizationRequest;
import com.orion.bridge.model.vo.AuthorizedDeviceVO;
import com.orion.bridge.model.vo.LoginHistoryVO;
import com.orion.bridge.service.api.UserManagerService;
import com.orion.bridge.utils.Currents;
import com.orion.bridge.utils.Valid;
import com.orion.lang.define.wrapper.HttpWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户管理 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/21 1:32
 */
@Api(tags = "用户管理")
@RestController
@RestWrapper
@RequestMapping("/bridge/api/user-manager")
public class UserManagerController {

    @Resource
    private UserManagerService userManagerService;

    @GetMapping("/get-authorized-devices-mine")
    @ApiOperation(value = "获取认证设备-自己")
    public List<AuthorizedDeviceVO> getAuthorizedDevicesMine() {
        return userManagerService.getAuthorizedDevices(Currents.getUserId());
    }

    @GetMapping("/get-authorized-devices-other")
    @ApiOperation(value = "获取认证设备-他人")
    public List<AuthorizedDeviceVO> getAuthorizedDevicesOther(@RequestParam Long id) {
        return userManagerService.getAuthorizedDevices(id);
    }

    @PostMapping("/offline-mine")
    @ApiOperation(value = "下线其他端账号-自己")
    public HttpWrapper<?> offlineDeviceMine(@RequestBody AuthorizationRequest request) {
        Long timestamp = Valid.notNull(request.getTimestamp());
        userManagerService.offlineDevice(Currents.getUserId(), timestamp);
        return HttpWrapper.ok();
    }

    @PostMapping("/offline-other")
    @ApiOperation(value = "下线其他端账号-他人")
    public HttpWrapper<?> offlineDeviceOther(@RequestBody AuthorizationRequest request) {
        Long id = Valid.notNull(request.getId());
        Long timestamp = Valid.notNull(request.getTimestamp());
        userManagerService.offlineDevice(id, timestamp);
        return HttpWrapper.ok();
    }

    @GetMapping("/login-history-mine")
    @ApiOperation(value = "登陆日志-自己")
    public List<LoginHistoryVO> getLoginHistoryMine(@RequestParam Integer limit) {
        return userManagerService.getLoginHistory(Currents.getUserId(), limit);
    }

    @GetMapping("/login-history-other")
    @ApiOperation(value = "登陆日志-他人")
    public List<LoginHistoryVO> getLoginHistoryOther(@RequestParam Long id, @RequestParam Integer limit) {
        return userManagerService.getLoginHistory(id, limit);
    }

}
