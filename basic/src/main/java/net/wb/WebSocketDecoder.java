package net.wb;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author pang-yun
 * Date:  2022-05-17 15:19
 * Description:
 */

public class WebSocketDecoder extends ChannelInboundHandlerAdapter {

    public WebSocketDecoder() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            super.channelRead(ctx, ((BinaryWebSocketFrame) msg).content());
        } else {
            super.channelRead(ctx, msg);
        }

    }
}
