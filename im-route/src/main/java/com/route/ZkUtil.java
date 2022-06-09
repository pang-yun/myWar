package com.route;

import com.alibaba.fastjson.JSON;
import com.cache.ServerCache;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author pang-yun
 * Date:  2022-06-09 11:09
 * Description:  使用zkclient 实现 zk
 */
@Component
@Slf4j
public class ZkUtil {


    @Autowired
    private ZkClient zkClient;


    @Autowired
    private ServerCache serverCache;





    /**
     * 从 zookeeper 中获取所有节点
     *
     * @return
     */
    public List<String> getAllNodeFromZk() {
        List<String> children = zkClient.getChildren("/route");
        log.info("zookeeper 中所有节点 : {}", JSON.toJSONString(children));
        return children;
    }


    /**
     * 节点发生变化时，更新节点数据
     *
     * @param path
     */
    public void subscribeEvent(String path) {
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                log.info("清除并更新当前服务器缓存， 父节点 : {}, children : {}", s, JSON.toJSONString(list));
                serverCache.updateCache(list);
            }
        });
    }





}
