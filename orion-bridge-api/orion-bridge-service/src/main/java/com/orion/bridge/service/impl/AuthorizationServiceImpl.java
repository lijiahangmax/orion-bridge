package com.orion.bridge.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.bridge.config.CacheKeys;
import com.orion.bridge.config.SystemConfig;
import com.orion.bridge.constant.Const;
import com.orion.bridge.constant.MessageConst;
import com.orion.bridge.dao.UserInfoDAO;
import com.orion.bridge.dao.UserLoginLogDAO;
import com.orion.bridge.entity.domain.UserInfoDO;
import com.orion.bridge.entity.domain.UserLoginLogDO;
import com.orion.bridge.model.dto.AuthorizationTokenDTO;
import com.orion.bridge.model.dto.UserBindDTO;
import com.orion.bridge.model.dto.UserDTO;
import com.orion.bridge.model.request.AuthorizationRequest;
import com.orion.bridge.model.vo.AuthorizationVO;
import com.orion.bridge.service.api.AuthorizationService;
import com.orion.bridge.utils.*;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/14 16:30
 */
@Service("authorizationService")
public class AuthorizationServiceImpl implements AuthorizationService {

    @Resource
    private UserLoginLogDAO userLoginLogDAO;

    @Resource
    private UserInfoDAO userInfoDAO;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public AuthorizationVO login(AuthorizationRequest request) {
        // 获取位置信息
        String address = request.getAddress();
        String location = Utils.getIpLocation(address);
        String userAgent = request.getUserAgent();
        String username = request.getUsername();
        // 验证登陆
        UserInfoDO userInfo = null;
        Long id = null;
        RuntimeException loginException = null;
        try {
            // 检查失败次数
            this.checkAuthorizationFailCount(username);
            // 查询用户
            LambdaQueryWrapper<UserInfoDO> query = new LambdaQueryWrapper<UserInfoDO>()
                    .eq(UserInfoDO::getUsername, username)
                    .last(Const.LIMIT_1);
            userInfo = Valid.notNull(userInfoDAO.selectOne(query), MessageConst.USERNAME_PASSWORD_ERROR);
            id = userInfo.getId();
            // 检查密码
            boolean validPassword = LoginUtils.validPassword(request.getPassword(), userInfo.getSalt(), userInfo.getPassword());
            if (validPassword) {
                // 验证锁定状态
                Valid.isTrue(Const.ENABLE.equals(userInfo.getLockStatus()), MessageConst.USER_LOCKED);
                // 验证启用状态
                Valid.isTrue(Const.ENABLE.equals(userInfo.getUserStatus()), MessageConst.USER_DISABLED);
                // 删除失败次数
                this.deleteAuthorizationFailCount(username);
                // 更新最后登录时间
                this.setLastLoginTime(id);
            } else {
                // 修改失败次数
                long count = this.addAuthorizationFailCount(username);
                // 检查是否锁定
                this.checkAuthorizationFailLockUser(id, count);
            }
            Valid.isTrue(validPassword, MessageConst.USERNAME_PASSWORD_ERROR);
        } catch (RuntimeException e) {
            // 未查询到用户则不跳过逻辑
            if (id == null) {
                throw e;
            }
            loginException = e;
        }
        // 记录登陆日志
        UserLoginLogDO loginLog = new UserLoginLogDO();
        loginLog.setUserId(id);
        loginLog.setLoginResult(loginException == null ? Const.ENABLE : Const.DISABLE);
        loginLog.setFailMessage(loginException == null ? null : loginException.getMessage());
        loginLog.setAddress(address);
        loginLog.setLocation(location);
        loginLog.setUserAgent(userAgent);
        loginLog.setIsRenew(Const.DISABLE);
        userLoginLogDAO.insert(loginLog);
        // 登陆失败则直接返回
        if (loginException != null) {
            throw loginException;
        }
        // 设置登陆缓存
        long timestamp = System.currentTimeMillis();
        String loginToken = LoginUtils.createLoginToken(id, timestamp);
        UserDTO userCache = new UserDTO();
        userCache.setId(id);
        userCache.setUsername(username);
        userCache.setNickname(userInfo.getNickname());
        userCache.setTimestamp(timestamp);
        userCache.setIsAdmin(userInfo.getIsAdmin());
        // 设置绑定信息
        UserBindDTO bind = new UserBindDTO();
        bind.setTimestamp(timestamp);
        bind.setAddress(address);
        bind.setLocation(location);
        bind.setUserAgent(userAgent);
        // 设置登陆缓存
        long expire = Long.parseLong(SystemConfig.LOGIN_TOKEN_EXPIRE.getValue());
        String userInfoKey = Strings.format(CacheKeys.AUTHORIZATION_INFO_KEY, id);
        redisTemplate.opsForValue().set(userInfoKey, JSON.toJSONString(userCache));
        // 单端登录删除已绑定的缓存
        this.deleteBindTokenWithSimpleLogin(id);
        // 设置绑定缓存
        String loginBindKey = Strings.format(CacheKeys.AUTHORIZATION_BIND_KEY, id, timestamp);
        redisTemplate.opsForValue().set(loginBindKey, JSON.toJSONString(bind), expire, TimeUnit.HOURS);
        // 返回
        AuthorizationVO loginInfo = new AuthorizationVO();
        loginInfo.setToken(loginToken);
        loginInfo.setId(id);
        loginInfo.setUsername(username);
        loginInfo.setNickname(userInfo.getNickname());
        loginInfo.setIsAdmin(userInfo.getIsAdmin());
        return loginInfo;
    }

    @Override
    public void logout() {
        UserDTO user = Currents.getUser();
        if (user == null) {
            return;
        }
        Long id = user.getId();
        Long timestamp = user.getCurrentTimestamp();
        // 删除token
        redisTemplate.delete(Strings.format(CacheKeys.AUTHORIZATION_BIND_KEY, id, timestamp));
    }

    @Override
    public void resetPassword(AuthorizationRequest request) {

    }

    @Override
    public UserDTO getUserByToken(String token, String address) {
        // 获取 token 信息
        AuthorizationTokenDTO tokenInfo = LoginUtils.getLoginTokenInfo(token);
        if (tokenInfo == null) {
            return null;
        }
        // 获取用户缓存信息
        Long id = tokenInfo.getId();
        String authInfo = redisTemplate.opsForValue().get(Strings.format(CacheKeys.AUTHORIZATION_INFO_KEY, id));
        if (authInfo == null) {
            return null;
        }
        String bindInfo = redisTemplate.opsForValue().get(Strings.format(CacheKeys.AUTHORIZATION_BIND_KEY, id, tokenInfo.getTimestamp()));
        if (bindInfo == null) {
            return null;
        }
        // 检查绑定ip
        UserBindDTO bind = JSON.parseObject(bindInfo, UserBindDTO.class);
        if (String.valueOf(Const.ENABLE).equals(SystemConfig.LOGIN_BIND_ADDRESS.getValue())) {
            if (address != null && !bind.getAddress().equals(address)) {
                return null;
            }
        }
        // 返回
        UserDTO user = JSON.parseObject(authInfo, UserDTO.class);
        user.setCurrentTimestamp(bind.getTimestamp());
        return user;
    }

    /**
     * 检查认证失败次数
     *
     * @param username username
     */
    private void checkAuthorizationFailCount(String username) {
        // 未开启失败认证
        if (!Const.ENABLE.toString().equals(SystemConfig.LOGIN_FAILURE_LOCK.getValue())) {
            return;
        }
        // 获取失败次数
        String failCountKey = Strings.format(CacheKeys.AUTHORIZATION_FAIL_COUNT_KEY, username);
        String failCountVal = redisTemplate.opsForValue().get(failCountKey);
        if (failCountVal == null || !Strings.isInteger(failCountVal)) {
            return;
        }
        // 检查失败次数
        Integer threshold = Integer.valueOf(SystemConfig.LOGIN_FAILURE_LOCK_THRESHOLD.getValue());
        Valid.lt(Integer.valueOf(failCountVal), threshold, MessageConst.USER_LOCKED);
    }

    /**
     * 删除认证失败次数
     *
     * @param username username
     */
    private void deleteAuthorizationFailCount(String username) {
        // 未开启失败认证
        if (!Const.ENABLE.toString().equals(SystemConfig.LOGIN_FAILURE_LOCK.getValue())) {
            return;
        }
        // 删除失败次数
        String failCountKey = Strings.format(CacheKeys.AUTHORIZATION_FAIL_COUNT_KEY, username);
        redisTemplate.delete(failCountKey);
    }

    /**
     * 增加认证失败次数
     *
     * @param username username
     * @return count
     */
    private long addAuthorizationFailCount(String username) {
        // 未开启失败认证
        if (!Const.ENABLE.toString().equals(SystemConfig.LOGIN_FAILURE_LOCK.getValue())) {
            return 0;
        }
        // 增加失败次数
        String failCountKey = Strings.format(CacheKeys.AUTHORIZATION_FAIL_COUNT_KEY, username);
        return redisTemplate.opsForValue().increment(failCountKey);
    }

    /**
     * 检查认证失败除数是否达到锁定用户阈值
     *
     * @param id    id
     * @param count count
     */
    private void checkAuthorizationFailLockUser(Long id, long count) {
        // 未开启失败认证
        if (!Const.ENABLE.toString().equals(SystemConfig.LOGIN_FAILURE_LOCK.getValue())) {
            return;
        }
        // 小于阈值
        if (count < Long.parseLong(SystemConfig.LOGIN_FAILURE_LOCK_THRESHOLD.getValue())) {
            return;
        }
        // 大于阈值则更新锁定状态
        UserInfoDO update = new UserInfoDO();
        update.setId(id);
        update.setLockStatus(Const.DISABLE);
        userInfoDAO.updateById(update);
    }

    /**
     * 设置最后登录时间
     *
     * @param id id
     */
    private void setLastLoginTime(Long id) {
        UserInfoDO update = new UserInfoDO();
        update.setId(id);
        update.setLastLoginTime(new Date());
        userInfoDAO.updateById(update);
    }

    /**
     * 单设备登录 清空已绑定 token
     *
     * @param id id
     */
    private void deleteBindTokenWithSimpleLogin(Long id) {
        // 多端登录则跳过
        if (String.valueOf(Const.ENABLE).equals(SystemConfig.ALLOW_MULTIPLE_LOGIN.getValue())) {
            return;
        }
        // 单端登录 删除绑定缓存
        this.deleteBindToken(id);
    }

    /**
     * 清空已绑定 token
     *
     * @param id id
     */
    private void deleteBindToken(Long id) {
        String scanMatches = Strings.format(CacheKeys.AUTHORIZATION_BIND_KEY, id, "*");
        Set<String> bindTokens = RedisUtils.scanKeys(redisTemplate, scanMatches, Const.N_10000);
        if (!Lists.isEmpty(bindTokens)) {
            redisTemplate.delete(bindTokens);
        }
    }

}
