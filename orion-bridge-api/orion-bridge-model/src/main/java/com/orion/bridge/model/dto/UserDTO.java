package com.orion.bridge.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/17 14:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户信息")
@SuppressWarnings("ALL")
public class UserDTO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "登录时间戳")
    private Long timestamp;

    @ApiModelProperty(value = "是否为管理员 1是 2否")
    private Integer isAdmin;

    @ApiModelProperty(value = "当前登录时间戳")
    private Long currentTimestamp;

}
