package com.orion.bridge.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 认证请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/14 16:43
 */
@Data
@ApiModel(value = "认证请求")
public class AuthorizationRequest {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "原密码")
    private String beforePassword;

    @ApiModelProperty(value = "登陆ip")
    private String address;

    @ApiModelProperty(value = "userAgent")
    private String userAgent;

}