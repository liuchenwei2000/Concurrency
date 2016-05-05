package chapter14.item4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 Lock 来实现 Semaphore
 * <p>
 *     ReentrantLock 和 Semaphore 在实现时都使用了一个共同的基类：
 *     AbstractQueuedSynchronizer（AQS），这个类也是其他许多同步类的基类。
 *     AQS 是一个用于构建锁和同步器的框架，许多同步器都可以通过它容易且高效地构造出来。
 *     不仅 ReentrantLock 和 Semaphore 是基于 AQS 构建的，还包括 CountDownLatch、
 *     ReentrantReadWriteLock、SemaphoreQueue 和 FutureTask。
 *     基于 AQS 来构建同步器能极大地减少实现工作，而且不必处理在多个位置上发生的竞争问题。
 * <p>
 * Created by liuchenwei on 2016/5/5
 */
public class SemaphoreOnLock {

    private final Lock lock = new ReentrantLock();
    // 条件谓词：permits > 0
    private final Condition permitsAvailable = lock.newCondition();

    private int permits;

    public SemaphoreOnLock(int permits) {
        this.permits = permits;
    }

    public void acquire() throws InterruptedException {
        lock.lock();

        try {
            while (permits == 0) {
                permitsAvailable.await();
            }
            --permits;
        } finally {
            lock.unlock();
        }
    }

    public void release() throws InterruptedException {
        lock.lock();

        try {
            ++permits;
            permitsAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
