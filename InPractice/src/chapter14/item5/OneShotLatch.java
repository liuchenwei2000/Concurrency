package chapter14.item5;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 使用 AQS 实现的二元闭锁
 * <p>
 *     二元闭锁包含两个公有方法：await 和 signal，分别对应获取操作和释放操作。
 *     最初闭锁是关闭的，任何调用 await 的线程都将阻塞直到闭锁被打开。
 *     当通过调用 singnal 打开闭锁时，所有等待中的线程都将被释放，并且随后到达闭锁的线程也允许执行。
 * <p>
 *     所有同步器类都没有直接扩展 AQS，而是都将它们的相应功能委托给私有的 AQS 子类来实现。
 * <p>
 * Created by liuchenwei on 2016/5/9.
 */
@ThreadSafe
public class OneShotLatch {

    private final Sync sync = new Sync();

    /**
     * await 方法调用 AQS 的 acquireSharedInterruptibly，
     * 后者接着调用 Sync 对象的 tryAcquireShared 方法。
     * 在 tryAcquireShared 的实现中必须返回一个值来表示该获取操作能否执行。
     * acquireSharedInterruptibly 方法处理失败的方式是把这个线程放入等待线程队列中。
     */
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }

    /**
     * signal 方法将调用 releaseShared，接着又会调用 Sync 对象的 tryReleaseShared。
     * 在 tryReleaseShared 中将无条件地把闭锁的状态设置为打开，
     * 通过返回值表示该同步器处于完全被释放的状态。
     * 因而 AQS 让所有等待中的线程都尝试重新请求该同步器，
     * 并且由于 tryAcquireShared 将返回成功，因此现在的请求操作都将成功。
     */
    public void signal() {
        sync.releaseShared(0);
    }

    /**
     * 基于 AQS 的同步器类
     * <p>
     *     AQS 状态用来表示闭锁状态——关闭（0）或者打开（1）。
     */
    private class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected int tryAcquireShared(int arg) {
            // 如果闭锁时开的（state == 1），则该操作将成功，否则将失败
            return (getState() == 1) ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);// 打开闭锁
            return true;// 允许其他线程获取该闭锁
        }
    }
}
