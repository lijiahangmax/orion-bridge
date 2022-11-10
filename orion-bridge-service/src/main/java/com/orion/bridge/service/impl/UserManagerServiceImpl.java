package com.orion.bridge.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.bridge.config.CacheKeys;
import com.orion.bridge.constant.Const;
import com.orion.bridge.constant.MessageConst;
import com.orion.bridge.dao.UserLoginLogDAO;
import com.orion.bridge.entity.domain.UserLoginLogDO;
import com.orion.bridge.model.dto.UserBindDTO;
import com.orion.bridge.model.dto.UserDTO;
import com.orion.bridge.model.vo.AuthorizedDeviceVO;
import com.orion.bridge.model.vo.LoginHistoryVO;
import com.orion.bridge.service.api.UserManagerService;
import com.orion.bridge.utils.*;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.convert.Converts;
import com.orion.lang.utils.time.Dates;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户管理服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/21 1:22
 */
@Service("userManagerService")
public class UserManagerServiceImpl implements UserManagerService {

    @Resource
    private UserLoginLogDAO userLoginLogDAO;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public List<AuthorizedDeviceVO> getAuthorizedDevices(Long userId) {
        UserDTO currentUser = Currents.getUser();
        final boolean isCurrentUser = userId.equals(currentUser.getId());
        // 获取绑定的token
        Set<String> bindTokens = this.findBindTokens(userId);
        if (Lists.isEmpty(bindTokens)) {
            return Lists.empty();
        }
        // 获取过期时间
        Map<Long, String> expireMapping = bindTokens.stream()
                .collect(Collectors.toMap(s -> Long.valueOf(s.split(":")[3]),
                        s -> Optional.of(s)
                                .map(redisTemplate::getExpire)
                                .map(e -> e * Dates.SECOND_STAMP)
                                .map(Utils::interval)
                                .orElse(Const.EMPTY)));
        // 封装数据
        List<AuthorizedDeviceVO> devices = bindTokens.stream()
                .map(s -> JSON.parseObject(s, UserBindDTO.class))
                .sorted(Comparator.comparing(UserBindDTO::getTimestamp).reversed())
                .map(s -> Converts.to(s, AuthorizedDeviceVO.class))
                .collect(Collectors.toList());
        // 设置过期时间
        devices.forEach(s -> {
            s.setExpireTime(expireMapping.get(s.getTimestamp()));
            if (isCurrentUser) {
                s.setCurrentTimestamp(currentUser.getCurrentTimestamp());
            }
        });
        return devices;
    }

    @Override
    public void offlineDevice(Long userId, Long timestamp) {
        String offlineKey = Strings.format(CacheKeys.AUTHORIZATION_BIND_KEY, userId, timestamp);
        // 删除绑定缓存
        Boolean result = redisTemplate.delete(offlineKey);
        Valid.isTrue(Objects.requireNonNull(result), MessageConst.OPERATOR_ERROR);
    }

    @Override
    public List<LoginHistoryVO> getLoginHistory(Long userId, Integer limit) {
        LambdaQueryWrapper<UserLoginLogDO> wrapper = new LambdaQueryWrapper<UserLoginLogDO>()
                .eq(UserLoginLogDO::getUserId, userId)
                .orderByDesc(UserLoginLogDO::getCreateTime)
                .last(Const.LIMIT + Const.SPACE + limit);
        return DataQuery.of(userLoginLogDAO)
                .wrapper(wrapper)
                .list(LoginHistoryVO.class);
    }

    /**
     * 获取用户绑定 token
     *
     * @param id id
     * @return token
     */
    private Set<String> findBindTokens(Long id) {
        String scanMatches = Strings.format(CacheKeys.AUTHORIZATION_BIND_KEY, id, "*");
        return RedisUtils.scanKeys(redisTemplate, scanMatches, Const.N_10000);
    }

}
