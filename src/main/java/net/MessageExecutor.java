package net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pang-yun
 * Date:  2022-05-17 16:51
 * Description:
 */

@Slf4j
@Data
public class MessageExecutor extends ChannelInboundHandlerAdapter {
    private NetworkConsumer consumer;
    protected NetworkEventListener listener;
    private boolean idleCheck = false;

    public MessageExecutor(NetworkConsumer consumer, NetworkEventListener listener, boolean idleCheck) {
        this.consumer = consumer;
        this.listener = listener;
        this.idleCheck = idleCheck;
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            this.consumer.consume(ctx.channel(), (Message)msg);
        } else {
            ReferenceCountUtil.release(msg);
            log.info("不识别的msg类型：{}", msg.getClass().getName());
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.listener.onExceptionOccur(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.listener.onConnected(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.listener.onDisconnected(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (this.idleCheck && evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            IdleState state = idleStateEvent.state();
            if (state == IdleState.READER_IDLE) {
                this.listener.idle(ctx, state);
            }
        }

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        this.listener.onChannelWritabilityChanged(ctx);
    }


}

