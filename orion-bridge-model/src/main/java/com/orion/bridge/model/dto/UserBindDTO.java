package com.orion.bridge.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户登陆绑定信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/17 14:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户登陆绑定信息")
public class UserBindDTO {

    @ApiModelProperty(value = "登陆时间戳")
    private Long timestamp;

    @ApiModelProperty(value = "登陆IP")
    private String address;

    @ApiModelProperty(value = "登陆地址")
    private String location;

    @ApiModelProperty(value = "userAgent")
    private String userAgent;

}
