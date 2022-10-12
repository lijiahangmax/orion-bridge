package com.orion.bridge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Jiahang Li
 */
@SpringBootApplication
@ImportResource(locations = {"classpath:config/spring-*.xml"})
@MapperScan("com.orion.bridge.dao")
public class OrionBridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrionBridgeApplication.class, args);
    }

}
