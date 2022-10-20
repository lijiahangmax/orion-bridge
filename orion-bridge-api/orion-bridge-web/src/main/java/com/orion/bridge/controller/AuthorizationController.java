package com.orion.bridge.controller;

import com.orion.bridge.annotation.RestWrapper;
import com.orion.bridge.constant.Const;
import com.orion.bridge.model.request.AuthorizationRequest;
import com.orion.bridge.model.vo.AuthorizationVO;
import com.orion.bridge.model.vo.AuthorizedDeviceVO;
import com.orion.bridge.model.vo.LoginHistoryVO;
import com.orion.bridge.rpc.annotation.IgnoreAuth;
import com.orion.bridge.service.api.AuthorizationService;
import com.orion.bridge.utils.Valid;
import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.web.servlet.web.Servlets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户认证 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/14 17:14
 */
@Api(tags = "用户认证")
@RestController
@RestWrapper
@RequestMapping("/bridge/api/auth")
public class AuthorizationController {

    @Resource
    private AuthorizationService authorizationService;

    @IgnoreAuth
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public AuthorizationVO login(@RequestBody AuthorizationRequest body, HttpServletRequest request) {
        String username = Valid.notBlank(body.getUsername()).trim();
        Valid.notBlank(body.getPassword());
        body.setUsername(username);
        body.setAddress(Servlets.getRemoteAddr(request));
        body.setUserAgent(Servlets.getUserAgent(request));
        // 登录
        return authorizationService.login(body);
    }

    @IgnoreAuth
    @GetMapping("/logout")
    @ApiOperation(value = "登出")
    public HttpWrapper<?> logout() {
        authorizationService.logout();
        return HttpWrapper.ok();
    }

    @PostMapping("/reset-mine")
    @ApiOperation(value = "重置密码-自己")
    public HttpWrapper<?> resetMinePassword(@RequestBody AuthorizationRequest request) {
        Valid.notNull(request.getId());
        Valid.allNotBlank(request.getPassword(), request.getBeforePassword());
        authorizationService.resetMinePassword(request);
        return HttpWrapper.ok();
    }

    @PostMapping("/reset-other")
    @ApiOperation(value = "重置密码-他人")
    public HttpWrapper<?> resetOtherPassword(@RequestBody AuthorizationRequest request) {
        Valid.notNull(request.getId());
        Valid.notBlank(request.getPassword());
        authorizationService.resetOtherPassword(request);
        return HttpWrapper.ok();
    }

    @PostMapping("/check")
    @ApiOperation(value = "验证登陆-并获取用户信息")
    public AuthorizationVO checkAuthorized() {
        return authorizationService.checkAuthorized();
    }

    @GetMapping("/get-authorized-devices")
    @ApiOperation(value = "获取认证设备")
    public List<AuthorizedDeviceVO> getAuthorizedDevices() {
        return authorizationService.getAuthorizedDevices();
    }

    @PostMapping("/offline")
    @ApiOperation(value = "下线其他端账号")
    public HttpWrapper<?> offlineDevice(@RequestBody AuthorizationRequest request) {
        Long timestamp = Valid.notNull(request.getTimestamp());
        authorizationService.offlineDevice(timestamp);
        return HttpWrapper.ok();
    }

    @GetMapping("/login-history")
    @ApiOperation(value = "登陆日志")
    public List<LoginHistoryVO> getLoginHistory(@RequestParam(Const.LIMIT) Long limit) {
        return authorizationService.getLoginHistory(limit);
    }

}
