package util;

import java.util.concurrent.*;

/**
 * @author pang-yun
 * Date:  2022-06-09 17:43
 * Description:
 */

public enum ExecutorUtil {
    ;

    public static ExecutorService COMMON_USE_THREAD = new ThreadPoolExecutor(1, 1, 10000000,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

}
