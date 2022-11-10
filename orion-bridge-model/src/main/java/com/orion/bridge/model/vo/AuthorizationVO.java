package com.orion.bridge.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 认证响应
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/14 16:44
 */
@Data
@ApiModel(value = "认证响应")
public class AuthorizationVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "名称")
    private String nickname;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "是否为管理员 1是 2否")
    private Integer isAdmin;

}
