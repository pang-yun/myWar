package net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;

/**
 * @author pang-yun
 * Date:  2022-05-16 14:09
 * Description:  网络时间 监听  连接  断开等等
 */

public interface NetworkEventListener {
    /**
     * 连接事件
     *
     * @param context
     */
    void onConnected(ChannelHandlerContext context);

    /**
     * 断开事件
     *
     * @param context
     */
    void onDisconnected(ChannelHandlerContext context);

    /**
     * 出现异常
     * @param context
     * @param throwable
     */
    void onExceptionOccur(ChannelHandlerContext context, Throwable throwable);

    void idle(ChannelHandlerContext context, IdleState state);

    /**
     * channel 写状态 发生变化时触发
     * @param ctx
     */
    default void onChannelWritabilityChanged(ChannelHandlerContext ctx) {
    }


}
