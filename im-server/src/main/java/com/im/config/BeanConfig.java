package com.im.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pang-yun
 * Date:  2022-06-09 19:46
 * Description:
 */

@Configuration
public class BeanConfig {

    @Autowired
    private ServerConfig serverConfig;

    @Bean
    public ZkClient buildZkClient() {
        return new ZkClient(serverConfig.getZkAddr(), serverConfig.getZkConnectTimeout());
    }
}
