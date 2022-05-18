package net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pang-yun
 * Date:  2022-05-17 16:47
 * Description:
 */

@Slf4j
public class MessagePackage {

    public MessagePackage() {
    }

    public static void packageMsg(ByteBuf buf, Message msg) {
        byte[] content = msg.encode();
        int length = 10 + content.length;
        msg.setLength(length);
        if (PackageCounter.isCounting()) {
            PackageCounter.down(msg.getId(), length);
        }

        buf.writeInt(length);
        buf.writeInt(msg.getId());
        buf.writeShort(msg.getSequence());
        buf.writeBytes(content);
    }

    public static ByteBuf packageMsgGroup(Message msg) {
        byte[] content = msg.encode();
        int length = content.length;
        msg.setLength(length);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(length);
        buffer.writeBytes(content);
        return buffer;
    }
}
