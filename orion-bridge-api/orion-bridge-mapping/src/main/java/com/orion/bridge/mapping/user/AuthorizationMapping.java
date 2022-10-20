package com.orion.bridge.mapping.user;

import com.orion.bridge.entity.domain.UserLoginLogDO;
import com.orion.bridge.model.dto.UserBindDTO;
import com.orion.bridge.model.dto.UserDTO;
import com.orion.bridge.model.vo.AuthorizationVO;
import com.orion.bridge.model.vo.AuthorizedDeviceVO;
import com.orion.bridge.model.vo.LoginHistoryVO;
import com.orion.lang.utils.convert.TypeStore;

/**
 * 认证信息 mapping
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/21 0:15
 */
public class AuthorizationMapping {

    static {
        TypeStore.STORE.register(UserDTO.class, AuthorizationVO.class, p -> {
            AuthorizationVO vo = new AuthorizationVO();
            vo.setId(p.getId());
            vo.setUsername(p.getUsername());
            vo.setNickname(p.getNickname());
            vo.setIsAdmin(p.getIsAdmin());
            return vo;
        });
    }

    static {
        TypeStore.STORE.register(UserBindDTO.class, AuthorizedDeviceVO.class, p -> {
            AuthorizedDeviceVO vo = new AuthorizedDeviceVO();
            vo.setAddress(p.getAddress());
            vo.setLocation(p.getLocation());
            vo.setUserAgent(p.getUserAgent());
            vo.setTimestamp(p.getTimestamp());
            return vo;
        });
    }

    static {
        TypeStore.STORE.register(UserLoginLogDO.class, LoginHistoryVO.class, p -> {
            LoginHistoryVO vo = new LoginHistoryVO();
            vo.setAddress(p.getAddress());
            vo.setLocation(p.getLocation());
            vo.setUserAgent(p.getUserAgent());
            vo.setResult(p.getLoginResult());
            vo.setFailMessage(p.getFailMessage());
            vo.setRenew(p.getAddress());
            vo.setLoginTime(p.getCreateTime());
            return vo;
        });
    }

}
