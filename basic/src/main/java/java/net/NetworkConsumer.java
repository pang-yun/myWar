package java.net;

import io.netty.channel.Channel;

/**
 * @author pang-yun
 * Date:  2022-05-16 14:06
 * Description:
 */

public interface NetworkConsumer {

    /**
     * 消费消息
     * @param channel
     * @param message
     */
    void consume(Channel channel, Message message);
}
