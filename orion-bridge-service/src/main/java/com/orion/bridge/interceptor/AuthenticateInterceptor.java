package com.orion.bridge.interceptor;

import com.orion.bridge.constant.ResultCode;
import com.orion.bridge.model.dto.UserDTO;
import com.orion.bridge.rpc.annotation.IgnoreAuth;
import com.orion.bridge.service.api.AuthorizationService;
import com.orion.bridge.utils.Currents;
import com.orion.bridge.utils.UserHolder;
import com.orion.lang.constant.StandardContentType;
import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.web.servlet.web.Servlets;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/1 17:20
 */
@Component
public class AuthenticateInterceptor implements HandlerInterceptor {

    @Resource
    private AuthorizationService authorizationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 获取用户信息
        UserDTO user = authorizationService.getUserByToken(Currents.getLoginToken(request), Servlets.getRemoteAddr(request));
        UserHolder.set(user);
        final boolean ignoreAuth = ((HandlerMethod) handler).hasMethodAnnotation(IgnoreAuth.class);
        // 匿名接口直接返回
        if (ignoreAuth) {
            return true;
        }
        // 未认证
        final boolean unauthorized = user == null;
        if (unauthorized) {
            response.setContentType(StandardContentType.APPLICATION_JSON);
            Servlets.transfer(response, HttpWrapper.of(ResultCode.UNAUTHORIZED).toJsonString().getBytes());
        }
        return unauthorized;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserHolder.remove();
    }

}
