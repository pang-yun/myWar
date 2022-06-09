package com.im.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pang-yun
 * Date:  2022-06-09 14:15
 * Description:
 */
@Data
@Component
public class ServerConfig {

    /**
     * 注册根节点
     */
    @Value("${zk.zkRoot}")
    private String zkRoot;

    /**
     * 注册地址
     */
    @Value("${zk.zkAddr}")
    private String zkAddr;

    /**
     * 是否注册
     */
    @Value("${zk.zkSwitch}")
    private boolean zkSwitch;


    /**
     * 聊天服务器端口
     */
    @Value("${im-server.port}")
    private int imServerPort;


    /**
     * 路由地址
     */
    @Value("${routeUrl}")
    private String routeUrl;

    /**
     * 检测多少秒没有收到客户端心跳后服务端关闭连接 单位秒
     */
    @Value("${im-server.heartbeat.time}")
    private long heartBeatTime;

    /**
     * zk 连接超时时限
     */
    @Value("${zk.connect.timeout}")
    private int zkConnectTimeout;
}
