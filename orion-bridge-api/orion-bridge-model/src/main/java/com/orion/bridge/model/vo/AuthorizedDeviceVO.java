package com.orion.bridge.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登陆设备
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/21 0:32
 */
@Data
@ApiModel(value = "登陆设备响应")
public class AuthorizedDeviceVO {

    @ApiModelProperty(value = "登陆IP")
    private String address;

    @ApiModelProperty(value = "登陆位置")
    private String location;

    @ApiModelProperty(value = "userAgent")
    private String userAgent;

    @ApiModelProperty(value = "时间戳")
    private Long timestamp;

    @ApiModelProperty(value = "当前时间戳")
    private Long currentTimestamp;

    @ApiModelProperty(value = "过期时间")
    private String expireTime;

}
