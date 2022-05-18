package util;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author pang-yun
 * Date:  2022-05-16 14:00
 * Description:
 */

public class SessionAttributeUtil {
    public static <T> T get(Channel channel, AttributeKey<T> key) {
        return channel.attr(key).get();
    }

    public static <T> void set(Channel channel, AttributeKey<T> key, T value) {
        channel.attr(key).set(value);
    }
}
