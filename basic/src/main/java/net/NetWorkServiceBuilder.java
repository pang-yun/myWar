package net;

import io.netty.channel.ChannelHandler;
import io.netty.channel.WriteBufferWaterMark;
import lombok.Data;


import java.util.List;

/**
 * @author pang-yun
 * Date:  2022-05-16 13:52
 * Description:
 */
@Data
public class NetWorkServiceBuilder<MonitorOption> {

    private int bossLoopGroupCount;
    private int workerLoopGroupCount;

    /**
     * 绑定端口
     */
    private int port;

    private MessagePool msgPool;
    private NetworkConsumer consumer;
    private NetworkEventListener networkEventListener;
    private boolean webSocket;
    private boolean ssl;
    private String sslKeyCertChainFile;
    private String sslKeyFile;
    private int idleMaxTime;
    private int soRecBuf;
    private int soSendBuf;


    /**
     * 设置 buff 水位线  防止 oom
     */
    private WriteBufferWaterMark writeBufferWaterMark;
    private MonitorOption clientPackageMonitorOption;
    private List<ChannelHandler> channelHandlerList;
}
