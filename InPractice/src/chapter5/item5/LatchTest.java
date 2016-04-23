package chapter5.item5;

import java.util.concurrent.CountDownLatch;

/**
 * 闭锁示例
 * <p>
 *     闭锁的作用相当于一扇门：在闭锁到达结束状态之前，这扇门一直是关闭的，没有任何线程能通过。
 *     当到达结束状态时，这扇门会打开并允许所有的线程通过，之后闭锁不会再改变状态，这扇门永远保持打开状态。
 * <p>
 *     闭锁可以用来确保某些活动直到其他活动都完成后才继续进行，例如：
 *     等待直到某个操作的所有参与者（例如多玩家游戏中的所有玩家）都就绪再继续执行。
 * <p>
 *     CountDownLatch 是一种灵活的闭锁实现，可以使一个或多个线程等待一组事件发生。
 *     闭锁状态包含一个计数器，该计数器被初始化为一个正数，表示需要等待的事件数量。
 *     countDown 方法递减计数器，表示有一个事件已经发生了；
 *     await 方法等待计数器到零，表示所有需要等待的事件都已经发生。
 *     如果计数器的值非零，那么 await 会一直阻塞直到计数器为零，或者等到中的线程中断，或者超时。
 * <p>
 * Created by liuchenwei on 2016/4/23
 */
public class LatchTest {

    public static void main(String[] args) {
        int nThreads = 5;

        // 启动门：确保所有工作线程都就绪后才开始执行
        final CountDownLatch startGate = new CountDownLatch(1);
        // 结束门：所有工作线程都结束后才开始执行
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        startGate.await();// 等待启动
                        try {
                            System.out.println("I'm running");
                        } finally {
                            // 工作线程结束时要做的最后一件事是调用结束门的 countDown 方法
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        long start = System.currentTimeMillis();
        // 在这行代码之前，所有工作线程都必须等待
        startGate.countDown();
        try {
            endGate.await();// 主线程等待所有工作线程结束
            long end = System.currentTimeMillis();
            System.out.println("Total time=" + (end - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
