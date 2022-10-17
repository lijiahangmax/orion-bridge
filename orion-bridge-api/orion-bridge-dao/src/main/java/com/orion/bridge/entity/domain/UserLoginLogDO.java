package com.orion.bridge.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户登陆日志
 * </p>
 *
 * @author Jiahang Li
 * @since 2022-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_login_log")
@ApiModel(value = "UserLoginLogDO对象", description = "用户登陆日志")
public class UserLoginLogDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "登陆结果 1成功 2失败")
    @TableField("login_result")
    private Integer loginResult;

    @ApiModelProperty(value = "登陆失败提示")
    @TableField("fail_message")
    private String failMessage;

    @ApiModelProperty(value = "ip地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "ip位置")
    @TableField("location")
    private String location;

    @ApiModelProperty(value = "userAgent")
    @TableField("user_agent")
    private String userAgent;

    @ApiModelProperty(value = "是否为续签 1是 2否")
    @TableField("is_renew")
    private Integer isRenew;

    @ApiModelProperty(value = "是否删除 1未删除 2已删除")
    @TableField("deleted")
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;


}
