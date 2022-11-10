package com.orion.bridge.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登陆凭证信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/17 18:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "登陆凭证信息")
public class AuthorizationTokenDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "时间戳")
    private Long timestamp;

}
