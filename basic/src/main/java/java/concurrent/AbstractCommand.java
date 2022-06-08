package java.concurrent;

import concurrent.IQueueDriverCommand;
import concurrent.queue.ICommandQueue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.concurrent.queue.ICommandQueue;

/**
 * @author pang-yun
 * Date:  2022-05-17 17:22
 * Description:
 */

@Slf4j
@Data
public abstract class AbstractCommand implements IQueueDriverCommand {

    private transient ICommandQueue<IQueueDriverCommand> commandQueue;
    protected transient int queueId;

    public AbstractCommand() {
    }

    @Override
    public void run() {
        try {
            if (QueueMonitor.open) {
                long time = System.currentTimeMillis();
                this.doAction();
                int total = (int) (System.currentTimeMillis() - time);
                QueueMonitor.monitor(this, time, total);
            } else {
                this.doAction();
            }
        } catch (Throwable var4) {
            log.error("命令执行错误", var4);
        }

    }

    public abstract void doAction();

}
