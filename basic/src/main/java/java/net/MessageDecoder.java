package java.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pang-yun
 * Date:  2022-05-17 15:45
 * Description:   消息解码器
 *
        自定义长度解码器LengthFieldBasedFrameDecoder构造器，涉及5个参数，都与长度域（数据包中的长度字段）相关，具体介绍如下：
 * （1） maxFrameLength - 发送的数据包最大长度；
 * （2） lengthFieldOffset - 长度域偏移量，指的是长度域位于整个数据包字节数组中的下标；
 * （3） lengthFieldLength - 长度域的自己的字节数长度。
 * （4） lengthAdjustment – 长度域的偏移量矫正。 如果长度域的值，除了包含有效数据域的长度外，还包含了其他域（如长度域自身）长度，
 *                              那么，就需要进行矫正。矫正的值为：包长 - 长度域的值 – 长度域偏移 – 长度域长。
 * （5） initialBytesToStrip – 丢弃的起始字节数。丢弃处于有效数据前面的字节数量。比如前面有4个节点的长度域，则它的值为4。
 */
@Slf4j
public class MessageDecoder extends LengthFieldBasedFrameDecoder {
    private static boolean msgUnregisterLog;
    private MessagePool msgPool;

    private MessageDecoder(MessagePool msgPool, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        this.msgPool = msgPool;
    }

    public MessageDecoder(MessagePool msgPool) {
        this(msgPool, 1048576, 0, 4, -4, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf frame) throws Exception {
        frame = (ByteBuf) super.decode(ctx, frame);
        if (frame == null) {
            return null;
        } else {
            Object var4;
            try {
                int length = frame.readInt();
                int id = frame.readInt();
                short sequence = frame.readShort();
                Message message = this.msgPool.getMessage(id);
                if (message == null) {
                    if (msgUnregisterLog) {
                        log.error("未注册的消息,id:" + id);
                    }

                    Object var16 = null;
                    return var16;
                }

                byte[] bytes = null;
                int remainLength = frame.readableBytes();
                if (remainLength > 0) {
                    bytes = new byte[remainLength];
                    frame.readBytes(bytes);
                } else if (remainLength == 0) {
                    bytes = new byte[0];
                }

                message.setLength(length);
                message.setSequence(sequence);
                if (bytes != null) {
                    message.decode(bytes);
                }

               log.debug("解析消息:" + message);
                Message var9 = message;
                return var9;
            } catch (Throwable var13) {
               log.error(ctx.channel() + "消息解码异常", var13);
                var4 = null;
            } finally {
                if (frame != null) {
                    // 自己释放内存
                    ReferenceCountUtil.release(frame);
                }

            }

            return var4;
        }
    }

    static {
        String msgUnregisterLogStr = System.getProperty("game.log.MsgUnregister", "false");
        msgUnregisterLog = Boolean.parseBoolean(msgUnregisterLogStr);
    }
}
