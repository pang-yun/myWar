package java.net.monitor;

import io.netty.channel.Channel;

/**
 * @author pang-yun
 * Date:  2022-05-16 15:21
 * Description: 默认的监视器 实现
 */

public class DefaultOpenMonitorOption implements MonitorOption {
    public DefaultOpenMonitorOption() {
    }

    @Override
    public boolean rateValidate() {
        return false;
    }

    @Override
    public int rateMax() {
        return 30;
    }

    @Override
    public boolean rateExceed(Channel channel) {
        return true;
    }

    @Override
    public boolean sequenceValidate() {
        return false;
    }

    @Override
    public boolean notSequential(Channel channel) {
        return true;
    }

}
