package chapter13.item1;

import chapter10.item1.DynamicDeadLock.InsufficientFundsException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1，轮询锁与定时锁
 * <p>
 *     可定时的与可轮询的锁获取模式是由 tryLock 方法实现的，
 *     与无条件的锁获取模式相比，它具有更完善的错误恢复机制。
 *     可定时的与可轮询的锁可以有效避免死锁的发生。
 * <p>
 *     如果不能获得所有需要的锁，那么可以使用可定时的或可轮询的锁获取方式，
 *     从而使你重新获得控制权，释放已经获得的锁，然后重新尝试获取所有的锁。
 * <p>
 * Created by liuchenwei on 2016/5/3.
 */
public class PolledAndTimedLockTest {

    /**
     * 1，通过 tryLock 的轮询锁获取模式来避免锁顺序死锁的 Bank
     * <p>
     *     使用 tryLock 来获取两个锁，如果不能同时获得，那么就回退并重新尝试。
     *     在休眠时间中包含随机部分，从而降低发生活锁的可能性。
     * <p>
     *     还可以考虑：如果在指定时间内不能获得所需要的锁，
     *     那么方法直接返回一个失败状态，从而使该操作平缓地失败。
     */
    public static class Bank {

        public void transferMoney(Account from, Account to, double amount)
                throws InsufficientFundsException, InterruptedException {
            // 轮询获得两个锁
            while (true) {
                if (from.lock.tryLock()) { // 尝试获取第一个锁
                    try {
                        if (to.lock.tryLock()) { // 尝试获取第二个锁
                            try {
                                if (from.getBalance() < amount) {
                                    throw new InsufficientFundsException();
                                }
                                from.debit(amount);
                                to.credit(amount);
                                return;
                            } finally {
                                to.lock.unlock();
                            }
                        }
                    } finally {
                        // 获取第二个锁失败时或第二个锁操作正常完成后，释放第一个锁
                        from.lock.unlock();
                    }
                }
                // 获取第一个锁失败时，线程睡眠随机的时间
                TimeUnit.NANOSECONDS.sleep((long) (Math.random() * 10));
            }
        }
    }

    /**
     * 模拟银行账户
     */
    public static class Account {

        public Lock lock = new ReentrantLock();

        private double balance;// 余额

        public double getBalance() {
            return balance;
        }

        public void debit(double amount) {
            this.balance -= amount;
        }

        public void credit(double amount) {
            this.balance += amount;
        }
    }

    /**
     * 2，在实现具有时间限制的操作时，定时锁非常有用，内置锁很难实现带有时间限制的操作。
     * <p>
     *     在 Lock 保护的共享通信线路上发送一条消息，如果不能在指定时间内完成，代码就会失败。
     *     定时的 tryLock 能够在这种带有时间限制的操作中实现独占加锁行为。
     */
    public static class TimedLockTest {

        public Lock lock = new ReentrantLock();

        public boolean trySendMessage(String message) throws InterruptedException {
            // 如果锁在给定的等待时间内空闲，并且当前线程未被中断，则获取锁。
            if (!lock.tryLock(100, TimeUnit.MILLISECONDS)) {
                return false;
            }
            try {
                return sendMessage(message);
            } finally {
                lock.unlock();
            }
        }

        private boolean sendMessage(String message) {
            System.out.println(message);
            return true;
        }
    }

    /**
     * 3，可中断的锁获取操作
     * <p>
     *     可中断的锁获取操作能在可取消的操作中使用加锁，
     *     lockInterruptibly 方法能够在获得锁的同时保持对中断的响应。
     *     定时的 tryLock 同样能响应中断，因此可以使用它实现一个定时的和可中断的锁获取操作。
     */
    public static class InterruptibleLockTest {

        public Lock lock = new ReentrantLock();

        public boolean sendMessage(String message) throws InterruptedException {
            /*
            * 如果当前线程未被中断，则尝试获取锁。如果锁可用，则获取锁，并立即返回。
            * 如果锁不可用，出于线程调度目的，将禁用当前线程，
            * 并且在发生以下两种情况之一以前，该线程将一直处于休眠状态：
            * 锁由当前线程获得；或者其他某个线程中断当前线程。
            *
            * 并且支持对锁获取的中断。如果当前线程：
            * 在进入此方法时已经设置了该线程的中断状态；或者在获取锁时被中断，并且支持对锁获取的中断，
            * 则将抛出 InterruptedException，并清除当前线程的已中断状态。
            */
             lock.lockInterruptibly();
            try {
                return cancellableSendMessage(message);
            } finally {
                lock.unlock();
            }
        }

        private boolean cancellableSendMessage(String message) throws InterruptedException {
            System.out.println(message);
            return true;
        }
    }
}
