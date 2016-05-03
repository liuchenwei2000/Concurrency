package chapter13.item1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 0，Lock 与 ReentrantLock
 * <p>
 * <p>
 * Created by liuchenwei on 2016/5/3.
 */
public class LockTest {

    /**
     * Lock 接口中定义了一组抽象的加锁操作。
     * <p>
     *     与内置加锁机制不同的是，Lock 提供了一种无条件的、可轮询的、
     *     定时的以及可中断的锁获取操作，所有加锁和解锁的方法都是显式的。
     *     Lock 的实现中必须提供与内置锁相同的内存可见性语义，
     *     但在加锁语义、调度算法、顺序保证以及性能特性等方面可以有所不同。
     */
    public static class LockImpl implements Lock {

        @Override
        public void lock() {

        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {

        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }

    /**
     * ReentrantLock 实现了 Lock 接口，并提供了与 synchronized 相同的互斥性和内存可见性。
     * 在获取 ReentrantLock 时，有着与进入同步代码块相同的内存语义，
     * 在释放 ReentrantLock 时，同样有着与退出同步代码块相同的内存语义。
     * 此外，与 synchronized 一样，ReentrantLock 还提供了可重入的加锁语义。
     * <p>
     *     在大多数情况下，内置锁都能很好地工作，但在功能上存在一些局限性：
     *     例如无法中断一个正在等待获取锁的线程，或者无法在请求获取锁时无限的等待下去。
     */
    public static class ReentrantLockTest {

        private Lock lock = new ReentrantLock();

        /**
         * 演示使用 Lock 接口的标准使用形式：
         * 必须在 finally 块中释放锁，否则，如果在被保护的代码中抛出了异常，那么这个锁永远都无法释放。
         */
        public void testLock() {
            lock.lock();// 加锁

            try {
                // 更新对象状态
            } catch (Exception e) {
                // 捕获异常，并在必要时恢复不变性条件
                e.printStackTrace();
            } finally {
                lock.unlock();// 释放锁
            }
        }
    }
}
