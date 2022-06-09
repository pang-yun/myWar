package com.im;

import com.im.config.ServerConfig;
import com.im.kit.RegisterToZk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import util.ExecutorUtil;

/**
 * @author pang-yun
 * Date:  2022-06-09 10:58
 * Description:
 * <p>
 * CommandLineRunner、ApplicationRunner 接口是在容器启动成功后的最后一步回调（类似开机自启动）。
 */

@SpringBootApplication
@Slf4j
public class ImServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ImServerApplication.class);
        log.info("ImServer start-----------------------------------------!!!");
    }


    @Autowired
    private ServerConfig serverConfig;


    @Value("${server.port}")
    private int httpPort;

    @Override
    public void run(String... args) throws Exception {
        // 启动后将本机注册到 zk
        String hostAddress = InetAddress.getLocalHost().getHostAddress();

        RegisterToZk registerToZk = new RegisterToZk(hostAddress, serverConfig.getImServerPort(), httpPort);

        ExecutorUtil.COMMON_USE_THREAD.execute(registerToZk);



    }
}
