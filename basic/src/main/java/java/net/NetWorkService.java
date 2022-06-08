//package java.net;
//
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.SslHandler;
//import io.netty.handler.timeout.IdleStateHandler;
//import io.netty.util.concurrent.Future;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.net.ssl.SSLException;
//import java.net.monitor.ClientPackageMonitor;
//import java.net.wb.WebSocketDecoder;
//import java.net.wb.WebSocketEncoder;
//import java.util.Iterator;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author pang-yun
// * Date:  2022-05-16 13:35
// * Description:  服务端启动项
// */
//@Slf4j
//public class NetWorkService {
//
//    private int bossLoopGroupCount;
//    private int workerLoopGroupCount;
//    private int port;
//    private ServerBootstrap bootstrap;
//    private int state;
//    private NioEventLoopGroup bossGroup;
//    private NioEventLoopGroup workerGroup;
//
//
//    private static final byte STATE_STOP = 0;
//    private static final byte STATE_START = 1;
//
//    /**
//     * 启动
//     */
//    public void start() {
//        try {
//            ChannelFuture channelFuture = this.bootstrap.bind(this.port);
//            channelFuture.sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//
//        this.state = 1;
//        log.info("Server on port : {} is started!!", this.port);
//    }
//
//    /**
//     * 关闭
//     */
//    public void stop() {
//        this.state = 0;
//        Future<?> bf = this.bossGroup.shutdownGracefully();
//        Future wf = this.workerGroup.shutdownGracefully();
//
//        try {
//            bf.get(5000L, TimeUnit.MILLISECONDS);
//            wf.get(5000L, TimeUnit.MILLISECONDS);
//        } catch (Exception var4) {
//            log.info("Netty服务器关闭失败", var4);
//        }
//
//        log.info("Netty Server on port:{} is closed", this.port);
//    }
//
//    public boolean isRunning() {
//        return this.state == 1;
//    }
//
//
//    NetWorkService(final NetWorkServiceBuilder builder) {
//        this.bossLoopGroupCount = builder.getBossLoopGroupCount();
//        this.workerLoopGroupCount = builder.getWorkerLoopGroupCount();
//        this.port = builder.getPort();
//        this.bossGroup = new NioEventLoopGroup(this.bossLoopGroupCount);
//        this.workerGroup = new NioEventLoopGroup(this.workerLoopGroupCount);
//        final SslContext sslCtx;
//        if (builder.isSsl()) {
//            try {
//                sslCtx = SslContextBuilder.forServer(new File(builder.getSslKeyCertChainFile()), new File(builder.getSslKeyFile())).build();
//            } catch (SSLException var4) {
//                throw new RuntimeException("sslCtx create failed.", var4);
//            }
//        } else {
//            sslCtx = null;
//        }
//
//        this.bootstrap = new ServerBootstrap();
//        this.bootstrap.group(this.bossGroup, this.workerGroup);
//        this.bootstrap.channel(NioServerSocketChannel.class);
//        this.bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
//        this.bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
//
//        // tcp 缓冲区 大小设置
//        if (builder.getSoRecBuf() > 0) {
//            this.bootstrap.childOption(ChannelOption.SO_RCVBUF, builder.getSoRecBuf());
//        } else {
//            this.bootstrap.childOption(ChannelOption.SO_RCVBUF, 131072);
//        }
//
//        if (builder.getSoSendBuf() > 0) {
//            this.bootstrap.childOption(ChannelOption.SO_SNDBUF, builder.getSoSendBuf());
//        } else {
//            this.bootstrap.childOption(ChannelOption.SO_SNDBUF, 131072);
//        }
//
//        // 水位线 设置
//        if (builder.getWriteBufferWaterMark() != null) {
//            this.bootstrap.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, builder.getWriteBufferWaterMark());
//        }
//
//        this.bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
//        this.bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//            @Override
//            protected void initChannel(SocketChannel ch) throws Exception {
//                ChannelPipeline pip = ch.pipeline();
//                if (sslCtx != null) {
//                    SslHandler sslHandler = sslCtx.newHandler(ch.alloc());
//                    pip.addLast("sslHandler", sslHandler);
//                }
//
//                // 如果是 web
//                if (builder.isWebSocket()) {
//                    //解码成 httpRequest
//                    pip.addLast(new ChannelHandler[]{new HttpServerCodec()});
//
//                    // 解码成 FullHttpRequest
//                    /*当用POST方式请求服务器的时候，对应的参数信息是保存在message body中的,
//                    如果只是单纯的用HttpServerCodec是无法完全的解析Http POST请求的，
//                    因为HttpServerCodec只能获取uri中参数，所以需要加上HttpObjectAggregator。*/
//                    pip.addLast(new ChannelHandler[]{new HttpObjectAggregator(65536)});
//
//                    // 添加 WebSocket解码器
//                    pip.addLast(new ChannelHandler[]{new WebSocketServerProtocolHandler("/", true)});
//
//                    // 自定义的编解码器
//                    pip.addLast(new ChannelHandler[]{new WebSocketDecoder()});
//                    pip.addLast(new ChannelHandler[]{new WebSocketEncoder()});
//                }
//
//                if (builder.getIdleMaxTime() > 0) {
//                    pip.addLast("Idle", new IdleStateHandler(builder.getIdleMaxTime(), 0, 0));
//                }
//
//                pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getMsgPool()));
//                pip.addLast("NettyMessageEncoder", new MessageEncoder());
//                if (builder.getClientPackageMonitorOption().rateValidate() || builder.getClientPackageMonitorOption().sequenceValidate()) {
//                    pip.addLast("ClientPackageMonitor", new ClientPackageMonitor(builder.getClientPackageMonitorOption()));
//                }
//
//                Iterator var5 = builder.getChannelHandlerList().iterator();
//
//                while (var5.hasNext()) {
//                    ChannelHandler handler = (ChannelHandler) var5.next();
//                    pip.addLast(new ChannelHandler[]{handler});
//                }
//
//                MessageExecutor executor = new MessageExecutor(builder.getConsumer(), builder.getNetworkEventListener(), builder.getIdleMaxTime() > 0);
//                pip.addLast("NettyMessageExecutor", executor);
//            }
//        });
//    }
//}
