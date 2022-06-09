package com.cache;

import com.google.common.cache.LoadingCache;
import com.route.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pang-yun
 * Date:  2022-06-09 11:40
 * Description:  服务器节点缓存
 */

@Component
@Slf4j
public class ServerCache {

    /**
     * LoadingCache 是 guava 提供的 本地缓存 提供了基于容量，时间和引用的缓存回收方式
     */
    @Autowired
    private LoadingCache<String, String> cache;


    @Autowired
    private ZkUtil zkUtil;


    public void addCache(String key) {
        cache.put(key, key);
    }

    /**
     * 更新所有缓存
     *
     * @param list
     */
    public void updateCache(List<String> list) {
        cache.invalidateAll();
        list.forEach(currentChild -> {
            // currentChildren=ip-127.0.0.1:11212:9082 or 127.0.0.1:11212:9082
            String key;
            if (currentChild.split("-").length == 2) {
                key = currentChild.split("-")[1];
            } else {
                key = currentChild;
            }
            addCache(key);
        });
    }


    /**
     * 获取服务器列表
     *
     * @return
     */
    public List<String> getServerList() {

        // 从 zookeeper 拉取
        if (cache.size() == 0) {
            updateCache(zkUtil.getAllNodeFromZk());
        }

        return new ArrayList<>(cache.asMap().keySet());
    }
}
