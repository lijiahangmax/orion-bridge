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
 * 用户表
 * </p>
 *
 * @author Jiahang Li
 * @since 2022-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_info")
@ApiModel(value = "UserInfoDO对象", description = "用户表")
public class UserInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "盐值")
    @TableField("salt")
    private String salt;

    @ApiModelProperty(value = "是否为管理员 1是 2否")
    @TableField("is_admin")
    private Integer isAdmin;

    @ApiModelProperty(value = "双因子秘钥")
    @TableField("factor_secret")
    private String factorSecret;

    @ApiModelProperty(value = "用户状态 1启用 2禁用")
    @TableField("user_status")
    private Integer userStatus;

    @ApiModelProperty(value = "锁定状态 1正常 2锁定")
    @TableField("lock_status")
    private Integer lockStatus;

    @ApiModelProperty(value = "头像路径")
    @TableField("avatar_path")
    private String avatarPath;

    @ApiModelProperty(value = "联系手机")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "最后登录时间")
    @TableField("last_login_time")
    private Date lastLoginTime;

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
