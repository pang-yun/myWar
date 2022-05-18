package net.monitor;

import io.netty.channel.Channel;

/**
 * @author pang-yun
 * Date:  2022-05-16 14:59
 * Description:
 */

public interface MonitorOption {

    /**
     *判断消息是否超速
     * @return
     */
    boolean  rateValidate();

    /**
     * 设置最大速率
     * @return
     */
    int rateMax();


    /**
     * 判断channel 发送频率
     * @param channel
     * @return
     */
    boolean rateExceed(Channel channel);


    /**
     * 次序是否合法
     * @return
     */
    boolean sequenceValidate();


    /**
     *不连续
     * @param channel
     * @return
     */
    boolean notSequential(Channel channel);
}
