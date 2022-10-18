package com.orion.bridge.config;

import com.orion.bridge.constant.MessageConst;
import com.orion.bridge.interceptor.AuthenticateInterceptor;
import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.exception.ApplicationException;
import com.orion.lang.exception.DecryptException;
import com.orion.lang.exception.EncryptException;
import com.orion.lang.exception.IORuntimeException;
import com.orion.lang.exception.argument.CodeArgumentException;
import com.orion.lang.exception.argument.HttpWrapperException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.exception.argument.RpcWrapperException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

/**
 * spring mvc 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/2 10:24
 */
@Slf4j
@Configuration
@RestControllerAdvice
public class WebMvcConfig implements WebMvcConfigurer {

    // @Resource
    // private IpFilterInterceptor ipFilterInterceptor;

    @Resource
    private AuthenticateInterceptor authenticateInterceptor;

    // @Resource
    // private RoleInterceptor roleInterceptor;
    //
    // @Resource
    // private UserActiveInterceptor userActiveInterceptor;
    //

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // // IP拦截器
        // registry.addInterceptor(ipFilterInterceptor)
        //         .addPathPatterns("/**")
        //         .order(5);
        // 认证拦截器
        registry.addInterceptor(authenticateInterceptor)
                .addPathPatterns("/orion/bridge-api/**")
                .order(10);
        // // 权限拦截器
        // registry.addInterceptor(roleInterceptor)
        //         .addPathPatterns("/orion/api/**")
        //         .order(20);
        // // 活跃拦截器
        // registry.addInterceptor(userActiveInterceptor)
        //         .addPathPatterns("/orion/api/**")
        //         .excludePathPatterns("/orion/api/auth/**")
        //         .order(30);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // registry.addMapping("/orion/bridge-api/**")
        //         .allowCredentials(true)
        //         .allowedOriginPatterns("*")
        //         .allowedMethods("*")
        //         .allowedHeaders("*")
        //         .maxAge(3600);
    }

    @ExceptionHandler(value = Exception.class)
    public HttpWrapper<?> normalExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("normalExceptionHandler url: {}, 抛出异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(MessageConst.EXCEPTION_MESSAGE).data(ex.getMessage());
    }

    @ExceptionHandler(value = ApplicationException.class)
    public HttpWrapper<?> applicationExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("applicationExceptionHandler url: {}, 抛出异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ex.getMessage());
    }

    @ExceptionHandler(value = DataAccessResourceFailureException.class)
    public HttpWrapper<?> dataAccessResourceFailureExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("dataAccessResourceFailureExceptionHandler url: {}, 抛出异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(MessageConst.NETWORK_FLUCTUATION);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, BindException.class})
    public HttpWrapper<?> httpRequestExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("httpRequestExceptionHandler url: {}, http请求异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(MessageConst.INVALID_PARAM);
    }

    @ExceptionHandler(value = {InvalidArgumentException.class, IllegalArgumentException.class})
    public HttpWrapper<?> invalidArgumentExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("invalidArgumentExceptionHandler url: {}, 参数异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ex.getMessage());
    }

    @ExceptionHandler(value = {IOException.class, IORuntimeException.class})
    public HttpWrapper<?> ioExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("ioExceptionHandler url: {}, io异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(MessageConst.IO_EXCEPTION_MESSAGE).data(ex.getMessage());
    }

    @ExceptionHandler(value = SQLException.class)
    public HttpWrapper<?> sqlExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("sqlExceptionHandler url: {}, sql异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(MessageConst.SQL_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(value = EncryptException.class)
    public HttpWrapper<?> encryptExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("encryptExceptionHandler url: {}, 数据加密异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(MessageConst.ENCRYPT_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = DecryptException.class)
    public HttpWrapper<?> decryptExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("decryptExceptionHandler url: {}, 数据解密异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(MessageConst.DECRYPT_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = CodeArgumentException.class)
    public HttpWrapper<?> codeArgumentExceptionHandler(CodeArgumentException ex) {
        return HttpWrapper.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = HttpWrapperException.class)
    public HttpWrapper<?> httpWrapperExceptionHandler(HttpWrapperException ex) {
        return ex.getWrapper();
    }

    @ExceptionHandler(value = RpcWrapperException.class)
    public HttpWrapper<?> rpcWrapperExceptionHandler(RpcWrapperException ex) {
        return ex.getWrapper().toHttpWrapper();
    }

}
