package net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pang-yun
 * Date:  2022-05-17 16:46
 * Description:
 */
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<Message> {

    public MessageEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) {
        if (msg.getId() == -1) {
            ByteBuf buffer = null;

            try {
                buffer = MessagePackage.packageMsgGroup(msg);
                if (buffer != null) {
                    out.writeBytes(buffer);
                    log.debug("编码消息成功:{}", msg);
                } else {
                    log.debug("编码消息失败:{}", msg);
                }
            } finally {
                if (buffer != null) {
                    ReferenceCountUtil.release(buffer);
                }

            }
        } else {
            MessagePackage.packageMsg(out, msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
