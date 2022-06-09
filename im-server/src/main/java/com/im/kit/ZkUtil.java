package com.im.kit;

import com.im.config.ServerConfig;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pang-yun
 * Date:  2022-06-09 16:40
 * Description:
 */
@Component
public class ZkUtil {

    @Autowired
    private ZkClient zkClient;


    @Autowired
    private ServerConfig serverConfig;


    /**
     * 创建父级节点
     */
    public void createRootNode() {
        boolean exists = zkClient.exists(serverConfig.getZkRoot());
        if (exists) {
            return;
        }

        //创建 root
        zkClient.createPersistent(serverConfig.getZkRoot());
    }

    /**
     * 写入指定节点 临时目录
     *
     * @param path
     */
    public void createNode(String path) {
        zkClient.createEphemeral(path);
    }
}
