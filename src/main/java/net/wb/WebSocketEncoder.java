package net.wb;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author pang-yun
 * Date:  2022-05-17 15:41
 * Description:
 */

public class WebSocketEncoder extends ChannelOutboundHandlerAdapter {
    public WebSocketEncoder() {
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            ctx.write(new BinaryWebSocketFrame((ByteBuf)msg), promise);
        } else {
            super.write(ctx, msg, promise);
        }

    }
}
