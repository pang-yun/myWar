package concurrent;

import concurrent.queue.ICommandQueue;

/**
 * @author pang-yun
 * Date:  2022-05-17 17:18
 * Description:
 */

public interface IQueueDriverCommand extends ICommand{
    int getQueueId();

    void setQueueId(int queueId);

    ICommandQueue<IQueueDriverCommand> getCommandQueue();

    void setCommandQueue(ICommandQueue<IQueueDriverCommand> commandQueue);
}
