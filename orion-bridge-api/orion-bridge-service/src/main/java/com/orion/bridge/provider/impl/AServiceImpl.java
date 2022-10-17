package com.orion.bridge.provider.impl;

import com.orion.bridge.rpc.provider.api.AService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/10/12 23:48
 */
@DubboService
public class AServiceImpl implements AService {

    @Override
    public void s() {
        System.out.println(123);
    }

}
