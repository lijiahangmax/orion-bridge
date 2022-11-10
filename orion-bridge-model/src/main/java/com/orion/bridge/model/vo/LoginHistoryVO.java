package com.orion.bridge.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 登陆历史
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/21 0:31
 */
@Data
@ApiModel(value = "登陆设备响应")
public class LoginHistoryVO {

    @ApiModelProperty(value = "登陆IP")
    private String address;

    @ApiModelProperty(value = "登陆位置")
    private String location;

    @ApiModelProperty(value = "userAgent")
    private String userAgent;

    @ApiModelProperty(value = "登陆结果 1成功 2失败")
    private Integer result;

    @ApiModelProperty(value = "登陆失败提示")
    private String failMessage;

    @ApiModelProperty(value = "是否为续签 1是 2否")
    private String renew;

    @ApiModelProperty(value = "登陆时间")
    private Date loginTime;

}
