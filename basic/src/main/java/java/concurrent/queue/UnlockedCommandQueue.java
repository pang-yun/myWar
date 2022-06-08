package java.concurrent.queue;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author pang-yun
 * Date:  2022-05-17 17:44
 * Description:
 */

public class UnlockedCommandQueue<V> implements ICommandQueue<V> {
    private final Queue<V> queue;
    private boolean running = false;
    private boolean destroy = false;
    private String name;

    public UnlockedCommandQueue() {
        this.queue = new ArrayDeque();
    }

    public UnlockedCommandQueue(int numElements) {
        this.queue = new ArrayDeque(numElements);
    }

    @Override
    public V poll() {
        return this.queue.poll();
    }

    @Override
    public boolean offer(V value) {
        return this.queue.offer(value);
    }

    @Override
    public void clear() {
        this.queue.clear();
    }

    @Override
    public int size() {
        return this.queue.size();
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDestroy() {
        return this.destroy;
    }

    @Override
    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }
}

