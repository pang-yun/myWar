package com.im.kit;

import com.im.config.ServerConfig;
import com.im.util.BeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pang-yun
 * Date:  2022-06-09 16:38
 * Description:  将节点注册进入zk
 */
@Slf4j
public class RegisterToZk implements Runnable {


    private String ip;

    /**
     * 服务器端口
     */
    private int imServerPort;

    /**
     * http 端口
     */
    private int httpPort;


    private ZkUtil zkUtil;

    private ServerConfig serverConfig;


    public RegisterToZk(String ip, int imServerPort, int httpPort) {
        this.ip = ip;
        this.imServerPort = imServerPort;
        this.httpPort = httpPort;
        this.zkUtil = BeanFactory.getBean(ZkUtil.class);
        this.serverConfig = BeanFactory.getBean(ServerConfig.class);
    }

    @Override
    public void run() {

        //创建父节点
        zkUtil.createRootNode();

        //是否要将自己注册到 ZK
        if (serverConfig.isZkSwitch()) {
            String path = serverConfig.getZkRoot() + "/ip-" + ip + ":" + imServerPort + ":" + httpPort;
            zkUtil.createNode(path);
            log.info("Registry zookeeper success, msg=[{}]", path);
        }

    }
}
