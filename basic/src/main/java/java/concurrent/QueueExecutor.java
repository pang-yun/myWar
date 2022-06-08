package java.concurrent;

import concurrent.IQueueDriverCommand;
import concurrent.queue.ICommandQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pang-yun
 * Date:  2022-05-17 17:55
 * Description:
 */
@Slf4j
public class QueueExecutor extends ThreadPoolExecutor {
    @Getter
    private String name;


    private int corePoolSize;

    @Getter
    private int maxPoolSize;

    public QueueExecutor(final String name, int corePoolSize, int maxPoolSize) {
        /**
         * 核心线程
         * 最大线程
         * 线程最大空闲时间
         * 时间单位
         * 线程等待队列
         * 线程工厂
         */
        super(corePoolSize, maxPoolSize, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
            AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                int curCount = this.count.incrementAndGet();
                log.error("创建线程:" + name + "-" + curCount);
                return new Thread(r, name + "-" + curCount);
            }
        });
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    protected void afterExecute(Runnable task, Throwable throwable) {
        super.afterExecute(task, throwable);
        IQueueDriverCommand work = (IQueueDriverCommand) task;
        ICommandQueue<IQueueDriverCommand> queue = work.getCommandQueue();
        synchronized (queue) {
            if (queue.isDestroy()) {
                log.info("队列已经销毁，不进行command传递执行");
            } else {
                IQueueDriverCommand nextCommand = (IQueueDriverCommand) queue.poll();
                if (nextCommand == null) {
                    queue.setRunning(false);
                } else {
                    this.execute(nextCommand);
                }

            }
        }
    }



    @Override
    public int getCorePoolSize() {
        return this.corePoolSize;
    }


}