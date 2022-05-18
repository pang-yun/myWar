package concurrent.queue;

/**
 * @author pang-yun
 * Date:  2022-05-17 17:18
 * Description:
 */

public interface ICommandQueue<V> {
    V poll();

    boolean offer(V var1);

    void clear();

    int size();

    boolean isRunning();

    void setRunning(boolean var1);

    void setName(String var1);

    String getName();

    boolean isDestroy();

    void setDestroy(boolean var1);
}
